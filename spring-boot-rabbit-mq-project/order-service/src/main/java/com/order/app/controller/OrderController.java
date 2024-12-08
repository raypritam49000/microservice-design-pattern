package com.order.app.controller;

import com.order.app.config.RabbitMQConfig;
import com.order.app.dto.OrderDetail;
import com.order.app.service.OrderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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
    private RabbitTemplate rabbitTemplate;

    @PostMapping
    public ResponseEntity<?> createOrder() {
        OrderDetail orderDetail = orderService.createOrder();
        orderCreatedNotification(orderDetail);
        return ResponseEntity.ok("Order Created");
    }

    private void orderCreatedNotification(OrderDetail orderDetail) {
        try {
             rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.REQUEST_ROUTING_KEY, orderDetail);
           // Object object = rabbitTemplate.convertSendAndReceive(RabbitMQConfig.EXCHANGE, RabbitMQConfig.REQUEST_ROUTING_KEY, orderDetail);
            System.out.println("Order Success Event is successfully send to notification service");
           // System.out.println("--------- OK --------"+object);
        } catch (Exception ex) {
            System.out.println("Event Failed!!!");
        }
    }

}
