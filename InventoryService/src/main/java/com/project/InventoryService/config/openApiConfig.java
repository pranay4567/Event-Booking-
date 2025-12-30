package com.project.InventoryService.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class openApiConfig {

    @Bean
    public OpenAPI inventoryServiceApi(){
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                                .title("Inventory Service API")
                                .description("Inventory Service API EndPoints")
                                .version("v1.0.0")
                        );
    }

}
