package com.yugao.controller.user;

import com.yugao.dto.auth.ActiveAccountDTO;
import com.yugao.dto.user.UserChangePasswordDTO;
import com.yugao.dto.user.UserChangeUsernameDTO;
import com.yugao.dto.user.UserInfoUpdateDTO;
import com.yugao.dto.auth.UserVerifyEmailDTO;
import com.yugao.result.ResultFormat;
import com.yugao.service.business.user.UserBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserBusinessService userBusinessService;

    @PreAuthorize("hasAnyAuthority('user:info:own')")
    @GetMapping("/info")
    public ResponseEntity<ResultFormat> getUserInfo(
            @RequestParam(name="userId", required = false) Long userId
    ) {
//        System.out.println("get user info: " + userId);
        return userBusinessService.getUserInfo(userId);
    }

    @PreAuthorize("hasAnyAuthority('user:password-change:own')")
    @PutMapping ("/change-password")
    public ResponseEntity<ResultFormat> changePasswoed(
            @Validated @RequestBody UserChangePasswordDTO userChangePasswordDTO) {

        return userBusinessService.changePassword(userChangePasswordDTO);
    }

    @PreAuthorize("hasAnyAuthority('user:username-change:own')")
    @PutMapping("/change-username")
    public ResponseEntity<ResultFormat> changeUsername(
            @Validated @RequestBody UserChangeUsernameDTO userChangeUsernameDTO) {

        return userBusinessService.changeUsername(userChangeUsernameDTO);
    }

    @PreAuthorize("hasAnyAuthority('user:update-info:own')")
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
//        System.out.println("change email verify " + activeAccountDTO);
        return userBusinessService.verifyEmail(activeAccountDTO);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<ResultFormat> logout(
            @RequestHeader(name="gforo-deviceId", required = false) String deviceId
    ) {
        return userBusinessService.logout(deviceId);
    }

    @GetMapping("/comments")
    public ResponseEntity<ResultFormat> getComments(
            @RequestParam(name="currentPage", defaultValue = "1") Integer currentPage,
            @RequestParam(name="pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name="isAsc", defaultValue = "false") Boolean isAsc
    ) {
//        System.out.println("get comments: " + currentPage + " " + pageSize + " " + isAsc);
        return userBusinessService.getCommentsByUserId(currentPage, pageSize, isAsc);
    }

    @GetMapping("/posts")
    public ResponseEntity<ResultFormat> getPosts(
            @RequestParam(name="userId", required = false) Long userId,
            @RequestParam(name="currentPage", defaultValue = "1") Integer currentPage,
            @RequestParam(name="pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name="isAsc", defaultValue = "false") Boolean isAsc
    ) {
//        System.out.println("get posts: " + currentPage + " " + pageSize + " " + isAsc);
        return userBusinessService.getPostsByUserId(userId, currentPage, pageSize, isAsc);
    }

}
