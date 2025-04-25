package com.yugao.exception;

import com.yugao.result.ResultCode;

public class SystemException extends RuntimeException {

    private final ResultCode resultCode;

    public SystemException(){
        super(ResultCode.SYSTEM_EXCEPTION.getMessage());
        this.resultCode = ResultCode.SYSTEM_EXCEPTION;
    }

    public SystemException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}