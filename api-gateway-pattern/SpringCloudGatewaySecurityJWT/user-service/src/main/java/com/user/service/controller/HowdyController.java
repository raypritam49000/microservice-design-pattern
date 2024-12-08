package com.user.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/howdy")
public class HowdyController {

    private static final Logger logger = LoggerFactory.getLogger(HowdyController.class);

    @GetMapping("/hello")
    public String howdy(@RequestHeader("id") String id,
                        @RequestHeader("email") String email,
                        @RequestHeader("username") String username,
                        @RequestHeader("role") String role) {
        logger.info("Received Request with headers - id: {}, email: {}, username: {}, role: {}", id, email, username, role);

        return "Hello Howdy";
    }
}
