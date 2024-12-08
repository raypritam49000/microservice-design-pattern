package com.product.app.service;

import com.product.app.dto.ProductEvent;
import com.product.app.entity.Product;

public interface ProductCommandService {
    Product createProduct(ProductEvent productEvent);

    Product updateProduct(String id, ProductEvent productEvent);
}
