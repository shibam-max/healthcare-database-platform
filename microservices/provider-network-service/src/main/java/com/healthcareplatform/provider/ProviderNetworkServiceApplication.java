package com.healthcareplatform.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;

/**
 * Provider Network Service Application
 * 
 * Demonstrates Oracle Health technical requirements:
 * - Spring WebFlux for reactive programming
 * - MongoDB for flexible provider data
 * - Redis for high-performance caching
 * - gRPC for inter-service communication
 */
@SpringBootApplication
@EnableReactiveMongoAuditing
public class ProviderNetworkServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderNetworkServiceApplication.class, args);
    }
}