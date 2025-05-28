package com.yugao.vo.notification;

import lombok.Data;

import java.util.Date;

@Data
public class AdminNotificationVO {

    private Long id;
    private String title;
    private String content;
    private Date createTime;
}
