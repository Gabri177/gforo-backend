package com.yugao.service.data.notification.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yugao.domain.notification.NotificationRead;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.exception.BusinessException;
import com.yugao.mapper.notification.NotificationReadMapper;
import com.yugao.service.data.notification.NotificationReadService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.executor.BatchResult;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationReadServiceImpl implements NotificationReadService {

    private final NotificationReadMapper notificationReadMapper;

    @Override
    public List<NotificationRead> getByUserId(Long userId) {

        LambdaQueryWrapper<NotificationRead> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(NotificationRead::getUserId, userId);
        return notificationReadMapper.selectList(queryWrapper);
    }

    @Override
    public NotificationRead getByUserIdAndNotificationId(Long userId, Long notificationId) {

        LambdaQueryWrapper<NotificationRead> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(NotificationRead::getUserId, userId);
        queryWrapper.eq(NotificationRead::getNotificationId, notificationId);
        return notificationReadMapper.selectOne(queryWrapper);
    }

    @Override
    public void add(NotificationRead notificationRead) {

        if (notificationReadMapper.insert(notificationRead) <= 0) {
            throw new BusinessException(ResultCodeEnum.SQL_EXCEPTION);
        }
    }

    @Override
    public void add(List<NotificationRead> notificationReads) {

        List<BatchResult> res = notificationReadMapper.insert(notificationReads);
        long total = res.stream()
                .flatMapToInt(result -> Arrays.stream(result.getUpdateCounts()))
                .filter(i -> i == 1)
                .count();
        if (total != notificationReads.size())
            throw new BusinessException(ResultCodeEnum.SQL_UPDATING_ERROR);
    }

    @Override
    public void deleteByNotificationId(Long notificationId) {

        LambdaQueryWrapper<NotificationRead> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(NotificationRead::getNotificationId, notificationId);
        notificationReadMapper.delete(queryWrapper);
    }
}
