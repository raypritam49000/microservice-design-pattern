package com.saga.orchestrator.app.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Data
public class OrderEvent {
    private String orderId;
    private String status;
}