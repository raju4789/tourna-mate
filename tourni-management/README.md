# Tournament Management Service

> Core business logic for tournament operations using Observer pattern for automated calculations

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java 17](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)

---

## Purpose

Handles complete tournament lifecycle including team management, match result recording, and automated statistics calculation. Demonstrates Observer design pattern for domain-driven event handling.

### Core Responsibilities

- **Tournament Management**: CRUD operations for tournaments
- **Team Management**: Team registration and tournament assignments
- **Match Results**: Recording scores and outcomes
- **Automated Calculations**: Observer pattern triggers real-time updates
  - Points table recalculation (winner gets 2 points)
  - Net Run Rate (NRR) calculation
  - Team statistics aggregation
- **Leaderboard Generation**: Real-time points table sorted by points and NRR

### Business Impact

| Feature | Implementation | Benefit |
|---------|---------------|---------|
| **Observer Pattern** | Auto-update on match result | Zero manual calculation, instant updates |
| **Optimistic Locking** | `@Version` on entities | Prevents concurrent update conflicts |
| **JPA Auditing** | Auto-tracked changes | Complete audit trail for compliance |
| **Transactional Consistency** | All updates in single transaction | All succeed or all rollback |

---

## Architecture

```
┌──────────────┐
│   Client     │
└──────┬───────┘
       │
       │ POST /api/v1/manage/addMatchResult
       ▼
┌─────────────────────────────────────────────┐
│   API Gateway                               │
│   (Validates JWT, checks ADMIN role)        │
└──────────────────┬──────────────────────────┘
                   │
                   ▼
┌──────────────────────────────────────────────────┐
│   Management Service (Port 8083)                 │
│                                                  │
│   @Transactional                                 │
│   ┌────────────────────────────────────────┐    │
│   │ 1. Save Match Result                   │    │
│   │    repository.save(matchResult)        │    │
│   └────────────────┬───────────────────────┘    │
│                    │                             │
│                    │ notify observers            │
│                    │                             │
│   ┌────────────────▼──────────┐                 │
│   │ 2. Observer Pattern        │                 │
│   │                            │                 │
│   │  PointsTableObserver:      │                 │
│   │  • Winner gets 2 points    │                 │
│   │  • Loser gets 0 points     │                 │
│   │                            │                 │
│   │  TeamStatsObserver:        │                 │
│   │  • Update runs scored      │                 │
│   │  • Update overs bowled     │                 │
│   │  • Calculate NRR           │                 │
│   │  • Win/Loss counters       │                 │
│   └────────────────────────────┘                 │
│                    │                             │
│                    │ all updates in transaction  │
│                    ▼                             │
│           ┌─────────────────┐                    │
│           │ Commit or       │                    │
│           │ Rollback        │                    │
│           └─────────────────┘                    │
└──────────────────────────────────────────────────┘
                   │
                   ▼
        ┌──────────────────────┐
        │ MySQL Database       │
        │ management_db        │
        │                      │
        │ • tournament         │
        │ • team               │
        │ • match_result       │
        │ • points_table       │
        │ • team_stats         │
        └──────────────────────┘
```

---

## API Endpoints

### 1. Get Points Table (Leaderboard)

**GET** `/api/v1/manage/pointstable/tournament/{tournamentId}`

**Authorization**: USER or ADMIN

```bash
curl http://localhost:8080/api/v1/manage/pointstable/tournament/101 \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

**Response**:
```json
{
  "status": "SUCCESS",
  "data": {
    "tournamentId": 101,
    "tournamentName": "IPL 2024",
    "pointsTable": [
      {
        "position": 1,
        "teamName": "Mumbai Indians",
        "played": 14,
        "won": 10,
        "lost": 4,
        "points": 20,
        "netRunRate": 1.456
      },
      {
        "position": 2,
        "teamName": "Chennai Super Kings",
        "played": 14,
        "won": 9,
        "lost": 5,
        "points": 18,
        "netRunRate": 0.892
      }
    ]
  }
}
```

**Sorting Logic**:
1. Points (descending)
2. Net Run Rate (descending)
3. Team name (ascending)

### 2. Add Match Result

**POST** `/api/v1/manage/addMatchResult`

**Authorization**: ADMIN only

```bash
curl -X POST http://localhost:8080/api/v1/manage/addMatchResult \
  -H "Authorization: Bearer <ADMIN_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "tournamentId": 101,
    "team1Id": 1101,
    "team2Id": 1102,
    "team1Score": 195,
    "team1Overs": 20,
    "team2Score": 180,
    "team2Overs": 20,
    "winningTeamId": 1101
  }'
```

**Automated Processing** (Observer Pattern):
```
1. ✅ Match result saved
2. ✅ PointsTableObserver triggered:
   - Team 1 (winner) gets +2 points
   - Team 2 (loser) gets 0 points
3. ✅ TeamStatsObserver triggered:
   - Team 1: +195 runs scored, +20 overs faced
   - Team 2: +180 runs scored, +20 overs faced
   - NRR recalculated for both teams
4. ✅ All updates committed in single transaction
```

**Response**:
```json
{
  "status": "SUCCESS",
  "data": {
    "matchId": 501,
    "tournament": "IPL 2024",
    "winner": "Mumbai Indians",
    "message": "Points table and statistics updated automatically"
  }
}
```

### 3. Get All Tournaments

**GET** `/api/v1/manage/tournaments`

**Authorization**: USER or ADMIN

```bash
curl http://localhost:8080/api/v1/manage/tournaments \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

### 4. Add Tournament

**POST** `/api/v1/manage/addTournament`

**Authorization**: ADMIN only

```bash
curl -X POST http://localhost:8080/api/v1/manage/addTournament \
  -H "Authorization: Bearer <ADMIN_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "tournamentName": "World Cup 2025",
    "tournamentYear": 2025,
    "tournamentTypeId": 1,
    "maximumOversPerMatch": 50,
    "tournamentDescription": "ICC Cricket World Cup"
  }'
```

---

## Observer Pattern Implementation

### Why Observer Pattern?

**Problem**: Match result update requires cascading updates (points, stats, NRR). Tightly coupled code leads to maintenance nightmare.

**Solution**: Observer pattern decouples match result from dependent calculations.

**Benefits**:
- **Separation of Concerns**: Match result logic separate from statistics
- **Extensibility**: Add new observers (notifications, analytics) without changing existing code
- **Transactional**: All observers execute within single transaction
- **Testability**: Can test observers independently

### Implementation

**Subject** (`MatchResultSubject`):
```java
@Component
public class MatchResultSubject extends Observable {
    
    public void notifyObservers(MatchResult matchResult) {
        setChanged();
        notifyObservers(matchResult);  // Triggers all registered observers
    }
}
```

**Observers**:

1. **PointsTableObserver**:
```java
@Component
public class PointsTableObserver implements Observer {
    
    @Override
    public void update(Observable o, Object arg) {
        MatchResult result = (MatchResult) arg;
        
        // Winner gets 2 points
        updatePoints(result.getWinningTeamId(), 2, true);
        
        // Loser gets 0 points (but record updated)
        long loserId = result.getTeam1Id().equals(result.getWinningTeamId()) 
            ? result.getTeam2Id() 
            : result.getTeam1Id();
        updatePoints(loserId, 0, false);
    }
}
```

2. **TeamStatsObserver**:
```java
@Component
public class TeamStatsObserver implements Observer {
    
    @Override
    public void update(Observable o, Object arg) {
        MatchResult result = (MatchResult) arg;
        
        // Update runs, overs, calculate NRR
        updateTeamStats(result.getTeam1Id(), 
            result.getTeam1Score(), result.getTeam1Overs());
        updateTeamStats(result.getTeam2Id(), 
            result.getTeam2Score(), result.getTeam2Overs());
    }
}
```

**Service Integration**:
```java
@Service
@Transactional
public class TourniManagementServiceImpl {
    
    @Autowired
    private MatchResultSubject matchResultSubject;
    
    public MatchResult addMatchResult(MatchRequest request) {
        // 1. Save match result
        MatchResult result = matchResultRepository.save(matchResult);
        
        // 2. Notify observers (all updates in same transaction)
        matchResultSubject.notifyObservers(result);
        
        // 3. Transaction commits (or rolls back if any observer fails)
        return result;
    }
}
```

---

## Net Run Rate (NRR) Calculation

### Formula

```
NRR = (Total Runs Scored / Total Overs Faced) - (Total Runs Conceded / Total Overs Bowled)
```

### Example

**Team A**:
- Match 1: Scored 200 in 20 overs, Conceded 180 in 20 overs
- Match 2: Scored 180 in 20 overs, Conceded 190 in 20 overs

```
Runs Scored = 200 + 180 = 380
Overs Faced = 20 + 20 = 40
Run Rate (Scoring) = 380 / 40 = 9.5

Runs Conceded = 180 + 190 = 370
Overs Bowled = 20 + 20 = 40
Run Rate (Conceding) = 370 / 40 = 9.25

NRR = 9.5 - 9.25 = +0.25
```

### Implementation

```java
public class NetRunRateCalculator {
    
    public static double calculate(TeamStats stats) {
        double runRateScoring = (double) stats.getTotalRunsScored() 
            / stats.getTotalOversFaced();
        double runRateConceding = (double) stats.getTotalRunsConceded() 
            / stats.getTotalOversBowled();
        
        return runRateScoring - runRateConceding;
    }
}
```

---

## Database Schema

### Tournament

| Column | Type | Purpose |
|--------|------|---------|
| `tournament_id` | BIGINT (PK) | Unique identifier |
| `tournament_name` | VARCHAR(200) | Tournament name (unique with year) |
| `tournament_year` | INT | Year (unique with name) |
| `tournament_type_id` | BIGINT | Tournament format (T20, ODI, Test) |
| `maximum_overs_per_match` | INT | Match format (20 for T20, 50 for ODI) |
| `tournament_description` | TEXT | Description |

**Unique Constraint**: `(tournament_name, tournament_year)`

### Team

| Column | Type | Purpose |
|--------|------|---------|
| `team_id` | BIGINT (PK) | Unique identifier |
| `team_name` | VARCHAR(200) (UNIQUE) | Team name |
| `team_description` | TEXT | Description |

### Match Result

| Column | Type | Purpose |
|--------|------|---------|
| `match_result_id` | BIGINT (PK) | Unique identifier |
| `tournament_id` | BIGINT (FK) | Tournament reference |
| `team1_id` | BIGINT (FK) | First team |
| `team2_id` | BIGINT (FK) | Second team |
| `team1_score` | INT | Team 1 runs |
| `team1_overs` | DOUBLE | Team 1 overs faced |
| `team2_score` | INT | Team 2 runs |
| `team2_overs` | DOUBLE | Team 2 overs faced |
| `winning_team_id` | BIGINT (FK) | Winner |
| `version` | BIGINT | Optimistic locking |

### Points Table

| Column | Type | Purpose |
|--------|------|---------|
| `points_table_id` | BIGINT (PK) | Unique identifier |
| `tournament_id` | BIGINT (FK) | Tournament reference |
| `team_id` | BIGINT (FK) | Team reference |
| `matches_played` | INT | Total matches |
| `matches_won` | INT | Wins |
| `matches_lost` | INT | Losses |
| `points` | INT | Total points (2 per win) |

### Team Stats

| Column | Type | Purpose |
|--------|------|---------|
| `team_stats_id` | BIGINT (PK) | Unique identifier |
| `tournament_id` | BIGINT (FK) | Tournament reference |
| `team_id` | BIGINT (FK) | Team reference |
| `total_runs_scored` | INT | Cumulative runs scored |
| `total_runs_conceded` | INT | Cumulative runs conceded |
| `total_overs_faced` | DOUBLE | Cumulative overs faced |
| `total_overs_bowled` | DOUBLE | Cumulative overs bowled |

---

## Concurrency Control

### Optimistic Locking

**Problem**: Two admins record same match result simultaneously

**Solution**: JPA `@Version` annotation

```java
@Entity
public class MatchResult extends BaseEntity {
    @Version
    @Column(name = "version")
    private Long version;  // Hibernate manages this
}
```

**How It Works**:
```
Time  Admin A                        Admin B
T1    Read match (version=5)
T2                                   Read match (version=5)
T3    Update → version=6 ✅
T4                                   Update → ERROR!
                                     OptimisticLockException
                                     (expected version=6, got version=5)
```

**Why Optimistic vs Pessimistic**:
- ✅ No database locks (better concurrency)
- ✅ Reads never blocked
- ✅ Conflicts rare in practice
- ❌ Requires retry logic in client

---

## Authorization

### Method-Level Security

```java
@RestController
@RequestMapping("/api/v1/manage")
public class TourniManagementController {
    
    @RequiresUser  // USER or ADMIN can read
    @GetMapping("/pointstable/tournament/{id}")
    public ResponseEntity<?> getPointsTable(@PathVariable Long id) {
        // ...
    }
    
    @RequiresAdmin  // Only ADMIN can write
    @PostMapping("/addMatchResult")
    public ResponseEntity<?> addMatchResult(@RequestBody MatchRequest req) {
        // ...
    }
}
```

**Implementation**:
```java
@PreAuthorize("@authorizationService.isAdmin()")
public @interface RequiresAdmin {}

@PreAuthorize("@authorizationService.isAuthenticated()")
public @interface RequiresUser {}
```

**User Context**:
```java
// Gateway adds headers
X-User-Username: admin
X-User-Roles: ADMIN,USER

// Service extracts via interceptor
UserContext context = UserContextHolder.getContext();
String username = context.getUsername();
List<String> roles = context.getRoles();
```

---

## Development

### Run Locally

```bash
cd tourni-management
mvn spring-boot:run

# Requires MySQL on port 3307
# Database: management_db_dev
```

### Dependencies

```xml
<!-- JPA & Database -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency>

<!-- Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- Validation -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

---

## Monitoring

### Health Check

```bash
curl http://localhost:8083/actuator/health
```

### Key Metrics

- `match_results_added_total`: Total matches recorded
- `points_table_updates_total`: Observer invocations
- `nrr_calculations_duration`: NRR calculation time
- `observer_notification_duration`: Observer pattern overhead

---

## Future Enhancements

- **Caching**: Redis cache for leaderboards (95% read operations)
- **Event-Driven**: Kafka for async observer processing
- **Match Schedule**: Pre-schedule matches with notifications
- **Live Scoring**: WebSocket updates for real-time leaderboards
- **Historical Analysis**: Trend analysis, team performance over time

---

[← Back to Main Documentation](../README.md)
