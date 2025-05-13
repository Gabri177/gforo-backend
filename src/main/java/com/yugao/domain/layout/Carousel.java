package com.yugao.domain.layout;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("carousel")
public class Carousel {

//    id BIGINT PRIMARY KEY AUTO_INCREMENT,
//    image_url VARCHAR(255) NOT NULL,
//    title VARCHAR(100),
//    description VARCHAR(500),
//    target_url VARCHAR(255),
//    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
//    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP

    @TableId(type = IdType.AUTO)
    private Long id;

    private String imageUrl; // 图片URL

    private String title; // 标题

    private String description; // 描述

    private String targetUrl; // 目标URL

    @TableField(fill = FieldFill.INSERT)
    private String createdAt; // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updatedAt; // 更新时间
}
