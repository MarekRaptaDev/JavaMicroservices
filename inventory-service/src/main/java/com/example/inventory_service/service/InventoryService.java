package com.example.inventory_service.service;

import com.example.inventory_service.dto.InventoryRequest;
import com.example.inventory_service.dto.InventoryResponse;
import com.example.inventory_service.model.Inventory;
import com.example.inventory_service.repository.InventoryRepository;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public boolean isInStock(String name, Integer quantity){
        return inventoryRepository.existsByNameAndQuantityIsGreaterThanEqual(name,quantity);
    }
    public Optional<InventoryResponse> findItem(String name){
        return inventoryRepository.findByName(name);
    }
    public void updateQuantity(InventoryRequest inventoryRequest) {

        InventoryResponse inventoryResponse = inventoryRepository.findByName(inventoryRequest.name())
                .orElseThrow(() -> new IllegalArgumentException("Item not found: " + inventoryRequest.name()));


        int updatedQuantity = inventoryResponse.quantity() + inventoryRequest.quantity();
        if (updatedQuantity < 0) {
            throw new IllegalArgumentException("Updated quantity must be 0 or more");
        } else {

            Inventory inventory = new Inventory();
            inventory.setId(inventoryResponse.id());
            inventory.setName(inventoryResponse.name());
            inventory.setQuantity(updatedQuantity);

            inventoryRepository.save(inventory);
        }
    }
    public void addItemToInventory(InventoryRequest inventoryRequest){
        if (inventoryRepository.findByName(inventoryRequest.name()).isPresent())
        {
            throw new IllegalArgumentException("Item "+inventoryRequest.name()+" is already in stock");
        }else {
            if (inventoryRequest.quantity()>=0){
                Inventory inventory = new Inventory();
                inventory.setName(inventoryRequest.name());
                inventory.setQuantity(inventoryRequest.quantity());
                inventoryRepository.save(inventory);
            }else {
                throw new IllegalArgumentException("Item's quantity must be 0 or more");
            }


        }
    }
}
