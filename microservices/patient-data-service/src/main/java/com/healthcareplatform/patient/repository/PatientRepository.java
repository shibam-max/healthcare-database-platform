package com.healthcareplatform.patient.repository;

import com.healthcareplatform.patient.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Patient Repository with Advanced JPA Queries
 * 
 * Demonstrates Oracle Health RDBMS expertise:
 * - Advanced JPA repository patterns
 * - Custom queries with performance optimization
 * - Healthcare-specific data access patterns
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    Optional<Patient> findByMedicalRecordNumber(String medicalRecordNumber);

    @Query("SELECT p FROM Patient p WHERE p.lastName = :lastName AND p.firstName = :firstName")
    List<Patient> findByFullName(@Param("lastName") String lastName, @Param("firstName") String firstName);

    @Query("SELECT p FROM Patient p WHERE p.dateOfBirth BETWEEN :startDate AND :endDate")
    List<Patient> findByAgeRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT p FROM Patient p JOIN p.chronicConditions cc WHERE cc IN :conditions")
    List<Patient> findByChronicConditions(@Param("conditions") List<String> conditions);

    @Query("SELECT p FROM Patient p WHERE p.status = 'ACTIVE' AND p.privacyLevel = 'NORMAL'")
    List<Patient> findActivePatients();

    @Query(value = "SELECT * FROM patients WHERE patient_embedding <-> ?1 < ?2 ORDER BY patient_embedding <-> ?1 LIMIT ?3", 
           nativeQuery = true)
    List<Patient> findSimilarPatientsByEmbedding(String embedding, double threshold, int limit);
}