package org.example.experiment.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.experiment.exception.ExperimentNotFoundException;
import org.example.experiment.exception.ResultNotFoundException;
import org.example.experiment.exception.UnauthorizedAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for controllers
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    /**
     * Handle ExperimentNotFoundException
     *
     * @param ex ExperimentNotFoundException
     * @param request WebRequest
     * @return Error response
     */
    @ExceptionHandler(ExperimentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleExperimentNotFoundException(
            ExperimentNotFoundException ex, WebRequest request) {
        log.error("Experiment not found: {}", ex.getMessage());
        
        Map<String, Object> body = new HashMap<>();
        body.put("code", 404);
        body.put("message", ex.getMessage());
        
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
    
    /**
     * Handle ResultNotFoundException
     *
     * @param ex ResultNotFoundException
     * @param request WebRequest
     * @return Error response
     */
    @ExceptionHandler(ResultNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleResultNotFoundException(
            ResultNotFoundException ex, WebRequest request) {
        log.error("Result not found: {}", ex.getMessage());
        
        Map<String, Object> body = new HashMap<>();
        body.put("code", 404);
        body.put("message", ex.getMessage());
        
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
    
    /**
     * Handle UnauthorizedAccessException
     *
     * @param ex UnauthorizedAccessException
     * @param request WebRequest
     * @return Error response
     */
    @ExceptionHandler(UnauthorizedAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleUnauthorizedAccessException(
            UnauthorizedAccessException ex, WebRequest request) {
        log.error("Unauthorized access: {}", ex.getMessage());
        
        Map<String, Object> body = new HashMap<>();
        body.put("code", 403);
        body.put("message", ex.getMessage());
        
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }
    
    /**
     * Handle validation exceptions
     *
     * @param ex MethodArgumentNotValidException
     * @return Error response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Validation error: {}", ex.getMessage());
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        Map<String, Object> body = new HashMap<>();
        body.put("code", 400);
        body.put("message", "Validation failed");
        body.put("errors", errors);
        
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Handle all other exceptions
     *
     * @param ex Exception
     * @param request WebRequest
     * @return Error response
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        log.error("Internal server error", ex);
        
        Map<String, Object> body = new HashMap<>();
        body.put("code", 500);
        body.put("message", "An unexpected error occurred");
        
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
