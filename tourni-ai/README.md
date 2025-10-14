# AI Service

> Machine learning service for tournament predictions and analytics (In Progress)

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java 17](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![Status](https://img.shields.io/badge/Status-In%20Progress-yellow.svg)]()

---

## Purpose

Provides AI-powered insights for tournament management including match predictions, team analysis, and performance forecasting. Currently implements basic infrastructure with planned ML integration.

### Planned Capabilities

- **Match Prediction**: Predict match outcomes based on historical data
- **Team Analysis**: Analyze team strengths and weaknesses
- **Player Performance**: Individual player statistics and trends
- **Tournament Insights**: Strategic insights using natural language (OpenAI GPT)
- **Recommendation Engine**: Suggest optimal team compositions

---

## Current Status

### Implemented ✅
- Spring Boot microservice infrastructure
- Eureka service registration
- Config Server integration
- Health check endpoints
- Basic test endpoint

### In Progress 🚧
- OpenAI GPT integration for natural language insights
- Statistical analysis algorithms
- Historical data analysis
- Match prediction models

### Planned 📋
- Machine learning model training
- Real-time prediction API
- Performance analytics dashboard
- Integration with Management Service via Kafka

---

## Architecture (Future)

```
┌────────────────────────────────────────────┐
│   Management Service                       │
│                                            │
│   Match Result Added                       │
│   ↓                                        │
│   Publish MatchResultEvent → Kafka        │
└────────────────┬───────────────────────────┘
                 │
                 │ Event Stream
                 ▼
┌────────────────────────────────────────────┐
│   AI Service (Port 8084)                   │
│                                            │
│   ┌─────────────────────────────────┐     │
│   │ 1. Consume Events (Kafka)       │     │
│   │    - Match results               │     │
│   │    - Team updates                │     │
│   └─────────────┬───────────────────┘     │
│                 │                          │
│   ┌─────────────▼───────────────────┐     │
│   │ 2. Feature Engineering           │     │
│   │    - Calculate features         │     │
│   │    - Normalize data             │     │
│   └─────────────┬───────────────────┘     │
│                 │                          │
│   ┌─────────────▼───────────────────┐     │
│   │ 3. Model Inference               │     │
│   │    - Statistical models         │     │
│   │    - ML predictions             │     │
│   └─────────────┬───────────────────┘     │
│                 │                          │
│   ┌─────────────▼───────────────────┐     │
│   │ 4. OpenAI Integration            │     │
│   │    - Generate insights          │     │
│   │    - Natural language output    │     │
│   └─────────────┬───────────────────┘     │
│                 │                          │
│   ┌─────────────▼───────────────────┐     │
│   │ 5. Cache & Serve                 │     │
│   │    - Redis cache predictions    │     │
│   │    - REST API endpoints         │     │
│   └──────────────────────────────────┘     │
└────────────────────────────────────────────┘
```

---

## Current API Endpoints

### 1. Health Check

**GET** `/actuator/health`

```bash
curl http://localhost:8080/api/v1/ai/actuator/health
```

### 2. Test Endpoint

**GET** `/api/v1/ai/test`

```bash
curl http://localhost:8080/api/v1/ai/test \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

**Response**:
```json
{
  "status": "SUCCESS",
  "data": "AI Service is running. ML features coming soon."
}
```

---

## Planned API Endpoints

### 1. Predict Match Outcome

**POST** `/api/v1/ai/predict-match`

```json
{
  "tournamentId": 101,
  "team1Id": 1101,
  "team2Id": 1102,
  "venue": "Mumbai",
  "matchType": "T20"
}
```

**Response**:
```json
{
  "status": "SUCCESS",
  "data": {
    "prediction": {
      "winningTeamId": 1101,
      "winProbability": 0.65,
      "predictedScore": {
        "team1": 185,
        "team2": 170
      },
      "confidence": 0.78
    },
    "analysis": {
      "keyFactors": [
        "Team 1 has 75% win rate at this venue",
        "Team 2 recent form: 2W-3L",
        "Head-to-head: Team 1 leads 6-4"
      ]
    }
  }
}
```

### 2. Get Team Analysis

**GET** `/api/v1/ai/team-analysis/{teamId}/{tournamentId}`

```json
{
  "status": "SUCCESS",
  "data": {
    "teamName": "Mumbai Indians",
    "statistics": {
      "winRate": 0.64,
      "avgScore": 178,
      "avgDefense": 165,
      "netRunRate": 1.23
    },
    "strengths": [
      "Strong batting lineup",
      "Effective death bowling"
    ],
    "weaknesses": [
      "Middle-order collapse tendency",
      "Inconsistent fielding"
    ],
    "aiInsights": "Based on recent performance, team shows strong form in home matches..."
  }
}
```

### 3. Get Tournament Insights

**GET** `/api/v1/ai/tournament-insights/{tournamentId}`

```json
{
  "status": "SUCCESS",
  "data": {
    "tournamentName": "IPL 2024",
    "insights": {
      "topPerformers": [
        "Team A: Consistent performance",
        "Player X: Leading run-scorer"
      ],
      "trends": [
        "Higher scores in evening matches",
        "Home advantage significant (65% win rate)"
      ],
      "predictions": {
        "likelyChampion": "Mumbai Indians",
        "probability": 0.42
      },
      "aiSummary": "Tournament characterized by high-scoring matches..."
    }
  }
}
```

---

## Technology Stack (Planned)

### Machine Learning
- **Apache Commons Math**: Statistical calculations
- **DL4J (DeepLearning4J)**: Java-based deep learning
- **Smile**: Statistical machine learning library
- **Weka**: Data mining and ML algorithms

### AI Integration
- **OpenAI API**: Natural language insights via GPT-4
- **LangChain4j**: LLM orchestration for Java

### Data Processing
- **Apache Kafka**: Event streaming for real-time data
- **Redis**: Prediction caching
- **PostgreSQL**: Historical data storage (time-series optimized)

---

## Implementation Roadmap

### Phase 1: Data Collection (Weeks 1-2)
- Integrate with Management Service via Kafka
- Collect historical match results
- Build feature dataset (team stats, venue data, etc.)
- Store in optimized format for ML

### Phase 2: Statistical Models (Weeks 3-4)
- Implement basic prediction algorithms:
  - Win probability based on historical head-to-head
  - Venue-based performance adjustment
  - Recent form analysis
- Calculate confidence scores
- REST API for predictions

### Phase 3: Machine Learning (Weeks 5-8)
- Feature engineering (normalize, encode categorical)
- Train models:
  - Logistic Regression (baseline)
  - Random Forest (ensemble)
  - Neural Network (complex patterns)
- Cross-validation and hyperparameter tuning
- Model versioning and A/B testing

### Phase 4: OpenAI Integration (Weeks 9-10)
- Integrate GPT-4 API
- Prompt engineering for tournament insights
- Natural language explanations of predictions
- Caching to manage API costs

### Phase 5: Production Optimization (Weeks 11-12)
- Redis caching for predictions
- Async processing via Kafka consumers
- Model serving optimization
- Monitoring and alerts

---

## Development

### Run Locally

```bash
cd tourni-ai
mvn spring-boot:run

# Service starts on port 8084
# Register with Eureka at http://tourni-discovery-dev:8761
```

### Dependencies (Current)

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```

### Dependencies (Planned)

```xml
<!-- OpenAI Integration -->
<dependency>
    <groupId>io.github.sashirestela</groupId>
    <artifactId>simple-openai</artifactId>
    <version>3.0.0</version>
</dependency>

<!-- Statistical ML -->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-math3</artifactId>
    <version>3.6.1</version>
</dependency>

<!-- Kafka for Event Processing -->
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>

<!-- Redis for Caching -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

---

## Machine Learning Approach

### Feature Engineering

**Input Features**:
- Team historical win rate
- Head-to-head record
- Recent form (last 5 matches)
- Venue statistics
- Tournament stage (group/playoff)
- Toss outcome
- Day/night match
- Weather conditions (future)

**Feature Transformation**:
```java
public class FeatureEngineer {
    public double[] extractFeatures(Match match) {
        return new double[]{
            team1WinRate,
            team2WinRate,
            headToHeadDiff,
            team1RecentForm,
            team2RecentForm,
            venueAdvantage,
            isNightMatch ? 1.0 : 0.0
        };
    }
}
```

### Model Training (Planned)

```java
public class MatchPredictionModel {
    private RandomForest model;
    
    public void train(List<HistoricalMatch> data) {
        double[][] features = extractFeatures(data);
        int[] labels = extractLabels(data);  // 1 = team1 win, 0 = team2 win
        
        model = RandomForest.fit(features, labels, 
            ntrees: 100,  // Number of trees
            maxDepth: 10,  // Tree depth
            mtry: 3  // Features per split
        );
    }
    
    public Prediction predict(Match match) {
        double[] features = extractFeatures(match);
        int prediction = model.predict(features);
        double[] probabilities = model.predictProbability(features);
        
        return new Prediction(
            prediction,
            probabilities[1],  // Probability of team1 win
            calculateConfidence(probabilities)
        );
    }
}
```

---

## Integration with Management Service

### Event-Driven Architecture

**Management Service**:
```java
@Service
@Transactional
public class MatchResultService {
    @Autowired
    private KafkaTemplate<String, MatchResultEvent> kafkaTemplate;
    
    public void addMatchResult(MatchResult result) {
        // Save to database
        repository.save(result);
        
        // Publish event for AI Service
        MatchResultEvent event = new MatchResultEvent(
            result.getMatchId(),
            result.getTournamentId(),
            result.getTeam1Id(),
            result.getTeam2Id(),
            result.getWinningTeamId(),
            result.getTeam1Score(),
            result.getTeam2Score()
        );
        
        kafkaTemplate.send("match-results", event);
    }
}
```

**AI Service Consumer**:
```java
@Service
public class AIEventConsumer {
    @KafkaListener(topics = "match-results")
    public void consumeMatchResult(MatchResultEvent event) {
        // Update training dataset
        historicalDataService.addMatch(event);
        
        // Retrain model periodically
        if (shouldRetrain()) {
            modelService.retrain();
        }
        
        // Invalidate cached predictions for affected teams
        predictionCache.invalidate(event.getTeam1Id(), event.getTeam2Id());
    }
}
```

---

## Future Enhancements

- **Model Explainability**: SHAP values to explain predictions
- **Real-Time Updates**: WebSocket for live prediction updates
- **Player-Level Analysis**: Individual player statistics and predictions
- **Video Analysis**: Computer vision for match highlight analysis
- **Fantasy League**: Optimal team selection recommendations

---

## Contributing

AI Service is actively under development. Contributions welcome:
- ML model implementations
- Feature engineering ideas
- OpenAI prompt templates
- Statistical analysis algorithms

---

[← Back to Main Documentation](../README.md)
