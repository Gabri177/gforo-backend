package com.yugao.domain.chat;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName("private_message")
public class PrivateMessage {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long sessionId;

    private Long senderId;

    private Long receiverId;

    private String content;

    private Boolean isRead;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
