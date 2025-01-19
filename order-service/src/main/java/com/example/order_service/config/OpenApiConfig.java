package com.example.order_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI orderServiceApi(){
        return new OpenAPI()
                .info(new Info().title("Order Service API documentation")
                        .description("Documentation of order-service responsible for submiting orders, and contacting inventory service with synchronous communication")
                        .version("v0.0.2")
                        .license(new License().name("Beerware"))
                        .contact(new Contact()
                                .name("Marek Rapta")
                                .email("marek.rapta@gmail.com"))
                );
    }
}
