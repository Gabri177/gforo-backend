package com.yugao.result;

import com.yugao.enums.ResultCodeEnum;
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

    public ResultFormat(ResultCodeEnum resultCodeEnum, Object data) {
        this.code = resultCodeEnum.code();
        this.message = resultCodeEnum.getMessage();
        this.data = data;
    }

    // default success resultformat constructor
    public static ResultFormat success(Object data) {
        return new ResultFormat(ResultCodeEnum.SUCCESS, data);
    }

    public static ResultFormat error(ResultCodeEnum resultCodeEnum) {
        return new ResultFormat(resultCodeEnum, null);
    }
}
