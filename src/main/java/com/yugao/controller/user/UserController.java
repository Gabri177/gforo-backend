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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserBusinessService userBusinessService;

    // 加入一个id参数 用来看看是查看是自己的信息还是查看别人的信息
    @PreAuthorize("hasAnyAuthority('user:info:own')")
    @GetMapping("/info")
    public ResponseEntity<ResultFormat> getUserInfo() {
        System.out.println("get user info");
        return userBusinessService.getUserInfo();
    }

    @PreAuthorize("hasAnyAuthority('user:password-change:own')")
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

    @PutMapping("/info")
    public ResponseEntity<ResultFormat> updateUserInfo(
            @Validated @RequestBody UserInfoUpdateDTO userInfoUpdateDTO) {
        return userBusinessService.updateUserInfo(userInfoUpdateDTO);
    }

    @PostMapping("/send-verify-email")
    public ResponseEntity<ResultFormat> sendChangeEmailVerify(
            @Validated @RequestBody UserVerifyEmailDTO userVerifyEmailDTO) {

        return userBusinessService.sendVerifyEmail(userVerifyEmailDTO);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ResultFormat> verifyChangeEmailCode(
            @Validated @RequestBody ActiveAccountDTO activeAccountDTO
            ) {
        System.out.println("change email verify " + activeAccountDTO);
        return userBusinessService.verifyEmail(activeAccountDTO);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<ResultFormat> logout() {
        return userBusinessService.logout();
    }

}
