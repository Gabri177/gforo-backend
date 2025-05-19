package com.yugao.dto.notification;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminAddNotificationDTO {

    @NotBlank(message = "Notification title cannot be blank")
    private String title;

    @NotBlank(message = "Notification content cannot be blank")
    private String content;
}
