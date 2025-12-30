package com.project.ApiGateway.route;

import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;


@Configuration
public class InventoryServiceRoutes {

    @Bean
    public RouterFunction<ServerResponse> inventoryRoutes(){
        return GatewayRouterFunctions.route("inventory-service")
                .route(RequestPredicates.path("/api/v1/inventory/venue/{venueId}"),
                        request ->  forwardWithPathVariables(request,"venueId",
                                "http://localhost:8080/api/v1/inventory/venue/"))

                .route(RequestPredicates.path("/api/v1/inventory/event/{eventId}"),
                        request -> forwardWithPathVariables(request,"eventId",
                                "http://localhost:8080/api/v1/inventory/event/"))
                .build();
    }

    private ServerResponse forwardWithPathVariables(ServerRequest request,
                                                    String pathVariables,
                                                    String baseUrl) throws Exception {
        String value= request.pathVariable(pathVariables);
        return HandlerFunctions.http(baseUrl+value).handle(request);

    }

}
