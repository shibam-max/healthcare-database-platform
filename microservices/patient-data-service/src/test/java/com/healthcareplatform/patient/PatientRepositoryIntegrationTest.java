package com.healthcareplatform.patient;

import com.healthcareplatform.patient.domain.Patient;
import com.healthcareplatform.patient.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration Tests for Patient Repository
 * 
 * Demonstrates Oracle Health testing requirements:
 * - TestContainers for integration testing
 * - PostgreSQL database testing
 * - Healthcare domain testing
 */
@DataJpaTest
@Testcontainers
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:tc:postgresql:15:///healthcare_test",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class PatientRepositoryIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("healthcare_test")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private PatientRepository patientRepository;

    @Test
    void shouldSaveAndFindPatientByMedicalRecordNumber() {
        // Given
        Patient patient = Patient.builder()
                .medicalRecordNumber("MRN-TEST-001")
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1980, 5, 15))
                .gender(Patient.Gender.MALE)
                .status(Patient.PatientStatus.ACTIVE)
                .privacyLevel(Patient.PrivacyLevel.NORMAL)
                .createdBy("test-system")
                .build();

        // When
        Patient savedPatient = patientRepository.save(patient);
        Optional<Patient> foundPatient = patientRepository.findByMedicalRecordNumber("MRN-TEST-001");

        // Then
        assertThat(savedPatient.getId()).isNotNull();
        assertThat(foundPatient).isPresent();
        assertThat(foundPatient.get().getFirstName()).isEqualTo("John");
        assertThat(foundPatient.get().getLastName()).isEqualTo("Doe");
        assertThat(foundPatient.get().getAge()).isEqualTo(44); // Approximate age
    }

    @Test
    void shouldFindActivePatients() {
        // Given
        Patient activePatient = Patient.builder()
                .medicalRecordNumber("MRN-ACTIVE-001")
                .firstName("Jane")
                .lastName("Smith")
                .dateOfBirth(LocalDate.of(1975, 8, 22))
                .gender(Patient.Gender.FEMALE)
                .status(Patient.PatientStatus.ACTIVE)
                .privacyLevel(Patient.PrivacyLevel.NORMAL)
                .createdBy("test-system")
                .build();

        patientRepository.save(activePatient);

        // When
        var activePatients = patientRepository.findActivePatients();

        // Then
        assertThat(activePatients).hasSize(1);
        assertThat(activePatients.get(0).getStatus()).isEqualTo(Patient.PatientStatus.ACTIVE);
    }
}