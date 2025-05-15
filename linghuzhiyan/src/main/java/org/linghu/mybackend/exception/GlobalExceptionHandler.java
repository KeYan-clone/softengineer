package org.linghu.mybackend.exception;

import lombok.extern.slf4j.Slf4j;

import org.linghu.mybackend.constants.SystemConstants;
import org.linghu.mybackend.dto.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * 全局异常处理器
 * 统一处理系统中的各类异常，并转换为标准化响应
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理用户相关异常
     */
    @ExceptionHandler(UserException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleUserException(UserException e) {
        log.warn("User exception: {} - {}", e.getCode(), e.getMessage());
        return Result.failure(e.getCode(), e.getMessage());
    }
    // 处理方法级别的权限异常
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<Result<String>> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        Result<String> result = new Result<>();
        result.setCode(403);
        result.setMessage("权限不足，无法访问该资源");
        return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
    }
    
    // 也处理标准的AccessDeniedException
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Result<String>> handleAccessDeniedException(AccessDeniedException ex) {
        Result<String> result = new Result<>();
        result.setCode(403);
        result.setMessage("权限不足，无法访问该资源");
        return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
    }
    // /**
    //  * 处理实验相关异常
    //  */
    // @ExceptionHandler(ExperimentException.class)
    // @ResponseStatus(HttpStatus.OK)
    // public Result<Void> handleExperimentException(ExperimentException e) {
    //     log.warn("Experiment exception: {} - {}", e.getCode(), e.getMessage());
    //     return Result.failure(e.getCode(), e.getMessage());
    // }
    
    // /**
    //  * 处理讨论相关异常
    //  */
    // @ExceptionHandler(DiscussionException.class)
    // @ResponseStatus(HttpStatus.OK)
    // public Result<Void> handleDiscussionException(DiscussionException e) {
    //     log.warn("Discussion exception: {} - {}", e.getCode(), e.getMessage());
    //     return Result.failure(e.getCode(), e.getMessage());
    // }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BaseException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleBusinessException(BaseException e) {
        log.warn("Business exception: {}", e.getMessage());
        return Result.failure(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        
        // 可配置是否返回所有错误或第一个错误
        String errorMessage = getErrorMessage(fieldErrors, "Parameter validation failed");
        
        log.warn("Validation exception: {}", errorMessage);
        
        return Result.failure(
                SystemConstants.PARAMETER_ERROR_PREFIX,
                errorMessage
        );
    }

    /**
     * 处理绑定异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleBindException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        
        // 可配置是否返回所有错误或第一个错误
        String errorMessage = getErrorMessage(fieldErrors, "Parameter binding failed");
        
        log.warn("Binding exception: {}", errorMessage);
        
        return Result.failure(
                SystemConstants.PARAMETER_ERROR_PREFIX,
                errorMessage
        );
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e) {
        log.error("Unexpected exception", e);
        
        return Result.failure(
                SystemConstants.SYSTEM_ERROR_PREFIX,
                "系统错误，请稍后再试"
        );
    }
    
    /**
     * 获取错误消息
     * @param fieldErrors 字段错误列表
     * @param defaultMsg 默认消息
     * @return 格式化的错误消息
     */
    private String getErrorMessage(List<FieldError> fieldErrors, String defaultMsg) {
        if (fieldErrors.isEmpty()) {
            return defaultMsg;
        }
        
        // 配置项：是否返回所有错误
        boolean returnAllErrors = false;
        
        if (returnAllErrors) {
            return fieldErrors.stream()
                    .map(FieldError::getDefaultMessage)
                    .filter(msg -> msg != null && !msg.isEmpty())
                    .collect(java.util.stream.Collectors.joining(", "));
        } else {
            return fieldErrors.get(0).getDefaultMessage();
        }
    }
}