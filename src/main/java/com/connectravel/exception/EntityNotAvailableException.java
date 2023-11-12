package com.connectravel.exception;

public class EntityNotAvailableException extends RuntimeException {
    public EntityNotAvailableException(String message) {
        super(message);
    }
    public EntityNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
