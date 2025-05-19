package com.yugao.service.data;

import com.yugao.domain.notification.Notification;
import com.yugao.enums.NotificationEntityTypeEnum;
import com.yugao.enums.NotificationTypeEnum;

import java.util.List;

public interface NotificationService {

    Long getNotificationCount(Long targetId);

    Notification getNotificationById(Long id);

    List<Notification> getNotifications(Long targetId, Integer currentPage, Integer pageSize, Boolean isAsc);

    void addNotification(Notification notification);

    void deleteNotification(Long notificationId);

    void updateNotification(Notification notification);

    Notification get(Long senderId,
            Long receiverId,
            Long entityId,
            NotificationEntityTypeEnum entityType,
            NotificationTypeEnum type);
    void delete(Long senderId,
                Long receiverId,
                Long entityId,
                NotificationEntityTypeEnum entityType,
                NotificationTypeEnum type);

}
