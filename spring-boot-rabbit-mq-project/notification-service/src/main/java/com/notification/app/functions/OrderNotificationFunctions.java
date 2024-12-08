package com.notification.app.functions;

import com.notification.app.config.RabbitMQConfig;
import com.notification.app.dto.OrderDetail;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class OrderNotificationFunctions {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.REQUEST_QUEUE)
    @SendTo(value = RabbitMQConfig.RESPONSE_QUEUE)
    public String orderEventReceiver(OrderDetail orderDetail) {
        System.out.println("Sending notification to user");
        logicToSendEmailAndMessageToUser(orderDetail.getEmailId(), orderDetail.getUserPhone());
        return orderDetail.getOrderId();
    }

    private void logicToSendEmailAndMessageToUser(String emailId, String userPhone) {
        System.out.println("Sending Email to " + emailId);
        System.out.println("Sending Sms to " + userPhone);
        System.out.println("notification send to user");
        System.out.println("----------------------");
    }

}
