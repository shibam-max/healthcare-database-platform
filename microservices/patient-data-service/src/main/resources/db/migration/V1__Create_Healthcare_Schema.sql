-- Healthcare Platform Database Migration V1
-- Vector Database Schema with pgvector for semantic search

-- Enable vector extension for semantic search
CREATE EXTENSION IF NOT EXISTS vector;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Patients table with vector embeddings for semantic search
CREATE TABLE IF NOT EXISTS patients (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    medical_record_number VARCHAR(50) UNIQUE NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    date_of_birth DATE NOT NULL,
    gender VARCHAR(20),
    patient_embedding vector(1536), -- OpenAI embedding dimension
    patient_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    privacy_level VARCHAR(20) NOT NULL DEFAULT 'NORMAL',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version BIGINT DEFAULT 1
);

-- Patient allergies table
CREATE TABLE IF NOT EXISTS patient_allergies (
    patient_id UUID REFERENCES patients(id) ON DELETE CASCADE,
    allergy VARCHAR(255) NOT NULL,
    PRIMARY KEY (patient_id, allergy)
);

-- Patient chronic conditions table
CREATE TABLE IF NOT EXISTS patient_conditions (
    patient_id UUID REFERENCES patients(id) ON DELETE CASCADE,
    condition_code VARCHAR(255) NOT NULL,
    PRIMARY KEY (patient_id, condition_code)
);

-- Performance indexes for healthcare queries
CREATE INDEX IF NOT EXISTS idx_patients_embedding ON patients USING hnsw (patient_embedding vector_cosine_ops);
CREATE INDEX IF NOT EXISTS idx_patients_mrn ON patients(medical_record_number);
CREATE INDEX IF NOT EXISTS idx_patients_name ON patients(last_name, first_name);
CREATE INDEX IF NOT EXISTS idx_patients_dob ON patients(date_of_birth);
CREATE INDEX IF NOT EXISTS idx_patients_status ON patients(patient_status);
CREATE INDEX IF NOT EXISTS idx_patients_created ON patients(created_at);

-- Insert sample healthcare data for testing
INSERT INTO patients (medical_record_number, first_name, last_name, date_of_birth, gender, created_by) VALUES
('MRN001', 'John', 'Doe', '1980-05-15', 'MALE', 'system'),
('MRN002', 'Jane', 'Smith', '1975-08-22', 'FEMALE', 'system'),
('MRN003', 'Robert', 'Johnson', '1965-12-10', 'MALE', 'system')
ON CONFLICT (medical_record_number) DO NOTHING;

-- Insert sample allergies
INSERT INTO patient_allergies (patient_id, allergy) 
SELECT p.id, 'penicillin' FROM patients p WHERE p.medical_record_number = 'MRN001'
ON CONFLICT DO NOTHING;

-- Insert sample conditions
INSERT INTO patient_conditions (patient_id, condition_code) 
SELECT p.id, 'diabetes' FROM patients p WHERE p.medical_record_number = 'MRN002'
ON CONFLICT DO NOTHING;