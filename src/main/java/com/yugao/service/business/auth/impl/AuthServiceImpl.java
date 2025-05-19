package com.yugao.service.business.auth.impl;

import com.yugao.constants.KafkaEventType;
import com.yugao.constants.RedisKeyConstants;
import com.yugao.domain.user.User;
import com.yugao.dto.auth.RefreshTokenDTO;
import com.yugao.dto.auth.UserForgetPasswordDTO;
import com.yugao.dto.auth.UserForgetPasswordResetDTO;
import com.yugao.dto.auth.UserRegisterDTO;
import com.yugao.exception.BusinessException;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.business.auth.AuthService;
import com.yugao.service.business.captcha.CaptchaService;
import com.yugao.service.data.UserService;
import com.yugao.service.builder.EmailBuilder;
import com.yugao.service.handler.EventHandler;
import com.yugao.util.security.PasswordUtil;
import com.yugao.service.handler.TokenHandler;
import com.yugao.service.validator.CaptchaValidator;
import com.yugao.service.validator.UserValidator;
import com.yugao.vo.auth.NewAccessTokenVO;
import com.yugao.vo.auth.TokenInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final TokenHandler tokenHandler;
    private final CaptchaValidator captchaValidator;
    private final UserValidator userValidator;
    private final EmailBuilder emailBuilder;
    private final CaptchaService captchaService;
    private final EventHandler eventHandler;


    @Override
    public ResponseEntity<ResultFormat> login(UserRegisterDTO userRegisterDTO) {

        // symbol 这里让前端运行的时候生成固定的uuid 然后存到localstorage中 用来做设备标识符
//        System.out.println("login =================== " + userRegisterDTO);
        captchaValidator.validateAndClearGraphCaptchaVerifiedFlag(RedisKeyConstants.LOGIN, userRegisterDTO.getSymbol());
        User loginUser = userValidator.validateLoginCredentials(userRegisterDTO);
        // userValidator.validateIfIsBlocked(loginUser); // 目前status标志位已经不能用来验证是否验证过邮箱 这里的值要重新考虑
//        tokenHandler.invalidateExistingToken(loginUser.getId());
        TokenInfoVO tokenInfoVO = tokenHandler.generateAndStoreToken(loginUser, userRegisterDTO.getSymbol());
        return ResultResponse.success(tokenInfoVO);
    }

    @Override
    public ResponseEntity<ResultFormat> refreshAccessToken(RefreshTokenDTO refreshTokenDTO) {

        NewAccessTokenVO newAccessTokenVO = tokenHandler.refreshAccessToken(refreshTokenDTO);
        return ResultResponse.success(newAccessTokenVO);
    }

    @Override
    public ResponseEntity<ResultFormat> sendForgetPasswordCode(UserForgetPasswordDTO userForgetPasswordDTO) {

        captchaValidator.validateAndClearGraphCaptchaVerifiedFlag(
                RedisKeyConstants.FORGET_PASSWORD,
                userForgetPasswordDTO.getSymbol());

        // 构造邮件内容
        User user = userService.getUserByEmail(userForgetPasswordDTO.getEmail());
        if (user == null)
            throw new BusinessException(ResultCodeEnum.USER_NOT_FOUND);
        String code = captchaService.generateSixDigitCaptcha(
                RedisKeyConstants.FORGET_PASSWORD,
                userForgetPasswordDTO.getEmail());
        String html = emailBuilder.buildSixCodeVerifyHtml(code);

        // 将邮件发送的事件放入Kafka队列
        eventHandler.sendHtmlEmail(user.getEmail(), "GForo: Reset Password", html,
                KafkaEventType.FORGET_PASSWORD);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> verifyForgetPasswordCode(UserForgetPasswordDTO userForgetPasswordDTO, String code) {

//        System.out.println("verifyForgetPasswordCode =================== " + userForgetPasswordDTO);
//        System.out.println("verified reset password code =================== " + code);
        captchaValidator.validateSixDigitCaptcha(
                RedisKeyConstants.FORGET_PASSWORD,
                userForgetPasswordDTO.getEmail(),
                code);
        captchaValidator.markSixDigitCaptchaAsVerified(
                RedisKeyConstants.FORGET_PASSWORD,
                userForgetPasswordDTO.getEmail());
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> resetPassword(UserForgetPasswordResetDTO userForgetPasswordResetDTO) {

        captchaValidator.validateAndClearSixDigitCaptchaVerifiedFlag(
                RedisKeyConstants.FORGET_PASSWORD ,
                userForgetPasswordResetDTO.getEmail());
        User existUser = userValidator.validateEmailExists(userForgetPasswordResetDTO.getEmail());
        String newPassword = PasswordUtil.encode(userForgetPasswordResetDTO.getPassword());
        boolean res = userService.updatePassword(existUser.getId(), newPassword);
        if (!res) {
            return ResultResponse.error(ResultCodeEnum.SQL_UPDATING_ERROR);
        }

        return ResultResponse.success(null);
    }
}
