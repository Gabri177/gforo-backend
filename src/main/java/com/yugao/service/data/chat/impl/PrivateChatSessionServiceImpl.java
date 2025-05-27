package com.yugao.service.data.chat.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yugao.domain.chat.PrivateChatSession;
import com.yugao.domain.chat.PrivateMessage;
import com.yugao.mapper.chat.PrivateChatSessionMapper;
import com.yugao.service.data.chat.PrivateChatSessionService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PrivateChatSessionServiceImpl extends ServiceImpl<PrivateChatSessionMapper, PrivateChatSession>
        implements PrivateChatSessionService {

    @Override
    public PrivateChatSession getSession(Long userAId, Long userBId) {
        Long small = Math.min(userAId, userBId);
        Long large = Math.max(userAId, userBId);
        PrivateChatSession session = lambdaQuery()
                .eq(PrivateChatSession::getUserSmallId, small)
                .eq(PrivateChatSession::getUserLargeId, large)
                .one();
        return session;
    }

    @Override
    public PrivateChatSession createSession(Long userAId, Long userBId) {
        Long small = Math.min(userAId, userBId);
        Long large = Math.max(userAId, userBId);

        PrivateChatSession session = new PrivateChatSession();
        session.setUser1Id(userAId);
        session.setUser2Id(userBId);
        session.setUserSmallId(small);
        session.setUserLargeId(large);
        session.setLastActiveTime(new Date());
        save(session);
        return session;
    }
}
