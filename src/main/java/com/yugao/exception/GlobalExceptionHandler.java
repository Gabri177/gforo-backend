package com.yugao.exception;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.yugao.result.ResultCode;
import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.util.common.ErrorParseUtil;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.dao.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 业务异常处理
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResultFormat> handleBusinessException(BusinessException e) {
        System.out.println("BusinessException: " + e.getMessage());
        return ResultResponse.error(ResultCode.BUSINESS_EXCEPTION);
    }
    // 系统异常处理
    @ExceptionHandler(SystemException.class)
    public ResponseEntity<ResultFormat> handleSystemException(Exception e) {
        System.out.println("SystemException: " + e.getMessage());
        return ResultResponse.error(ResultCode.SYSTEM_EXCEPTION);
    }

    // SQL异常处理
    @ExceptionHandler({SQLException.class, MybatisPlusException.class,
            DuplicateKeyException.class, PersistenceException.class,
            DuplicateKeyException.class,
            SQLIntegrityConstraintViolationException.class,
            DataIntegrityViolationException.class,
            InvalidDataAccessApiUsageException.class,
            InvalidDataAccessResourceUsageException.class,
            QueryTimeoutException.class,
            TransientDataAccessResourceException.class,
            TypeMismatchDataAccessException.class})
    public ResponseEntity<ResultFormat> handleSQLException(Exception e) {
        // 解析错误信息 修改错误信息需要修改ErrorParse类
        String message = ErrorParseUtil.parseDuplicateEntryMessage(e.getMessage());
        System.out.println("SQLException: " + e.getMessage());
        return ResultResponse.error(ResultCode.SQL_EXCEPTION);
    }

    // 事务异常处理
    @ExceptionHandler({TransactionSystemException.class, UnexpectedRollbackException.class})
    public ResponseEntity<ResultFormat> handleTransactionException(Exception e) {
        System.out.println("TransactionException: " + e.getMessage());
        return ResultResponse.error(ResultCode.SQL_TRANSACTION_ERROR);
    }

    // 未知异常处理
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultFormat> handleException(Exception e) {
        System.out.println("UnknownException: " + e.getMessage());
        System.out.println(e.getClass());
        return ResultResponse.error(ResultCode.UNKNOWN_EXCEPTION);
    }

    // 参数校验异常处理
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResultFormat> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errmsgs = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errmsgs.put(error.getField(), error.getDefaultMessage())
        );
        String msg = errmsgs.toString();
        return ResultResponse.error(ResultCode.INPUT_FORMAT_ERROR);
    }


}
