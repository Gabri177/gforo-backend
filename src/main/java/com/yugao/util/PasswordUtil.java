package com.yugao.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // 这是spring security提供的加密方法 bcrypt
    public static String encode(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    // 这是spring security提供的比对方法
    public static boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
