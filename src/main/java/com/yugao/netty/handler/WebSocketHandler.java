package com.yugao.netty.handler;

import com.yugao.dto.websocket.WsMessageDTO;
import com.yugao.netty.registry.ChannelRegistry;
import com.yugao.netty.util.WsUtil;
import com.yugao.service.handler.TokenHandler;
import com.yugao.util.serialize.SerializeUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype") // 每次请求创建新的实例
@RequiredArgsConstructor
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final TokenHandler tokenHandler;
    private final WsUtil wsUtil;
    private final ChannelRegistry channelRegistry;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {


        String deviceId = (String) ctx.channel().attr(AttributeKey.valueOf("deviceId")).get();
        String userId = (String) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if(!tokenHandler.isUserDeviceSessionExist(Long.parseLong(userId), deviceId)){
            System.out.println("❌ 连接已失效，断开连接：" + ctx.channel().id());
            // 移除全局map中的channel实例
            channelRegistry.removeByChannel(ctx.channel());
            ctx.close();
            return;
        }
        String text = msg.text();
        WsMessageDTO message = SerializeUtil.fromJson(text, WsMessageDTO.class);
        String type = message.getType();

        switch (type) {
            case "ping":
                wsUtil.sendMsg(userId, "pong", null);
                break;
            case "chat":
                System.out.println("收到消息：" + text);
                wsUtil.sendMsg(userId, "chat", message);
                break;
            default:
                System.out.println("未识别的消息类型: " + type);
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        System.out.println("🟢 新连接：" + ctx.channel().id());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        channelRegistry.removeByChannel(ctx.channel());
        System.out.println("🔴 连接断开：" + ctx.channel().id());
    }

    private void sendMessage(ChannelHandlerContext ctx, String type, Object content) {
        WsMessageDTO reply = new WsMessageDTO();
        reply.setType(type);
        reply.setContent(content != null ? content.toString() : "");
        reply.setTimeStamp(String.valueOf(System.currentTimeMillis()));

        String json = SerializeUtil.toJson(reply);
        ctx.channel().writeAndFlush(new TextWebSocketFrame(json));
    }

}
