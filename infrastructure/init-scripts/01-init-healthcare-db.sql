-- Healthcare Semantic Database Platform Schema
-- Vector Database with pgvector for semantic search

-- Enable vector extension
CREATE EXTENSION IF NOT EXISTS vector;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Patients table with vector embeddings
CREATE TABLE patients (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    medical_record_number VARCHAR(50) UNIQUE NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    date_of_birth DATE NOT NULL,
    gender VARCHAR(20),
    patient_embedding vector(1536),
    patient_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    privacy_level VARCHAR(20) NOT NULL DEFAULT 'NORMAL',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    version BIGINT DEFAULT 1
);

-- Patient allergies
CREATE TABLE patient_allergies (
    patient_id UUID REFERENCES patients(id),
    allergy VARCHAR(255),
    PRIMARY KEY (patient_id, allergy)
);

-- Patient conditions
CREATE TABLE patient_conditions (
    patient_id UUID REFERENCES patients(id),
    condition_code VARCHAR(255),
    PRIMARY KEY (patient_id, condition_code)
);

-- Vector indexes for performance
CREATE INDEX idx_patients_embedding ON patients USING hnsw (patient_embedding vector_cosine_ops);
CREATE INDEX idx_patients_mrn ON patients(medical_record_number);
CREATE INDEX idx_patients_name ON patients(last_name, first_name);

-- Insert sample data
INSERT INTO patients (medical_record_number, first_name, last_name, date_of_birth, gender, created_by) VALUES
('MRN001', 'John', 'Doe', '1980-05-15', 'MALE', 'system'),
('MRN002', 'Jane', 'Smith', '1975-08-22', 'FEMALE', 'system'),
('MRN003', 'Robert', 'Johnson', '1965-12-10', 'MALE', 'system');