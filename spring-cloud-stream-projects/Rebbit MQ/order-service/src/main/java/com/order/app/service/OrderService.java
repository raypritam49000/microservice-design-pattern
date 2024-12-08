package com.order.app.service;

import com.order.app.dto.OrderDetail;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class OrderService {

    public OrderDetail createOrder() {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(UUID.randomUUID().toString());
        orderDetail.setUserId(UUID.randomUUID().toString());
        orderDetail.setEmailId("manish@gmail.com");
        orderDetail.setCreatedDate(new Date());
        orderDetail.setPrice("1200");
        orderDetail.setUserPhone("8699535682");
        orderDetail.setCourseId(UUID.randomUUID().toString());
        return orderDetail;
    }
}
