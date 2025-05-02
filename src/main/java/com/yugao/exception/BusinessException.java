package com.yugao.exception;

import com.yugao.enums.ResultCodeEnum;

public class BusinessException extends RuntimeException {

    private final ResultCodeEnum resultCodeEnum;

    public BusinessException() {
        super(ResultCodeEnum.BUSINESS_EXCEPTION.getMessage());
        this.resultCodeEnum = ResultCodeEnum.BUSINESS_EXCEPTION;
    }

    public BusinessException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.resultCodeEnum = resultCodeEnum;
    }

    public ResultCodeEnum getResultCode() {
        return resultCodeEnum;
    }
}

