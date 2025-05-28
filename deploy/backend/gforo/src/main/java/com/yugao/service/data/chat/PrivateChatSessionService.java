package com.yugao.service.data.chat;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yugao.domain.chat.PrivateChatSession;

public interface PrivateChatSessionService extends IService<PrivateChatSession> {

    PrivateChatSession getSession(Long userAId, Long userBId);

    PrivateChatSession createSession(Long userAId, Long userBId);
}
