package com.yugao.service.validator;

import com.yugao.constants.RedisKeyConstants;
import com.yugao.exception.BusinessException;
import com.yugao.result.ResultCode;
import com.yugao.service.base.RedisService;
import com.yugao.service.business.captcha.CaptchaService;
import com.yugao.util.captcha.VerificationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CaptchaValidator {

    @Value("${captcha.sixDigVerifyCodeExpireTimeMinutes}")
    private long sixDigVerifyCodeExpireTimeMinutes;

    @Value("${captcha.verifiedSixDigVerifyCodeExpireTimeMinutes}")
    private long verifiedSixDigVerifyCodeExpireTimeMinutes;

    @Autowired
    private RedisService redisService;

    @Autowired
    private CaptchaService captchaService;


    // 判断是否已经通过数字验证码验证
    private boolean verifyVerifiedSigDigitCode(String scene, String symbol) {
        return redisService.hasKey(RedisKeyConstants.sixDigitCodeVerified(scene, symbol)) &&
                "true".equals(redisService.get(RedisKeyConstants.sixDigitCodeVerified(scene, symbol)));
    }
    // 删除通过数字验证码验证的标志===============================
    private void deleteVerifiedSigDigitCode(String scene, String symbol) {
        redisService.delete(RedisKeyConstants.sixDigitCodeVerified(scene, symbol));
    }

    public void validateAndClearCaptcha(String scene, String symbol) {

        boolean opr = redisService.hasKey(RedisKeyConstants.captchaVerified(scene, symbol)) &&
                "true".equals(redisService.get(RedisKeyConstants.captchaVerified(scene, symbol)));
        if (!opr) {
            throw new BusinessException(ResultCode.CAPTCHA_VERIFIED_ERROR);
        }
        redisService.delete(RedisKeyConstants.captchaVerified(scene, symbol));
    }

    public String generateAndCacheSixDigitCode(String scene, String symbol){
        // 生成六位数验证码
        String sixDigVerifyCode = VerificationUtil.generateSixNumVerifCode();

        // 存储到redis中
        redisService.setTemporarilyByMinutes(
                RedisKeyConstants.sixDigitCode(scene, symbol),
                sixDigVerifyCode,
                sixDigVerifyCodeExpireTimeMinutes);

        return sixDigVerifyCode;
    }

    public void verifySixDigitCode(String scene, String symbol, String code) {

        String redisSixDigitCode = redisService.get(RedisKeyConstants.sixDigitCode(scene, symbol));
        System.out.println("redisSixDigitCode = " + redisSixDigitCode);
        boolean res = code.equals(redisSixDigitCode);

        if (!res) {
            throw new BusinessException(ResultCode.SIX_DIGIT_CODE_NOT_MATCH);
        }

        // 删除redis中存储的验证码
        redisService.delete(RedisKeyConstants.sixDigitCode(scene, code));
    }

    public void setVerifiedSixDigitCode(String scene, String symbol){
        redisService.setTemporarilyByMinutes(
                RedisKeyConstants.sixDigitCodeVerified(scene, symbol),
                "true",
                verifiedSixDigVerifyCodeExpireTimeMinutes
        );
    }

    public void validateVerifiedCodeFlag(String scene, String symbol){
        boolean res = verifyVerifiedSigDigitCode(
                scene,
                symbol
        );
        if (!res) {
            throw new BusinessException(ResultCode.SIX_DIGIT_CODE_NOT_MATCH);
        }

        // 删除redis中存储的验证码
        deleteVerifiedSigDigitCode(RedisKeyConstants.FORGET_PASSWORD, symbol);
    }
}
