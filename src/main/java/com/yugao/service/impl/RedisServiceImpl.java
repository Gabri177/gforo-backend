package com.yugao.service.impl;

import com.yugao.constants.RedisKeyConstants;
import com.yugao.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    @Value("${jwt.accessTokenExpiredMillis}")
    private long accessTokenExpireTimeMillis;

    @Value("${jwt.refreshTokenExpiredMillis}")
    private long refreshTokenExpireTimeMillis;

    @Value("${captcha.expire-time-minutes}")
    private Long captchaExpireTimeMinutes;

    @Value("${captcha.verified-expire-time-minutes}")
    private Long captchaVerifiedExpireTimeMinutes;

    @Value("${resetPassword.sixDigVerifyCodeExpireTimeMinutes}")
    private long resetPasswordSixDigVerifyCodeExpireTimeMinutes;

    @Value("${resetPassword.verifiedSixDigVerifyCodeExpireTimeMinutes}")
    private long verifiedSixDigVerifyCodeExpireTimeMinutes;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void set(String key, String value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    private boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }


    @Override
    public void setTemporarilyByMinutes(String key, String value, long timeoutByMinutes) {
        redisTemplate.opsForValue().set(key, value, timeoutByMinutes, TimeUnit.MINUTES);
    }

    /**
     * 图形验证码相关业务逻辑
     */
    // 设置验证码过期时间
    public void setCaptchaByMinutes(String captchaId, String captchaText) {
        setTemporarilyByMinutes(RedisKeyConstants.captcha(captchaId),
                captchaText, captchaExpireTimeMinutes);
    }
    // 判断是否通过验证码
    public boolean verifyCaptcha(String captchaId, String captchaCode) {
        String captchaText = get(RedisKeyConstants.captcha(captchaId));
        return captchaCode != null && captchaCode.equals(captchaText);
    }
    // 删除验证码
    public void deleteCaptcha(String captchaId) {
        delete(RedisKeyConstants.captcha(captchaId));
    }
    // 设置通过验证过期时间
    public void setVerifiedCaptchaByMinutes(String scene, String username) {
        setTemporarilyByMinutes(RedisKeyConstants.captchaVerified(scene, username),
                "true", captchaVerifiedExpireTimeMinutes);
    }
    // 判断是否通过验证
    public boolean verifyVerifiedCaptcha(String scene, String username) {
        return hasKey(RedisKeyConstants.captchaVerified(scene, username)) &&
                "true".equals(get(RedisKeyConstants.captchaVerified(scene, username)));
    }
    // 删除通过验证
    public void deleteVerifiedCaptcha(String scene, String username) {
        delete(RedisKeyConstants.captchaVerified(scene, username));
    }

    @Override
    public void setUserAccessToken(Long userId, String accessToken) {
        set(RedisKeyConstants.userIdAccessToken(userId), accessToken,
                accessTokenExpireTimeMillis, TimeUnit.MILLISECONDS);
    }

    @Override
    public void deleteUserAccessToken(Long userId) {
        delete(RedisKeyConstants.userIdAccessToken(userId));
    }

    @Override
    public boolean verifyUserAccessToken(Long userId, String accessToken){
        String redisAccessToken = get(RedisKeyConstants.userIdAccessToken(userId));
        return accessToken != null && accessToken.equals(redisAccessToken);
    }

    @Override
    public void setSigDigitCodeByMinutes(String scene, String username, String sixDigitCode) {
        setTemporarilyByMinutes(
                RedisKeyConstants.sixDigitCode(scene, username),
                sixDigitCode,
                resetPasswordSixDigVerifyCodeExpireTimeMinutes);
    }

    @Override
    public boolean verifySigDigitCode(String scene, String username, String sixDigitCode) {
        String redisSixDigitCode = get(RedisKeyConstants.sixDigitCode(scene, username));
        return sixDigitCode != null && sixDigitCode.equals(redisSixDigitCode);
    }

    @Override
    public void deleteSigDigitCode(String scene, String username) {
        delete(RedisKeyConstants.sixDigitCode(scene, username));
    }

    @Override
    public void setVerifiedSigDigitCodeByMinutes(String scene, String username) {
        setTemporarilyByMinutes(
                RedisKeyConstants.sixDigitCodeVerifyed(scene, username),
                "true",
                verifiedSixDigVerifyCodeExpireTimeMinutes
        );
    }

    @Override
    public boolean verifyVerifiedSigDigitCode(String scene, String username) {
        return hasKey(RedisKeyConstants.sixDigitCodeVerifyed(scene, username)) &&
                "true".equals(get(RedisKeyConstants.sixDigitCodeVerifyed(scene, username)));
    }

    @Override
    public void deleteVerifiedSigDigitCode(String scene, String username) {
        delete(RedisKeyConstants.sixDigitCodeVerifyed(scene, username));
    }


//    public void setEmailActivationIntervalBy(){
//        setTemporarilyByMinutes(
//                RedisKeyConstants.usernameForgetPasswordSixDigitCode(userForgetPasswordDTO.getUsername()),
//                sixDigVerifyCode,
//                resetPasswordSixDigVerifyCodeExpireTimeMinutes);
//    }
}
