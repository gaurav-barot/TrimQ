package com.trimq.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when user doesn't have permission for an action.
 */
public class ForbiddenException extends TrimQException {

    private static final String ERROR_CODE = "FORBIDDEN";

    public ForbiddenException(String message) {
        super(message, ERROR_CODE, HttpStatus.FORBIDDEN);
    }

    public ForbiddenException() {
        super("You don't have permission to perform this action", ERROR_CODE, HttpStatus.FORBIDDEN);
    }
}

