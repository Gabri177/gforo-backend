package com.yugao.service.data.notification.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yugao.domain.notification.Notification;
import com.yugao.enums.EntityTypeEnum;
import com.yugao.enums.NotificationTypeEnum;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.enums.StatusEnum;
import com.yugao.exception.BusinessException;
import com.yugao.mapper.notification.NotificationMapper;
import com.yugao.service.data.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationMapper notificationMapper;

    @Override
    public Long getNotificationCount(Long targetId) {
        LambdaQueryWrapper<Notification> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(targetId != 0L, Notification::getTargetId, targetId, 0L);
        queryWrapper.eq(targetId == 0L, Notification::getType, NotificationTypeEnum.SYSTEM);
        queryWrapper.ne(Notification::getStatus, StatusEnum.DELETED);
        return notificationMapper.selectCount(queryWrapper);
    }

    @Override
    public Long getReadNotificationCount(Long targetId) {

        return notificationMapper.getReadNotificationCount(targetId);
    }

    @Override
    public Long getUnreadNotificationCount(Long targetId) {

        return notificationMapper.getUnreadNotificationCount(targetId);
    }

    @Override
    public Notification getNotificationById(Long id) {

        Notification notification = notificationMapper.selectById(id);
        if (notification == null) {
            throw new BusinessException(ResultCodeEnum.NOTIFICATION_NOT_FOUND);
        }
        return notification;
    }

    @Override
    public List<Notification> getAllNotifications(Long targetId) {

        LambdaQueryWrapper<Notification> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Notification::getTargetId, targetId);
        queryWrapper.ne(Notification::getStatus, StatusEnum.DELETED);
        return notificationMapper.selectList(queryWrapper);
    }

    @Override
    public List<Notification> getAllNotifications(Long targetId, Integer currentPage, Integer pageSize, Boolean isAsc) {
        LambdaQueryWrapper<Notification> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(targetId != 0L, Notification::getTargetId, targetId , 0L);
        queryWrapper.eq(targetId == 0L, Notification::getType, NotificationTypeEnum.SYSTEM);
        queryWrapper.ne(Notification::getStatus, StatusEnum.DELETED);
        queryWrapper.orderBy(true, isAsc, Notification::getCreateTime);
        Page<Notification> page = new Page<>(currentPage, pageSize);
        return notificationMapper.selectPage(page, queryWrapper).getRecords();

    }

    @Override
    public List<Notification> getReadNotifications(Long targetId, Integer currentPage, Integer pageSize) {

        Page<Notification> page = new Page<>(currentPage, pageSize);
        List<Notification> list = notificationMapper.getReadNotifications(page, targetId);
        return list;
    }

    @Override
    public List<Notification> getUnreadNotifications(Long targetId, Integer currentPage, Integer pageSize) {

        Page<Notification> page = new Page<>(currentPage, pageSize);
        List<Notification> list = notificationMapper.getUnreadNotifications(page, targetId);
        return list;
    }

    @Override
    public void addNotification(Notification not) {

        LambdaQueryWrapper<Notification> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Notification::getSenderId, not.getSenderId());
        queryWrapper.eq(Notification::getTargetId, not.getTargetId());
        queryWrapper.eq(Notification::getEntityId, not.getEntityId());
        queryWrapper.eq(Notification::getEntityType, not.getEntityType());
        queryWrapper.ne(Notification::getType, NotificationTypeEnum.SYSTEM);
        queryWrapper.ne(Notification::getStatus, StatusEnum.DELETED);

        Long count = notificationMapper.selectCount(queryWrapper);
        if (count == 0)
            notificationMapper.insert(not);
    }

    @Override
    public void deleteNotification(Long notificationId) {

        Notification notification = notificationMapper.selectById(notificationId);
        if (notification == null) {
            return ;
        }
        notification.setStatus(StatusEnum.DELETED);
        if (notificationMapper.updateById(notification) <= 0) {
            throw new BusinessException(ResultCodeEnum.SQL_EXCEPTION);
        }
    }

    @Override
    public void updateNotification(Notification notification) {

        if (notificationMapper.updateById(notification) <= 0) {
            throw new BusinessException(ResultCodeEnum.SQL_EXCEPTION);
        }
    }

    @Override
    public Notification get(Long senderId, Long receiverId, Long entityId, EntityTypeEnum entityType, NotificationTypeEnum type) {

        LambdaQueryWrapper<Notification> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Notification::getSenderId, senderId);
        queryWrapper.eq(Notification::getTargetId, receiverId);
        queryWrapper.eq(Notification::getEntityId, entityId);
        queryWrapper.eq(Notification::getEntityType, entityType);
        queryWrapper.ne(Notification::getStatus, StatusEnum.DELETED);
        queryWrapper.eq(Notification::getType, type);
        return notificationMapper.selectOne(queryWrapper);
    }

    @Override
    public void delete(Long senderId, Long receiverId,
                       Long entityId, EntityTypeEnum entityType,
                       NotificationTypeEnum type) {

        Notification not = get(senderId, receiverId, entityId, entityType, type);
        if (not == null) {
            return;
        }
        not.setStatus(StatusEnum.DELETED);
        notificationMapper.updateById(not);
    }
}
