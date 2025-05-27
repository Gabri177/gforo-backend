package com.yugao.netty.dispatcher;

import com.yugao.constants.RedisKeyConstants;
import com.yugao.domain.chat.PrivateMessage;
import com.yugao.dto.chat.PrivateMessageDTO;
import com.yugao.enums.WsMessageTypeEnum;
import com.yugao.netty.util.WsUtil;
import com.yugao.service.base.RedisService;
import com.yugao.service.base.impl.RedisServiceImpl;
import com.yugao.service.handler.EventHandler;
import com.yugao.util.serialize.SerializeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class ChatMessageDispatcher {

    private final WsUtil wsUtil;
    private final EventHandler eventHandler;
    private final RedisService redisService;

    public void dispatchChatMessage(String fromUserId, String toUserId, String content) {

        String key1 = RedisKeyConstants.buildChatLimitedKey(Long.parseLong(fromUserId), Long.parseLong(toUserId));
        String key2 = RedisKeyConstants.buildChatLimitedKey(Long.parseLong(toUserId), Long.parseLong(fromUserId));
        if (redisService.hasKey(key1)) {
            redisService.increment(key1, 1);
            int val = Integer.parseInt(redisService.get(key1));
            if (val > 3) {
                wsUtil.sendMsg(fromUserId, WsMessageTypeEnum.CHAT_LIMIT, null);
                return ; // 超过限制，发送限制消息
            }
        } else if (redisService.hasKey(key2)) {
            redisService.delete(key2);
        }
        PrivateMessageDTO privateMessageDTO = SerializeUtil.fromJson(content, PrivateMessageDTO.class);
        if (privateMessageDTO == null) {
            log.error("❌ 无效的消息内容，无法解析为 PrivateMessageDTO");
            return;
        }
        PrivateMessage privateMessage = new PrivateMessage();
        BeanUtils.copyProperties(privateMessageDTO, privateMessage);
        // 构造传输消息，content为业务对象的JSON
        wsUtil.sendMsg(toUserId, WsMessageTypeEnum.CHAT, content);
        log.info("✅ 消息已发送给用户 {}，内容：{}", toUserId, content);

        eventHandler.handleSaveChatMessage(privateMessage);
    }
}


