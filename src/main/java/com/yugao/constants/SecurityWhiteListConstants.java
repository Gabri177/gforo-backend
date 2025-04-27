package com.yugao.constants;

public final class SecurityWhiteListConstants {
    public static final String[] URLS = {
            "/api/auth/**",
            "/api/register/**",
            "/api/captcha/**",
            "/api/home/**",
            "/api/post/*/*" // 未登录状态下放行帖子查看的接口
    };
}
