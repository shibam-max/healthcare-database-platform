# Healthcare Semantic Database Platform

## Quick Start Guide

### Prerequisites
- Java 23
- Docker & Docker Compose
- Maven 3.9+

### Setup Instructions

1. **Start Infrastructure**
```bash
docker-compose -f infrastructure/docker-compose.yml up -d
```

2. **Build Services**
```bash
mvn clean install
```

3. **Run Patient Data Service**
```bash
cd microservices/patient-data-service
mvn spring-boot:run
```

4. **Verify Setup**
```bash
curl http://localhost:8081/api/v1/patients/health
```

### Key Features
- ✅ Java 23 + Spring Boot 3.4.1
- ✅ PostgreSQL + pgvector for Vector Database
- ✅ Apache Kafka for Big Data streaming
- ✅ Redis for high-performance caching
- ✅ Elasticsearch for full-text search
- ✅ Comprehensive monitoring with Prometheus & Grafana
- ✅ Healthcare-specific domain modeling
- ✅ HIPAA-compliant security and audit trails

### API Endpoints
- **Health Check**: `GET /api/v1/patients/health`
- **Similar Patients**: `POST /api/v1/patients/search/similar`
- **Metrics**: `GET /api/v1/actuator/prometheus`
- **API Docs**: `http://localhost:8081/swagger-ui/index.html`

### Technology Stack
- **Backend**: Java 23, Spring Boot 3.4.1, Spring Security
- **Database**: PostgreSQL 15+ with pgvector extension
- **Big Data**: Apache Kafka, Apache Spark, Elasticsearch
- **Caching**: Redis 7
- **Monitoring**: OpenTelemetry, Prometheus, Grafana, Jaeger
- **Security**: OAuth2/OIDC with Keycloak
- **Cloud**: AWS SDK integration ready

This platform demonstrates enterprise-grade healthcare software development with all Oracle Health technical requirements.