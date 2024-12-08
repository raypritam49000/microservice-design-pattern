package com.shipping.app.service;

import com.shipping.app.entity.OrderEvent;

public interface ShippingEventService {
    void consumeOrderEvent(String orderEvent);
    void shipOrder(String orderId);
    void deliverOrder(String orderId);
}
