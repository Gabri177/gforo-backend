package com.yugao.vo.notification;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserNotificationPageVO {

    private Integer currentPage;
    private Integer pageSize;
    private Long totalRows;
    private List<UserNotificationVO> notificationList;
}
