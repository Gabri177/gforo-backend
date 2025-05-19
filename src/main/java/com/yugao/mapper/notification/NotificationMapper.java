package com.yugao.mapper.notification;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yugao.domain.notification.Notification;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {
}
