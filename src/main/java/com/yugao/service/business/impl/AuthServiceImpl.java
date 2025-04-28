package com.yugao.service.business.impl;

import com.yugao.constants.RedisKeyConstants;
import com.yugao.domain.User;
import com.yugao.dto.UserForgetPasswordDTO;
import com.yugao.dto.UserForgetPasswordResetDTO;
import com.yugao.dto.UserRegisterDTO;
import com.yugao.result.ResultCode;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.business.AuthService;
import com.yugao.service.data.UserService;
import com.yugao.service.builder.EmailBuilder;
import com.yugao.util.mail.MailClientUtil;
import com.yugao.util.security.PasswordUtil;
import com.yugao.service.handler.TokenHandler;
import com.yugao.service.validator.CaptchaValidator;
import com.yugao.service.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private MailClientUtil mailClient;

    @Autowired
    private TokenHandler tokenHandler;

    @Autowired
    private CaptchaValidator captchaValidator;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private EmailBuilder emailBuilder;


    @Override
    public ResponseEntity<ResultFormat> login(UserRegisterDTO userRegisterDTO) {

        captchaValidator.validateAndClearCaptcha(RedisKeyConstants.LOGIN, userRegisterDTO.getUsername());
        User loginUser = userValidator.validateUserLogin(userRegisterDTO);
        tokenHandler.invalidateExistingToken(loginUser.getId());
        Map<String, String> tokenMap = tokenHandler.generateAndStoreToken(loginUser.getId(), loginUser);
        return ResultResponse.success(tokenMap);
    }

    @Override
    public ResponseEntity<ResultFormat> logout(String accessToken) {
        tokenHandler.invalidateAccessToken(accessToken);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> refresh(String refreshToken) {
        Map<String, String> resultMap = tokenHandler.refreshAccessToken(refreshToken);
        return ResultResponse.success(resultMap);
    }

    @Override
    public ResponseEntity<ResultFormat> sendForgetPasswordCode(UserForgetPasswordDTO userForgetPasswordDTO) {

        captchaValidator.validateAndClearCaptcha(RedisKeyConstants.FORGET_PASSWORD, userForgetPasswordDTO.getUsername());
        User user = userValidator.validateUsernameAndEmail(userForgetPasswordDTO.getUsername(), userForgetPasswordDTO.getEmail());
        String code = userValidator.generateAndCacheSixDigitCode(userForgetPasswordDTO.getUsername());
        String html = emailBuilder.buildSixCodeVerifyHtml(code);
        mailClient.sendHtmlMail(user.getEmail(), "GForo: Reset Password", html);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> verifyForgetPasswordCode(UserForgetPasswordDTO userForgetPasswordDTO, String code) {

        userValidator.verifySixDigitCode(userForgetPasswordDTO.getUsername(), code);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> resetPassword(UserForgetPasswordResetDTO userForgetPasswordResetDTO) {

        userValidator.validateVerifiedCodeFlag(userForgetPasswordResetDTO.getUsername());
        User existUser = userValidator.validateExistenceName(userForgetPasswordResetDTO.getUsername());
        String newPassword = PasswordUtil.encode(userForgetPasswordResetDTO.getPassword());
        boolean res = userService.updatePassword(existUser.getId(), newPassword);
        if (!res) {
            return ResultResponse.error(ResultCode.SQL_UPDATING_ERROR);
        }

        return ResultResponse.success(null);
    }
}
