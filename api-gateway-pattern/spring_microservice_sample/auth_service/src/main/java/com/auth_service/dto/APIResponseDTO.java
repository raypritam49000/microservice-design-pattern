package com.auth_service.dto;

public record APIResponseDTO<T>(
        String status,
        int statusCode,
        String statusMessage,
        boolean success,
        T data,
        String message
) {
}