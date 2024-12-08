package com.user.service.service;

import com.user.service.dto.AuthRequestDTO;
import com.user.service.dto.UserDTO;

public interface UserService {
    public UserDTO getUserByUsername(String username);

    public UserDTO getUserByEmail(String email);

    public boolean exitsByUsername(String username);

    public boolean exitsByEmail(String email);

    public String loginUser(AuthRequestDTO authRequestDTO);

    public UserDTO registerUser(UserDTO userDTO);
}
