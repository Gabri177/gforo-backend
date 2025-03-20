package com.yugao.result;

import com.yugao.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultFormat {

    private int code;
    private String message;
    private Object data = null;

    public ResultFormat() {
    }

    public ResultFormat(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResultFormat(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // default success resultformat constructor
    public static ResultFormat success(Object data) {
        return new ResultFormat(ResultCode.SUCCESS, "Success", data);
    }

    public static ResultFormat success(Object data, String message) {
        return new ResultFormat(ResultCode.SUCCESS, message, data);
    }

    public static ResultFormat error(int code, String message) {
        return new ResultFormat(code, message);
    }

    public static ResultFormat error(int code, Object data , String message) {
        return new ResultFormat(code, message, data);
    }
}
