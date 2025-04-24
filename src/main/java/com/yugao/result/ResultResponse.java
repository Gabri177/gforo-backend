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

    // 错误返回
    public static ResponseEntity<ResultFormat> error(ResultCode resultCode) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResultFormat.error(resultCode));
    }
}


