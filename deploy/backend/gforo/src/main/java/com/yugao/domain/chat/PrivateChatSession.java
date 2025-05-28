package com.yugao.domain.chat;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName("private_chat_session")
public class PrivateChatSession {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long user1Id;

    private Long user2Id;

    private Long userSmallId;

    private Long userLargeId;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date lastActiveTime;
}
