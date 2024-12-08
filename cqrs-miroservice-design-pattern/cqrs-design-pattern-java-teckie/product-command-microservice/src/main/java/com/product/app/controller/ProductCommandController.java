package com.product.app.controller;

import com.product.app.dto.ProductEvent;
import com.product.app.entity.Product;
import com.product.app.service.ProductCommandService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductCommandController {
    private final ProductCommandService commandService;

    public ProductCommandController(ProductCommandService commandService) {
        this.commandService = commandService;
    }

    @PostMapping
    public Product createProduct(@RequestBody ProductEvent productEvent) {
        return commandService.createProduct(productEvent);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable String id, @RequestBody ProductEvent productEvent) {
        return commandService.updateProduct(id, productEvent);
    }
    
}