package com.auth_service.service;

import com.auth_service.dto.AuthenticationApiResponse;
import com.auth_service.dto.AuthenticationRequestDTO;
import com.auth_service.dto.UserDTO;

public interface UserService {
    UserDTO getUserByEmail(String email);
    AuthenticationApiResponse loginUser(AuthenticationRequestDTO authenticationRequest);
    UserDTO registerUser(UserDTO userDTO);
}
