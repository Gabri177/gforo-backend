package com.yugao.controller;

import com.yugao.converter.UserConverter;
import com.yugao.domain.User;
import com.yugao.dto.UserChangePasswordDTO;
import com.yugao.result.ResultCode;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.UserService;
import com.yugao.util.EncryptedUtil;
import com.yugao.vo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public ResponseEntity<ResultFormat> getUserInfo(@RequestHeader("Authorization") String token) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getPrincipal().toString());

        User userDomain = userService.getUserById(userId);

        if (userDomain == null) {
            return ResultResponse.error(ResultCode.USER_NOT_FOUND, "user not found");
        }

        UserInfoVO userInfoVO = UserConverter.toVO(userDomain);
        return ResultResponse.success(userInfoVO, "userinfo get success");
    }

    @PutMapping ("/change-password")
    public ResponseEntity<ResultFormat> changePasswoed(
            @Validated @RequestBody UserChangePasswordDTO userChangePasswordDTO) {

        System.out.println(userChangePasswordDTO);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getPrincipal().toString());

        User userDomain = userService.getUserById(userId);

        if (userDomain == null) {
            return ResultResponse.error(ResultCode.USER_NOT_FOUND, "user not found");
        }

        if (userChangePasswordDTO.getOldPassword().equals(userChangePasswordDTO.getNewPassword())) {
            return ResultResponse.error(ResultCode.NEW_PASSWORD_SAME, "tow password is the same");
        }

        String oldPassword = EncryptedUtil.md5(userChangePasswordDTO.getOldPassword() + userDomain.getSalt());
        String newPassword = EncryptedUtil.md5(userChangePasswordDTO.getNewPassword() + userDomain.getSalt());
        if (!userDomain.getPassword().equals(oldPassword)) {
            return ResultResponse.error(ResultCode.OLD_PASSWORD_INCORRECT, "old password is error");
        }

        if (userDomain.getPassword().equals(newPassword)) {
            return ResultResponse.error(ResultCode.NEW_PASSWORD_SAME, "new password is the same as old password");
        }

        userService.updatePassword(userId, newPassword);
        return ResultResponse.success("password change success");
    }

    @PutMapping("/info")
    public ResponseEntity<ResultFormat> updateUserInfo(@RequestBody UserInfoVO userInfoVO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getPrincipal().toString());

        User userDomain = userService.getUserById(userId);

        if (userDomain == null) {
            return ResultResponse.error(ResultCode.USER_NOT_FOUND, "user not found");
        }

        userDomain.setUsername(userInfoVO.getUsername());
        userDomain.setBio(userInfoVO.getBio());
        userDomain.setHeaderUrl(userInfoVO.getHeaderUrl());

        userService.updateUser(userDomain);

        return ResultResponse.success("userinfo update success");
    }

}
