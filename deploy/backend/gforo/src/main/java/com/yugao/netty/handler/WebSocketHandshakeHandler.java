package com.yugao.netty.handler;

import com.yugao.enums.ResultCodeEnum;
import com.yugao.service.business.session.SessionService;
import com.yugao.util.security.JwtUtil;
import com.yugao.netty.registry.ChannelRegistry;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Arrays;
import java.util.Optional;

@Component
@Scope("prototype") // 每次请求创建新的实例
@RequiredArgsConstructor
public class WebSocketHandshakeHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final ChannelRegistry channelRegistry;
    private final JwtUtil jwtUtil;
    private final SessionService sessionService;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) {
        URI uri = URI.create(req.uri()); // 如 /ws/123?token=abc
        System.out.println("连接地址：" + uri.getPath());   // 👉 你可以解析路径
        System.out.println("请求参数：" + uri.getQuery());  // 👉 你可以解析token

        // ✅ 示例：从路径中提取userId
        String path = uri.getPath(); // 例：/ws/123
        String[] segments = path.split("/");
        System.out.println(Arrays.stream(segments).toList());
        String deviceId = segments.length >= 2 ? segments[1] : null;


        // ✅ 示例：从查询参数中提取token
        QueryStringDecoder decoder = new QueryStringDecoder(req.uri());
        String token = Optional.ofNullable(decoder.parameters().get("token"))
                .map(list -> list.get(0)).orElse(null);
        String userId = jwtUtil.getUserIdWithToken(token);
        System.out.println("⭕️ UserId: " + userId + " DeviceId: " + deviceId);
        if (userId == null || deviceId == null) {
            System.out.println("userId == null || deviceId == null");
            sendResponse(ctx, ResultCodeEnum.ACCESSION_UNAUTHORIZED, HttpResponseStatus.UNAUTHORIZED);
            return;
            // Unauthorized
        } else if (!sessionService.isDeviceSessionExist(Long.parseLong(userId), deviceId)){
            sendResponse(ctx, ResultCodeEnum.REFRESHMENT_EXPIRED, HttpResponseStatus.UNAUTHORIZED);
            return;
        }

        // ✅ 验证token和deviceId  这里应该加入是access过期了还是非法token 然后返回指定数据

        Channel ch = channelRegistry.getChannel(userId, deviceId);
        if (ch != null){
            ch.close();
            channelRegistry.remove(userId, deviceId);
        }
        channelRegistry.register(userId, deviceId, ctx.channel());
        System.out.println("🔵 连接成功，注册信息：" + userId + "::" + deviceId);
        // TODO：你可以在这里做 token 验证、用户绑定、记录 userId-channel 映射等操作



        // ✅ 手动完成 WebSocket 协议升级
        WebSocketServerHandshakerFactory factory =
                new WebSocketServerHandshakerFactory(getWebSocketLocation(req), null, true);
        WebSocketServerHandshaker handshaker = factory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
            // 如果需要保存 handshaker，可以放进 AttributeMap
            ctx.channel().attr(AttributeKey.valueOf("deviceId")).set(deviceId);
            ctx.channel().attr(AttributeKey.valueOf("userId")).set(userId);
        }
    }

    private String getWebSocketLocation(FullHttpRequest req) {
        return "ws://" + req.headers().get(HttpHeaderNames.HOST) + req.uri();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        String deviceId = (String) channel.attr(AttributeKey.valueOf("deviceId")).get();
        String userId = (String) channel.attr(AttributeKey.valueOf("userId")).get();

        if (userId != null && deviceId != null) {
            System.out.println("🔴 连接断开，清理注册信息：" + userId + "::" + deviceId);
            channelRegistry.remove(userId, deviceId);
        } else {
            System.out.println("🔴 连接断开，但缺少 userId 或 deviceId");
        }
    }

    private void sendResponse(ChannelHandlerContext ctx, ResultCodeEnum resultCodeEnum, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                status,
                Unpooled.copiedBuffer(resultCodeEnum.getMessage(), CharsetUtil.UTF_8)
        );
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

}
