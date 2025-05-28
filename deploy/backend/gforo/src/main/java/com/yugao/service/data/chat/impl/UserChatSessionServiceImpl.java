package com.yugao.service.data.chat.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yugao.domain.chat.UserChatSession;
import com.yugao.mapper.chat.UserChatSessionMapper;
import com.yugao.service.data.chat.UserChatSessionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class UserChatSessionServiceImpl extends ServiceImpl<UserChatSessionMapper, UserChatSession>
        implements UserChatSessionService {

    @Override
    public List<UserChatSession> getUserSessions(Long userId) {
        return lambdaQuery().eq(UserChatSession::getUserId, userId).list();
    }

    @Override
    @Transactional
    public void createUserChatSessionPair(Long sessionId, Long userAId, Long userBId) {
        Date now = new Date();

        UserChatSession sessionA = new UserChatSession();
        sessionA.setUserId(userAId);
        sessionA.setSessionId(sessionId);
        sessionA.setOtherUserId(userBId);
        sessionA.setLastActiveTime(now);

        UserChatSession sessionB = new UserChatSession();
        sessionB.setUserId(userBId);
        sessionB.setSessionId(sessionId);
        sessionB.setOtherUserId(userAId);
        sessionB.setLastActiveTime(now);
        saveBatch(List.of(sessionA, sessionB));
    }
}
