package com.trimq.common.exception;

import com.trimq.common.dto.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for consistent error responses across all services.
 * Follows Single Responsibility Principle - handles only exception mapping.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle TrimQ business exceptions
     */
    @ExceptionHandler(TrimQException.class)
    public ResponseEntity<ApiResponse<Object>> handleTrimQException(
            TrimQException ex, WebRequest request) {
        
        log.error("Business exception: {} - {}", ex.getErrorCode(), ex.getMessage());
        
        ApiResponse<Object> response = ApiResponse.error(
                ex.getMessage(),
                ApiResponse.ErrorDetails.builder()
                        .code(ex.getErrorCode())
                        .message(ex.getMessage())
                        .details(ex.getDetails())
                        .build()
        );
        
        return new ResponseEntity<>(response, ex.getHttpStatus());
    }

    /**
     * Handle validation exceptions from @Valid
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(
            MethodArgumentNotValidException ex) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        log.warn("Validation failed: {}", errors);

        ApiResponse<Object> response = ApiResponse.error(
                "Validation failed",
                ApiResponse.ErrorDetails.builder()
                        .code("VALIDATION_ERROR")
                        .message("One or more fields have invalid values")
                        .details(errors)
                        .build()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle constraint violation exceptions
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolation(
            ConstraintViolationException ex) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        });

        log.warn("Constraint violation: {}", errors);

        ApiResponse<Object> response = ApiResponse.error(
                "Constraint violation",
                ApiResponse.ErrorDetails.builder()
                        .code("CONSTRAINT_VIOLATION")
                        .message("Request violates constraints")
                        .details(errors)
                        .build()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle all other exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(
            Exception ex, WebRequest request) {
        
        log.error("Unexpected error occurred", ex);

        ApiResponse<Object> response = ApiResponse.error(
                "An unexpected error occurred. Please try again later.",
                ApiResponse.ErrorDetails.builder()
                        .code("INTERNAL_ERROR")
                        .message("Internal server error")
                        .build()
        );

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

