package com.order.app.functions;

import jakarta.jms.TextMessage;
import org.apache.activemq.Message;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.JmsListener;

@Configuration
public class NotifyService {

    @JmsListener(destination = "order.ack")
    public void consumeAck(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String orderAck = textMessage.getText();
                System.out.println(orderAck);
            }
        } catch (Exception e) {
            System.out.println("Received Exception: " + e.getMessage());
        }
    }

    private void updateOrder() {
    }
}
