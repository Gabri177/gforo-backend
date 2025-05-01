package com.yugao.constants;

public final class RedisKeyConstants {

    private RedisKeyConstants() {}

    // 使用场景
    public static final String LOGIN = "login:";
    public static final String REGISTER = "register:";
    public static final String FORGET_PASSWORD = "forget_password:";
    public static final String RESET_PASSWORD = "reset_password:";
    public static final String ACTIVATE_ACCOUNT = "activate_account:";
    public static final String CHANGE_EMAIL = "change_email:";

    // 图片验证码相关
    private static final String GRAPH_CAPTCHA_VERIFIED = "graph_captcha_verified:";
    private static final String GRAPH_CAPTCHA = "graph_captcha:";

    // 数字验证码相关
    private static final String SIX_DIGIT_CAPTCHA = "six_digit_captcha:";
    private static final String SIX_DIGIT_CAPTCHA_VERIFIED = "six_digit_captcha_verify:";

    // 访问令牌 (Access Token) 相关
    private static final String ACCESS_TOKEN = "access_token:";

    // 请求账号激活邮件
    private static final String REQUEST_ACCOUNT_ACTIVATION_EMAIL = "request_account_activation_email:";

    // 图形验证码
    public static String buildGraphCaptchaKey(String captchaId) {
        return GRAPH_CAPTCHA + captchaId;
    }

    // 图形验证码验证通过: captcha_verified: + scene + symbol
    public static String buildGraphCaptchaVerifiedKey(String scene, String symbol) {
        return GRAPH_CAPTCHA_VERIFIED + scene + symbol;
    }

    public static String buildUserIdAccessTokenKey(Long userId) {
        return ACCESS_TOKEN + userId;
    }

    public static String buildSixDigitCaptchaKey(String scene, String symbol) {

        return SIX_DIGIT_CAPTCHA + scene + symbol;
    }

    public static String buildSixDigitCaptchaVerifiedKey(String scene, String symbol) {
        return SIX_DIGIT_CAPTCHA_VERIFIED + scene + symbol;
    }

    public static String buildEmailActivationIntervalKey(String email) {
        return REQUEST_ACCOUNT_ACTIVATION_EMAIL + email;
    }

    public static String buildRegisterEmailIntervalKey(String email) {
        return REGISTER + email;
    }

}

