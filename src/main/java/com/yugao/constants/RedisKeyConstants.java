package com.yugao.constants;

public final class RedisKeyConstants {


    // 验证码图片相关
    public static final String CAPTCHA_VERIFIED = "captcha_verified:";
    public static final String CAPTCHA_ID = "captcha:";
    // 访问令牌 (Access Token) 相关
    public static final String ACCESS_TOKEN = "access_token:";
    // 忘记密码六位验证码
    public static final String FORGET_PASSWORD_SIX_DIGIT_CODE = "forget_password_six_digit_code:";
    public static final String FORGET_PASSWORD_SIX_DIGIT_CODE_VERIFYED = "forget_password_six_digit_code_verify:";

    public static String captchaId(String captchaId) {
        return CAPTCHA_ID + captchaId;
    }

    public static String usernameCaptchaVerified(String username) {
        return CAPTCHA_VERIFIED + username;
    }

    public static String userIdAccessToken(Long userId) {
        return ACCESS_TOKEN + userId;
    }

    public static String usernameForgetPasswordSixDigitCode(String username) {
        return FORGET_PASSWORD_SIX_DIGIT_CODE + username;
    }

    public static String usernameForgetPasswordSixDigitCodeVerifyed(String username) {
        return FORGET_PASSWORD_SIX_DIGIT_CODE_VERIFYED + username;
    }
}
