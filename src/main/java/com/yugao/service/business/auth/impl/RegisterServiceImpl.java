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
import com.yugao.service.business.captcha.CaptchaService;
import com.yugao.service.data.UserService;
import com.yugao.service.limiter.EmailRateLimiter;
import com.yugao.service.validator.CaptchaValidator;
import com.yugao.service.validator.UserValidator;
import com.yugao.util.mail.MailClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Value("${email.active-account.request-expire-time-minutes}")
    private Long emailRequestExpireTimeMinutes;

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
    @Autowired
    private CaptchaValidator captchaValidator;
    @Autowired
    private CaptchaService captchaService;

    @Override
    public ResponseEntity<ResultFormat> registerAccount(UserRegisterDTO userRegisterDTO) {

        // 校验图形验证码
        captchaValidator.validateAndClearCaptcha(RedisKeyConstants.REGISTER, userRegisterDTO.getSymbol());

        // 检查redis是否存在正在注册的用户使用该邮箱
        if (redisService.hasKey(RedisKeyConstants.registerEmail(userRegisterDTO.getEmail())))
            throw new BusinessException(ResultCode.ACCOUNT_IS_REGISTERING);
        if (userService.existsByEmail(userRegisterDTO.getEmail()))
            throw new BusinessException(ResultCode.EMAIL_ALERADY_REGISTERED);
        if (userService.existsByUsername(userRegisterDTO.getUsername()))
            throw new BusinessException(ResultCode.USERNAME_ALREADY_REGISTERED);

        User userDomain = UserConverter.toDomain(userRegisterDTO);


        emailRateLimiter.check(userDomain.getEmail());
        // 先保存到redis
        redisService.setObjectTemmporarilyByMinutes(
                RedisKeyConstants.registerEmail(userDomain.getEmail()),
                userDomain,
                emailRequestExpireTimeMinutes
        );
        String code = captchaValidator.generateAndCacheSixDigitCode(
                RedisKeyConstants.ACTIVATE_ACCOUNT ,
                userDomain.getEmail());
        String link = emailBuilder.buildActivationLink(userDomain.getEmail(), code);
        String html = emailBuilder.buildSixCodeVerifyHtml(code, link);
        mailClient.sendHtmlMail(userDomain.getEmail(), "GForo: Active Account", html);
        return ResultResponse.success(null);
    }

    @Override
    public ResponseEntity<ResultFormat> activateAccount(ActiveAccountDTO activeAccountDTO) {

        if (!redisService.hasKey(RedisKeyConstants.registerEmail(activeAccountDTO.getEmail())))
            throw new BusinessException(ResultCode.VERIFY_EXPIRED);
        captchaValidator.verifySixDigitCode(
                RedisKeyConstants.ACTIVATE_ACCOUNT,
                activeAccountDTO.getEmail(),
                activeAccountDTO.getSixDigitCode());
        User user = redisService.getObject(
                RedisKeyConstants.registerEmail(activeAccountDTO.getEmail()),
                User.class
        );
        redisService.delete(RedisKeyConstants.registerEmail(activeAccountDTO.getEmail()));
        userService.addUser(user);
        return ResultResponse.success(null);
    }
}
