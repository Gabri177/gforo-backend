package com.yugao.controller.auth;

import com.yugao.dto.auth.RefreshTokenDTO;
import com.yugao.dto.auth.UserRegisterDTO;
import com.yugao.dto.auth.UserForgetPasswordDTO;
import com.yugao.dto.auth.UserForgetPasswordResetDTO;
import com.yugao.result.ResultFormat;
import com.yugao.service.business.auth.AuthService;
import com.yugao.validation.ValidationGroups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 验证是否通过验证 并登录
     * @param userRegisterDTO
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<ResultFormat> login(
            @Validated({ValidationGroups.Login.class}) @RequestBody UserRegisterDTO userRegisterDTO) {
        return authService.login(userRegisterDTO);
    }

    /**
     * 刷新token
     * @param refreshTokenDTO
     * @return
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<ResultFormat> refresh(
           @Validated @RequestBody RefreshTokenDTO refreshTokenDTO) {
        System.out.println("refreshToken ===" + refreshTokenDTO);
        return authService.refreshAccessToken(refreshTokenDTO);
    }

    /**
     * 忘记密码
     * @param userForgetPasswordDTO
     * @return 返回六位数验证码
     */
    @PostMapping("/forget-password/send-code")
    public ResponseEntity<ResultFormat> getSixDigVerifyCode(
            @Validated @RequestBody UserForgetPasswordDTO userForgetPasswordDTO) {
        return authService.sendForgetPasswordCode(userForgetPasswordDTO);
    }

    @PostMapping("/forget-password/{verify-code}")
    public ResponseEntity<ResultFormat> verifySixDigCode(
            @Validated @RequestBody UserForgetPasswordDTO userForgetPasswordDTO,
            @PathVariable("verify-code") String code) {
        return authService.verifyForgetPasswordCode(userForgetPasswordDTO, code);
    }

    @PostMapping("/forget-password/reset")
    public ResponseEntity<ResultFormat> resetPassword(
            @Validated @RequestBody UserForgetPasswordResetDTO userForgetPasswordResetDTO
            ) {
        return authService.resetPassword(userForgetPasswordResetDTO);
    }

}
