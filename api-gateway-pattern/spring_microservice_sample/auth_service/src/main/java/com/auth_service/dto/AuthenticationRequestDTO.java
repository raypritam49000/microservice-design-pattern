package com.auth_service.dto;

import com.auth_service.validation.ValidEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record AuthenticationRequestDTO(
        @NotBlank(message = "Email can't be null or empty")
        @Email(message = "Email should be valid")
        //@ValidEmail(message = "Email should be valid")
        String email,
        @NotBlank(message = "Password can't be null or empty")
        String password
) {

}