package com.yugao.result;

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

    public ResultFormat(ResultCode resultCode, Object data) {
        this.code = resultCode.code();
        this.message = resultCode.message();
        this.data = data;
    }

    // default success resultformat constructor
    public static ResultFormat success(Object data) {
        return new ResultFormat(ResultCode.SUCCESS, data);
    }

    public static ResultFormat error(ResultCode resultCode) {
        return new ResultFormat(resultCode, null);
    }
}
