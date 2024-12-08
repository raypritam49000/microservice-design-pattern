package com.order.app.controller;

import com.order.app.dto.OrderDetail;
import com.order.app.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private StreamBridge streamBridge;

    @PostMapping
    public ResponseEntity<?> createOrder() {
        OrderDetail orderDetail = orderService.createOrder();
        orderCreatedNotification(orderDetail);
        return ResponseEntity.ok("Order Created");
    }

    private void orderCreatedNotification(OrderDetail orderDetail) {
        boolean sendMessage = streamBridge.send("orderCreatedEvent-out-0", orderDetail);
        if (sendMessage) {
            System.out.println("Order Success Event is successfully send to notification service");
        } else {
            System.out.println("Event Failed!!!");
        }
    }

}
