package com.user.service.controller;

import com.user.service.dto.APIResponseDTO;
import com.user.service.dto.AuthRequestDTO;
import com.user.service.dto.UserDTO;
import com.user.service.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody AuthRequestDTO authRequestDTO) {
        LOGGER.info("@@@ loginUser ::: {}", authRequestDTO);
        return new ResponseEntity<Map<String, Object>>(Map.<String, Object>of("accessToken", userService.loginUser(authRequestDTO), "type", "Bearer"), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<APIResponseDTO<UserDTO>> registerUser(@RequestBody UserDTO userDTO) {
        LOGGER.info("@@@ registerUser ::: {}", userDTO);
        return new ResponseEntity<APIResponseDTO<UserDTO>>(new APIResponseDTO<UserDTO>(HttpStatus.CREATED.name(), HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase(), Boolean.TRUE, userService.registerUser(userDTO), "User has been register successfully"), HttpStatus.CREATED);
    }
}
