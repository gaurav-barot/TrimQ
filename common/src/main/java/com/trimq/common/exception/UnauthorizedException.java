package com.trimq.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when authentication fails or token is invalid.
 */
public class UnauthorizedException extends TrimQException {

    private static final String ERROR_CODE = "UNAUTHORIZED";

    public UnauthorizedException(String message) {
        super(message, ERROR_CODE, HttpStatus.UNAUTHORIZED);
    }

    public UnauthorizedException() {
        super("Authentication required", ERROR_CODE, HttpStatus.UNAUTHORIZED);
    }
}

