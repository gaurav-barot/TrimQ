package com.trimq.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when an external service is unavailable.
 */
public class ServiceUnavailableException extends TrimQException {

    private static final String ERROR_CODE = "SERVICE_UNAVAILABLE";

    public ServiceUnavailableException(String serviceName) {
        super(
            String.format("%s service is currently unavailable. Please try again later.", serviceName),
            ERROR_CODE,
            HttpStatus.SERVICE_UNAVAILABLE
        );
    }

    public ServiceUnavailableException(String serviceName, Throwable cause) {
        super(
            String.format("%s service is currently unavailable. Please try again later.", serviceName),
            ERROR_CODE,
            HttpStatus.SERVICE_UNAVAILABLE,
            cause
        );
    }
}

