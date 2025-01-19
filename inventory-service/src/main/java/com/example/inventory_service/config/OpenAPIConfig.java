package com.example.inventory_service.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI TablesServiceApi(){
        return new OpenAPI()
                .info(new Info().title("Inventory Service API documentation")
                        .description("Documentation of inventory-service responsible for motorizing stock, and sending responses to order-service ")
                        .version("v0.0.2")
                        .license(new License().name("Beerware"))
                        .contact(new Contact()
                                .name("Marek Rapta")
                                .email("marek.rapta@gmail.com"))
                );
    }
}
