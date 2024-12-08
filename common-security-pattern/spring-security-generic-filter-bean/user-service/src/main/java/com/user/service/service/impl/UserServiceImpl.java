package com.user.service.service.impl;

import com.common.exception.BadCredentialsException;
import com.common.exception.ResourceConflictException;
import com.common.exception.ResourceNotFoundException;
import com.common.jsonwebtoken.AuthTokenDetailsDTO;
import com.common.jsonwebtoken.JsonWebTokenUtility;
import com.common.jsonwebtoken.PasswordEncoder;
import com.user.service.dto.AuthRequestDTO;
import com.user.service.dto.UserDTO;
import com.user.service.mappers.UserMapper;
import com.user.service.model.User;
import com.user.service.repository.UserRepository;
import com.user.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JsonWebTokenUtility jwtUtility;

    @Override
    public UserDTO getUserByUsername(String username) {
        return userMapper.userToUserDTO(userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found with given username : " + username)));
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return userMapper.userToUserDTO(userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with given email : " + email)));
    }

    @Override
    public boolean exitsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public boolean exitsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public String loginUser(AuthRequestDTO authRequestDTO) {
        UserDTO userDetails = getUserByUsername(authRequestDTO.username());
        if (!passwordEncoder.matches(authRequestDTO.password(), userDetails.password()))
            throw new BadCredentialsException("Invalid Credentials");
        AuthTokenDetailsDTO authTokenDetailsDTO = new AuthTokenDetailsDTO();
        authTokenDetailsDTO.setId(userDetails.id());
        authTokenDetailsDTO.setUsername(userDetails.username());
        authTokenDetailsDTO.setEmail(userDetails.email());
        authTokenDetailsDTO.setRoles(List.of(userDetails.role()));
        return jwtUtility.createJsonWebToken(authTokenDetailsDTO);
    }

    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        if (exitsByUsername(userDTO.username()))
            throw new ResourceConflictException("User already register with given username : " + userDTO.username());
        if (exitsByEmail(userDTO.email()))
            throw new ResourceConflictException("User already register with given email : " + userDTO.email());
        User user = userMapper.userDTOToUser(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.userToUserDTO(userRepository.save(user));
    }
}
