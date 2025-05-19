package com.yugao.service.handler;

import com.yugao.constants.KafkaEventType;
import com.yugao.constants.KafkaTopicConstants;
import com.yugao.domain.comment.Comment;
import com.yugao.domain.email.HtmlEmail;
import com.yugao.domain.event.Event;
import com.yugao.domain.notification.Notification;
import com.yugao.enums.CommentEntityTypeEnum;
import com.yugao.enums.NotificationEntityTypeEnum;
import com.yugao.enums.NotificationTypeEnum;
import com.yugao.enums.StatusEnum;
import com.yugao.event.EventProducer;
import com.yugao.util.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventHandler {

    private final EventProducer eventProducer;

    public void notifyComment(Long toUserId, Comment comment, Boolean isPost){

        System.out.println("发送评论通知" + comment);
        Long currentUserId = SecurityUtils.mustGetLoginUserId();
        // 如果评论者和被评论者是同一个人，则不发送通知
        if (currentUserId.equals(toUserId))
            return;
        // 创建通知对象
        Notification not = new Notification();
        not.setSenderId(currentUserId);
        not.setType(NotificationTypeEnum.COMMENT);
        not.setTargetId(toUserId);
        not.setEntityType(NotificationEntityTypeEnum.COMMENT);
        not.setEntityId(comment.getId());
        not.setStatus(StatusEnum.NORMAL);
        // 发送评论事件
        eventProducer.send(
                KafkaTopicConstants.NOTIFICATION_COMMENT,
                comment.getEntityType() == CommentEntityTypeEnum.POST ?
                Event.create(KafkaEventType.COMMENT_TO_POST, "Comment to Post", null, not) :
                Event.create(KafkaEventType.COMMENT_TO_COMMENT, "Comment to Comment", null, not)
        );
    }

    public void notifyLike(Long toUserId, Long entityId, Boolean isPost){
        Long currentUserId = SecurityUtils.mustGetLoginUserId();
        if (currentUserId.equals(toUserId))
            return;
        Notification not = new Notification();
        not.setSenderId(currentUserId);
        not.setType(NotificationTypeEnum.LIKE);
        not.setTargetId(toUserId);
        if (isPost)
            not.setEntityType(NotificationEntityTypeEnum.POST);
        else
            not.setEntityType(NotificationEntityTypeEnum.COMMENT);
        not.setEntityId(entityId);
        not.setStatus(StatusEnum.NORMAL);
        // 发送点赞事件
        eventProducer.send(
                KafkaTopicConstants.NOTIFICATION_LIKE,
                isPost ?
                Event.create(KafkaEventType.LIKE_POST, "Notification", "null", not) :
                Event.create(KafkaEventType.LIKE_COMMENT, "Notification", "null", not)
        );
    }

    public void sendHtmlEmail(String to, String subject, String content, String eventType) {
        eventProducer.send(
                KafkaTopicConstants.SEND_VERIFICATION_EMAIL,
                Event.create(
                        eventType,
                        "Email",
                        "null",
                        HtmlEmail.builder()
                                .to(to)
                                .subject(subject)
                                .content(content)
                                .build()
                )
        );
    }

    public void notifySystem(Notification not) {
        eventProducer.send(
                KafkaTopicConstants.NOTIFICATION_SYSTEM,
                Event.create(
                        KafkaEventType.SYSTEM_MESSAGE,
                        "System",
                        "null",
                        not
                )
        );
    }

}
