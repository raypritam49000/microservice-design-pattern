package com.spring.boot.cqrs.project.command.api.commands;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;

@Data
@Builder
public class UpdateProductCommand {
    @TargetAggregateIdentifier
    private String productId;
    private String name;
    private BigDecimal price;
    private int quantity;
}
