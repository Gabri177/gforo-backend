package com.yugao.dto.chat;

import lombok.Data;

@Data
public class PrivateMessageDTO {

    private Long sessionId;

    private Long senderId;

    private Long receiverId;

    private String content;
}
