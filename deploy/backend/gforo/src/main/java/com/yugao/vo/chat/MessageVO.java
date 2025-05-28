package com.yugao.vo.chat;

import lombok.Data;

import java.util.Date;

@Data
public class MessageVO {
    private Long messageId;
    private Long senderId;
    private String content;
    private Boolean isMe; // 前端展示用
    private Date createTime;
}