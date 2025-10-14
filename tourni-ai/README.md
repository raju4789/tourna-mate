# 🤖 AI Service

> AI-powered analytics and predictions service for tournament data (currently in development).

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![Status](https://img.shields.io/badge/Status-In%20Development-yellow.svg)]()

---

## What It Does

**Current State:** Minimal viable service with test endpoint (placeholder for future AI features)

**Planned Capabilities:**
- Match outcome predictions using ML models
- Player performance analytics
- Tournament winner forecasting
- Real-time match commentary generation
- Team strength analysis based on historical data
- Injury impact analysis on team performance

**Future Vision:**
- **Predictive Analytics**: "India has 78% chance to win based on current form"
- **Player Insights**: "Virat Kohli averages 72 runs in chase scenarios"
- **Strategic Recommendations**: "Bowl spinners in powerplay (85% success rate)"

---

## Quick Start

### Run Locally

```bash
cd tourni-ai
mvn spring-boot:run

# Service runs on port 8084
```

### Test Endpoint

```bash
# Public test endpoint (requires USER or ADMIN role via Gateway)
curl http://localhost:8080/api/v1/ai/test \
  -H "Authorization: Bearer <JWT_TOKEN>"

# Response
"Hello World!"
```

**Direct Access (bypassing Gateway):**
```bash
curl http://localhost:8084/api/v1/ai/test

# Response
"Hello World!"
```

---

## Architecture

```
┌────────────────────────────────────────────┐
│        AI Service (Port 8084)              │
│                                            │
│  ┌──────────────────────────────────┐    │
│  │   TestController                 │    │
│  │    GET /api/v1/ai/test          │    │
│  │    → Returns "Hello World!"      │    │
│  └──────────────────────────────────┘    │
│                                            │
│  ┌──────────────────────────────────┐    │
│  │   Future: ML Models              │    │
│  │    - Match prediction            │    │
│  │    - Player analytics            │    │
│  │    - Tournament forecasting      │    │
│  └──────────────────────────────────┘    │
└────────────────────────────────────────────┘
```

**Current Implementation:**
```java
@RestController
@RequestMapping("/api/v1/ai")
public class TestController {
    
    @GetMapping("/test")
    public String test() {
        logger.info("TestController.test() called");
        return "Hello World!";
    }
}
```

**Design Philosophy:**
- **Microservice Ready**: Independent service, stateless, scalable
- **Placeholder**: Demonstrates service registration, routing, observability
- **Future-Proof**: Architecture supports adding ML models without breaking changes

---

## API Endpoints

| Method | Endpoint | Description | Auth Required | Roles |
|--------|----------|-------------|---------------|-------|
| GET | `/api/v1/ai/test` | Health check / test endpoint | Yes | USER, ADMIN |

### Future Endpoints (Planned)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/v1/ai/predict/match` | Predict match outcome | Yes |
| GET | `/api/v1/ai/analytics/player/{id}` | Player performance analytics | Yes |
| GET | `/api/v1/ai/forecast/tournament/{id}` | Tournament winner prediction | Yes |
| POST | `/api/v1/ai/commentary/generate` | AI-generated match commentary | Yes |

---

## Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| **Spring Boot** | 3.2.1 | Application framework |
| **Spring Web** | 3.2.1 | REST API |
| **Spring Cloud Eureka Client** | 2023.0.0 | Service discovery |
| **Spring Cloud Config** | 2023.0.0 | External configuration |
| **Actuator** | 3.2.1 | Health monitoring |
| **Micrometer** | - | Metrics & tracing |
| **Prometheus** | - | Metrics export |

**Future Technologies:**
- **TensorFlow Serving / ONNX**: ML model serving
- **Python ML Service**: Separate ML microservice (Python FastAPI)
- **Redis**: Model prediction caching
- **Apache Kafka**: Real-time data streaming for live predictions

---

## Configuration

### External Configuration (from Config Server)

```yaml
# Loaded from: https://github.com/raju4789/tourni-config/application.yml (shared)

server:
  port: 8084

spring:
  application:
    name: tourni-ai

eureka:
  client:
    service-url:
      defaultZone: http://tourni-discovery-service:8761/eureka/
```

### Local Bootstrap (application.yml)

```yaml
server:
  port: 8084

spring:
  application:
    name: tourni-ai
  config:
    import: "optional:configserver:http://tourni-config-server:8888"
```

---

## Why This Service Exists (As Placeholder)

**1. Microservices Best Practice:**
- Demonstrates independent, loosely-coupled services
- Shows complete service registration, discovery, routing
- Observability setup (metrics, tracing, logging)

**2. Future Scalability:**
- AI/ML models can be added without affecting other services
- Independent scaling based on AI workload (CPU-intensive)
- GPU support for deep learning models (separate deployment)

**3. Portfolio Value:**
- Shows understanding of placeholder services
- Demonstrates clean architecture principles
- Ready for AI integration when needed

---

## Future Implementation Roadmap

### Phase 1: Data Collection (Month 1)
```
Goal: Collect historical match data
- Integrate with Management Service
- Store match results, player stats
- Build data pipeline (ETL)
```

### Phase 2: ML Model Development (Month 2-3)
```
Goal: Train prediction models
- Match outcome prediction (binary classification)
- Player performance prediction (regression)
- Tournament winner prediction (multi-class)

Tech Stack:
- Python (scikit-learn, TensorFlow)
- Jupyter Notebooks for experimentation
- MLflow for model tracking
```

### Phase 3: Model Serving (Month 4)
```
Goal: Deploy models in production
- TensorFlow Serving or ONNX Runtime
- REST API endpoints for predictions
- Redis caching for frequent predictions
- A/B testing framework
```

### Phase 4: Real-Time Analytics (Month 5-6)
```
Goal: Live predictions during matches
- Apache Kafka for real-time data streaming
- Streaming ML pipelines (Kafka Streams)
- WebSocket integration for live updates
```

---

## Planned ML Models

### 1. Match Outcome Prediction

**Input Features:**
- Team historical win/loss ratio
- Head-to-head record
- Current form (last 5 matches)
- Home/away advantage
- Toss winner
- Weather conditions
- Pitch report

**Output:**
```json
{
  "team1WinProbability": 0.68,
  "team2WinProbability": 0.32,
  "predictedWinner": "India",
  "confidence": 0.85
}
```

### 2. Player Performance Analytics

**Input:**
- Player historical stats
- Opposition team
- Match venue
- Match format (T20/ODI/Test)

**Output:**
```json
{
  "predictedRuns": 45,
  "predictedStrikeRate": 125.3,
  "formTrend": "improving",
  "confidence": 0.72
}
```

### 3. Tournament Winner Forecast

**Input:**
- All team statistics
- Tournament format
- Group stage standings

**Output:**
```json
{
  "predictions": [
    {"team": "India", "winProbability": 0.35},
    {"team": "Australia", "winProbability": 0.28},
    {"team": "England", "winProbability": 0.18}
  ]
}
```

---

## Production Considerations

### Current State (Placeholder)
- **Stateless**: No database, no state
- **Lightweight**: < 50MB memory footprint
- **Fast Startup**: < 5 seconds

### Future State (With ML Models)
- **CPU/GPU Intensive**: 4-8 CPU cores, optional GPU
- **Memory**: 2-4GB RAM for model loading
- **Model Updates**: Blue-green deployments for model updates
- **Caching**: Redis for prediction caching (90% cache hit rate)

### Monitoring
- **Metrics**: `/actuator/prometheus`
  - Request count
  - Response time
  - Model inference time (future)
  - Prediction accuracy (future)
- **Health**: `/actuator/health`
- **Tracing**: OpenTelemetry → Tempo
- **Logs**: Structured JSON → Loki

---

## Interview Highlights

**Architecture:**
- Why separate AI service? (Independent scaling, GPU support, technology flexibility)
- Placeholder vs full implementation (MVP approach, iterative development)
- Microservices best practices (service registration, observability, configuration)

**Machine Learning:**
- Model serving strategies (TensorFlow Serving, ONNX, REST API)
- Online vs offline predictions (real-time vs batch)
- Model versioning and A/B testing
- Feature engineering for cricket analytics

**Scalability:**
- How to scale ML inference? (Model caching, GPU acceleration, batch predictions)
- Handling prediction spikes during live matches (Redis cache, load balancing)
- Cost optimization (CPU vs GPU instances, serverless ML)

---

## Future Enhancements

| Feature | Priority | Impact | Effort |
|---------|----------|--------|--------|
| Match Outcome Prediction | 🔴 High | 80% user engagement | 30-45 days |
| Player Performance Analytics | 🔴 High | Competitive advantage | 30-45 days |
| Data Collection Pipeline | 🔴 High | Foundation for ML | 10-15 days |
| Tournament Winner Forecast | 🟡 Medium | Viral content potential | 20-30 days |
| Real-Time Predictions | 🟡 Medium | Live match engagement | 45-60 days |
| AI-Generated Commentary | 🟢 Low | Entertainment value | 30-45 days |

---

## 🚀 What's Next?

### For Developers

```bash
# Run tests
mvn test

# Build Docker image
docker build -t tourni-ai:latest .

# Run with Docker Compose
cd ../../docker/dev
docker-compose up tourni-ai
```

### Key Concepts
- **Placeholder Service**: Demonstrates microservices architecture without full implementation
- **Future-Ready**: Architecture supports ML model integration
- **Stateless**: No database dependency, scales horizontally

### Related Services
- [Management Service](../tourni-management/README.md) - Data source for ML models
- [Gateway](../tourni-gateway/README.md) - Request routing
- [Config Server](../tourni-config-server/README.md) - Configuration source
- [Discovery Service](../tourni-discovery-service/README.md) - Service registry

### Learning Path

**For ML Integration:**
1. Learn Python ML libraries (scikit-learn, TensorFlow, PyTorch)
2. Study cricket analytics datasets (Kaggle, ESPN Cricinfo)
3. Understand model serving (TensorFlow Serving, ONNX)
4. Explore MLOps tools (MLflow, Kubeflow, SageMaker)

**For Java Developers:**
5. Learn inter-service communication (REST, gRPC, Kafka)
6. Study caching strategies (Redis, Caffeine)
7. Understand GPU deployment (CUDA, TensorFlow GPU)

---

**[← Back to Main README](../README.md)**
