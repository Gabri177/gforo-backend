package com.yugao.netty.handler;

import com.yugao.domain.websocket.WsMessage;
import com.yugao.dto.chat.PrivateMessageDTO;
import com.yugao.enums.WsMessageTypeEnum;
import com.yugao.netty.dispatcher.ChatMessageDispatcher;
import com.yugao.netty.registry.ChannelRegistry;
import com.yugao.netty.util.WsUtil;
import com.yugao.service.business.session.SessionService;
import com.yugao.util.serialize.SerializeUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype") // 每次请求创建新的实例
@RequiredArgsConstructor
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final WsUtil wsUtil;
    private final ChannelRegistry channelRegistry;
    private final SessionService sessionService;
    private final ChatMessageDispatcher chatMessageDispatcher;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {


        String deviceId = (String) ctx.channel().attr(AttributeKey.valueOf("deviceId")).get();
        String userId = (String) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if(!sessionService.isDeviceSessionExist(Long.parseLong(userId), deviceId)){
            System.out.println("❌ 连接已失效，断开连接：" + ctx.channel().id());
            // 移除全局map中的channel实例
            channelRegistry.removeByChannel(ctx.channel());
            ctx.close();
            return;
        }
        String text = msg.text();
        WsMessage message = SerializeUtil.fromJson(text, WsMessage.class);
        String type = message.getType();

        switch (type) {
            case "ping":
//                System.out.println("收到心跳：" + text);
                wsUtil.sendMsg(userId, WsMessageTypeEnum.HEARTBEAT, null);
                break;
            case "chat":
                System.out.println("收到消息 chat：" + text);
                chatMessageDispatcher.dispatchChatMessage(
                        userId,
                        getReceiverIdFromChatMessage(message),  // 需要从 content 中解析出 toUserId
                        message.getContent()
                );
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

    private String getReceiverIdFromChatMessage(WsMessage message) {
        try {
            PrivateMessageDTO dto = SerializeUtil.fromJson(message.getContent(), PrivateMessageDTO.class);
            return String.valueOf(dto.getReceiverId());
        } catch (Exception e) {
            System.out.println("❌ 解析 chat 消息失败：" + e.getMessage());
            return null;
        }
    }


}
