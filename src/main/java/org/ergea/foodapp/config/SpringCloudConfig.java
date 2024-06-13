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
                .route(r -> r.path("/api/v1/auth/**")
                        .uri("http://localhost:8081")
                        .id("master"))
                .route(r -> r.path("/api/v1/users/**")
                        .uri("http://localhost:8081")
                        .id("users"))
                .route(r -> r.path("/api/v1/files/**")
                        .uri("http://localhost:8081")
                        .id("files"))
                .route(r -> r.path("/api/v1/products/**")
                        .uri("http://localhost:8082")
                        .id("product"))
                .route(r -> r.path("/api/v1/merchants/**")
                        .uri("http://localhost:8082")
                        .id("merchants"))
                .route(r -> r.path("/api/v1/orders/**")
                        .uri("http://localhost:8083")
                        .id("order"))
                .route(r -> r.path("/api/v1/invoice/**")
                        .uri("http://localhost:8083")
                        .id("invoice"))
                .route(r -> r.path("/api/v1/kafka/registry/**")
                        .uri("http://localhost:8083")
                        .id("kafka"))
                .build();
    }

}