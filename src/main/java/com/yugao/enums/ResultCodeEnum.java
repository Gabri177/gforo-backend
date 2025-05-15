package com.yugao.enums;

public enum ResultCodeEnum {

    // 成功
    SUCCESS(20000, "Success"),

    // 业务异常 40000~49999
    BUSINESS_EXCEPTION(40000, "Business Error Unknown"),

    // // auth error
    LOGOUT_WITHOUT_LOGIN(40001, "Logout without login"),
    LOGIN_WITHOUT_CAPTCHA(40002, "Login without passing captcha"),
    USER_NOT_FOUND(40003, "User not found"),
    EMAIL_NOT_MATCH(40004, "Email not match"),
    SIX_DIGIT_CAPTCHA_NOT_MATCH(40005, "Six-digit captcha does not match"),
    SIX_DIGIT_CAPTCHA_EXPIRED(40006, "Six-digit captcha expired"),
    NO_PERMISSION(40007, "No permission, Please contact administrator"),

    // // sql error
    SQL_EXCEPTION(40007, "SQL error"),
    SQL_UPDATING_ERROR(40008, "SQL updating error"),

    // // user error
    USER_ALREADY_VERIFIED(40009, "User already verified"),
    USER_INFO_INVALID(40010, "User info invalid"),
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
    USER_DELETED(40031, "User deleted"),
    REDIS_SAVING_ERROR(40032, "Redis saving error"),
    JSON_PARSE_ERROR(40033, "JSON parse error"),
    VERIFY_EXPIRED(40034, "Verification expired, Please re-register"),
    ACCOUNT_IS_REGISTERING(40035, "Count is already registering, Please check your email"),
    EMAIL_ALERADY_REGISTERED(40036, "Email is already registered"),
    USERNAME_ALREADY_REGISTERED(40037, "Username is already registered"),
    USERNAME_CHANGE_INTERVAL_TOO_SHORT(40038, "Username change interval is too short"),
    EMAIL_CHANGE_INTERVAL_TOO_SHORT(40039, "Email change interval is too short"),
    USER_PROFILE_UPDATE_ERROR(40040, "User profile update error"),
    USER_PASSWORD_UPDATE_ERROR(40041, "User password update error"),
    USER_EMAIL_UPDATE_ERROR(40042, "User email update error"),
    USER_USERNAME_UPDATE_ERROR(40043, "User username update error"),
    INVALID_STATUS_VALUE(40044, "Invalid status value"),
    BOARD_NOT_FOUND(40045, "Board not found"),
    USER_ACCOUNT_ABNORMAL(40046, "User account abnormal"),
    INVALID_ROLE(40047, "Invalid role"),
    ROLE_NOT_EXISTS(40048, "Role not exists"),
    BOARD_NOT_EXISTS(40049, "Board not exists"),
    SQL_DELETE_ERROR(40050, "SQL delete error"),
    ROLE_NAME_EXISTS(40051, "Role name already exists"),
    PERMISSION_NOT_EXISTS(40052, "Permission not exists"),
    ROLE_LEVEL_NOT_ENOUGH(40053, "You cannot assign a role with a lower level than your own"),
    INVALID_LIKE_TYPE(40054, "Invalid like type"),
    INVALID_BOOLEAN_ENUM(70005, "Invalid boolean enum"),
    SERIALIZATION_ERROR(70006, "Serialization error"),
    DESERIALIZATION_ERROR(70007, "Deserialization error"),


    // 系统异常
    SYSTEM_EXCEPTION(50000, "System Exception"),

    // 未知异常
    UNKNOWN_EXCEPTION(60000, "Unknown Exception"),

    // 权限相关 70000~79999
    TOKEN_INVALID(70000, "Token invalid"),
    ACCESSION_UNAUTHORIZED(70001, "Access token unauthorized"),
    ACCESSION_EXPIRED(70002, "Access token expired"),
    REFRESHMENT_UNAUTHORIZED(70003, "Refresh token unauthorized"),
    REFRESHMENT_EXPIRED(70004, "Refresh token expired");

    private final int code;
    private final String message;

    ResultCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static ResultCodeEnum fromCode(int code) {
        for (ResultCodeEnum value : ResultCodeEnum.values()) {
            if (value.code == code) {
                return value;
            }
        }
        return UNKNOWN_EXCEPTION;
    }
}
