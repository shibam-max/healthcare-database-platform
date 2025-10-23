package com.healthcareplatform.analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Healthcare Analytics Service Application
 * 
 * Demonstrates Oracle Health Big Data requirements:
 * - Apache Kafka for real-time streaming
 * - Apache Spark for large-scale analytics
 * - Elasticsearch for healthcare insights
 * - Population health analytics
 */
@SpringBootApplication
public class HealthcareAnalyticsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthcareAnalyticsServiceApplication.class, args);
    }
}