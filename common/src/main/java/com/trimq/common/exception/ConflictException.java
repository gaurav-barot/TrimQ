package com.trimq.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown for resource conflicts (e.g., duplicate booking, slot already booked).
 */
public class ConflictException extends TrimQException {

    private static final String ERROR_CODE = "CONFLICT";

    public ConflictException(String message) {
        super(message, ERROR_CODE, HttpStatus.CONFLICT);
    }

    public ConflictException(String message, Object details) {
        super(message, ERROR_CODE, HttpStatus.CONFLICT, details);
    }
}

