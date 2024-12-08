package com.course.service.exceptions;

public class AuthorizationDeniedException extends RuntimeException {

    public AuthorizationDeniedException() {
    }

    public AuthorizationDeniedException(String message) {
        super(message);
    }
}
