package com.yugao.service.business.chat.impl;

import com.yugao.constants.RedisKeyConstants;
import com.yugao.domain.chat.PrivateChatSession;
import com.yugao.domain.chat.PrivateMessage;
import com.yugao.domain.chat.UserChatSession;
import com.yugao.domain.user.User;
import com.yugao.dto.chat.ChatSessionDTO;
import com.yugao.service.base.RedisService;
import com.yugao.service.business.chat.ChatService;
import com.yugao.service.data.chat.PrivateChatSessionService;
import com.yugao.service.data.chat.PrivateMessageService;
import com.yugao.service.data.chat.UserChatSessionService;
import com.yugao.service.data.user.UserService;
import com.yugao.service.handler.EventHandler;
import com.yugao.service.validator.UserValidator;
import com.yugao.util.security.SecurityUtils;
import com.yugao.vo.chat.MessageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final PrivateChatSessionService privateChatSessionService;
    private final UserChatSessionService userChatSessionService;
    private final PrivateMessageService privateMessageService;
    private final RedisService redisService;
    private final UserService userService;
    private final UserValidator userValidator;
    private final EventHandler eventHandler;

    @Override
    public List<ChatSessionDTO> getUserSessions() {
        Long userId = SecurityUtils.mustGetLoginUserId();
        List<UserChatSession> sessions = userChatSessionService.getUserSessions(userId);
        return sessions.stream().map(s -> {
            ChatSessionDTO dto = new ChatSessionDTO();
            dto.setSessionId(s.getSessionId());
            dto.setOtherUserId(s.getOtherUserId());
            dto.setLastActiveTime(s.getLastActiveTime());
            User otherUser = userService.getUserById(s.getOtherUserId());
            dto.setOtherUsername(otherUser.getUsername());
            dto.setOtherAvatar(otherUser.getHeaderUrl());
            dto.setHasUnread(privateMessageService.hasUnreadMessage(s.getSessionId(), userId));

            // TODO: 补充 lastMessage
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<MessageVO> getMessagesForSession(Long sessionId) {
        Long userId = SecurityUtils.mustGetLoginUserId();
        List<PrivateMessage> messages = privateMessageService.getMessagesBySession(sessionId);

        List<MessageVO> vos = messages.stream().map(m -> {
            MessageVO vo = new MessageVO();
            vo.setMessageId(m.getId());
            vo.setSenderId(m.getSenderId());
            vo.setContent(m.getContent());
            vo.setCreateTime(m.getCreateTime());
            vo.setIsMe(m.getSenderId().equals(userId));
            return vo;
        }).toList();

        // 标记所有发给当前用户的消息为已读
        privateMessageService.lambdaUpdate()
                .eq(PrivateMessage::getSessionId, sessionId)
                .eq(PrivateMessage::getReceiverId, userId)
                .eq(PrivateMessage::getIsRead, false)
                .set(PrivateMessage::getIsRead, true)
                .update();

        return vos;
    }

    @Override
    @Transactional
    public void initSession(Long targetUserId) {
        Long fromUserId = SecurityUtils.mustGetLoginUserId();
        userValidator.validateUserIdExists(targetUserId);

        // 获取或创建会话
        PrivateChatSession session = privateChatSessionService.getSession(fromUserId, targetUserId);

        if (session == null) {
            String key = RedisKeyConstants.buildChatLimitedKey(fromUserId, targetUserId);
            session = privateChatSessionService.createSession(fromUserId, targetUserId);
            userChatSessionService.createUserChatSessionPair(session.getId(), fromUserId, targetUserId);
            redisService.set(key, "0");
        }

    }

    @Override
    @Transactional
    public void deleteSession(Long sessionId) {

        System.out.println("删除会话: " + sessionId);
        Long currentUserId = SecurityUtils.mustGetLoginUserId();

        // 校验当前用户是否有权限删除该会话（是否参与者）
        boolean isParticipant = userChatSessionService.lambdaQuery()
                .eq(UserChatSession::getSessionId, sessionId)
                .eq(UserChatSession::getUserId, currentUserId)
                .exists();

        Long otherUserId = userChatSessionService.lambdaQuery()
                .eq(UserChatSession::getSessionId, sessionId)
                .eq(UserChatSession::getUserId, currentUserId)
                .select(UserChatSession::getOtherUserId)
                .oneOpt()
                .map(UserChatSession::getOtherUserId)
                .orElse(null);
        System.out.println("会话的其他用户ID: " + otherUserId);
        if (!isParticipant) {
            throw new IllegalArgumentException("你没有权限删除该会话");
        }

        // 删除消息记录
        privateMessageService.lambdaUpdate()
                .eq(PrivateMessage::getSessionId, sessionId)
                .remove();

        // 删除会话映射（user_chat_session）
        userChatSessionService.lambdaUpdate()
                .eq(UserChatSession::getSessionId, sessionId)
                .remove();

        // 删除主会话（private_chat_session）
        privateChatSessionService.removeById(sessionId);

        // 清除 Redis 限制（双向）
        redisService.delete("chat:lock:" + currentUserId + ":" + sessionId);
        redisService.delete("chat:lock:" + sessionId + ":" + currentUserId);

        eventHandler.notifyDeleteSession(otherUserId, sessionId);
    }



}
