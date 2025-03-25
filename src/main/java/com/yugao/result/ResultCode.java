package com.yugao.result;

public class ResultCode {

    public static final int SUCCESS = 20000;

    /**
     * 40000~49999 为业务异常错误码
     */
    // 默认业务异常
    public static final int BUSINESS_EXCEPTION = 40000;
    // Auth
    public static final int LOGOUT_WITHOUT_LOGIN = 40001;
    public static final int LOGIN_WITHOUT_CAPTCHA = 40002;
    // User
    public static final int USER_NOT_FOUND = 40003;
    public static final int EMAIL_NOT_MATCH = 40004;
    public static final int SIX_DIGIT_CODE_NOT_MATCH = 40005;
    public static final int SIX_DIGIT_CODE_EXPIRED = 40006;
    public static final int SQL_UPDATING_ERROR = 40007;
    public static final int USER_ALREADY_VERIFIED = 40008;
    public static final int USER_INFO_INVALID = 40009;
    public static final int EMAIL_ALREADY_VERIFIED = 40010;
    public static final int NEW_PASSWORD_SAME = 40011;
    public static final int OLD_PASSWORD_INCORRECT = 40012;
    public static final int TOO_SHORT_INTERVAL = 40013;
    public static final int NO_PASS_THE_CAPTCHA = 40014;

    public static final int SYSTEM_EXCEPTION = 50000;

    public static final int UNKNOW_EXCEPTION = 60000;


    /**
     * 70000~79999 为用户权限相关错误码
     */
    // AuthController JwtAuthenticationFilter RegisterController
    public static final int TOKEN_INVALID = 70000;
    public static final int ACCESSTOKEN_UNAUTHORIZED = 70001;
    public static final int ACCESSTOKEN_EXPIRED = 70002;
    public static final int REFRESHTOKEN_UNAUTHORIZED = 70003;
    public static final int REFRESHTOKEN_EXPIRED = 70004;

}
