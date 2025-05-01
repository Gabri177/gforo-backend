package com.yugao.service.business.auth.impl;

import com.yugao.constants.RedisKeyConstants;
import com.yugao.domain.User;
import com.yugao.dto.auth.UserForgetPasswordDTO;
import com.yugao.dto.auth.UserForgetPasswordResetDTO;
import com.yugao.dto.auth.UserRegisterDTO;
import com.yugao.exception.BusinessException;
import com.yugao.result.ResultCode;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.business.auth.AuthService;
import com.yugao.service.data.UserService;
import com.yugao.service.builder.EmailBuilder;
import com.yugao.util.mail.MailClientUtil;
import com.yugao.util.security.PasswordUtil;
import com.yugao.service.handler.TokenHandler;
import com.yugao.service.validator.CaptchaValidator;
import com.yugao.service.validator.UserValidator;
import com.yugao.vo.auth.NewAccessTokenVO;
import com.yugao.vo.auth.TokenInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

        // symbol 这里让前端运行的时候生成固定的uuid 然后存到localstorage中 用来做设备标识符
        System.out.println("login =================== " + userRegisterDTO);
        captchaValidator.validateAndClearCaptcha(RedisKeyConstants.LOGIN, userRegisterDTO.getSymbol());
        User loginUser = userValidator.validateLoginCredentials(userRegisterDTO);
        // userValidator.validateIfIsBlocked(loginUser); // 目前status标志位已经不能用来验证是否验证过邮箱 这里的值要重新考虑
        tokenHandler.invalidateExistingToken(loginUser.getId());
        TokenInfoVO tokenInfoVO = tokenHandler.generateAndStoreToken(loginUser.getId(), loginUser);
        return ResultResponse.success(tokenInfoVO);
    }

    @Override
    public ResponseEntity<ResultFormat> logout(String accessToken) {
        tokenHandler.invalidateAccessToken(accessToken);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> refresh(String refreshToken) {
        System.out.println("refreshToken =================== " + refreshToken);
        NewAccessTokenVO newAccessTokenVO = tokenHandler.refreshAccessToken(refreshToken);
        return ResultResponse.success(newAccessTokenVO);
    }

    @Override
    public ResponseEntity<ResultFormat> sendForgetPasswordCode(UserForgetPasswordDTO userForgetPasswordDTO) {

        captchaValidator.validateAndClearCaptcha(
                RedisKeyConstants.FORGET_PASSWORD,
                userForgetPasswordDTO.getSymbol());
        User user = userService.getUserByEmail(userForgetPasswordDTO.getEmail());
        if (user == null)
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        String code = captchaValidator.generateAndCacheSixDigitCode(
                RedisKeyConstants.FORGET_PASSWORD,
                userForgetPasswordDTO.getEmail());
        System.out.println("reset password code =================== " + code);
        String html = emailBuilder.buildSixCodeVerifyHtml(code);
        mailClient.sendHtmlMail(user.getEmail(), "GForo: Reset Password", html);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> verifyForgetPasswordCode(UserForgetPasswordDTO userForgetPasswordDTO, String code) {

        System.out.println("verifyForgetPasswordCode =================== " + userForgetPasswordDTO);
        System.out.println("verified reset password code =================== " + code);
        captchaValidator.verifySixDigitCode(
                RedisKeyConstants.FORGET_PASSWORD,
                userForgetPasswordDTO.getEmail(),
                code);
        captchaValidator.setVerifiedSixDigitCode(
                RedisKeyConstants.FORGET_PASSWORD,
                userForgetPasswordDTO.getEmail());
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> resetPassword(UserForgetPasswordResetDTO userForgetPasswordResetDTO) {

        captchaValidator.validateVerifiedCodeFlag(
                RedisKeyConstants.FORGET_PASSWORD ,
                userForgetPasswordResetDTO.getEmail());
        User existUser = userValidator.validateEmailExists(userForgetPasswordResetDTO.getEmail());
        String newPassword = PasswordUtil.encode(userForgetPasswordResetDTO.getPassword());
        boolean res = userService.updatePassword(existUser.getId(), newPassword);
        if (!res) {
            return ResultResponse.error(ResultCode.SQL_UPDATING_ERROR);
        }

        return ResultResponse.success(null);
    }
}
