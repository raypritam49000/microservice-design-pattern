package com.user.service.dto;

public record UserDTO(
        String id,
        String username,
        String password,
        String email,
        String role) {
}
