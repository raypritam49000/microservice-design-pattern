package com.auth_service.service.impl;

import com.auth_service.dto.AuthenticationApiResponse;
import com.auth_service.dto.AuthenticationRequestDTO;
import com.auth_service.dto.UserDTO;
import com.auth_service.entity.User;
import com.auth_service.exception.BadCredentialsException;
import com.auth_service.exception.ResourceConflictException;
import com.auth_service.exception.ResourceNotFoundException;
import com.auth_service.mappers.UserMapper;
import com.auth_service.repository.UserRepository;
import com.auth_service.service.UserService;
import com.auth_service.util.JwtUtil;
import com.auth_service.crypto.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDTO getUserByEmail(String email) {
        return userMapper.userToUserDTO(userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with given email : " + email)));
    }

    @Override
    public AuthenticationApiResponse loginUser(AuthenticationRequestDTO request) {
        UserDTO userDTO = getUserByEmail(request.email());
        if (!passwordEncoder.matches(request.password(), userDTO.password())) {
            throw new BadCredentialsException("Invalid Credentials");
        }
        return new AuthenticationApiResponse(jwtUtil.generateToken(userDTO.email(), Map.<String, Object>of("id", userDTO.id(), "email", userDTO.email(), "role", userDTO.role())), "Bearer", "User has been login successfully");
    }

    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        userRepository.findByEmail(userDTO.email()).ifPresent(user -> {
            throw new ResourceConflictException("User already register with given email : " + user.getEmail());
        });

        User user = userMapper.userDTOToUser(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.userToUserDTO(userRepository.save(user));
    }
}
