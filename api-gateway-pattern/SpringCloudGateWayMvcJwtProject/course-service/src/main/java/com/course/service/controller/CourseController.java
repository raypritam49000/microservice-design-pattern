package com.course.service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/course")
public class CourseController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/getProducts")
    public List<Map<String, Object>> getOrders(
            @RequestHeader("id") String id,
            @RequestHeader("email") String email,
            @RequestHeader("username") String username,
            @RequestHeader("role") String role
    ) {
        logger.info("Received Request with headers - id: {}, email: {}, username: {}, role: {}", id, email, username, role);
        List<Map<String, Object>> ordersList = new ArrayList<>();

        // Creating the first order
        Map<String, Object> order1 = new HashMap<>();
        order1.put("orderId", "O001");
        order1.put("productName", "Laptop");
        order1.put("quantity", 1);
        order1.put("price", 1000.00);
        ordersList.add(order1);

        // Creating the second order
        Map<String, Object> order2 = new HashMap<>();
        order2.put("orderId", "O002");
        order2.put("productName", "Smartphone");
        order2.put("quantity", 2);
        order2.put("price", 500.00);
        ordersList.add(order2);

        // Creating the third order
        Map<String, Object> order3 = new HashMap<>();
        order3.put("orderId", "O003");
        order3.put("productName", "Tablet");
        order3.put("quantity", 3);
        order3.put("price", 300.00);
        ordersList.add(order3);

        return ordersList;
    }
}