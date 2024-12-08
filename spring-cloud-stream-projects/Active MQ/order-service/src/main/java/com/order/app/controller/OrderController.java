package com.order.app.controller;

import com.google.gson.Gson;
import com.order.app.dto.OrderDetail;
import com.order.app.service.OrderService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Resource
    private JmsTemplate jmsTemplate;

    @PostMapping
    public ResponseEntity<?> createOrder() {
        OrderDetail orderDetail = orderService.createOrder();
        orderCreatedNotification(orderDetail);
        return ResponseEntity.ok("Order Created");
    }

    private void orderCreatedNotification(OrderDetail orderDetail) {
        try {
            jmsTemplate.convertAndSend("orderCreatedEvent", new Gson().toJson(orderDetail));
            System.out.println("Order Success Event is successfully send to notification service");
        } catch (Exception ex) {
            System.out.println("Event Failed!!!");
        }
    }

}
