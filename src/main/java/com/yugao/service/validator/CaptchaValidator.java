package com.yugao.service.validator;

import com.yugao.exception.BusinessException;
import com.yugao.result.ResultCode;
import com.yugao.service.base.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CaptchaValidator {

    @Autowired
    private RedisService redisService;

    public void validateAndClearCaptcha(String scene, String username) {
        if (!redisService.verifyVerifiedCaptcha(scene, username)) {
            throw new BusinessException(ResultCode.LOGIN_WITHOUT_CAPTCHA);
        }
        redisService.deleteVerifiedCaptcha(scene, username);
    }


}
