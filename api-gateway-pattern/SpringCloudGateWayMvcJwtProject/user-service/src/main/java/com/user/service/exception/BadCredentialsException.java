package com.user.service.exception;

public class BadCredentialsException extends RuntimeException {

    public BadCredentialsException() {
    }

    public BadCredentialsException(String message) {
        super(message);
    }
}
