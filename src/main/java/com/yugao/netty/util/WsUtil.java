package com.yugao.netty.util;

import com.yugao.domain.websocket.WsMessage;
import com.yugao.enums.WsMessageTypeEnum;
import com.yugao.netty.registry.ChannelRegistry;
import com.yugao.util.serialize.SerializeUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WsUtil {

    private final ChannelRegistry channelRegistry;

    public void sendMsgToAll(WsMessageTypeEnum type, Object content) {
        List<Channel> channels = channelRegistry.getAllChannels();
        if (channels == null || channels.isEmpty()){
            System.out.println("❌ 发送消息失败，通道不存在");
            return;
        }
        for (Channel ch : channels) {
            if (ch != null && ch.isActive()) {
                sendMsg(ch, type.getCode(), content);
                System.out.println("✅ 发送消息成功，通道活跃" +
                        "，消息内容：" + content);
            } else {
                System.out.println("❌ 发送消息失败，通道不活跃");
            }
        }
    }

    public void sendMsg(String userId, WsMessageTypeEnum type, Object content){

        List<Channel> chs = channelRegistry.getUserChannels(userId);
        if (chs == null || chs.isEmpty()){
            System.out.println("❌ 发送消息失败，通道不存在");
            return;
        }
        for (Channel ch : chs) {
            if (ch != null && ch.isActive()) {
                sendMsg(ch, type.getCode(), content);
                System.out.println("✅ 发送消息成功，通道活跃" +
                        "，消息内容：" + content);
            } else {
                System.out.println("❌ 发送消息失败，通道不活跃");
            }
        }
    }

    private void closeUserChannels(String userId) {
        List<Channel> chs = channelRegistry.getUserChannels(userId);
        if (chs == null || chs.isEmpty()){
            System.out.println("❌ 关闭通道失败，通道不存在");
            return;
        }
        for (Channel ch : chs) {
            closeChannel(ch);
        }
    }

    // 快速构造并发送 WsMessageDTO（type + content）
    private void sendMsg(Channel ch, String type, Object content) {
        WsMessage msg = new WsMessage();
        msg.setType(type);
        msg.setContent(content != null ? content.toString() : "");
        msg.setTimeStamp(String.valueOf(System.currentTimeMillis()));
        if (ch != null && ch.isActive()) {
            String json = SerializeUtil.toJson(msg);
            //System.out.println("📤 WebSocket发送消息：" + json);
            ch.writeAndFlush(new TextWebSocketFrame(json));
        }
    }

    // 错误消息快速发送
    private void sendError(Channel ch, String errorMsg) {
        sendMsg(ch, "error", errorMsg);
    }

    // 关闭通道
    private void closeChannel(Channel ch) {
        if (ch != null && ch.isActive()) {
            ch.close();
        }
    }
}
