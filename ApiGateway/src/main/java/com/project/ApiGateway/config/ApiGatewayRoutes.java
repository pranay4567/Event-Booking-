package com.project.ApiGateway.config;

import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class ApiGatewayRoutes {

    @Bean
    public RouterFunction<ServerResponse> openApiRoutes() {
        return GatewayRouterFunctions.route("booking-api-docs")
                .route(RequestPredicates.GET("/docs/booking/v3/api-docs"),
                        HandlerFunctions.http("http://localhost:8081/v3/api-docs"))
                .route(RequestPredicates.GET("/docs/inventory/v3/api-docs"),
                        HandlerFunctions.http("http://localhost:8080/v3/api-docs"))
                .build();
    }
}

