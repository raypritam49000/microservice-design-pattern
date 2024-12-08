package com.auth_service.controller;

import com.auth_service.dto.APIResponseDTO;
import com.auth_service.dto.AuthenticationApiResponse;
import com.auth_service.dto.AuthenticationRequestDTO;
import com.auth_service.dto.UserDTO;
import com.auth_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/auth/")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login")
    public ResponseEntity<AuthenticationApiResponse> authenticateUser(@Valid @RequestBody AuthenticationRequestDTO request) {
        return new ResponseEntity<AuthenticationApiResponse>(userService.loginUser(request), HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<APIResponseDTO<UserDTO>> registerUser(@Valid @RequestBody UserDTO userDTO) {
        return new ResponseEntity<APIResponseDTO<UserDTO>>(new APIResponseDTO<UserDTO>(HttpStatus.CREATED.name(), HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase(), Boolean.TRUE, userService.registerUser(userDTO), "User has been registered successfully"), HttpStatus.CREATED);
    }
}
