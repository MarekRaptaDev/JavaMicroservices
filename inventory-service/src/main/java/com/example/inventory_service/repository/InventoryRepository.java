package com.example.inventory_service.repository;

import com.example.inventory_service.dto.InventoryResponse;
import com.example.inventory_service.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory,Long> {
    boolean existsByNameAndQuantityIsGreaterThanEqual(String name, Integer quantity);
    Optional<InventoryResponse> findByName(String name);
}
