package com.example.inventory_service.controller;

import com.example.inventory_service.dto.InventoryRequest;
import com.example.inventory_service.dto.InventoryResponse;
import com.example.inventory_service.service.InventoryService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Validated
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(
            @RequestParam @NotBlank(message = "Name cannot be blank") String name,
            @RequestParam @Min(value = 0, message = "Quantity must be greater than or equal to 0") Integer quantity) {

        return inventoryService.isInStock(name, quantity);
    }
    @GetMapping("/find")
    @ResponseStatus(HttpStatus.OK)
    public Optional<InventoryResponse> findItem( @RequestParam @NotBlank(message = "Name cannot be blank") String name) {
        return inventoryService.findItem(name);
    }
    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public String updateQuantity( @RequestBody InventoryRequest inventoryRequest) {
        inventoryService.updateQuantity(inventoryRequest);
        return "Quantity of item "+inventoryRequest.name()+" updated";
    }
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public String addItemToInventory(@RequestBody InventoryRequest inventoryRequest){
        inventoryService.addItemToInventory(inventoryRequest);
        return "Item added to stock";
    }
}
