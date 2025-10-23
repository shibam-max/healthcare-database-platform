package com.healthcareplatform.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

/**
 * Healthcare API Gateway Application
 * 
 * Demonstrates Oracle Health microservices architecture:
 * - Spring Cloud Gateway for intelligent routing
 * - Load balancing and circuit breaker patterns
 * - Healthcare-specific security and monitoring
 */
@SpringBootApplication
public class HealthcareApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthcareApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("patient-service", r -> r.path("/api/v1/patients/**")
                        .uri("http://localhost:8081"))
                .route("semantic-search", r -> r.path("/api/v1/semantic/**")
                        .uri("http://localhost:8082"))
                .route("analytics-service", r -> r.path("/api/v1/analytics/**")
                        .uri("http://localhost:8083"))
                .build();
    }
}