package org.ergea.foodapp.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringCloudConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/api/v1/**")
                        .uri("http://localhost:8081")
                        .id("master"))
                .route(r -> r.path("/api/v1/**")
                        .uri("http://localhost:8082")
                        .id("product"))
                .route(r -> r.path("/api/v1/**")
                        .uri("http://localhost:8083")
                        .id("order"))
                .build();
    }

}