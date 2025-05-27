package com.yugao.domain.chat;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName("user_chat_session")
public class UserChatSession {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long sessionId;

    private Long otherUserId;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date lastActiveTime;
}
