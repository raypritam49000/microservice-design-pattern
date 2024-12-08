package com.order.app.service;

import com.order.app.dto.request.OrderRequest;
import com.order.app.dto.response.OrderResponse;

public interface OrderService {
    OrderResponse placeAnOrder(OrderRequest orderRequest);
    OrderResponse confirmOrder(String orderId);
}
