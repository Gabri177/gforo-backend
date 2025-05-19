package com.yugao.dto.notification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdminUpdateNotificationDTO {

    @NotNull(message = "Notification ID cannot be null")
    private Long id;

    @NotBlank(message = "Notification title cannot be blank")
    private String title;

    @NotBlank(message = "Notification content cannot be blank")
    private String content;
}
