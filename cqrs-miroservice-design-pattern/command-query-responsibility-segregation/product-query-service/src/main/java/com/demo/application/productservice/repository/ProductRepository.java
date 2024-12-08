package com.demo.application.productservice.repository;

import com.demo.application.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
    Product findByProductId(String productId);
}
