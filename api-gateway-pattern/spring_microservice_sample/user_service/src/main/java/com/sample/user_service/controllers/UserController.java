package com.sample.user_service.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public ResponseEntity<String> getUsers(@RequestHeader("email") String email, @RequestHeader("id") String id, @RequestHeader("role") String role) {
        logger.info("Request received with email: {}, id: {}, role: {}", email, id, role);
        return ResponseEntity.ok("These are all the users");
    }
}
