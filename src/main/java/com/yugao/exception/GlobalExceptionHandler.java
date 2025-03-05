package com.yugao.exception;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.yugao.result.ResultCode;
import com.yugao.result.ResultFormat;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResultFormat> handleBusinessException(BusinessException e) {
        System.out.println("BusinessException: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResultFormat.error(ResultCode.BUSINESS_EXCEPTION, "Bussiness error"));
    }

    @ExceptionHandler(SystemException.class)
    public ResponseEntity<ResultFormat> handleSystemException(Exception e) {
        System.out.println("SystemException: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResultFormat.error(ResultCode.SYSTEM_EXCEPTION, "System error"));
    }

    @ExceptionHandler({SQLException.class, MybatisPlusException.class, DuplicateKeyException.class, PersistenceException.class})
    public ResponseEntity<ResultFormat> handleSQLException(Exception e) {
        System.out.println("SQLException: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResultFormat.error(ResultCode.BUSINESS_EXCEPTION, "SQL: invalid operation"));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultFormat> handleException(Exception e) {
        System.out.println("UnknownException: " + e.getMessage());
        System.out.println(e.getClass());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResultFormat.error(ResultCode.SYSTEM_EXCEPTION, "Unknown error"));
    }


}
