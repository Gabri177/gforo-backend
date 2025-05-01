package com.yugao.controller.user;

import com.yugao.dto.auth.ActiveAccountDTO;
import com.yugao.dto.user.UserChangePasswordDTO;
import com.yugao.dto.user.UserChangeUsernameDTO;
import com.yugao.dto.user.UserInfoUpdateDTO;
import com.yugao.dto.auth.UserVerifyEmailDTO;
import com.yugao.result.ResultFormat;
import com.yugao.service.business.user.UserBusinessService;
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

    @PutMapping("/change-username")
    public ResponseEntity<ResultFormat> changeUsername(
            @Validated @RequestBody UserChangeUsernameDTO userChangeUsernameDTO) {

        return userBusinessService.changeUsername(userChangeUsernameDTO);
    }

    @PutMapping("/info") //需要修改
    public ResponseEntity<ResultFormat> updateUserInfo(
            @Validated @RequestBody UserInfoUpdateDTO userInfoUpdateDTO) {
        return userBusinessService.updateUserInfo(userInfoUpdateDTO);
    }


    /**
     * 发送邮件验证码
     * @param userVerifyEmailDTO
     * @return
     */
    @PostMapping("/send-verify-email")
    public ResponseEntity<ResultFormat> sendChangeEmailVerify(
            @Validated @RequestBody UserVerifyEmailDTO userVerifyEmailDTO) {

        return userBusinessService.sendVerifyEmail(userVerifyEmailDTO);
    }

    /**
     * 验证邮件验证码
     * @param activeAccountDTO
     * @return
     */
    @PostMapping("/verify-email")
    public ResponseEntity<ResultFormat> verifyChangeEmailCode(
            @Validated @RequestBody ActiveAccountDTO activeAccountDTO
            ) {
        System.out.println("change email verify " + activeAccountDTO);
        return userBusinessService.verifyEmail(activeAccountDTO);
    }

}
