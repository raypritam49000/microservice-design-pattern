package com.user.service.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/howdy")
public class HowdyController {


    @GetMapping("/hello")
    @PreAuthorize("hasAuthority('admin')")
    public String howdy() {
        return "Hello Howdy";
    }
}