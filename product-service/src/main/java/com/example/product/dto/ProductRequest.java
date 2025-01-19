package com.example.product.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

// Dzięki temu, że to record, nie potrzebuje getterów
public record ProductRequest(

        String id,

        @NotBlank(message = "Name cannot be blank")
        String name,


        String description,

        @NotNull(message = "Price cannot be null")
        @Positive(message = "Price must be greater than 0")
        BigDecimal price
) {
}