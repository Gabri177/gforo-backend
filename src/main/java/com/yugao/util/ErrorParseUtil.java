package com.yugao.util;

import java.util.HashMap;
import java.util.Map;

public class ErrorParseUtil {

    // 定义唯一约束字段 -> 友好错误消息的映射
    private static final Map<String, String> UNIQUE_CONSTRAINT_MESSAGES = new HashMap<>();
    static {
        UNIQUE_CONSTRAINT_MESSAGES.put("uniq_username", "Username already exists");
        UNIQUE_CONSTRAINT_MESSAGES.put("uniq_email", "Email already exists");
    }

    public static String parseDuplicateEntryMessage(String errorMessage) {
        if (errorMessage == null || !errorMessage.contains("Duplicate entry")) {
            return "Database error";
        }

        // 遍历已定义的唯一约束错误
        for (Map.Entry<String, String> entry : UNIQUE_CONSTRAINT_MESSAGES.entrySet()) {
            if (errorMessage.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        return "Duplicate entry"; // 未知的唯一约束错误
    }
}
