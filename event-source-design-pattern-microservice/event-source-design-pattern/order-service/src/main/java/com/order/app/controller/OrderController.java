package com.order.app.controller;

import com.order.app.dto.request.OrderRequest;
import com.order.app.dto.response.OrderResponse;
import com.order.app.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    // Endpoint to place an order
    @PostMapping("/place")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest orderRequest) {
        LOGGER.info("Received request to place order with details: {}", orderRequest);
        try {
            OrderResponse orderResponse = orderService.placeAnOrder(orderRequest);
            LOGGER.info("Order placed successfully with response: {}", orderResponse);
            return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error("Error occurred while placing the order: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint to confirm an order
    @PutMapping("/confirm/{orderId}")
    public ResponseEntity<OrderResponse> confirmOrder(@PathVariable String orderId) {
        LOGGER.info("Received request to confirm order with ID: {}", orderId);
        try {
            OrderResponse orderResponse = orderService.confirmOrder(orderId);
            LOGGER.info("Order confirmation successful with response: {}", orderResponse);
            return new ResponseEntity<>(orderResponse, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Error occurred while confirming order with ID {}: {}", orderId, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
