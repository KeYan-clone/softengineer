package org.example.discussion.config;

import org.example.discussion.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ValidationException;

/**
 * Global exception handler for REST controllers
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Handle validation exceptions
     *
     * @param e MethodArgumentNotValidException
     * @return Error response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse.Result<Void>> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, message));
    }
    
    /**
     * Handle bind exceptions
     *
     * @param e BindException
     * @return Error response
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse.Result<Void>> handleBindException(BindException e) {
        String message = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, message));
    }
    
    /**
     * Handle general validation exceptions
     *
     * @param e ValidationException
     * @return Error response
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse.Result<Void>> handleValidationException(ValidationException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, e.getMessage()));
    }
    
    /**
     * Handle access denied exceptions
     *
     * @param e AccessDeniedException
     * @return Error response
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse.Result<Void>> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(403, "Access denied"));
    }
    
    /**
     * Handle runtime exceptions
     *
     * @param e RuntimeException
     * @return Error response
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse.Result<Void>> handleRuntimeException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, e.getMessage()));
    }
    
    /**
     * Handle all other exceptions
     *
     * @param e Exception
     * @return Error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse.Result<Void>> handleException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "Internal server error"));
    }
}
