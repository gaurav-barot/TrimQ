package com.trimq.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown for invalid request data or business rule violations.
 */
public class BadRequestException extends TrimQException {

    private static final String ERROR_CODE = "BAD_REQUEST";

    public BadRequestException(String message) {
        super(message, ERROR_CODE, HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(String message, Object details) {
        super(message, ERROR_CODE, HttpStatus.BAD_REQUEST, details);
    }
}

