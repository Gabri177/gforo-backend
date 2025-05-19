package com.yugao.converter;

import com.yugao.domain.notification.Notification;
import com.yugao.dto.notification.AdminAddNotificationDTO;
import com.yugao.dto.notification.AdminUpdateNotificationDTO;
import com.yugao.enums.NotificationEntityTypeEnum;
import com.yugao.enums.NotificationTypeEnum;
import com.yugao.enums.StatusEnum;
import com.yugao.vo.notification.AdminNotificationVO;
import com.yugao.vo.notification.UserNotificationVO;

public class NotificationConverter {

    public static AdminNotificationVO toAdminNotificationVO(Notification notification){
        AdminNotificationVO adminNotificationVO = new AdminNotificationVO();
        adminNotificationVO.setId(notification.getId());
        adminNotificationVO.setTitle(notification.getTitle());
        adminNotificationVO.setContent(notification.getContent());
        adminNotificationVO.setCreateTime(notification.getCreateTime());
        return adminNotificationVO;
    }

    public static UserNotificationVO toUserNotificationVO(Notification notification){
        UserNotificationVO userNotificationVO = new UserNotificationVO();
        userNotificationVO.setId(notification.getId());
        userNotificationVO.setTitle(notification.getTitle());
        userNotificationVO.setContent(notification.getContent());
        userNotificationVO.setCreateTime(notification.getCreateTime());
        userNotificationVO.setType(notification.getType().getValue());
        userNotificationVO.setSenderId(notification.getSenderId());
        userNotificationVO.setEntityId(notification.getEntityId());
        userNotificationVO.setEntityType(notification.getEntityType());
        return userNotificationVO;
    }

    public static Notification toNotification(
            AdminAddNotificationDTO adminAddNotificationDTO,
            Long senderId,
            Long targetId
            ){
        Notification notification = new Notification();
        notification.setTitle(adminAddNotificationDTO.getTitle());
        notification.setContent(adminAddNotificationDTO.getContent());
        notification.setType(NotificationTypeEnum.SYSTEM);
        notification.setStatus(StatusEnum.NORMAL);
        notification.setSenderId(senderId);
        notification.setTargetId(targetId);
        notification.setEntityId(0L);
        notification.setEntityType(NotificationEntityTypeEnum.NULL);
        return notification;
    }

    public static Notification toNotification(
            AdminUpdateNotificationDTO dto
    ) {
        Notification notification = new Notification();
        notification.setId(dto.getId());
        notification.setTitle(dto.getTitle());
        notification.setContent(dto.getContent());
        return notification;
    }

}
