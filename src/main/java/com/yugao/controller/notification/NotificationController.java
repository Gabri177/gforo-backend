package com.yugao.controller.notification;

import com.yugao.result.ResultFormat;
import com.yugao.service.business.notification.NotificationBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationBusinessService notificationBusinessService;

    @GetMapping
    public ResponseEntity<ResultFormat> getMyNotification(
            @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "isAsc", defaultValue = "false") Boolean isAsc
    ){

        System.out.println("currentPage = " + currentPage +
                ", pageSize = " + pageSize + ", isAsc = " + isAsc);
        return notificationBusinessService.getMyNotification(currentPage, pageSize, isAsc);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultFormat> readNotification(
            @PathVariable("id") Long id
    ){

        return notificationBusinessService.readNotification(id);
    }
}
