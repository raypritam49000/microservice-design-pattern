package com.api.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Autowired
    private AuthFilter authFilter;

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("client-service", r -> r.path("/api/v1/**")
                        .filters(f -> f.removeRequestHeader("Authorization")
                                .filter(authFilter.apply(new AuthFilterConfig())))
                        .uri("lb://client-service/")
                ).build();
    }


}
