package com.auth_service.exception;

public class ResourceConflictException extends RuntimeException{

    public ResourceConflictException(String message) {
        super(message);
    }

    public ResourceConflictException() {
    }
}
