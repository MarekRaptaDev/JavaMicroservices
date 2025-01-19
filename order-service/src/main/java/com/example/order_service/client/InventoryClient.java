package com.example.order_service.client;

import groovy.util.logging.Slf4j;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

@Slf4j
public interface InventoryClient {

    Logger log = LoggerFactory.getLogger(InventoryClient.class);

    @GetExchange("/api/inventory")
    @CircuitBreaker(name="inventory", fallbackMethod="fallbackMethod")
    @Retry(name = "inventory")
    boolean isInStock(@RequestParam String name, @RequestParam Integer quantity);

    default boolean fallbackMethod(String name, Integer quantity, Throwable throwable) {
        log.info("Cannot get inventory status for item named {}, failure reason {}", name, throwable.getMessage());
        return false;
    }
}
