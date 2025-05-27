package com.yugao.service.data.chat;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yugao.domain.chat.UserChatSession;

import java.util.List;

public interface UserChatSessionService extends IService<UserChatSession> {
    List<UserChatSession> getUserSessions(Long userId);

    void createUserChatSessionPair(Long sessionId, Long userAId, Long userBId);
}
