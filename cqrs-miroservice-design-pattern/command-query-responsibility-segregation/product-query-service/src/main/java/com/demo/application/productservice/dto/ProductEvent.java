package com.demo.application.productservice.dto;

import com.demo.application.productservice.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductEvent {
    private String eventType;
    private Product product;
}
