package com.auth_service.dto;

public record AuthenticationApiResponse(String accessToken, String type, String message) {
}
