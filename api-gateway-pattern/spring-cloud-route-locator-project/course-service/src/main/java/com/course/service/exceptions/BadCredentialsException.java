package com.course.service.exceptions;

public class BadCredentialsException extends RuntimeException {

    public BadCredentialsException() {
    }

    public BadCredentialsException(String message) {
        super(message);
    }
}
