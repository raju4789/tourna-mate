# 🏆 Tournament Management Service

> Core business logic for tournament operations, match results, team statistics, and real-time leaderboard calculations using the Observer design pattern.

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)

---

## What It Does

Manages the complete tournament lifecycle including tournament creation, match result recording, automatic statistics calculation, and real-time leaderboard updates using the Observer design pattern.

**Key Capabilities:**
- **Tournament Management**: Create and manage cricket tournaments (IPL, World Cup, etc.)
- **Match Result Recording**: Record scores, winner, and match details
- **Automatic Statistics**: Observer pattern triggers real-time stat updates
- **Leaderboard Calculation**: Net Run Rate (NRR) and points table auto-calculated
- **Team Management**: Track team statistics (wins, losses, runs, overs)
- **Complete Audit Trail**: JPA Auditing tracks who/when for all changes
- **Optimistic Locking**: @Version prevents concurrent update conflicts

**Business Impact:**
- **Real-Time Updates**: Leaderboards update instantly when match results added
- **Zero Manual Calculation**: Observer pattern automates all stat updates
- **Audit Compliance**: Every change tracked (who, what, when)
- **Consistency**: Optimistic locking prevents data corruption

---

## Quick Start

### Run Locally

```bash
cd tourni-management
mvn spring-boot:run

# Service runs on port 8083
```

### Get Points Table (Leaderboard)

```bash
# Requires USER or ADMIN role
curl http://localhost:8080/api/v1/manage/pointstable/tournament/101 \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

**Response:**
```json
{
  "status": "SUCCESS",
  "data": {
    "tournamentId": 101,
    "tournamentName": "Cricket World Cup 2023",
    "pointsTable": [
      {
        "teamName": "India",
        "played": 9,
        "won": 9,
        "lost": 0,
        "points": 18,
        "netRunRate": 2.570
      },
      {
        "teamName": "Australia",
        "played": 9,
        "won": 8,
        "lost": 1,
        "points": 16,
        "netRunRate": 0.970
      }
    ]
  }
}
```

### Add Match Result (ADMIN Only)

```bash
curl -X POST http://localhost:8080/api/v1/manage/addMatchResult \
  -H "Authorization: Bearer <ADMIN_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "tournamentId": 101,
    "team1Id": 1101,
    "team2Id": 1102,
    "team1Score": 285,
    "team1Overs": 50,
    "team2Score": 270,
    "team2Overs": 50,
    "winningTeamId": 1101
  }'
```

**What Happens Automatically (Observer Pattern):**
1. ✅ Match result saved to database
2. ✅ TeamStatsObserver → Updates team statistics (runs, overs)
3. ✅ PointsTableObserver → Updates points table (played, won, lost, points, NRR)
4. ✅ All changes audited (created_by, created_date)

---

## Architecture

```
┌────────────────────────────────────────────────┐
│     TourniManagementController                 │
│       @RequiresAdmin on write operations       │
│       @RequiresUser on read operations         │
└────────────────┬───────────────────────────────┘
                 │
                 ↓
┌────────────────────────────────────────────────┐
│     TourniManagementService                    │
│       - addMatchResult()                       │
│       - getPointsTableByTournamentId()         │
└────────────────┬───────────────────────────────┘
                 │
                 ↓
┌────────────────────────────────────────────────┐
│     MatchResultSubject (Observable)            │
│       - notifyObserversSequentially()          │
└─────┬──────────────────────────────────────────┘
      │
      ├──→ TeamStatsObserver
      │      - Update runs scored/conceded
      │      - Update overs played/bowled
      │
      └──→ PointsTableObserver
             - Increment played count
             - Update won/lost
             - Calculate Net Run Rate
             - Calculate points (2 per win)

             ⏱️ < 5ms total overhead
```

**Observer Pattern Benefits:**
- **Decoupled**: Easy to add new observers (e.g., NotificationObserver)
- **Automatic**: Stats update without manual intervention
- **Consistent**: All updates in single transaction
- **Extensible**: New statistics can be added without modifying core logic

---

## API Endpoints

### Tournament Endpoints

| Method | Endpoint | Description | Auth | Roles |
|--------|----------|-------------|------|-------|
| GET | `/api/v1/manage/tournaments` | List all tournaments | Yes | USER, ADMIN |
| GET | `/api/v1/manage/teams?tournamentId={id}` | Get teams by tournament | Yes | USER, ADMIN |
| GET | `/api/v1/manage/pointstable/tournament/{id}` | Get leaderboard | Yes | USER, ADMIN |
| POST | `/api/v1/manage/addMatchResult` | Record match result | Yes | ADMIN |

### Authorization Annotations

```java
@RequiresUser  // Allows USER or ADMIN roles
public ResponseEntity<...> getAllTournaments()

@RequiresAdmin  // Allows ADMIN role only
public ResponseEntity<...> addMatchResult(...)
```

---

## Domain Model

```
Tournament (1) ───── (N) Team
    │                    │
    │                    │
    │                    │
    ↓                    ↓
MatchResult ──────→ TeamStats
    │                    │
    │                    │
    ↓                    ↓
PointsTable (Leaderboard)
```

### Key Entities

#### Tournament
```java
@Entity
public class Tournament extends BaseEntity {
    private Long tournamentId;
    private String tournamentName;
    private Integer tournamentYear;
    private Integer maximumOversPerMatch;  // 20 for T20, 50 for ODI
    // ... audit fields inherited from BaseEntity
}
```

#### MatchResult
```java
@Entity
public class MatchResult extends BaseEntity {
    private Long matchResultId;
    private Long tournamentId;
    private Long team1Id;
    private Long team2Id;
    private Integer team1Score;
    private Double team1Overs;
    private Integer team2Score;
    private Double team2Overs;
    private Long winningTeamId;
    // ... audit fields
}
```

#### PointsTable
```java
@Entity
public class PointsTable extends BaseEntity {
    private Long pointsTableId;
    private Long tournamentId;
    private Long teamId;
    private Integer played;
    private Integer won;
    private Integer lost;
    private Integer tied;
    private Integer noResult;
    private Integer points;
    private Double netMatchRate;  // Net Run Rate (NRR)
    
    @Version  // Optimistic locking
    private Long version;
}
```

---

## Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| **Spring Boot** | 3.2.1 | Application framework |
| **Spring Data JPA** | 3.2.1 | Database abstraction |
| **Hibernate** | 6.4.1 | ORM with optimistic locking |
| **MySQL** | 8.0 | Tournament data storage |
| **Observer Pattern** | - | Event-driven stat updates |
| **ModelMapper** | - | DTO to entity mapping |
| **OpenAPI 3.0** | 2.3.0 | API documentation |
| **Lombok** | - | Boilerplate reduction |

---

## Configuration

### External Configuration (from Config Server)

```yaml
# Loaded from: https://github.com/raju4789/tourni-config/tourni-management.yml

spring:
  datasource:
    url: jdbc:mysql://mysql:3306/tournament_db
    username: ${VAULT_DB_USERNAME}
    password: ${VAULT_DB_PASSWORD}
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      
  jpa:
    hibernate:
      ddl-auto: update              # Creates/updates tables
    show-sql: false                 # Disable SQL logging in prod
    auditing:
      enabled: true                 # Enable JPA auditing
    properties:
      hibernate:
        format_sql: true
        use_sql_comments: true
```

### Local Bootstrap (application.yml)

```yaml
server:
  port: 8083

spring:
  application:
    name: tourni-management
  config:
    import: "optional:configserver:http://tourni-config-server:8888"
```

---

## Observer Pattern Implementation

### How It Works

**1. Subject (MatchResultSubject.java)**
```java
@Component
public class MatchResultSubject {
    private final List<MatchResultObserver> observers;
    
    public void notifyObserversSequentially(AddMatchResultRequest request) {
        for (MatchResultObserver observer : observers) {
            observer.update(request);  // Calls each observer
        }
    }
}
```

**2. Observers**

**TeamStatsObserver:**
```java
@Component
public class TeamStatsObserver implements MatchResultObserver {
    @Override
    public void update(AddMatchResultRequest request) {
        // Update team1 stats
        team1Stats.setTotalRunsScored(team1Stats.getTotalRunsScored() + team1Score);
        team1Stats.setTotalOversPlayed(team1Stats.getTotalOversPlayed() + team1Overs);
        
        // Update team2 stats
        team2Stats.setTotalRunsScored(team2Stats.getTotalRunsScored() + team2Score);
        team2Stats.setTotalOversPlayed(team2Stats.getTotalOversPlayed() + team2Overs);
    }
}
```

**PointsTableObserver:**
```java
@Component
public class PointsTableObserver implements MatchResultObserver {
    @Override
    public void update(AddMatchResultRequest request) {
        // Update winner
        winnerRow.setPlayed(winnerRow.getPlayed() + 1);
        winnerRow.setWon(winnerRow.getWon() + 1);
        winnerRow.setPoints(winnerRow.getPoints() + 2);
        
        // Update loser
        loserRow.setPlayed(loserRow.getPlayed() + 1);
        loserRow.setLost(loserRow.getLost() + 1);
        
        // Calculate Net Run Rate
        calculateNetRunRate(winnerRow);
        calculateNetRunRate(loserRow);
    }
}
```

**3. Service Triggers Observers**
```java
@Service
public class TourniManagementServiceImpl {
    public void addMatchResult(AddMatchResultRequest request) {
        // 1. Save match result
        matchResultRepository.save(matchResult);
        
        // 2. Notify all observers (automatic updates)
        matchResultSubject.notifyObserversSequentially(request);
    }
}
```

**Execution Flow:**
```
addMatchResult() → Save MatchResult → notifyObservers()
                                            ↓
                                    TeamStatsObserver.update()
                                            ↓
                                    PointsTableObserver.update()
                                            ↓
                                    All updates in single transaction
```

---

## Net Run Rate Calculation

**Formula:**
```
NRR = (Total Runs Scored / Total Overs Faced) - (Total Runs Conceded / Total Overs Bowled)
```

**Example:**
```
India:
  Runs Scored: 2856 in 450 overs
  Runs Conceded: 1700 in 430 overs
  
NRR = (2856 / 450) - (1700 / 430)
    = 6.347 - 3.953
    = 2.394
```

**Implementation:** [`NetRunRateCalculator.java`](src/main/java/com/tournament/management/utils/NetRunRateCalculator.java)

---

## Sample Data (Development)

### Tournaments

```sql
-- Tournament ID: 101
Tournament Name: Cricket World Cup 2023
Year: 2023
Max Overs: 50 (ODI)

-- Tournament ID: 102
Tournament Name: IPL 2023
Year: 2023
Max Overs: 20 (T20)
```

### Teams

**World Cup Teams (Country-based):**
- 1101: India
- 1102: Australia
- 1103: England
- 1104: South Africa
- 1105: New Zealand
- 1106: Pakistan
- 1107: Sri Lanka
- 1108: Afghanistan
- 1109: Bangladesh
- 1110: Netherlands

**IPL Teams (Franchise-based):**
- 1111: Chennai Super Kings (CSK)
- 1112: Mumbai Indians (MI)
- 1113: Royal Challengers Bangalore (RCB)
- 1114: Kolkata Knight Riders (KKR)
- 1115: Sunrisers Hyderabad (SRH)
- 1116: Rajasthan Royals (RR)
- 1117: Delhi Capitals (DC)
- 1118: Punjab Kings (PBKS)
- 1119: Lucknow Super Giants (LSG)
- 1120: Gujarat Titans (GT)

---

## Production Considerations

### Performance
- **Observer Pattern Overhead**: < 5ms per match result
- **Optimistic Locking**: @Version prevents lost updates
- **Connection Pooling**: HikariCP with 20 connections (configurable)
- **Indexed Queries**: tournamentId, teamId indexed for fast lookups
- **Transaction Management**: All updates in single transaction (ACID)

### Data Integrity
- **Optimistic Locking**: @Version field prevents concurrent update conflicts
- **Audit Trail**: Complete who/when tracking via JPA Auditing
- **Soft Deletes**: is_active flag instead of hard deletes
- **Foreign Keys**: Referential integrity enforced at database level

### Monitoring
- **Metrics**: `/actuator/prometheus`
  - Match result processing time
  - Leaderboard calculation time
  - Observer execution time
- **Health**: `/actuator/health` (checks database connectivity)
- **Tracing**: OpenTelemetry → Tempo (distributed tracing)
- **Logs**: Structured JSON → Loki

### Scalability
- **Current**: Single database instance
- **Future**: Read replicas for leaderboard queries (95% reads)
- **Caching**: Redis for hot leaderboards (tournament in progress)
- **Horizontal Scaling**: Stateless service, scales via replicas

---

## Interview Highlights

**Design Patterns:**
- Why Observer pattern? (Decoupled, extensible, automatic updates)
- Optimistic vs Pessimistic locking trade-offs (performance vs consistency)
- How to prevent race conditions? (@Version field, transactions)
- How to add new statistics without modifying core logic? (New observer)

**Scalability:**
- Current architecture limitations (single database instance)
- How to scale reads? (Read replicas, Redis caching)
- How to handle 10,000 match results/hour? (Async processing, Kafka)
- Database sharding strategy (shard by tournament_id)

**Domain Modeling:**
- Bounded context separation (Management vs Identity)
- Aggregate roots (Tournament, Team)
- Event sourcing potential (match history, point-in-time queries)
- Why separate TeamStats and PointsTable? (Different aggregation levels)

**Performance:**
- Observer pattern overhead (< 5ms, acceptable trade-off)
- Net Run Rate calculation complexity (O(1) per match)
- How to optimize leaderboard queries? (Materialized views, Redis cache)

---

## Future Enhancements

| Feature | Priority | Business Value | Effort |
|---------|----------|----------------|--------|
| Redis Caching (Leaderboards) | 🔴 High | 10x faster response (20ms → 2ms) | 2-3 days |
| Event-Driven (Kafka) | 🔴 High | Async processing, 5x throughput | 5-7 days |
| Match Scheduling | 🟡 Medium | Auto-generate fixtures | 3-5 days |
| Advanced Analytics | 🟡 Medium | Player performance tracking | 5-7 days |
| Real-Time WebSocket Updates | 🟡 Medium | Live score updates | 7-10 days |
| Tournament Templates | 🟢 Low | Quick tournament setup | 2-3 days |
| Historical Comparisons | 🟢 Low | Team performance over years | 3-5 days |

---

## 🚀 What's Next?

### Performance Optimization Roadmap

**Phase 1: Caching (Week 1)**
```
Redis Cache Layer:
  - Cache leaderboards (95% hit rate)
  - Response time: 200ms → 20ms
  - TTL: 5 minutes (tournament in progress), 1 day (completed)
```

**Phase 2: Event-Driven (Week 2-3)**
```
Kafka Integration:
  - Async stat calculation via Kafka topics
  - 5x throughput improvement
  - Decoupled observers as Kafka consumers
```

**Phase 3: Database Optimization (Week 4)**
```
Read Replicas:
  - Read-only replicas for leaderboard queries
  - Materialized views for complex aggregations
  - Database sharding by tournament_id
```

### Key Concepts
- **Observer Pattern**: Automatic leaderboard updates when match results change
- **Optimistic Locking**: @Version prevents data corruption from concurrent updates
- **JPA Auditing**: Tracks who created/modified tournament data and when
- **Net Run Rate**: Cricket-specific metric calculated automatically

### Related Services
- [Identity Service](../tourni-identity-service/README.md) - User authentication
- [Gateway](../tourni-gateway/README.md) - Request routing & auth
- [Config Server](../tourni-config-server/README.md) - Configuration source
- [Discovery Service](../tourni-discovery-service/README.md) - Service registry

### Development Commands

```bash
# Run tests
mvn test

# Build Docker image
docker build -t tourni-management:latest .

# Run with Docker Compose
cd ../../docker/dev
docker-compose up management-service mysql

# Check database
docker exec -it mysql mysql -u root -p
mysql> USE tournament_db;
mysql> SELECT * FROM points_table WHERE tournament_id = 101 ORDER BY points DESC, net_match_rate DESC;
```

---

**[← Back to Main README](../README.md)**
