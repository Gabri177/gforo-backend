package com.yugao.vo.notification;

import com.yugao.enums.NotificationEntityTypeEnum;
import com.yugao.vo.comment.SimpleCommentVO;
import com.yugao.vo.post.SimpleDiscussPostVO;
import com.yugao.vo.user.SimpleUserVO;
import lombok.Data;

import java.util.Date;

@Data
public class UserNotificationVO {

    private Long id;
    private String title;
    private String content;
    private Date createTime;
    private Integer type; // 通知类型
    private Long senderId; // 发送者ID
    private SimpleUserVO author; // 通知发送者 不是数据库字段
    private Boolean isRead; // 需要自己装配 不是数据库字段
    private Long entityId; // 通知实体ID
    private NotificationEntityTypeEnum entityType; // 通知实体类型
    private SimpleDiscussPostVO post; // 关联的帖子 不是数据库字段
    private SimpleCommentVO comment; // 关联的评论 不是数据库字段
}
