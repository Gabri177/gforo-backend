package com.yugao.service.business.notification;

import com.yugao.dto.notification.AdminAddNotificationDTO;
import com.yugao.dto.notification.AdminUpdateNotificationDTO;
import com.yugao.result.ResultFormat;
import org.springframework.http.ResponseEntity;

public interface NotificationBusinessService {

    ResponseEntity<ResultFormat> isUnreadNotification();

    ResponseEntity<ResultFormat> getNotification(Integer currentPage, Integer pageSize, Boolean isAsc);

    ResponseEntity<ResultFormat> publishNotification(AdminAddNotificationDTO dto);

    ResponseEntity<ResultFormat> modifyNotification(AdminUpdateNotificationDTO dto);

    ResponseEntity<ResultFormat> deleteNotification(Long id);

    ResponseEntity<ResultFormat> getAllMyNotification(Integer currentPage, Integer pageSize, Boolean isAsc);

    ResponseEntity<ResultFormat> getMyUnreadNotification(Integer currentPage, Integer pageSize);

    ResponseEntity<ResultFormat> getMyReadNotification(Integer currentPage, Integer pageSize);

    ResponseEntity<ResultFormat> readNotification(Long id);

    ResponseEntity<ResultFormat> readAllNotification();
}
