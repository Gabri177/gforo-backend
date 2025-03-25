package com.yugao.service.base.impl;

import com.yugao.constants.RedisKeyConstants;
import com.yugao.service.base.RedisService;
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

    @Value("${email.active-account.request-expire-time-minutes}")
    private Long emailRequestTimeInterval;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    private String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    private void set(String key, String value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    private boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    private void delete(String key) {
        redisTemplate.delete(key);
    }


    private void setTemporarilyByMinutes(String key, String value, long timeoutByMinutes) {
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

    /**
     * 访问令牌相关业务逻辑
     */
    // 设置用户访问令牌
    @Override
    public void setUserAccessToken(Long userId, String accessToken) {
        set(RedisKeyConstants.userIdAccessToken(userId), accessToken,
                accessTokenExpireTimeMillis, TimeUnit.MILLISECONDS);
    }
    // 删除用户访问令牌
    @Override
    public void deleteUserAccessToken(Long userId) {
        delete(RedisKeyConstants.userIdAccessToken(userId));
    }
    // 验证用户访问令牌
    @Override
    public boolean verifyUserAccessToken(Long userId, String accessToken){
        String redisAccessToken = get(RedisKeyConstants.userIdAccessToken(userId));
        return accessToken != null && accessToken.equals(redisAccessToken);
    }

    /**
     * 数字验证码相关业务逻辑
     * @param scene 场景
     * @param username 用户名
     * @param sixDigitCode 六位数字验证码
     */
    // 设置数字验证码过期时间
    @Override
    public void setSigDigitCodeByMinutes(String scene, String username, String sixDigitCode) {
        setTemporarilyByMinutes(
                RedisKeyConstants.sixDigitCode(scene, username),
                sixDigitCode,
                resetPasswordSixDigVerifyCodeExpireTimeMinutes);
    }
    // 判断是否通过数字验证码验证
    @Override
    public boolean verifySigDigitCode(String scene, String username, String sixDigitCode) {
        String redisSixDigitCode = get(RedisKeyConstants.sixDigitCode(scene, username));
        return sixDigitCode != null && sixDigitCode.equals(redisSixDigitCode);
    }
    // 删除数字验证码
    @Override
    public void deleteSigDigitCode(String scene, String username) {
        delete(RedisKeyConstants.sixDigitCode(scene, username));
    }
    // 设置通过数字验证码验证的标志过期时间
    @Override
    public void setVerifiedSigDigitCodeByMinutes(String scene, String username) {
        setTemporarilyByMinutes(
                RedisKeyConstants.sixDigitCodeVerified(scene, username),
                "true",
                verifiedSixDigVerifyCodeExpireTimeMinutes
        );
    }
    // 判断是否已经通过数字验证码验证
    @Override
    public boolean verifyVerifiedSigDigitCode(String scene, String username) {
        return hasKey(RedisKeyConstants.sixDigitCodeVerified(scene, username)) &&
                "true".equals(get(RedisKeyConstants.sixDigitCodeVerified(scene, username)));
    }
    // 删除通过数字验证码验证的标志
    @Override
    public void deleteVerifiedSigDigitCode(String scene, String username) {
        delete(RedisKeyConstants.sixDigitCodeVerified(scene, username));
    }

    /**
     * 邮箱激活相关业务逻辑
     */
    // 设置邮箱激活请求的过期时间
    @Override
    public void setEmailActivationIntervalByMinutes(String email) {
        setTemporarilyByMinutes(
                RedisKeyConstants.emailActivationInterval(email),
                "true",
                emailRequestTimeInterval);
    }
    // 判断请求激活的时间标志是否过期
    @Override
    public boolean verifyEmailActivationInterval(String email) {
        return hasKey(RedisKeyConstants.emailActivationInterval(email)) &&
                "true".equals(get(RedisKeyConstants.emailActivationInterval(email)));
    }
    // 删除邮箱激活请求的时间标志
    @Override
    public void deleteEmailActivationInterval(String email) {
        delete(RedisKeyConstants.emailActivationInterval(email));
    }

}
