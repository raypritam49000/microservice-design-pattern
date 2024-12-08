package com.order.service.exceptions;

public class ResourceConflictException extends RuntimeException{

    public ResourceConflictException(String message) {
        super(message);
    }

    public ResourceConflictException() {
    }
}
