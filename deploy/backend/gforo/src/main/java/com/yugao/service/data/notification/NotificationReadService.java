package com.yugao.service.data.notification;

import com.yugao.domain.notification.NotificationRead;

import java.util.List;

public interface NotificationReadService {

    List<NotificationRead> getByUserId(Long userId);

    NotificationRead getByUserIdAndNotificationId(Long userId, Long notificationId);

    void add(NotificationRead notificationRead);

    void add(List<NotificationRead> notificationReads);

    void deleteByNotificationId(Long notificationId);
}
