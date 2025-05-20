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
    public ResponseEntity<ResultFormat> getAllMyNotification(
            @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "isAsc", defaultValue = "false") Boolean isAsc
    ){

        System.out.println("currentPage = " + currentPage +
                ", pageSize = " + pageSize + ", isAsc = " + isAsc);
        return notificationBusinessService.getAllMyNotification(currentPage, pageSize, isAsc);
    }

    @GetMapping("/unread")
    public ResponseEntity<ResultFormat> getMyUnreadNotification(
            @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "isAsc", defaultValue = "false") Boolean isAsc
    ){

        System.out.println("currentPage = " + currentPage +
                ", pageSize = " + pageSize + ", isAsc = " + isAsc);
        return notificationBusinessService.getMyUnreadNotification(currentPage, pageSize, isAsc);
    }

    @GetMapping("/read")
    public ResponseEntity<ResultFormat> getMyReadNotification(
            @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "isAsc", defaultValue = "false") Boolean isAsc
    ){

        System.out.println("currentPage = " + currentPage +
                ", pageSize = " + pageSize + ", isAsc = " + isAsc);
        return notificationBusinessService.getMyReadNotification(currentPage, pageSize, isAsc);
    }



    @PutMapping("/{id}")
    public ResponseEntity<ResultFormat> readNotification(
            @PathVariable("id") Long id
    ){

        return notificationBusinessService.readNotification(id);
    }

    @PutMapping
    public ResponseEntity<ResultFormat> readAllNotification(){

        return notificationBusinessService.readAllNotification();
    }
}
