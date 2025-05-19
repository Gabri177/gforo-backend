package com.yugao.service.business.notification.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yugao.converter.CommentConverter;
import com.yugao.converter.DiscussPostConverter;
import com.yugao.converter.NotificationConverter;
import com.yugao.converter.UserConverter;
import com.yugao.domain.notification.Notification;
import com.yugao.domain.notification.NotificationRead;
import com.yugao.dto.notification.AdminAddNotificationDTO;
import com.yugao.dto.notification.AdminUpdateNotificationDTO;
import com.yugao.enums.NotificationEntityTypeEnum;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.mapper.notification.NotificationMapper;
import com.yugao.mapper.notification.NotificationReadMapper;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.business.notification.NotificationBusinessService;
import com.yugao.service.data.*;
import com.yugao.service.handler.EventHandler;
import com.yugao.util.security.SecurityUtils;
import com.yugao.vo.comment.SimpleCommentVO;
import com.yugao.vo.notification.AdminNotificationPageVO;
import com.yugao.vo.notification.AdminNotificationVO;
import com.yugao.vo.notification.UserNotificationPageVO;
import com.yugao.vo.notification.UserNotificationVO;
import com.yugao.vo.post.SimpleDiscussPostVO;
import com.yugao.vo.user.SimpleUserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class NotificationBusinessServiceImpl implements NotificationBusinessService {

    private final UserService userService;
    private final DiscussPostService discussPostService;
    private final CommentService commentService;
    private final NotificationReadService notificationReadService;
    private final NotificationService notificationService;
    private final EventHandler eventHandler;

    @Override
    public ResponseEntity<ResultFormat> getNotification(Integer currentPage, Integer pageSize, Boolean isAsc) {

        Long totalRows = notificationService.getNotificationCount(0L);
        Page<Notification> page = new Page<>(currentPage, pageSize);
        List<AdminNotificationVO> res = notificationService.getNotifications(0L, currentPage, pageSize, isAsc)
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
    public ResponseEntity<ResultFormat> getMyNotification(Integer currentPage, Integer pageSize, Boolean isAsc) {

        Long userId = SecurityUtils.mustGetLoginUserId();
        List<UserNotificationVO> res = notificationService.getNotifications(userId, currentPage, pageSize, isAsc)
                .stream()
                .map(NotificationConverter::toUserNotificationVO)
                .toList();
        System.out.println("当前的通知列表为" + res);
        // 获取是否已读
        List<Long> readIds = notificationReadService.getByUserId(userId)
                .stream()
                .map(NotificationRead::getNotificationId)
                .toList();
        // 获取作者信息
        List<Long> authorIds = res.stream()
                .map(UserNotificationVO::getSenderId)
                .distinct()
                .toList();
        Map<Long, SimpleUserVO> authorMap = userService.getUsersByIds(authorIds)
                .stream()
                .map(UserConverter::toSimpleVO)
                .collect(Collectors.toMap(SimpleUserVO::getId, Function.identity()));
        // 获取实体信息
        // 获取实体类型是comment的commentId的list
        System.out.println("组装实体类型是comment的commentId的list");
        Map<Long, SimpleCommentVO> commentMap = commentService.findCommentsByIds(
                res.stream()
                        .filter(notification -> notification.getEntityType() == NotificationEntityTypeEnum.COMMENT)
                        .map(UserNotificationVO::getEntityId)
                        .distinct()
                        .toList()
        )
                .stream()
                .map(CommentConverter::toSimpleCommentVO)
                .collect(Collectors.toMap(SimpleCommentVO::getId, Function.identity()));

        // 获取实体类型是post的postId的list

        // 1）通知中直接是帖子的 entityId
        List<Long> postIdsFromNotification = res.stream()
                .filter(notification -> notification.getEntityType() == NotificationEntityTypeEnum.POST)
                .map(UserNotificationVO::getEntityId)
                .toList();
        // 2）评论中关联的 postId
        List<Long> postIdsFromComments = commentMap.values().stream()
                .map(SimpleCommentVO::getPostId)
                .toList();
        // 合并并去重
        Set<Long> allPostIds = Stream.concat(postIdsFromNotification.stream(), postIdsFromComments.stream())
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        System.out.println("组装实体类型是post的postId的list");
        Map<Long, SimpleDiscussPostVO> postMap = discussPostService.getDiscussPostsByIds(new ArrayList<>(allPostIds))
                .stream()
                .map(DiscussPostConverter::toSimpleDiscussPostVO)
                .collect(Collectors.toMap(SimpleDiscussPostVO::getId, Function.identity()));
        // 装配已读以及作者信息
        System.out.println("装配已读以及作者信息");
        res.forEach(notification -> {
            notification.setIsRead(readIds.contains(notification.getId()));
            notification.setAuthor(authorMap.get(notification.getSenderId()));
            if (notification.getEntityType() == NotificationEntityTypeEnum.COMMENT) {
                notification.setComment(commentMap.get(notification.getEntityId()));
                notification.setPost(postMap.get(commentMap.get(notification.getEntityId()).getPostId()));
            } else if (notification.getEntityType() == NotificationEntityTypeEnum.POST) {
                notification.setPost(postMap.get(notification.getEntityId()));
            }
        });
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
        if (notification.getTargetId() != 0L && !notification.getTargetId().equals(userId))
            return ResultResponse.error(ResultCodeEnum.NOTIFICATION_NOT_FOUND);
        NotificationRead notificationRead = notificationReadService.getByUserIdAndNotificationId(userId, id);
        if (notificationRead == null)
            notificationReadService.add(
                    NotificationRead.builder()
                            .notificationId(id)
                            .userId(userId)
                            .build()
            );
        return ResultResponse.success(null);
    }
}
