package com.yugao.domain.notification;

import com.baomidou.mybatisplus.annotation.*;
import com.yugao.enums.EntityTypeEnum;
import com.yugao.enums.NotificationTypeEnum;
import com.yugao.enums.StatusEnum;
import lombok.Data;

import java.util.Date;

@Data
@TableName("notification")
public class Notification {

//    id	bigint	NO	PRI		auto_increment
//    sender_id	bigint	YES
//    type	int	NO		0
//    target_id	bigint	YES
//    status	int	NO		0
//    content	text	YES
//    create_time	datetime	YES		CURRENT_TIMESTAMP	DEFAULT_GENERATED

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long senderId; // 发送者ID

    private NotificationTypeEnum type; // 通知类型

    private Long targetId; // 目标ID

    private StatusEnum status; // 通知是否删除

    private String title; // 通知标题

    private String content; // 通知内容

    @TableField(fill = FieldFill.INSERT)
    private Date createTime; // 创建时间

    private Long entityId; // 关联的实体ID

    private EntityTypeEnum entityType; // 关联的实体类型
}
