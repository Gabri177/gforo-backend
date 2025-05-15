package com.yugao.exception;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.yugao.enums.ResultCodeEnum;
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

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 业务异常处理
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResultFormat> handleBusinessException(BusinessException e) {
        System.out.println("BusinessException: " + e.getMessage());
        e.printStackTrace();
        return ResultResponse.error(e.getResultCode());
    }
    // 系统异常处理
    @ExceptionHandler(SystemException.class)
    public ResponseEntity<ResultFormat> handleSystemException(SystemException e) {
        System.out.println("SystemException: " + e.getMessage());
        return ResultResponse.error(e.getResultCode());
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
        if (e.getMessage().contains("Duplicate entry")) {
            if (e.getMessage().contains("uniq_username"))
                return ResultResponse.error(ResultCodeEnum.SQL_USERNAME_EXIST);
            else if (e.getMessage().contains("uniq_email"))
                return ResultResponse.error(ResultCodeEnum.SQL_EMAIL_EXIST);
        }
        return ResultResponse.error(ResultCodeEnum.SQL_EXCEPTION);
    }

    // 事务异常处理
    @ExceptionHandler({TransactionSystemException.class, UnexpectedRollbackException.class})
    public ResponseEntity<ResultFormat> handleTransactionException(Exception e) {
        System.out.println("TransactionException: " + e.getMessage());
        return ResultResponse.error(ResultCodeEnum.SQL_TRANSACTION_ERROR);
    }

    // 认证异常处理
    @ExceptionHandler({org.springframework.security.access.AccessDeniedException.class})
    public ResponseEntity<ResultFormat> handleAccessDeniedException(Exception e) {
        System.out.println("AccessDeniedException: " + e.getMessage());
        return ResultResponse.error(ResultCodeEnum.NO_PERMISSION);
    }

    // 未知异常处理
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultFormat> handleException(Exception e) {
        System.out.println("UnknownException: " + e.getMessage());
        System.out.println(e.getClass());
        e.printStackTrace();
        return ResultResponse.error(ResultCodeEnum.UNKNOWN_EXCEPTION);
    }

    // 参数校验异常处理
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResultFormat> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder combindMsg = new StringBuilder(ResultCodeEnum.INPUT_FORMAT_ERROR.getMessage() + ": ");
        ex.getBindingResult().getFieldErrors().forEach(error ->
                combindMsg.append(error.getField())
                        .append(" ")
                        .append(error.getDefaultMessage())
                        .append("; ")
        );
        if (!combindMsg.isEmpty() && combindMsg.charAt(combindMsg.length() - 2) == ';') {
            combindMsg.setLength(combindMsg.length() - 2);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResultFormat(ResultCodeEnum.INPUT_FORMAT_ERROR,
                        combindMsg.toString()));
    }


}
