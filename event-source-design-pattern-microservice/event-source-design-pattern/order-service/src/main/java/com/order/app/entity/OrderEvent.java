package com.order.app.entity;

import com.order.app.dto.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Document(collection = "OrderEvents")
public class OrderEvent {
    @Id
    private String id;
    private String orderId;
    private OrderStatus status;  // CREATED, CONFIRMED
    private String details;
    private LocalDateTime eventTimestamp;

    public OrderEvent(String orderId, OrderStatus status, String details, LocalDateTime eventTimestamp) {
        this.orderId = orderId;
        this.status = status;
        this.details = details;
        this.eventTimestamp = eventTimestamp;
    }
}