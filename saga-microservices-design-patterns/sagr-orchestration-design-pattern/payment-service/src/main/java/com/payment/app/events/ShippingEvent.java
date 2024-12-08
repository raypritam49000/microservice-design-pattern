package com.payment.app.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Data
public class ShippingEvent {
    private String orderId;
    private String status;
}
