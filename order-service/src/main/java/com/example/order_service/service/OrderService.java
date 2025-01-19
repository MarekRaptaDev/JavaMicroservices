package com.example.order_service.service;

import com.example.order_service.client.InventoryClient;
import com.example.order_service.dto.OrderRequest;
import com.example.order_service.model.Order;
import com.example.order_service.model.OrderItem;
import com.example.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public void placeOrder(OrderRequest orderRequest) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderRequest.ProductItem productItem : orderRequest.products()) {

            boolean isInStock = inventoryClient.isInStock(productItem.name(), productItem.quantity());

            if (!isInStock) {
                throw new RuntimeException("Product named " + productItem.name() + " isn't in stock");
            }


            BigDecimal itemTotalPrice = productItem.price().multiply(BigDecimal.valueOf(productItem.quantity()));
            totalPrice = totalPrice.add(itemTotalPrice);


            OrderItem orderItem = new OrderItem();
            orderItem.setProductName(productItem.name());
            orderItem.setPrice(productItem.price());
            orderItem.setQuantity(productItem.quantity());
            orderItem.setTotalPrice(itemTotalPrice);

            orderItems.add(orderItem);
        }

        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setName(orderRequest.name());
        order.setPrice(totalPrice);
        order.setOrderItems(orderItems);

        orderRepository.save(order);
        System.out.println("Total Order Price: " + totalPrice);
    }
}
