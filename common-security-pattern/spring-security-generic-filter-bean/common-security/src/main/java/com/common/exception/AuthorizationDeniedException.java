package com.common.exception;

public class AuthorizationDeniedException extends RuntimeException {

    public AuthorizationDeniedException() {
    }

    public AuthorizationDeniedException(String message) {
        super(message);
    }
}
