package com.healthcareplatform.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Healthcare Event DTO
 * 
 * Shared domain model for healthcare events across microservices.
 * Demonstrates Oracle Health technical requirements for event-driven architecture.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthcareEventDto {
    
    private UUID eventId;
    private String eventType;
    private UUID patientId;
    private UUID providerId;
    private String eventData;
    private LocalDateTime timestamp;
    private String source;
    private EventPriority priority;
    private List<String> tags;

    public enum EventPriority {
        LOW, NORMAL, HIGH, CRITICAL, EMERGENCY
    }
}