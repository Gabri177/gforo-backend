package com.yugao.controller.auth;

import com.yugao.constants.RedisKeyConstants;
import com.yugao.domain.User;
import com.yugao.domain.UserToken;
import com.yugao.dto.UserRegisterDTO;
import com.yugao.dto.UserForgetPasswordDTO;
import com.yugao.dto.UserForgetPasswordResetDTO;
import com.yugao.exception.BusinessException;
import com.yugao.result.ResultCode;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.base.RedisService;
import com.yugao.service.business.AuthService;
import com.yugao.service.data.UserService;
import com.yugao.service.data.UserTokenService;
import com.yugao.util.captcha.VerificationUtil;
import com.yugao.util.mail.MailClientUtil;
import com.yugao.util.security.JwtUtil;
import com.yugao.util.security.PasswordUtil;
import com.yugao.validation.ValidationGroups;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
     * 退出登录
     * @return
     */
    @DeleteMapping("/logout")
    public ResponseEntity<ResultFormat> logout(@RequestHeader("Authorization") String accessToken) {
       return authService.logout(accessToken);
    }

    /**
     * 刷新token
     * @param refreshToken
     * @return
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<ResultFormat> refresh(@RequestHeader("Authorization") String refreshToken) {
        return authService.refresh(refreshToken);
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
