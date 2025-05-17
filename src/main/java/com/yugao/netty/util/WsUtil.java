package com.yugao.netty.util;

import com.yugao.dto.websocket.WsMessageDTO;
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

    public void sendMsg(String userId, WsMessageDTO wsMessageDTO){

        List<Channel> chs = channelRegistry.getUserChannels(userId);
        if (chs == null || chs.isEmpty()){
            System.out.println("❌ 发送消息失败，通道不存在");
            return;
        }
        for (Channel ch : chs) {
            if (ch != null && ch.isActive()) {
                sendMsg(ch, wsMessageDTO);
            } else {
                System.out.println("❌ 发送消息失败，通道不活跃");
            }
        }

    }

    public void sendMsg(String userId, String type, Object content){

        List<Channel> chs = channelRegistry.getUserChannels(userId);
        if (chs == null || chs.isEmpty()){
            System.out.println("❌ 发送消息失败，通道不存在");
            return;
        }
        for (Channel ch : chs) {
            if (ch != null && ch.isActive()) {
                sendMsg(ch, type, content);
            } else {
                System.out.println("❌ 发送消息失败，通道不活跃");
            }
        }
    }

    private void sendText(String userId, String content) {
        List<Channel> chs = channelRegistry.getUserChannels(userId);
        if (chs == null || chs.isEmpty()){
            System.out.println("❌ 发送消息失败，通道不存在");
            return;
        }
        for (Channel ch : chs) {
            if (ch != null && ch.isActive()) {
                sendText(ch, content);
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

    // 发送完整消息对象
    private void sendMsg(Channel ch, WsMessageDTO wsMessageDTO) {
        if (ch != null && ch.isActive()) {
            String json = SerializeUtil.toJson(wsMessageDTO);
            ch.writeAndFlush(new TextWebSocketFrame(json));
        }
    }

    // 发送快速文本
    private void sendText(Channel ch, String content) {
        if (ch != null && ch.isActive()) {
            ch.writeAndFlush(new TextWebSocketFrame(content));
        }
    }

    // 快速构造并发送 WsMessageDTO（type + content）
    private void sendMsg(Channel ch, String type, Object content) {
        WsMessageDTO msg = new WsMessageDTO();
        msg.setType(type);
        msg.setContent(content != null ? content.toString() : "");
        msg.setTimeStamp(String.valueOf(System.currentTimeMillis()));
        sendMsg(ch, msg);
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
