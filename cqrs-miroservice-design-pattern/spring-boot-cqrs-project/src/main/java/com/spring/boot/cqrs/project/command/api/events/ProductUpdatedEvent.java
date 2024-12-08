package com.spring.boot.cqrs.project.command.api.events;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductUpdatedEvent {
    private String productId;
    private String name;
    private BigDecimal price;
    private int quantity;
}