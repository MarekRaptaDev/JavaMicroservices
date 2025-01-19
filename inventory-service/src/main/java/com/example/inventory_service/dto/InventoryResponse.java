package com.example.inventory_service.dto;

public record InventoryResponse(Long id,
                                String name,
                                Integer quantity) {
}
