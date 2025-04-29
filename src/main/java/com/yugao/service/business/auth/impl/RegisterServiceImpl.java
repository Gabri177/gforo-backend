package com.yugao.service.business.auth.impl;

import com.yugao.constants.RedisKeyConstants;
import com.yugao.converter.UserConverter;
import com.yugao.domain.User;
import com.yugao.dto.auth.ActiveAccountDTO;
import com.yugao.dto.auth.UserRegisterDTO;
import com.yugao.exception.BusinessException;
import com.yugao.result.ResultCode;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.base.RedisService;
import com.yugao.service.builder.EmailBuilder;
import com.yugao.service.business.auth.RegisterService;
import com.yugao.service.data.UserService;
import com.yugao.service.limiter.EmailRateLimiter;
import com.yugao.service.validator.UserValidator;
import com.yugao.util.mail.MailClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private EmailBuilder emailBuilder;

    @Autowired
    private MailClientUtil mailClient;

    @Autowired
    private EmailRateLimiter emailRateLimiter;

    @Override
    public ResponseEntity<ResultFormat> registerAccount(UserRegisterDTO userRegisterDTO) {

        // 校验图形验证码
        boolean res = redisService.verifyVerifiedCaptcha(RedisKeyConstants.REGISTER, userRegisterDTO.getUsername());
        if (!res) {
            return ResultResponse.error(ResultCode.NO_PASS_THE_CAPTCHA);
        }
        redisService.deleteCaptcha(RedisKeyConstants.REGISTER);

        // 检查redis是否存在正在注册的用户使用该邮箱
        if (redisService.hasTemporaryUserByEmail(userRegisterDTO.getEmail()))
            throw new BusinessException(ResultCode.ACCOUNT_IS_REGISTERING);
        if (userService.existsByEmail(userRegisterDTO.getEmail()))
            throw new BusinessException(ResultCode.EMAIL_ALERADY_REGISTERED);
        if (userService.existsByUsername(userRegisterDTO.getUsername()))
            throw new BusinessException(ResultCode.USERNAME_ALREADY_REGISTERED);

        User userDomain = UserConverter.toDomain(userRegisterDTO);


        emailRateLimiter.check(userDomain.getEmail());
        // 先保存到redis
        redisService.saveTemporaryUser(userDomain);
        String code = userValidator.generateAndCacheSixDigitCode(
                RedisKeyConstants.ACTIVATE_ACCOUNT ,
                userDomain.getEmail());
        String link = emailBuilder.buildActivationLink(userDomain.getEmail(), code);
        String html = emailBuilder.buildSixCodeVerifyHtml(code, link);
        mailClient.sendHtmlMail(userDomain.getEmail(), "GForo: Active Account", html);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> activateAccount(ActiveAccountDTO activeAccountDTO) {

        if (!redisService.hasTemporaryUserByEmail(activeAccountDTO.getEmail()))
            throw new BusinessException(ResultCode.VERIFY_EXPIRED);
        userValidator.verifySixDigitCode(
                RedisKeyConstants.ACTIVATE_ACCOUNT,
                activeAccountDTO.getEmail(),
                activeAccountDTO.getSixDigitCode());
        User user = redisService.getTemporaryUserByEmail(activeAccountDTO.getEmail());
        redisService.deleteTemporaryUserByEmail(activeAccountDTO.getEmail());
        userService.addUser(user);
        return ResultResponse.success(null);
    }
}
