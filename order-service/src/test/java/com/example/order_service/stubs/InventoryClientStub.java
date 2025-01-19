package com.example.order_service.stubs;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class InventoryClientStub {
    public static void stubInventoryCall(String name, Integer quantity, boolean inStock) {
        // Stub the inventory call for the product
        stubFor(get(urlEqualTo("/api/inventory?name=" + name + "&quantity=" + quantity))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(Boolean.toString(inStock)) // Return true or false as the response body
                        .withHeader("Content-Type", "application/json"))); // Set the content type as JSON
    }
}
