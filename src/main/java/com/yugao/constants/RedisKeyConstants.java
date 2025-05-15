package com.yugao.constants;

import com.yugao.enums.LikeTypeEnum;

public final class RedisKeyConstants {

    private RedisKeyConstants() {}

    public static final String SPLIT = ":";
    // 使用场景
    public static final String LOGIN = "login";
    public static final String REGISTER = "register";
    public static final String FORGET_PASSWORD = "forget_password";
    public static final String RESET_PASSWORD = "reset_password";
    public static final String ACTIVATE_ACCOUNT = "activate_account";
    public static final String CHANGE_EMAIL = "change_email";

    // 图片验证码相关
    private static final String GRAPH_CAPTCHA_VERIFIED = "graph_captcha_verified";
    private static final String GRAPH_CAPTCHA = "graph_captcha";

    // 数字验证码相关
    private static final String SIX_DIGIT_CAPTCHA = "six_digit_captcha";
    private static final String SIX_DIGIT_CAPTCHA_VERIFIED = "six_digit_captcha_verify";

    // 访问令牌 (Access Token) 相关
    private static final String ACCESS_TOKEN = "access_token";

    // 请求账号激活邮件
    private static final String REQUEST_ACCOUNT_ACTIVATION_EMAIL = "request_account_activation_email";

    // 点赞相关
    private static final String LIKE = "like";

    // 图形验证码
    public static String buildGraphCaptchaKey(String captchaId) {
        return GRAPH_CAPTCHA + SPLIT + captchaId;
    }

    // 图形验证码验证通过: captcha_verified: + scene + symbol
    public static String buildGraphCaptchaVerifiedKey(String scene, String symbol) {
        return GRAPH_CAPTCHA_VERIFIED + SPLIT + scene + SPLIT + symbol;
    }

    public static String buildUserIdAccessTokenKey(Long userId) {

        return ACCESS_TOKEN + SPLIT + userId;
    }

    public static String buildSixDigitCaptchaKey(String scene, String symbol) {

        return SIX_DIGIT_CAPTCHA + SPLIT + scene + SPLIT + symbol;
    }

    public static String buildSixDigitCaptchaVerifiedKey(String scene, String symbol) {
        return SIX_DIGIT_CAPTCHA_VERIFIED + SPLIT + scene + SPLIT + symbol;
    }

    public static String buildEmailActivationIntervalKey(String email) {
        return REQUEST_ACCOUNT_ACTIVATION_EMAIL + SPLIT + email;
    }

    public static String buildRegisterEmailIntervalKey(String email) {

        return REGISTER + SPLIT + email;
    }

    // 用于存储用户对某个对象的点赞状态
    public static String buildLikeKeyWithUserId(Long userId, LikeTypeEnum likeType, Long targetId) {
        return LIKE + SPLIT + userId + SPLIT + likeType.getValue() + SPLIT + targetId;
    }

    // 用来计算针对某个对象总的点赞数
    public static String buildLikeKeyWithTargetId(Long targetId, LikeTypeEnum likeType) {
        return LIKE + SPLIT + likeType.getValue() + SPLIT + targetId;
    }

}

