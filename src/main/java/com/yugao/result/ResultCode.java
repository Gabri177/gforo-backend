package com.yugao.result;

public enum ResultCode {

    // 成功
    SUCCESS(20000, "Success"),

    // 业务异常 40000~49999
    BUSINESS_EXCEPTION(40000, "Business Error Unknown"),

    // // auth error
    LOGOUT_WITHOUT_LOGIN(40001, "Logout without login"),
    LOGIN_WITHOUT_CAPTCHA(40002, "Login without passing captcha"),
    USER_NOT_FOUND(40003, "User not found"),
    EMAIL_NOT_MATCH(40004, "Email not match"),
    SIX_DIGIT_CODE_NOT_MATCH(40005, "Six-digit code does not match"),
    SIX_DIGIT_CODE_EXPIRED(40006, "Six-digit code expired"),

    // // sql error
    SQL_EXCEPTION(40007, "SQL error"),
    SQL_UPDATING_ERROR(40008, "SQL updating error"),
    USER_ALREADY_VERIFIED(40009, "User already verified"),
    USER_INFO_INVALID(400010, "User info invalid"),
    EMAIL_ALREADY_VERIFIED(40011, "Email already verified"),
    NEW_PASSWORD_SAME(40012, "New password cannot be same as old"),
    OLD_PASSWORD_INCORRECT(40013, "Old password incorrect"),
    TOO_SHORT_INTERVAL(40014, "Operation interval too short"),
    NO_PASS_THE_CAPTCHA(40015, "Captcha not passed"),
    PASSWORD_NOT_MATCH(40016, "Password does not match"),
    SQL_TRANSACTION_ERROR(40017, "SQL transaction error"),
    INPUT_FORMAT_ERROR(40018, "Input format error"),
    CAPTCHA_VERIFIED_ERROR(40019, "Captcha verified error"),
    JWT_SETTING_ERROR(40020, "JWT setting error"),
    JWT_INIT_ERROR(40021, "JWT init error"),
    SQL_USERNAME_EXIST(40022, "Username already exists"),
    SQL_EMAIL_EXIST(40023, "Email already exists"),
    POST_NOT_FOUND(40024, "Post not found"),
    COMMENT_ENTITY_NOT_FOUND(40025, "Comment entity not found"),
    COMMENT_TARGET_NOT_FOUND(40026, "Comment target not found"),
    COMMENT_TYPE_UNKNOWN(40027, "Comment type unknown"),
    USER_NOT_LOGIN(40028, "User not logged in"),
    USER_NOT_AUTHORIZED(40029, "User not authorized"),
    COMMENT_NOT_FOUND(40030, "Comment not found"),
    USER_BLOCKED (40031, "User has been blocked, Please contact the Admin"),

    // 系统异常
    SYSTEM_EXCEPTION(50000, "System Exception"),

    // 未知异常
    UNKNOWN_EXCEPTION(60000, "Unknown Exception"),

    // 权限相关 70000~79999
    TOKEN_INVALID(70000, "Token invalid"),
    ACCESSTOKEN_UNAUTHORIZED(70001, "Access token unauthorized"),
    ACCESSTOKEN_EXPIRED(70002, "Access token expired"),
    REFRESHTOKEN_UNAUTHORIZED(70003, "Refresh token unauthorized"),
    REFRESHTOKEN_EXPIRED(70004, "Refresh token expired");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static ResultCode fromCode(int code) {
        for (ResultCode value : ResultCode.values()) {
            if (value.code == code) {
                return value;
            }
        }
        return UNKNOWN_EXCEPTION;
    }
}
