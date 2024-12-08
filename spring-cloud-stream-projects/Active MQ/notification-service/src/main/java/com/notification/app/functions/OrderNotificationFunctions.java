package com.notification.app.functions;

import com.google.gson.Gson;
import com.notification.app.dto.OrderDetail;
import jakarta.annotation.Resource;
import jakarta.jms.TextMessage;
import org.apache.activemq.Message;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class OrderNotificationFunctions {

    @Resource
    private JmsTemplate jmsTemplate;

    @JmsListener(destination = "orderCreatedEvent")
    public void orderEventReceiver(Message message) {
        try {
            System.out.println("Sending notification to user");
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String orderDetailString = textMessage.getText();
                OrderDetail orderDetail = new Gson().fromJson(orderDetailString, OrderDetail.class);
                System.out.println("Received Message: " + orderDetail.toString());
                logicToSendEmailAndMessageToUser(orderDetail.getEmailId(), orderDetail.getUserPhone());
                jmsTemplate.convertAndSend("order.ack","sending notification to user");
            }
        } catch (Exception e) {
            System.out.println("Received Exception: " + e.getMessage());
        }
    }

    private void logicToSendEmailAndMessageToUser(String emailId, String userPhone) {
        System.out.println("Sending Email to " + emailId);
        System.out.println("Sending Sms to " + userPhone);
        System.out.println("notification send to user");
        System.out.println("----------------------");
    }

}
