package com.example.order_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public record OrderRequest(
        Long id,
        String orderNumber,
        @NotNull(message = "Name cannot be null")
        @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
        String name,
        @NotNull(message = "Product list cannot be null")
        List<ProductItem> products
) {
        public record ProductItem(
                @NotNull(message = "Product name cannot be null")
                String name,
                @NotNull(message = "Product price cannot be null")
                BigDecimal price,
                @NotNull(message = "Product quantity cannot be null")
                @Min(value = 1, message = "Product quantity must be at least 1")
                Integer quantity
        ) {}
}
