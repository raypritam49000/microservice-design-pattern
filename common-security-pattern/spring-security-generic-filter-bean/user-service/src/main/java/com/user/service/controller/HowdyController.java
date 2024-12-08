package com.user.service.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/howdy")
public class HowdyController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HowdyController.class);

    @GetMapping("/hello")
    public String howdy(HttpServletRequest request) {
        LOGGER.info("@@@ Inside HowdyController howdy : {} ", request.getAttribute("loggedInUser"));
        return "Hello Howdy";
    }
}
