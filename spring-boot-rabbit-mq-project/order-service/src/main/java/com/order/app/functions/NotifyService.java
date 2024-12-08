package com.order.app.functions;

import com.order.app.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class NotifyService {

    @RabbitListener(queues = RabbitMQConfig.RESPONSE_QUEUE)
    public void consumeAck(String orderId) {
        updateOrder();
        System.out.println("Ack Notification Success : " + orderId);
    }

    private void updateOrder() {
    }
}
