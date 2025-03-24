package com.yugao.constants;

public final class RedisKeyConstants {


    // 使用场景
    public static final String LOGIN = "login:";
    public static final String REGISTER = "register:";
    public static final String FORGET_PASSWORD = "forget_password:";


    // 验证码图片相关
    private static final String CAPTCHA_VERIFIED = "captcha_verified:";
    private static final String CAPTCHA = "captcha:";

    // 访问令牌 (Access Token) 相关
    private static final String ACCESS_TOKEN = "access_token:";
    // 忘记密码六位验证码
    private static final String FORGET_PASSWORD_SIX_DIGIT_CODE = "forget_password_six_digit_code:";
    private static final String FORGET_PASSWORD_SIX_DIGIT_CODE_VERIFYED = "forget_password_six_digit_code_verify:";
    // 请求账号激活邮件
    private static final String REQUEST_ACCOUNT_ACTIVATION_EMAIL = "request_account_activation_email:";

    // 图形验证码
    public static String captcha(String captchaId) {
        return CAPTCHA + captchaId;
    }

    // 图形验证码验证通过: captcha_verified: + scene + username
    public static String captchaVerified(String scene, String username) {
        return CAPTCHA_VERIFIED + scene + username;
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

    public static String emailRequestAccountActivationEmail(String email) {
        return REQUEST_ACCOUNT_ACTIVATION_EMAIL + email;
    }



}
