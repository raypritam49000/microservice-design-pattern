package com.shipping.app.controller;

import com.shipping.app.service.ShippingEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shipping")
public class ShippingController {
    private static final Logger logger = LoggerFactory.getLogger(ShippingController.class);

    @Autowired
    private ShippingEventService shippingEventService;

    @PostMapping("/{orderId}/ship")
    public ResponseEntity<String> shipOrder(@PathVariable String orderId) {
        logger.info("Received request to ship order: {}", orderId);
        try {
            shippingEventService.shipOrder(orderId);
            logger.info("Order {} shipped successfully.", orderId);
            return ResponseEntity.ok("Order shipped successfully.");
        } catch (Exception e) {
            logger.error("Error while shipping order {}: {}", orderId, e.getMessage());
            return ResponseEntity.status(500).body("Error while shipping the order.");
        }
    }

    @PostMapping("/{orderId}/deliver")
    public ResponseEntity<String> deliverOrder(@PathVariable String orderId) {
        logger.info("Received request to deliver order: {}", orderId);
        try {
            shippingEventService.deliverOrder(orderId);
            logger.info("Order {} delivered successfully.", orderId);
            return ResponseEntity.ok("Order delivered successfully.");
        } catch (Exception e) {
            logger.error("Error while delivering order {}: {}", orderId, e.getMessage());
            return ResponseEntity.status(500).body("Error while delivering the order.");
        }
    }
}
