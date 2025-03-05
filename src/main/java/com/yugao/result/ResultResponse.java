package com.yugao.result;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 统一构建 ResponseEntity<ResultFormat> 的工具类
 */
public class ResultResponse {

    // 成功返回（带数据）
    public static ResponseEntity<ResultFormat> success(Object data) {
        return ResponseEntity.ok(ResultFormat.success(data));
    }

    // 成功返回（带消息和数据）
    public static ResponseEntity<ResultFormat> success(Object data, String message) {
        return ResponseEntity.ok(ResultFormat.success(data, message));
    }

    // 成功返回（仅消息）
    public static ResponseEntity<ResultFormat> success(String message) {
        return ResponseEntity.ok(ResultFormat.success(message, null));
    }

    // 错误返回（默认500）
    public static ResponseEntity<ResultFormat> error(String message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResultFormat.error(ResultCode.SYSTEM_EXCEPTION, message));
    }

    // 错误返回（自定义状态码和业务错误码）
    public static ResponseEntity<ResultFormat> error(HttpStatus status, int code, String message) {
        return ResponseEntity.status(status)
                .body(ResultFormat.error(code, message));
    }

    // 错误返回（状态码和默认业务错误码）
    public static ResponseEntity<ResultFormat> error(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(ResultFormat.error(ResultCode.BUSINESS_EXCEPTION, message));
    }

    // 错误返回（自定义错误码和错误信息）
    public static ResponseEntity<ResultFormat> error(int code, String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResultFormat.error(code, message));
    }
}


