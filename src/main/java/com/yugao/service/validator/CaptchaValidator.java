package com.yugao.service.validator;

import com.yugao.constants.RedisKeyConstants;
import com.yugao.exception.BusinessException;
import com.yugao.enums.ResultCodeEnum;
import com.yugao.service.base.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CaptchaValidator {

    @Value("${captcha.verifiedSixDigVerifyCodeExpireTimeMinutes}")
    private long verifiedSixDigVerifyCodeExpireTimeMinutes;

    @Autowired
    private RedisService redisService;

    // 判断是否已经通过数字验证码验证
    private boolean isSixDigitCaptchaVerified(String scene, String symbol) {
        return redisService.hasKey(RedisKeyConstants.buildSixDigitCaptchaVerifiedKey(scene, symbol)) &&
                "true".equals(redisService.get(RedisKeyConstants.buildSixDigitCaptchaVerifiedKey(scene, symbol)));
    }

    public void validateAndClearGraphCaptchaVerifiedFlag(String scene, String symbol) {

        System.out.println("验证图形验证码是否通过验证");
        System.out.println("scene = " + scene + ", symbol = " + symbol);
        System.out.println(RedisKeyConstants.buildGraphCaptchaVerifiedKey(scene, symbol));
        boolean opr = redisService.hasKey(RedisKeyConstants.buildGraphCaptchaVerifiedKey(scene, symbol)) &&
                "true".equals(redisService.get(RedisKeyConstants.buildGraphCaptchaVerifiedKey(scene, symbol)));
        if (!opr) {
            throw new BusinessException(ResultCodeEnum.CAPTCHA_VERIFIED_ERROR);
        }
        redisService.delete(RedisKeyConstants.buildGraphCaptchaVerifiedKey(scene, symbol));
    }

    public void validateSixDigitCaptcha(String scene, String symbol, String code) {
        String redisSixDigitCaptcha = redisService.get(RedisKeyConstants.buildSixDigitCaptchaKey(scene, symbol));
        System.out.println("redisSixDigitCaptcha = " + redisSixDigitCaptcha);
        boolean res = code.equals(redisSixDigitCaptcha);

        if (!res) {
            throw new BusinessException(ResultCodeEnum.SIX_DIGIT_CAPTCHA_NOT_MATCH);
        }

        // 删除redis中存储的验证码
        redisService.delete(RedisKeyConstants.buildSixDigitCaptchaKey(scene, code));
    }

    public void markSixDigitCaptchaAsVerified(String scene, String symbol){
        redisService.setTemporarilyByMinutes(
                RedisKeyConstants.buildSixDigitCaptchaVerifiedKey(scene, symbol),
                "true",
                verifiedSixDigVerifyCodeExpireTimeMinutes
        );
    }

    public void validateAndClearSixDigitCaptchaVerifiedFlag(String scene, String symbol){
        boolean res = isSixDigitCaptchaVerified(
                scene,
                symbol
        );
        if (!res)
            throw new BusinessException(ResultCodeEnum.SIX_DIGIT_CAPTCHA_NOT_MATCH);

        // 删除redis中存储的验证码
        redisService.delete(RedisKeyConstants.buildSixDigitCaptchaVerifiedKey(scene, symbol));
    }
}
