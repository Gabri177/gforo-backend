package com.yugao.service.business.chat;


import com.yugao.dto.chat.ChatSessionDTO;
import com.yugao.vo.chat.MessageVO;

import java.util.List;

public interface ChatService {

    // 获取当前用户的所有会话列表
    List<ChatSessionDTO> getUserSessions();

    // 获取指定会话的所有消息
    List<MessageVO> getMessagesForSession(Long sessionId);

    // 初始化与目标用户的私信会话（若无则创建）
    void initSession(Long targetUserId);

    // 删除指定会话（真删除）
    void deleteSession(Long sessionId);

}
