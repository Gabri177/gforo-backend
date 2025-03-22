package com.yugao.result;

public class ResultCode {

    public static final int SUCCESS = 20000;

    public static final int BUSINESS_EXCEPTION = 40000;
    public static final int LOGOUT_WITHOUT_LOGIN = 40001;


    public static final int SYSTEM_EXCEPTION = 50000;

    public static final int UNKNOW_EXCEPTION = 60000;


    // 70000~79999 为用户权限相关错误码
    public static final int TOKEN_INVALID = 70000;
    public static final int ACCESSTOKEN_UNAUTHORIZED = 70001;
    public static final int ACCESSTOKEN_EXPIRED = 70002;
    public static final int REFRESHTOKEN_UNAUTHORIZED = 70003;
    public static final int REFRESHTOKEN_EXPIRED = 70004;

}
