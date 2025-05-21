package com.yugao.service.handler;

import com.yugao.constants.KafkaEventType;
import com.yugao.constants.KafkaTopicConstants;
import com.yugao.domain.comment.Comment;
import com.yugao.domain.email.HtmlEmail;
import com.yugao.domain.event.Event;
import com.yugao.domain.notification.Notification;
import com.yugao.domain.post.DiscussPost;
import com.yugao.domain.title.Title;
import com.yugao.domain.user.User;
import com.yugao.enums.CommentEntityTypeEnum;
import com.yugao.enums.EntityTypeEnum;
import com.yugao.enums.NotificationTypeEnum;
import com.yugao.enums.StatusEnum;
import com.yugao.event.EventProducer;
import com.yugao.service.data.title.TitleService;
import com.yugao.util.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventHandler {

    private final EventProducer eventProducer;
    private final UserHandler userHandler;
    private final TitleService titleService;

    public void notifyComment(Long toUserId, Comment comment, Boolean isPost){

        System.out.println("发送评论通知" + comment);
        Long currentUserId = SecurityUtils.mustGetLoginUserId();
        // 如果评论者和被评论者是同一个人，则不发送通知
        if (currentUserId.equals(toUserId))
            return;
        // 创建通知对象
        Notification not = new Notification();
        not.setSenderId(currentUserId);
        if (comment.getEntityType() == CommentEntityTypeEnum.POST)
            not.setType(NotificationTypeEnum.COMMENT_POST);
        else
            not.setType(NotificationTypeEnum.COMMENT_COMMENT);
        not.setTargetId(toUserId);
        not.setEntityType(EntityTypeEnum.COMMENT);
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
            not.setEntityType(EntityTypeEnum.POST);
        else
            not.setEntityType(EntityTypeEnum.COMMENT);
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

    public void notifyDelete(Long toUserId, Class<?> clazz, Object obj){

        Long currentUserId = SecurityUtils.mustGetLoginUserId();

        if (currentUserId.equals(toUserId))
            return;
        Notification not = new Notification();
        not.setSenderId(currentUserId);
        not.setType(NotificationTypeEnum.ADMIN);
        not.setTargetId(toUserId);
        not.setEntityType(EntityTypeEnum.NULL);
        not.setStatus(StatusEnum.NORMAL);
        if (clazz == Comment.class){
            not.setTitle("Your comment has been deleted");
            Comment cmt = (Comment) obj;
            not.setContent("Your comment with content: '" + cmt.getContent()+ "' has been deleted by admin");
        } else if (clazz == DiscussPost.class){
            not.setTitle("Your post has been deleted");
            DiscussPost post = (DiscussPost) obj;
            not.setContent("Your post with title: '" + post.getTitle()+ "' has been deleted by admin");
        } else {
            return ;
        }
        // 发送删除事件
        eventProducer.send(
                KafkaTopicConstants.NOTIFICATION_DELETE,
                Event.create(KafkaEventType.DELETE, "Notification", "null", not)
        );

    }

    public void notifyRefreshUserInfo(Long toUserId){
        User user = userHandler.getUser(toUserId);
        eventProducer.send(
                KafkaTopicConstants.REFRESH,
                Event.create(KafkaEventType.REFRESH_USER_INFO, "Notification", "null", user)
        );
    }

    public void notifyNewTitle(Long toUserId, Long titleId){

        Long currentUserId = SecurityUtils.mustGetLoginUserId();
        Title title = titleService.getTitleById(titleId);
        Notification not = new Notification();
        not.setSenderId(currentUserId);
        not.setType(NotificationTypeEnum.ADMIN);
        not.setTargetId(toUserId);
        not.setEntityType(EntityTypeEnum.TITLE);
        not.setStatus(StatusEnum.NORMAL);
        not.setEntityId(titleId);
        not.setTitle("New title has been granted");
        not.setContent("You have been granted a new title: " + title.getName());
        eventProducer.send(
                KafkaTopicConstants.POP_UP_HINT,
                Event.create(
                        KafkaEventType.NEW_TITLE,
                        "Notification",
                        "null",
                        not
                )
        );
    }

}
