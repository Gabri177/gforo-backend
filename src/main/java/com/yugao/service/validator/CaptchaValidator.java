package com.yugao.service.validator;

import com.yugao.constants.RedisKeyConstants;
import com.yugao.exception.BusinessException;
import com.yugao.result.ResultCode;
import com.yugao.service.base.RedisService;
import com.yugao.util.captcha.VerificationUtil;
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

    public String generateAndCacheSixDigitCode(String scene, String symbol){
        // 生成六位数验证码
        String sixDigVerifyCode = VerificationUtil.generateSixNumVerifCode();

        // 存储到redis中
        redisService.setSigDigitCodeByMinutes(
                scene,
                symbol,
                sixDigVerifyCode
        );

        return sixDigVerifyCode;
    }

    public void verifySixDigitCode(String scene, String symbol, String code) {
        boolean res = redisService.verifySigDigitCode(
                scene,
                symbol,
                code
        );
        if (!res) {
            throw new BusinessException(ResultCode.SIX_DIGIT_CODE_NOT_MATCH);
        }

        // 删除redis中存储的验证码
        redisService.deleteSigDigitCode(scene, code);
    }

    public void setVerifiedSixDigitCode(String scene, String symbol){
        redisService.setVerifiedSigDigitCodeByMinutes(scene, symbol);
    }

    public void validateVerifiedCodeFlag(String scene ,String username){
        boolean res = redisService.verifyVerifiedSigDigitCode(
                scene,
                username
        );
        if (!res) {
            throw new BusinessException(ResultCode.SIX_DIGIT_CODE_NOT_MATCH);
        }

        // 删除redis中存储的验证码
        redisService.deleteVerifiedSigDigitCode(RedisKeyConstants.FORGET_PASSWORD, username);
    }
}
