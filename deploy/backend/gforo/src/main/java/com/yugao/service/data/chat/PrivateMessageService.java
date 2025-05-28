package com.yugao.service.data.chat;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yugao.domain.chat.PrivateMessage;

import java.util.List;

public interface PrivateMessageService extends IService<PrivateMessage> {

    List<PrivateMessage> getMessagesBySession(Long sessionId);

    PrivateMessage getLastMessage(Long sessionId);

    void sendMessage(PrivateMessage message);

    boolean hasUnreadMessage(Long sessionId, Long userId);

}