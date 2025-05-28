package com.yugao.exception;

import com.yugao.enums.ResultCodeEnum;

public class SystemException extends RuntimeException {

    private final ResultCodeEnum resultCodeEnum;

    public SystemException(){
        super(ResultCodeEnum.SYSTEM_EXCEPTION.getMessage());
        this.resultCodeEnum = ResultCodeEnum.SYSTEM_EXCEPTION;
    }

    public SystemException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.resultCodeEnum = resultCodeEnum;
    }

    public ResultCodeEnum getResultCode() {
        return resultCodeEnum;
    }
}