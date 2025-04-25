package com.yugao.exception;

import com.yugao.result.ResultCode;

public class BusinessException extends RuntimeException {

    private final ResultCode resultCode;

    public BusinessException() {
        super(ResultCode.BUSINESS_EXCEPTION.getMessage());
        this.resultCode = ResultCode.BUSINESS_EXCEPTION;
    }

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}

