package com.yugao.dto.chat;

import lombok.Data;

import java.util.Date;

@Data
public class ChatSessionDTO {
    private Long sessionId;
    private Long otherUserId;
    private String otherUsername;
    private String otherAvatar;
    private String lastMessage;
    private Date lastActiveTime;
    private Boolean hasUnread; // 是否有未读消息
}
