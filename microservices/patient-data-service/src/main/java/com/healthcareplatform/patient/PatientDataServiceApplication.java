package com.healthcareplatform.patient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Patient Data Service Application
 * 
 * Healthcare microservice demonstrating Oracle Health technical requirements:
 * - Spring Boot 3.4.1 enterprise application
 * - Vector database integration with pgvector
 * - Big data processing with Kafka
 * - JVM performance optimization
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableKafka
public class PatientDataServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PatientDataServiceApplication.class, args);
    }
}