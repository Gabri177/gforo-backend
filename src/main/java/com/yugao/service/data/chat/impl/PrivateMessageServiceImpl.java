package com.yugao.service.data.chat.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yugao.domain.chat.PrivateMessage;
import com.yugao.mapper.chat.PrivateMessageMapper;
import com.yugao.service.data.chat.PrivateMessageService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PrivateMessageServiceImpl extends ServiceImpl<PrivateMessageMapper, PrivateMessage>
        implements PrivateMessageService {

    @Override
    public List<PrivateMessage> getMessagesBySession(Long sessionId) {
        return lambdaQuery()
                .eq(PrivateMessage::getSessionId, sessionId)
                .orderByAsc(PrivateMessage::getCreateTime)
                .list();
    }

    @Override
    public PrivateMessage getLastMessage(Long sessionId) {
        return lambdaQuery()
                .eq(PrivateMessage::getSessionId, sessionId)
                .orderByDesc(PrivateMessage::getCreateTime)
                .last("LIMIT 1")
                .one();
    }

    @Override
    public void sendMessage(PrivateMessage message) {
        message.setCreateTime(new Date());
        message.setIsRead(false);
        save(message);
    }

    @Override
    public boolean hasUnreadMessage(Long sessionId, Long userId) {
        return lambdaQuery()
                .eq(PrivateMessage::getSessionId, sessionId)
                .eq(PrivateMessage::getReceiverId, userId)
                .eq(PrivateMessage::getIsRead, false)
                .exists();
    }

}
