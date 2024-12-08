package com.javatechie.controller;

import com.javatechie.dto.request.OrderRequest;
import com.javatechie.dto.response.OrderResponse;
import com.javatechie.service.OrderService;
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
        LOGGER.info("@@@ OrderController placeOrder method OrderRequest : {} ", orderRequest);
        try {
            OrderResponse orderResponse = orderService.placeAnOrder(orderRequest);
            LOGGER.info("@@@ OrderController placeOrder method OrderResponse : {} ", orderResponse);
            return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint to confirm an order
    @PutMapping("/confirm/{orderId}")
    public ResponseEntity<OrderResponse> confirmOrder(@PathVariable String orderId) {
        LOGGER.info("@@@ OrderController confirmOrder method orderId : {} ", orderId);
        try {
            OrderResponse orderResponse = orderService.confirmOrder(orderId);
            LOGGER.info("@@@ OrderController confirmOrder method OrderResponse : {} ", orderResponse);
            return new ResponseEntity<>(orderResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
