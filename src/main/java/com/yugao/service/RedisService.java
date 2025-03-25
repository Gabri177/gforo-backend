package com.yugao.service;


import java.util.concurrent.TimeUnit;

public interface RedisService {

    void set(String key, String value);

    void set(String key, String value, long timeout, TimeUnit unit);

    String get(String key);

    void delete(String key);

    void setTemporarilyByMinutes(String key, String value, long timeoutByMinutes);

    // 给验证码设置过期时间
    void setCaptchaByMinutes(String captchaId, String captchaText);

    // 检查是否通过验证
    boolean verifyCaptcha(String captchaId, String captchaCode);

    // 删除验证码
    void deleteCaptcha(String captchaId);

    // 给通过验证的标志设置过期时间
    void setVerifiedCaptchaByMinutes(String scene, String username);

    // 检查是否已经通过验证码验证
    boolean verifyVerifiedCaptcha(String scene, String username);

    // 删除通过验证的标志
    void deleteVerifiedCaptcha(String scene, String username);

    // 给用户设置访问令牌
    void setUserAccessToken(Long userId, String accessToken);

    // 删除用户的访问令牌
    void deleteUserAccessToken(Long userId);

    // 验证用户的访问令牌
    boolean verifyUserAccessToken(Long userId, String accessToken);

    // 设置数字验证码过期时间
    void setSigDigitCodeByMinutes(String scene, String username, String sixDigitCode);

    // 检查是否通过数字验证码验证
    boolean verifySigDigitCode(String scene, String username, String sixDigitCode);

    // 删除数字验证码
    void deleteSigDigitCode(String scene, String username);

    // 设置通过数字验证码验证的标志过期时间
    void setVerifiedSigDigitCodeByMinutes(String scene, String username);

    // 检查是否已经通过数字验证码验证
    boolean verifyVerifiedSigDigitCode(String scene, String username);

    // 删除通过数字验证码验证的标志
    void deleteVerifiedSigDigitCode(String scene, String username);
}

