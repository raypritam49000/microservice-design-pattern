package com.user.service.service.impl;

import com.user.service.dto.AuthRequestDTO;
import com.user.service.dto.UserDTO;
import com.user.service.exception.ResourceConflictException;
import com.user.service.exception.ResourceNotFoundException;
import com.user.service.mappers.UserMapper;
import com.user.service.model.User;
import com.user.service.repository.UserRepository;
import com.user.service.security.JwtUtility;
import com.user.service.security.UserDetailsImpl;
import com.user.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    @Lazy
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtility jwtUtility;

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
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequestDTO.username());
        if (!passwordEncoder.matches(authRequestDTO.password(), userDetails.getPassword()))
            throw new BadCredentialsException("Invalid Credentials");
        return jwtUtility.generateToken((UserDetailsImpl) userDetails);
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
