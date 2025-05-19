package com.yugao.service.data;

import com.yugao.domain.notification.NotificationRead;

import java.util.List;

public interface NotificationReadService {

    List<NotificationRead> getByUserId(Long userId);

    NotificationRead getByUserIdAndNotificationId(Long userId, Long notificationId);

    void add(NotificationRead notificationRead);
}
