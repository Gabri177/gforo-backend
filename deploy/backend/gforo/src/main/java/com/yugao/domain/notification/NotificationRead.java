package com.yugao.domain.notification;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@TableName("notification_read")
@Builder
public class NotificationRead {

//    notification_id	bigint	NO	PRI
//    user_id	bigint	NO	PRI
//    read_time	datetime	YES

    private Long notificationId; // 通知ID

    private Long userId; // 用户ID

    @TableField(fill = FieldFill.INSERT)
    private Date readTime; // 阅读时间
}
