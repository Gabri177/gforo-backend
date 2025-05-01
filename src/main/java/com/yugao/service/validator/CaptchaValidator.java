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

    private void setSigDigitCodeByMinutes(String scene, String symbol, String sixDigitCode) {
        redisService.setTemporarilyByMinutes(
                RedisKeyConstants.sixDigitCode(scene, symbol),
                sixDigitCode,
                sixDigVerifyCodeExpireTimeMinutes);
    }

    private boolean verifySigDigitCode(String scene, String username, String sixDigitCode) {
        String redisSixDigitCode = redisService.get(RedisKeyConstants.sixDigitCode(scene, username));
        return sixDigitCode != null && sixDigitCode.equals(redisSixDigitCode);
    }

    public void deleteSigDigitCode(String scene, String symbol) {
        redisService.delete(RedisKeyConstants.sixDigitCode(scene, symbol));
    }

    public void setVerifiedSigDigitCodeByMinutes(String scene, String username) {
        redisService.setTemporarilyByMinutes(
                RedisKeyConstants.sixDigitCodeVerified(scene, username),
                "true",
                verifiedSixDigVerifyCodeExpireTimeMinutes
        );
    }
    // 判断是否已经通过数字验证码验证
    private boolean verifyVerifiedSigDigitCode(String scene, String username) {
        return redisService.hasKey(RedisKeyConstants.sixDigitCodeVerified(scene, username)) &&
                "true".equals(redisService.get(RedisKeyConstants.sixDigitCodeVerified(scene, username)));
    }
    // 删除通过数字验证码验证的标志
    private void deleteVerifiedSigDigitCode(String scene, String username) {
        redisService.delete(RedisKeyConstants.sixDigitCodeVerified(scene, username));
    }

    public void validateAndClearCaptcha(String scene, String username) {
        if (!captchaService.verifyVerifiedCaptcha(scene, username)) {
            throw new BusinessException(ResultCode.LOGIN_WITHOUT_CAPTCHA);
        }
        captchaService.deleteVerifiedCaptcha(scene, username);
    }

    public String generateAndCacheSixDigitCode(String scene, String symbol){
        // 生成六位数验证码
        String sixDigVerifyCode = VerificationUtil.generateSixNumVerifCode();

        // 存储到redis中
        setSigDigitCodeByMinutes(scene, symbol, sixDigVerifyCode);

        return sixDigVerifyCode;
    }

    public void verifySixDigitCode(String scene, String symbol, String code) {
        boolean res = verifySigDigitCode(
                scene,
                symbol,
                code
        );
        if (!res) {
            throw new BusinessException(ResultCode.SIX_DIGIT_CODE_NOT_MATCH);
        }

        // 删除redis中存储的验证码
        deleteSigDigitCode(scene, code);
    }

    public void setVerifiedSixDigitCode(String scene, String symbol){
        setVerifiedSigDigitCodeByMinutes(scene, symbol);
    }

    public void validateVerifiedCodeFlag(String scene ,String username){
        boolean res = verifyVerifiedSigDigitCode(
                scene,
                username
        );
        if (!res) {
            throw new BusinessException(ResultCode.SIX_DIGIT_CODE_NOT_MATCH);
        }

        // 删除redis中存储的验证码
        deleteVerifiedSigDigitCode(RedisKeyConstants.FORGET_PASSWORD, username);
    }
}
