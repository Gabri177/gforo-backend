package com.yugao.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yugao.constants.KafkaEventType;
import com.yugao.constants.KafkaTopicConstants;
import com.yugao.converter.TitleConverter;
import com.yugao.domain.email.HtmlEmail;
import com.yugao.domain.event.Event;
import com.yugao.domain.notification.Notification;
import com.yugao.domain.post.DiscussPost;
import com.yugao.domain.title.Title;
import com.yugao.domain.user.User;
import com.yugao.enums.EntityTypeEnum;
import com.yugao.enums.WsMessageTypeEnum;
import com.yugao.netty.util.WsUtil;
import com.yugao.service.business.search.ElasticSearchService;
import com.yugao.service.business.title.TitleBusinessService;
import com.yugao.service.data.notification.NotificationService;
import com.yugao.service.data.title.TitleService;
import com.yugao.util.mail.MailClientUtil;
import com.yugao.util.serialize.SerializeUtil;
import com.yugao.vo.title.SimpleTitleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class EventConsumer {

    private final MailClientUtil mailClient;
    private final ObjectMapper objectMapper;
    private final WsUtil wsUtil;
    private final NotificationService notificationService;
    private final TitleService titleService;
    private final TitleBusinessService titleBusinessService;
    private final ElasticSearchService elasticSearchService;

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
                        not.getTargetId().toString(),WsMessageTypeEnum.LIKE_COMMENT, "你有新的评论被点赞了");
                notificationService.addNotification(not);
                break;
            case KafkaEventType.LIKE_POST:
                System.out.println("收到点赞帖子的通知 通知UserId: " + not.getTargetId());
                wsUtil.sendMsg(
                        not.getTargetId().toString(),WsMessageTypeEnum.LIKE_POST, "你有新的帖子被点赞了");
                notificationService.addNotification(not);
                break;

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
                        not.getTargetId().toString(), WsMessageTypeEnum.COMMENT_POST, "你的帖子被回复了");
                notificationService.addNotification(not);
                break;
            case KafkaEventType.COMMENT_TO_COMMENT:
                System.out.println("收到回复评论的通知 通知UserId: " + not.getTargetId());
                wsUtil.sendMsg(
                        not.getTargetId().toString(), WsMessageTypeEnum.COMMENT_COMMENT, "你的评论被回复了");
                notificationService.addNotification(not);
                break;
        }
    }

    @KafkaListener(topics = KafkaTopicConstants.NOTIFICATION_MESSAGE)
    public void handleNotificationMessage(Event<User> event){
        System.out.println("Received event: " + event);
    }

    @KafkaListener(topics = KafkaTopicConstants.NOTIFICATION_DELETE)
    public void handleNotificationDelete(Event<Notification> event){
        System.out.println("Received event: " + event);
        Notification not = event.getPayloadAs(Notification.class, objectMapper);
        if (not == null)
            return;
        System.out.println("用户的实体被有权限的人删除 通知UserId: " + not.getTargetId());
        wsUtil.sendMsg(
                not.getTargetId().toString(), WsMessageTypeEnum.DELETE_ENTITY, "你的内容被删除了");
        notificationService.addNotification(not);
    }

    @KafkaListener(topics = KafkaTopicConstants.NOTIFICATION_SYSTEM)
    public void handleNotificationSystem(Event<Notification> event){
        System.out.println("Received notifycation system event");
        Notification not = event.getPayloadAs(Notification.class, objectMapper);
        if (not == null)
            return;
        wsUtil.sendMsgToAll(WsMessageTypeEnum.SYSTEM, SerializeUtil.toJson(not));

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

    @KafkaListener(topics = KafkaTopicConstants.REFRESH_USER_INFO)
    public void handleRefresh(Event<User> event){
        System.out.println("Received event: " + event);
        User user = event.getPayloadAs(User.class, objectMapper);
        if (user == null)
            return;
        System.out.println("刷新用户信息 通知UserId: " + user.getId());
        wsUtil.sendMsg(
                user.getId().toString(), WsMessageTypeEnum.REFRESH_USER_INFO, "你的信息被修改了");
    }

    @KafkaListener(topics = KafkaTopicConstants.POP_UP_HINT)
    public void handleNewTitle(Event<?> event){
        switch (event.getEventType()){
            case KafkaEventType.NEW_TITLE:
                System.out.println("Received event: " + event);
                Notification not = event.getPayloadAs(Notification.class, objectMapper);
                Long toUserId = not.getTargetId();
                Title t = titleService.getTitleById(not.getEntityId());
                if (t == null)
                    return;
                SimpleTitleVO title = TitleConverter.toSimpleTitleVO(t);
                System.out.println("用户获得新称号 通知UserId: " + toUserId);
                wsUtil.sendMsg(
                        toUserId.toString(), WsMessageTypeEnum.NEW_TITLE, SerializeUtil.toJson(title));
                notificationService.addNotification(not);
                break;
            default:
                break;
        }
    }

    @KafkaListener(topics = KafkaTopicConstants.EXP_CHANGE)
    public void handleExpChange(Event<?> event){

        EntityTypeEnum type = EntityTypeEnum.fromValue(
                event.getMetadataValue("entityType", Integer.class, objectMapper));
        Long entityId = event.getMetadataValue("entityId", Long.class, objectMapper);
        Integer exp = event.getMetadataValue("exp", Integer.class, objectMapper);
        Long userId = event.getMetadataValue("userId", Long.class, objectMapper);
        if (entityId != null && exp != null && userId != null) {
            if (type == EntityTypeEnum.POST && exp > 0)
                titleBusinessService.addExp(userId, exp, "发布帖子", type, entityId);
            else if (type == EntityTypeEnum.POST && exp < 0)
                titleBusinessService.subtractExp(userId, -exp, "删除帖子", type, entityId);
            else if (type == EntityTypeEnum.COMMENT && exp > 0)
                titleBusinessService.addExp(userId, exp, "发布评论", type, entityId);
            else if (type == EntityTypeEnum.COMMENT && exp < 0)
                titleBusinessService.subtractExp(userId, -exp, "删除评论", type, entityId);
        }

    }

    @KafkaListener(topics = KafkaTopicConstants.ELASTICSEARCH)
    public void handleElasticSearch(Event<?> event) {
        System.out.println("Received event: " + event);
        if (Objects.equals(event.getEventType(), KafkaEventType.NULL)) {
            DiscussPost post = event.getPayloadAs(DiscussPost.class, objectMapper);
            if (post == null)
                return;
            elasticSearchService.savePost(post);
            System.out.println("保存帖子到es");
        } else if (Objects.equals(event.getEventType(), KafkaEventType.DELETE)) {
            Long postId = event.getMetadataValue("postId", Long.class, objectMapper);
            if (postId == null)
                return;
            elasticSearchService.deletePost(postId);
            System.out.println("删除es中的帖子");
        }
    }

}
