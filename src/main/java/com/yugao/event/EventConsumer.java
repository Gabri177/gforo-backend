package com.yugao.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yugao.constants.KafkaEventType;
import com.yugao.constants.KafkaTopicConstants;
import com.yugao.domain.email.HtmlEmail;
import com.yugao.domain.event.Event;
import com.yugao.domain.notification.Notification;
import com.yugao.domain.user.User;
import com.yugao.domain.websocket.WsMessage;
import com.yugao.netty.registry.ChannelRegistry;
import com.yugao.netty.util.WsUtil;
import com.yugao.service.data.NotificationService;
import com.yugao.service.handler.TokenHandler;
import com.yugao.util.mail.MailClientUtil;
import com.yugao.util.serialize.SerializeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class EventConsumer {

    private final MailClientUtil mailClient;
    private final ObjectMapper objectMapper;
    private final WsUtil wsUtil;
    private final NotificationService notificationService;

    // TODO: 下面除了邮件发送是正确的其他的逻辑还要再次修改

    @KafkaListener(topics = KafkaTopicConstants.NOTIFICATION_LIKE)
    public void handleNotificationLike(Event<Notification> event){

        System.out.println("Received event: " + event);
        Notification not = event.getPayloadAs(Notification.class, objectMapper);
        if (not == null)
            return;
        switch (event.getEventType()){
            case KafkaEventType.LIKE_COMMENT:
                System.out.println("收到点赞评论的通知 通知UserId: " + not.getTargetId());
                wsUtil.sendMsg(
                        not.getTargetId().toString(),"LIKE_COMMENT", "你有新的评论被点赞了");
                notificationService.addNotification(not);
                break;
            case KafkaEventType.LIKE_POST:
                System.out.println("收到点赞帖子的通知 通知UserId: " + not.getTargetId());
                wsUtil.sendMsg(
                        not.getTargetId().toString(),"LIKE_POST", "你有新的帖子被点赞了");
                notificationService.addNotification(not);
                break;
//            case KafkaEventType.DISLIKE_POST:
//                // notificationService.deleteNotification(notification.getTargetId());
//                System.out.println("收到取消点赞帖子的通知");
//                break;
        }

    }

    @KafkaListener(topics = KafkaTopicConstants.NOTIFICATION_COMMENT)
    public void handleNotificationComment(Event<Notification> event){

        System.out.println("Received event: " + event);
        Notification not = event.getPayloadAs(Notification.class, objectMapper);
        if (not == null)
            return;
        switch (event.getEventType()) {
            case KafkaEventType.COMMENT_TO_POST:
                System.out.println("收到回复帖子的通知 通知UserId: " + not.getTargetId());
                wsUtil.sendMsg(
                        not.getTargetId().toString(), "COMMENT_POST", "你的帖子被回复了");
                notificationService.addNotification(not);
                break;
            case KafkaEventType.COMMENT_TO_COMMENT:
                System.out.println("收到回复评论的通知 通知UserId: " + not.getTargetId());
                wsUtil.sendMsg(
                        not.getTargetId().toString(), "COMMENT_COMMENT", "你的评论被回复了");
                notificationService.addNotification(not);
                break;
        }
    }

    @KafkaListener(topics = KafkaTopicConstants.NOTIFICATION_FOLLOW)
    public void handleNotificationFollow(Event<User> event){
        System.out.println("Received event: " + event);
    }

    @KafkaListener(topics = KafkaTopicConstants.NOTIFICATION_MESSAGE)
    public void handleNotificationMessage(Event<User> event){
        System.out.println("Received event: " + event);
    }

    @KafkaListener(topics = KafkaTopicConstants.NOTIFICATION_SYSTEM)
    public void handleNotificationSystem(Event<Notification> event){
        System.out.println("Received notifycation system event");
        Notification not = event.getPayloadAs(Notification.class, objectMapper);
        if (not == null)
            return;
        wsUtil.sendMsgToAll("SYSTEM_MESSAGE", SerializeUtil.toJson(not));

    }

    @KafkaListener(topics = KafkaTopicConstants.SEND_VERIFICATION_EMAIL)
    public void handleSendVerificationEmail(Event<HtmlEmail> event){

        System.out.println("消费者： 发送验证邮件");
        HtmlEmail email = event.getPayloadAs(HtmlEmail.class, objectMapper);
        mailClient.sendHtmlMail(
                email.getTo(),
                email.getSubject(),
                email.getContent()
        );
        System.out.println("消费者： 发送验证邮件成功");
    }
}
