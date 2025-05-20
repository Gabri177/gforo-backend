package com.yugao.service.business.notification.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yugao.converter.NotificationConverter;
import com.yugao.domain.notification.Notification;
import com.yugao.domain.notification.NotificationRead;
import com.yugao.dto.notification.AdminAddNotificationDTO;
import com.yugao.dto.notification.AdminUpdateNotificationDTO;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.builder.VOBuilder;
import com.yugao.service.business.notification.NotificationBusinessService;
import com.yugao.service.data.notification.NotificationReadService;
import com.yugao.service.data.notification.NotificationService;
import com.yugao.service.handler.EventHandler;
import com.yugao.util.security.SecurityUtils;
import com.yugao.vo.notification.AdminNotificationPageVO;
import com.yugao.vo.notification.AdminNotificationVO;
import com.yugao.vo.notification.UserNotificationPageVO;
import com.yugao.vo.notification.UserNotificationVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class NotificationBusinessServiceImpl implements NotificationBusinessService {

    private final NotificationReadService notificationReadService;
    private final NotificationService notificationService;
    private final EventHandler eventHandler;
    private final VOBuilder voBuilder;

    @Override
    public ResponseEntity<ResultFormat> getNotification(Integer currentPage, Integer pageSize, Boolean isAsc) {

        Long totalRows = notificationService.getNotificationCount(0L);
        Page<Notification> page = new Page<>(currentPage, pageSize);
        List<AdminNotificationVO> res = notificationService.getAllNotifications(0L, currentPage, pageSize, isAsc)
                .stream()
                .map(NotificationConverter::toAdminNotificationVO)
                .toList();
        return ResultResponse.success(
                AdminNotificationPageVO.builder()
                        .currentPage(currentPage)
                        .pageSize(pageSize)
                        .totalRows(totalRows)
                        .notificationList(res)
                        .build()
        );
    }

    @Override
    public ResponseEntity<ResultFormat> publishNotification(AdminAddNotificationDTO dto) {

        Long userId = SecurityUtils.mustGetLoginUserId();
        Notification notification = NotificationConverter.toNotification(dto, userId, 0L);
        notificationService.addNotification(notification);
        eventHandler.notifySystem(notification);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> modifyNotification(AdminUpdateNotificationDTO dto) {

        Notification notification = NotificationConverter.toNotification(dto);
        notificationService.updateNotification(notification);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> deleteNotification(Long id) {

        notificationService.deleteNotification(id);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> getAllMyNotification(Integer currentPage, Integer pageSize, Boolean isAsc) {

        Long userId = SecurityUtils.mustGetLoginUserId();
        List<UserNotificationVO> res = notificationService.getAllNotifications(userId, currentPage, pageSize, isAsc)
                .stream()
                .map(NotificationConverter::toUserNotificationVO)
                .toList();
        /// //////////////////////////////////

        System.out.println("userId = " + userId);
        System.out.println("allIds = " + res.stream().map(UserNotificationVO::getId).toList());


        /// //////////////////////////////////
        res = voBuilder.assembleUserNotificationListVO(res, userId);
        return ResultResponse.success(
                UserNotificationPageVO.builder()
                        .currentPage(currentPage)
                        .pageSize(pageSize)
                        .totalRows(notificationService.getNotificationCount(userId))
                        .notificationList(res)
                        .build()
        );
    }

    @Override
    public ResponseEntity<ResultFormat> getMyUnreadNotification(Integer currentPage, Integer pageSize, Boolean isAsc) {
        Long userId = SecurityUtils.mustGetLoginUserId();
        List<UserNotificationVO> res = notificationService.getUnreadNotifications(userId, currentPage, pageSize, isAsc)
                .stream()
                .map(NotificationConverter::toUserNotificationVO)
                .toList();

        /// //////////////////////////////
        System.out.println("userId = " + userId);
        System.out.println("unReadIds = " + res.stream().map(UserNotificationVO::getId).toList());

        /// /////////////////////////////
        res = voBuilder.assembleUserNotificationListVO(res, userId);
        return ResultResponse.success(
                UserNotificationPageVO.builder()
                        .currentPage(currentPage)
                        .pageSize(pageSize)
                        .totalRows(notificationService.getNotificationCount(userId))
                        .notificationList(res)
                        .build()
        );
    }

    @Override
    public ResponseEntity<ResultFormat> getMyReadNotification(Integer currentPage, Integer pageSize, Boolean isAsc) {
        Long userId = SecurityUtils.mustGetLoginUserId();
        List<UserNotificationVO> res = notificationService.getReadNotifications(userId, currentPage, pageSize, isAsc)
                .stream()
                .map(NotificationConverter::toUserNotificationVO)
                .toList();
        res = voBuilder.assembleUserNotificationListVO(res, userId);
        return ResultResponse.success(
                UserNotificationPageVO.builder()
                        .currentPage(currentPage)
                        .pageSize(pageSize)
                        .totalRows(notificationService.getNotificationCount(userId))
                        .notificationList(res)
                        .build()
        );
    }

    @Override
    public ResponseEntity<ResultFormat> readNotification(Long id) {

        Long userId = SecurityUtils.mustGetLoginUserId();
        Notification notification = notificationService.getNotificationById(id);
        System.out.println("notification = " + notification);
        System.out.println("notification.getTargetId() = " + notification.getTargetId());
        System.out.println("userId = " + userId);
        if (notification.getTargetId() != 0L && !notification.getTargetId().equals(userId))
            return ResultResponse.error(ResultCodeEnum.NOTIFICATION_NOT_FOUND);
        NotificationRead notificationRead = notificationReadService.getByUserIdAndNotificationId(userId, id);
        if (notificationRead == null)
            notificationReadService.add(
                    NotificationRead.builder()
                            .notificationId(id)
                            .userId(userId)
                            .readTime(new Date())
                            .build()
            );
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> readAllNotification() {

        Long userId = SecurityUtils.mustGetLoginUserId();
        List<Long> readIds = notificationReadService.getByUserId(userId)
                .stream()
                .map(NotificationRead::getNotificationId)
                .toList();
        List<Long> allIds = notificationService.getAllNotifications(userId)
                .stream()
                .map(Notification::getId)
                .toList();
        List<Long> unreadIds = allIds.stream()
                .filter(id -> !readIds.contains(id))
                .toList();

        if (!unreadIds.isEmpty()) {
            List<NotificationRead> notificationReads = unreadIds.stream()
                    .map(id -> NotificationRead.builder()
                            .notificationId(id)
                            .userId(userId)
                            .readTime(new Date())
                            .build())
                    .toList();
            notificationReadService.add(notificationReads);
        }
        return ResultResponse.success(null);
    }
}
