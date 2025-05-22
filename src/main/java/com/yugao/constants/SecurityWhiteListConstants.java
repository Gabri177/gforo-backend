package com.yugao.constants;

public final class SecurityWhiteListConstants {
    public static final String[] URLS = {
            "/api/auth/login",
            "/api/auth/refresh-token",
            "/api/auth/forget-password/*",
            "/api/register",
            "/api/register/activate",
            "/api/captcha/**",
            "api/board/**",
            "/api/post/detail/*", // 未登录状态下放行帖子查看的接口
            "/api/post/board/*", // 未登录状态下放行帖子查看的接口
            "/api/post/search", // 未登录状态下放行帖子查看的接口
            "/api/layout",
            "/api/statistics",
    };
}
