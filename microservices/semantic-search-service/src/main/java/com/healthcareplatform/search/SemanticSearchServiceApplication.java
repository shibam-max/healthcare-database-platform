package com.healthcareplatform.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Semantic Search Service Application
 * 
 * Demonstrates Oracle Health Vector Database requirements:
 * - Advanced semantic search with pgvector
 * - Healthcare data similarity matching
 * - Real-time search capabilities
 */
@SpringBootApplication
public class SemanticSearchServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SemanticSearchServiceApplication.class, args);
    }
}