package com.yugao.controller.notification;

import com.yugao.result.ResultFormat;
import com.yugao.service.business.notification.NotificationBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationBusinessService notificationBusinessService;

    @PreAuthorize("hasAnyAuthority('notification:all:own')")
    @GetMapping
    public ResponseEntity<ResultFormat> getAllMyNotification(
            @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "isAsc", defaultValue = "false") Boolean isAsc
    ){

        System.out.println("currentPage = " + currentPage +
                ", pageSize = " + pageSize + ", isAsc = " + isAsc);
        return notificationBusinessService.getAllMyNotification(currentPage, pageSize, isAsc);
    }

    @PreAuthorize("hasAnyAuthority('notification:unread:own')")
    @GetMapping("/unread")
    public ResponseEntity<ResultFormat> getMyUnreadNotification(
            @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ){

        System.out.println("currentPage = " + currentPage +
                ", pageSize = " + pageSize );
        return notificationBusinessService.getMyUnreadNotification(currentPage, pageSize);
    }

    @PreAuthorize("hasAnyAuthority('notification:read:own')")
    @GetMapping("/read")
    public ResponseEntity<ResultFormat> getMyReadNotification(
            @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ){

        System.out.println("currentPage = " + currentPage +
                ", pageSize = " + pageSize);
        return notificationBusinessService.getMyReadNotification(currentPage, pageSize);
    }


    @PreAuthorize("hasAnyAuthority('notification:mark-read-system:own')")
    @PutMapping("/{id}")
    public ResponseEntity<ResultFormat> readNotification(
            @PathVariable("id") Long id
    ){

        return notificationBusinessService.readNotification(id);
    }

    @PreAuthorize("hasAnyAuthority('notification:mark-read-all:own')")
    @PutMapping
    public ResponseEntity<ResultFormat> readAllNotification(){

        return notificationBusinessService.readAllNotification();
    }
}
