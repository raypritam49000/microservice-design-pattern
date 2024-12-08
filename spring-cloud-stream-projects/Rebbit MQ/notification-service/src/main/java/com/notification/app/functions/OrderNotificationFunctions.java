package com.notification.app.functions;

import com.notification.app.dto.OrderDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class OrderNotificationFunctions {

    @Bean
    public Function<OrderDetail, String> orderEventReceiver() {
        return (orderDetail) -> {
            System.out.println("Sending notification to user");
            logicToSendEmailAndMessageToUser(orderDetail.getEmailId(), orderDetail.getUserPhone());
            return orderDetail.getOrderId();
        };
    }

    private void logicToSendEmailAndMessageToUser(String emailId, String userPhone) {
        System.out.println("Sending Email to " + emailId);
        System.out.println("Sending Sms to " + userPhone);
        System.out.println("notification send to user");
        System.out.println("----------------------");
    }

}
