package com.yugao.controller.admin;

import com.yugao.dto.notification.AdminAddNotificationDTO;
import com.yugao.dto.notification.AdminUpdateNotificationDTO;
import com.yugao.result.ResultFormat;
import com.yugao.service.business.notification.NotificationBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/notification")
@RequiredArgsConstructor
public class AdminNotificationController {

    private final NotificationBusinessService notificationBusinessService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('sys-notification:info:any')")
    public ResponseEntity<ResultFormat> getNotification(
            @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "isAsc", defaultValue = "false") Boolean isAsc
    ){

        return notificationBusinessService.getNotification(currentPage, pageSize, isAsc);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('sys-notification:publish:any')")
    public ResponseEntity<ResultFormat> publishNotification(
            @Validated @RequestBody AdminAddNotificationDTO dto
            ){

        return notificationBusinessService.publishNotification(dto);
    }

    // 这里考虑要不要这个接口
    // 逻辑上来看 如果发布者更改了通知内容 那么“notification_read"
    // 中所有对应id的条目都要删除
    // TODO: 如果发布者更改了通知内容 中所有对应id的条目都要删除
    @PutMapping
    @PreAuthorize("hasAnyAuthority('sys-notification:modify:any')")
    public ResponseEntity<ResultFormat> modifyNotification(
            @Validated @RequestBody AdminUpdateNotificationDTO dto
            ){

        return notificationBusinessService.modifyNotification(dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('sys-notification:delete:any')")
    public ResponseEntity<ResultFormat> deleteNotification(
            @PathVariable("id") Long id
    ){

        return notificationBusinessService.deleteNotification(id);
    }
}
