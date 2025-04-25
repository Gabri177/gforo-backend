package com.yugao.controller.user;

import com.yugao.dto.UserChangePasswordDTO;
import com.yugao.dto.UserInfoUpdateDTO;
import com.yugao.dto.UserVerifyEmailDTO;
import com.yugao.result.ResultFormat;
import com.yugao.service.business.UserBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserBusinessService userBusinessService;

    @GetMapping("/info")
    public ResponseEntity<ResultFormat> getUserInfo(@RequestHeader("Authorization") String token) {

        return userBusinessService.getUserInfo(token);
    }

    @PutMapping ("/change-password")
    public ResponseEntity<ResultFormat> changePasswoed(
            @Validated @RequestBody UserChangePasswordDTO userChangePasswordDTO) {

        return userBusinessService.changePassword(userChangePasswordDTO);
    }

    @PutMapping("/info")
    public ResponseEntity<ResultFormat> updateUserInfo(
            @Validated @RequestBody UserInfoUpdateDTO userInfoUpdateDTO) {
        return userBusinessService.updateUserInfo(userInfoUpdateDTO);
    }


    /**
     * 发送验证邮件
     * @param userVerifyEmailDTO
     * @return
     */
    @PostMapping("/send-verify-email")
    public ResponseEntity<ResultFormat> verifyEmail(
            @Validated @RequestBody UserVerifyEmailDTO userVerifyEmailDTO) {

        return userBusinessService.sendVerifyEmail(userVerifyEmailDTO);
    }

    /**
     * 当前端打开验证链接 前端会将token作为参数传递给后端 验证token是否正确
     * @param userId
     * @param token
     * @return
     */
    @GetMapping("/verify-email/{userId}/{token}")
    public ResponseEntity<ResultFormat> verifyEmail(@PathVariable String userId, @PathVariable String token) {

        return userBusinessService.verifyEmail(userId, token);
    }

}
