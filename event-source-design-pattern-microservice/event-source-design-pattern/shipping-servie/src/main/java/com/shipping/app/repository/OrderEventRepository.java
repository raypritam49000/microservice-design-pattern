package com.shipping.app.repository;

import com.shipping.app.entity.OrderEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderEventRepository extends MongoRepository<OrderEvent, String> {
}