package com.yugao.mapper.notification;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yugao.domain.notification.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {

    @Select("""
    SELECT n.*
    FROM notification n
    LEFT JOIN notification_read r
        ON n.id = r.notification_id AND r.user_id = #{targetId}
    WHERE r.notification_id IS NULL
      AND (
        (n.target_id = #{targetId} AND n.status = 1)
        OR (n.type = 5 AND n.status = 1)
      )
    ORDER BY n.create_time DESC
    """)
    List<Notification> getUnreadNotifications(
            Page<?> page,
            @Param("targetId") Long targetId
    );

    @Select("""
    SELECT n.*
    FROM notification n
    INNER JOIN notification_read r
        ON n.id = r.notification_id AND r.user_id = #{targetId} AND n.status = 1
    ORDER BY n.create_time DESC
    """)
    List<Notification> getReadNotifications(
            Page<?> page,
            @Param("targetId") Long targetId
    );

    @Select("""
    SELECT COUNT(*)
    FROM notification n
    INNER JOIN notification_read r
        ON n.id = r.notification_id
    WHERE r.user_id = #{targetId}
      AND n.status = 1
    """)
    Long getReadNotificationCount(@Param("targetId") Long targetId);


    @Select("""
    SELECT COUNT(*)
    FROM notification n
    LEFT JOIN notification_read r
        ON n.id = r.notification_id AND r.user_id = #{targetId}
    WHERE r.notification_id IS NULL
      AND (
        (n.target_id = #{targetId} AND n.status = 1)
        OR (n.type = 5 AND n.status = 1)
      )
    """)
    Long getUnreadNotificationCount(@Param("targetId") Long targetId);

}
