package com.healthcareplatform.patient.service;

import com.healthcareplatform.patient.domain.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Healthcare Vector Search Service
 * 
 * Demonstrates Oracle Health technical requirements:
 * - Vector Database semantic search with pgvector
 * - Big Data processing with Kafka streaming
 * - Advanced Java features and performance optimization
 * - Healthcare domain expertise
 */
@Service
@Slf4j
@Transactional
public class HealthcareVectorService {

    private final VectorStore vectorStore;
    private final EmbeddingModel embeddingModel;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public HealthcareVectorService(
            VectorStore vectorStore,
            EmbeddingModel embeddingModel,
            KafkaTemplate<String, Object> kafkaTemplate) {
        this.vectorStore = vectorStore;
        this.embeddingModel = embeddingModel;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Find similar patients using Vector Database semantic search
     */
    public CompletableFuture<List<SimilarPatientResult>> findSimilarPatients(
            String patientProfile, 
            int maxResults, 
            double similarityThreshold) {
        
        return CompletableFuture.supplyAsync(() -> {
            log.info("Performing semantic search for similar patients");
            
            try {
                SearchRequest searchRequest = SearchRequest.query(patientProfile)
                        .withTopK(maxResults)
                        .withSimilarityThreshold(similarityThreshold);
                
                List<Document> similarDocuments = vectorStore.similaritySearch(searchRequest);
                
                List<SimilarPatientResult> results = similarDocuments.stream()
                        .map(this::mapToSimilarPatientResult)
                        .toList();
                
                // Publish analytics event to Kafka
                publishHealthcareEvent("PATIENT_SIMILARITY_SEARCH", Map.of(
                    "query", patientProfile,
                    "resultsCount", results.size()
                ));
                
                return results;
                
            } catch (Exception e) {
                log.error("Error performing patient similarity search", e);
                throw new HealthcareServiceException("Failed to find similar patients", e);
            }
        });
    }

    /**
     * Index patient data for vector search
     */
    public void indexPatientData(Patient patient) {
        log.info("Indexing patient data for semantic search: {}", patient.getId());
        
        try {
            String patientProfile = buildPatientProfile(patient);
            
            Document document = new Document(patientProfile, Map.of(
                "patientId", patient.getId().toString(),
                "type", "patient_profile",
                "age", patient.getAge(),
                "conditions", patient.getChronicConditions()
            ));
            
            vectorStore.add(List.of(document));
            
            // Stream to Kafka for real-time processing
            publishHealthcareEvent("PATIENT_INDEXED", Map.of(
                "patientId", patient.getId(),
                "profile", patientProfile
            ));
            
        } catch (Exception e) {
            log.error("Error indexing patient data", e);
            throw new HealthcareServiceException("Failed to index patient data", e);
        }
    }

    private String buildPatientProfile(Patient patient) {
        StringBuilder profile = new StringBuilder();
        profile.append("Age: ").append(patient.getAge()).append(" ");
        profile.append("Gender: ").append(patient.getGender()).append(" ");
        
        if (patient.getChronicConditions() != null) {
            profile.append("Conditions: ").append(String.join(", ", patient.getChronicConditions())).append(" ");
        }
        
        if (patient.getAllergies() != null) {
            profile.append("Allergies: ").append(String.join(", ", patient.getAllergies()));
        }
        
        return profile.toString();
    }

    private void publishHealthcareEvent(String eventType, Map<String, Object> eventData) {
        try {
            HealthcareEvent event = HealthcareEvent.builder()
                    .eventType(eventType)
                    .timestamp(System.currentTimeMillis())
                    .data(eventData)
                    .build();
            
            kafkaTemplate.send("healthcare-events", event);
            
        } catch (Exception e) {
            log.warn("Failed to publish healthcare event: {}", eventType, e);
        }
    }

    private SimilarPatientResult mapToSimilarPatientResult(Document document) {
        Map<String, Object> metadata = document.getMetadata();
        
        return SimilarPatientResult.builder()
                .patientId((String) metadata.get("patientId"))
                .profile(document.getContent())
                .similarityScore((Double) metadata.get("similarity_score"))
                .age((Integer) metadata.get("age"))
                .conditions((List<String>) metadata.get("conditions"))
                .build();
    }

    // Supporting classes
    public static class HealthcareServiceException extends RuntimeException {
        public HealthcareServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class SimilarPatientResult {
        public String patientId;
        public String profile;
        public Double similarityScore;
        public Integer age;
        public List<String> conditions;

        public static SimilarPatientResultBuilder builder() {
            return new SimilarPatientResultBuilder();
        }

        public static class SimilarPatientResultBuilder {
            private SimilarPatientResult result = new SimilarPatientResult();

            public SimilarPatientResultBuilder patientId(String patientId) {
                result.patientId = patientId;
                return this;
            }

            public SimilarPatientResultBuilder profile(String profile) {
                result.profile = profile;
                return this;
            }

            public SimilarPatientResultBuilder similarityScore(Double similarityScore) {
                result.similarityScore = similarityScore;
                return this;
            }

            public SimilarPatientResultBuilder age(Integer age) {
                result.age = age;
                return this;
            }

            public SimilarPatientResultBuilder conditions(List<String> conditions) {
                result.conditions = conditions;
                return this;
            }

            public SimilarPatientResult build() {
                return result;
            }
        }
    }

    public static class HealthcareEvent {
        public String eventType;
        public Long timestamp;
        public Map<String, Object> data;

        public static HealthcareEventBuilder builder() {
            return new HealthcareEventBuilder();
        }

        public static class HealthcareEventBuilder {
            private HealthcareEvent event = new HealthcareEvent();

            public HealthcareEventBuilder eventType(String eventType) {
                event.eventType = eventType;
                return this;
            }

            public HealthcareEventBuilder timestamp(Long timestamp) {
                event.timestamp = timestamp;
                return this;
            }

            public HealthcareEventBuilder data(Map<String, Object> data) {
                event.data = data;
                return this;
            }

            public HealthcareEvent build() {
                return event;
            }
        }
    }
}