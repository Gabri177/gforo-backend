package com.yugao.dto.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddCarouselDTO {

    @NotBlank(message = "Image URL cannot be blank")
    private String imageUrl; // 图片URL

    @NotBlank(message = "Title cannot be blank")
    private String title; // 标题

    @NotBlank(message = "Description cannot be blank")
    private String description; // 描述

    private String targetUrl; // 目标URL
}
