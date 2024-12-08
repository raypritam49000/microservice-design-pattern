package com.common.exception;

public class ResourceConflictException extends RuntimeException{

    public ResourceConflictException(String message) {
        super(message);
    }

    public ResourceConflictException() {
    }
}
