package com.client.service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("first")
public class FirstController {

    @GetMapping("test")
    public ResponseEntity<Map<String, String>> readFirst(Authentication authentication) {
        return ResponseEntity.ok(Map.of("status", "access granted"));
    }

}