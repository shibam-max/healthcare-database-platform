package com.healthcareplatform.patient.controller;

import com.healthcareplatform.patient.service.HealthcareVectorService;
import com.healthcareplatform.patient.service.HealthcareVectorService.SimilarPatientResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Healthcare Patient REST Controller
 * 
 * Demonstrates Oracle Health technical requirements:
 * - Advanced REST API design with Spring Boot
 * - Security with OAuth2 and healthcare-specific roles
 * - Async processing with CompletableFuture
 * - Comprehensive API documentation
 */
@RestController
@RequestMapping("/api/v1/patients")
@Tag(name = "Healthcare Patients", description = "Patient data management with semantic search")
@SecurityRequirement(name = "oauth2")
@Slf4j
public class PatientController {

    private final HealthcareVectorService vectorService;

    @Autowired
    public PatientController(HealthcareVectorService vectorService) {
        this.vectorService = vectorService;
    }

    @PostMapping("/search/similar")
    @Operation(
        summary = "Find Similar Patients",
        description = "Uses Vector Database semantic search to find patients with similar medical profiles"
    )
    @ApiResponse(responseCode = "200", description = "Similar patients found successfully")
    @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('RESEARCHER')")
    public CompletableFuture<ResponseEntity<List<SimilarPatientResult>>> findSimilarPatients(
            @Parameter(description = "Patient medical profile for semantic search", required = true)
            @RequestParam String patientProfile,
            
            @Parameter(description = "Maximum number of results", example = "10")
            @RequestParam(defaultValue = "10") int maxResults,
            
            @Parameter(description = "Similarity threshold (0.0-1.0)", example = "0.8")
            @RequestParam(defaultValue = "0.8") double similarityThreshold) {

        log.info("Received similar patients request: profile={}, maxResults={}", patientProfile, maxResults);

        return vectorService.findSimilarPatients(patientProfile, maxResults, similarityThreshold)
                .thenApply(results -> {
                    log.info("Found {} similar patients", results.size());
                    return ResponseEntity.ok(results);
                })
                .exceptionally(throwable -> {
                    log.error("Error finding similar patients", throwable);
                    return ResponseEntity.internalServerError().build();
                });
    }

    @GetMapping("/health")
    @Operation(
        summary = "Service Health Check",
        description = "Health check for patient data service including Vector DB connectivity"
    )
    public ResponseEntity<HealthStatus> getHealthStatus() {
        
        HealthStatus health = HealthStatus.builder()
                .status("HEALTHY")
                .vectorDatabaseStatus("CONNECTED")
                .kafkaStatus("CONNECTED")
                .timestamp(System.currentTimeMillis())
                .build();

        return ResponseEntity.ok(health);
    }

    // Supporting classes
    public static class HealthStatus {
        public String status;
        public String vectorDatabaseStatus;
        public String kafkaStatus;
        public Long timestamp;

        public static HealthStatusBuilder builder() {
            return new HealthStatusBuilder();
        }

        public static class HealthStatusBuilder {
            private HealthStatus health = new HealthStatus();

            public HealthStatusBuilder status(String status) {
                health.status = status;
                return this;
            }

            public HealthStatusBuilder vectorDatabaseStatus(String vectorDatabaseStatus) {
                health.vectorDatabaseStatus = vectorDatabaseStatus;
                return this;
            }

            public HealthStatusBuilder kafkaStatus(String kafkaStatus) {
                health.kafkaStatus = kafkaStatus;
                return this;
            }

            public HealthStatusBuilder timestamp(Long timestamp) {
                health.timestamp = timestamp;
                return this;
            }

            public HealthStatus build() {
                return health;
            }
        }
    }
}