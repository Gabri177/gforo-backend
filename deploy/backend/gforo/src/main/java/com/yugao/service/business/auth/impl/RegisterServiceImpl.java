package com.yugao.service.business.auth.impl;

import com.yugao.constants.KafkaEventType;
import com.yugao.constants.RedisKeyConstants;
import com.yugao.converter.UserConverter;
import com.yugao.domain.user.User;
import com.yugao.domain.permission.UserRole;
import com.yugao.dto.auth.ActiveAccountDTO;
import com.yugao.dto.auth.UserRegisterDTO;
import com.yugao.enums.RoleEnum;
import com.yugao.exception.BusinessException;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.base.RedisService;
import com.yugao.service.builder.EmailBuilder;
import com.yugao.service.business.auth.RegisterService;
import com.yugao.service.business.captcha.CaptchaService;
import com.yugao.service.business.title.TitleBusinessService;
import com.yugao.service.data.permission.UserRoleService;
import com.yugao.service.data.title.TitleService;
import com.yugao.service.data.title.UserTitleService;
import com.yugao.service.data.user.UserService;
import com.yugao.service.handler.EventHandler;
import com.yugao.service.limiter.EmailRateLimiter;
import com.yugao.service.validator.CaptchaValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    private final TitleBusinessService titleBusinessService;
    private final TitleService titleService;
    private final UserTitleService userTitleService;
    @Value("${email.active-account.request-expire-time-minutes}")
    private Long emailRequestExpireTimeMinutes;
    private final UserService userService;
    private final RedisService redisService;
    private final EmailBuilder emailBuilder;
    private final EmailRateLimiter emailRateLimiter;
    private final CaptchaValidator captchaValidator;
    private final CaptchaService captchaService;
    private final UserRoleService userRoleService;
    private final EventHandler eventHandler;


    @Override
    public ResponseEntity<ResultFormat> registerAccount(UserRegisterDTO userRegisterDTO) {

        // 校验图形验证码
        captchaValidator.validateAndClearGraphCaptchaVerifiedFlag(RedisKeyConstants.REGISTER, userRegisterDTO.getSymbol());

        // 检查redis是否存在正在注册的用户使用该邮箱
        if (redisService.hasKey(RedisKeyConstants.buildRegisterEmailIntervalKey(userRegisterDTO.getEmail())))
            throw new BusinessException(ResultCodeEnum.ACCOUNT_IS_REGISTERING);
        if (userService.existsByEmail(userRegisterDTO.getEmail()))
            throw new BusinessException(ResultCodeEnum.EMAIL_ALERADY_REGISTERED);
        if (userService.existsByUsername(userRegisterDTO.getUsername()))
            throw new BusinessException(ResultCodeEnum.USERNAME_ALREADY_REGISTERED);

        User userDomain = UserConverter.toDomain(userRegisterDTO);


        emailRateLimiter.check(userDomain.getEmail());
        // 先保存到redis
        redisService.setObjectTemmporarilyByMinutes(
                RedisKeyConstants.buildRegisterEmailIntervalKey(userDomain.getEmail()),
                userDomain,
                emailRequestExpireTimeMinutes
        );

        // 构建邮件内容
        String code = captchaService.generateSixDigitCaptcha(
                RedisKeyConstants.ACTIVATE_ACCOUNT ,
                userDomain.getEmail());
        String link = emailBuilder.buildActivationLink(userDomain.getEmail(), code);
        String html = emailBuilder.buildSixCodeVerifyHtml(code, link);

        // 将事件发送到Kafka 异步消费
        eventHandler.sendHtmlEmail(
                userDomain.getEmail(),
                "GForo: Active Account",
                html,
                KafkaEventType.REGISTER_ACCOUNT);

        return ResultResponse.success(null);
    }


    @Transactional
    @Override
    public ResponseEntity<ResultFormat> activateAccount(ActiveAccountDTO activeAccountDTO) {

        if (!redisService.hasKey(RedisKeyConstants.buildRegisterEmailIntervalKey(activeAccountDTO.getEmail())))
            throw new BusinessException(ResultCodeEnum.VERIFY_EXPIRED);
        captchaValidator.validateSixDigitCaptcha(
                RedisKeyConstants.ACTIVATE_ACCOUNT,
                activeAccountDTO.getEmail(),
                activeAccountDTO.getSixDigitCaptcha());
        User user = redisService.getObject(
                RedisKeyConstants.buildRegisterEmailIntervalKey(activeAccountDTO.getEmail()),
                User.class
        );
        redisService.delete(RedisKeyConstants.buildRegisterEmailIntervalKey(activeAccountDTO.getEmail()));
        // 在用户表添加用户
        userService.addUser(user);
        // 给用户分配角色
        userRoleService.addUserRole(new UserRole(user.getId(), RoleEnum.ROLE_USER.getCode()));
        // 刷新用户title
        titleBusinessService.refreshUserExpTitle(user.getId());
        return ResultResponse.success(null);
    }
}
