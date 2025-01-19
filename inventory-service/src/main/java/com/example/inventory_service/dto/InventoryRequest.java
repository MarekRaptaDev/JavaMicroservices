package com.example.inventory_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record InventoryRequest(@NotBlank(message = "Name cannot be blank") String name,

                               Integer quantity) {
}
