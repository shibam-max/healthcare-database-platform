package com.healthcareplatform.patient.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Patient Entity for Healthcare Semantic Database Platform
 * 
 * Demonstrates Oracle Health technical requirements:
 * - Advanced JPA entity design with healthcare domain modeling
 * - Vector database integration for semantic search
 * - HIPAA-compliant audit fields and data handling
 * - Performance optimization with proper indexing
 */
@Entity
@Table(name = "patients", 
       indexes = {
           @Index(name = "idx_patient_mrn", columnList = "medical_record_number", unique = true),
           @Index(name = "idx_patient_name", columnList = "last_name, first_name"),
           @Index(name = "idx_patient_dob", columnList = "date_of_birth"),
           @Index(name = "idx_patient_created", columnList = "created_at")
       })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "medical_record_number", unique = true, nullable = false, length = 50)
    private String medicalRecordNumber;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 20)
    private Gender gender;

    // Vector embedding for semantic patient matching
    @Column(name = "patient_embedding", columnDefinition = "vector(1536)")
    private String patientEmbedding;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "patient_allergies", 
                    joinColumns = @JoinColumn(name = "patient_id"))
    @Column(name = "allergy")
    private List<String> allergies;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "patient_conditions", 
                    joinColumns = @JoinColumn(name = "patient_id"))
    @Column(name = "condition_code")
    private List<String> chronicConditions;

    @Enumerated(EnumType.STRING)
    @Column(name = "patient_status", nullable = false)
    private PatientStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "privacy_level", nullable = false)
    private PrivacyLevel privacyLevel;

    // Audit fields for HIPAA compliance
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by", nullable = false, length = 100)
    private String createdBy;

    @Version
    @Column(name = "version")
    private Long version;

    // Business methods
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public int getAge() {
        return LocalDate.now().getYear() - dateOfBirth.getYear();
    }

    public boolean hasAllergy(String allergen) {
        return allergies != null && allergies.contains(allergen.toLowerCase());
    }

    public boolean isHighRiskPatient() {
        return getAge() > 65 || 
               (chronicConditions != null && chronicConditions.size() > 2);
    }

    // Enums
    public enum Gender {
        MALE, FEMALE, OTHER, UNKNOWN
    }

    public enum PatientStatus {
        ACTIVE, INACTIVE, DECEASED, TRANSFERRED
    }

    public enum PrivacyLevel {
        NORMAL, RESTRICTED, CONFIDENTIAL
    }
}