# Identity Service

> JWT-based authentication microservice providing user management and token generation

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-6.2.1-brightgreen.svg)](https://spring.io/projects/spring-security)
[![Java 17](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)

---

## Purpose

Centralized authentication service handling user registration, login, JWT token generation, and role management for the TOURNA-MATE platform.

### Core Responsibilities

- **User Registration**: Account creation with BCrypt password hashing
- **Authentication**: Credential validation and JWT token issuance
- **Token Management**: JWT generation with embedded roles and expiration
- **Role Management**: Multi-role support (ADMIN, USER) with extensibility
- **Token Versioning**: Incremental versioning for immediate token invalidation

### Security Metrics

| Aspect | Implementation | Value |
|--------|---------------|-------|
| **Password Hashing** | BCrypt (cost factor 12) | ~250ms per hash, 2^12 iterations |
| **Token Validity** | JWT expiration | 24 hours default |
| **Token Algorithm** | HS256 (HMAC-SHA256) | Symmetric key signing |
| **Stateless Auth** | No session storage | Horizontal scaling without shared state |

---

## Architecture

```
┌─────────┐
│ Client  │
└────┬────┘
     │
     │ POST /api/v1/auth/register
     │ POST /api/v1/auth/authenticate
     ▼
┌────────────────────────────────────────┐
│   API Gateway                          │
│   (Routes to Identity Service)         │
└────────────────┬───────────────────────┘
                 │
                 ▼
┌────────────────────────────────────────┐
│   Identity Service (Port 8082)         │
│                                        │
│   ┌──────────────────────────────┐    │
│   │ 1. Registration              │    │
│   │    - Validate input          │    │
│   │    - Hash password (BCrypt)  │    │
│   │    - Save to database        │    │
│   │    - Generate JWT            │    │
│   └──────────────────────────────┘    │
│                                        │
│   ┌──────────────────────────────┐    │
│   │ 2. Authentication            │    │
│   │    - Find user by username   │    │
│   │    - Verify password (BCrypt)│    │
│   │    - Generate JWT token      │    │
│   │    - Return token + roles    │    │
│   └──────────────────────────────┘    │
│                                        │
│   ┌──────────────────────────────┐    │
│   │ 3. JWT Generation            │    │
│   │    - Username (subject)      │    │
│   │    - Roles (claim)           │    │
│   │    - Email (claim)           │    │
│   │    - Expiration (24h)        │    │
│   │    - Sign with secret key    │    │
│   └──────────────────────────────┘    │
└────────────────┬───────────────────────┘
                 │
                 ▼
        ┌────────────────┐
        │ MySQL Database │
        │ identity_db    │
        │                │
        │ • app_user     │
        │ • app_user_role│
        └────────────────┘
```

---

## API Endpoints

### 1. Register User

**POST** `/api/v1/auth/register`

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "SecurePass123",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "roles": ["USER"]
  }'
```

**Response** (201 Created):
```json
{
  "status": "SUCCESS",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huX2RvZSIsInJvbGVzIjoiVVNFUiIsImVtYWlsIjoiam9obkBleGFtcGxlLmNvbSIsImlhdCI6MTcwNjEyMzQ1NiwiZXhwIjoxNzA2MjA5ODU2fQ...",
    "expiryTime": "24 Hours",
    "username": "john_doe",
    "email": "john@example.com",
    "roles": ["USER"]
  }
}
```

**Processing**:
1. Validate input (username unique, password strength)
2. Hash password with BCrypt (cost 12, ~250ms)
3. Save user to database with role assignments
4. Generate JWT token
5. Return token for immediate use

### 2. Authenticate (Login)

**POST** `/api/v1/auth/authenticate`

```bash
curl -X POST http://localhost:8080/api/v1/auth/authenticate \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin@4789"
  }'
```

**Response** (200 OK):
```json
{
  "status": "SUCCESS",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "expiryTime": "24 Hours",
    "username": "admin",
    "email": "admin@tournimate.com",
    "roles": ["ADMIN", "USER"]
  }
}
```

**Processing**:
1. Query database for user by username
2. Compare provided password with stored BCrypt hash (~250ms)
3. If match → Generate JWT with user's current roles
4. If no match → Return 401 Unauthorized

### 3. Validate Token (Internal)

**POST** `/api/v1/auth/validateToken`

Used by gateway for JWT validation (Note: Gateway performs local validation instead).

### 4. Generate Token (Internal)

**POST** `/api/v1/auth/generateToken`

Generate new token for existing user (used for token refresh flows).

---

## Database Schema

### Table: `app_user`

| Column | Type | Constraints | Purpose |
|--------|------|-------------|---------|
| `user_id` | BIGINT | PRIMARY KEY | Unique identifier |
| `username` | VARCHAR(100) | UNIQUE, NOT NULL | Login username |
| `password` | VARCHAR(255) | NOT NULL | BCrypt hashed password |
| `first_name` | VARCHAR(100) | - | User's first name |
| `last_name` | VARCHAR(100) | - | User's last name |
| `email` | VARCHAR(255) | UNIQUE | User's email |
| `token_version` | INT | DEFAULT 0 | Token invalidation mechanism |
| `record_created_date` | TIMESTAMP | AUTO | Audit trail |
| `record_created_by` | VARCHAR(100) | - | Audit trail |
| `record_updated_date` | TIMESTAMP | AUTO | Audit trail |
| `record_updated_by` | VARCHAR(100) | - | Audit trail |
| `version` | BIGINT | DEFAULT 0 | Optimistic locking |

### Table: `app_user_roles`

| Column | Type | Constraints | Purpose |
|--------|------|-------------|---------|
| `user_id` | BIGINT | FOREIGN KEY → app_user | References user |
| `roles` | VARCHAR(50) | - | Role name (ADMIN, USER, etc.) |

**Relationship**: One user → Many roles (one-to-many)

---

## Implementation Details

### Password Security

**BCrypt Configuration**:
```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);  // Cost factor 12
}
```

**Why BCrypt**:
- Adaptive hashing (cost increases with hardware improvements)
- Built-in salt generation
- Industry standard for password storage
- Cost factor 12 = 2^12 (4,096) iterations

**Performance Trade-off**:
- Registration: ~250ms per user (acceptable, one-time)
- Login: ~250ms verification (acceptable for security)

**Alternative Considered**: Argon2 rejected due to Spring Security's default BCrypt integration.

### JWT Token Generation

**Token Structure**:
```json
{
  "sub": "admin",  // Subject (username)
  "roles": "ADMIN,USER",  // Comma-separated roles
  "email": "admin@tournimate.com",
  "iat": 1706123456,  // Issued at (Unix timestamp)
  "exp": 1706209856   // Expiration (Unix timestamp, 24h later)
}
```

**Implementation**:
```java
public String generateToken(AppUser user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("roles", user.getRoles().stream()
        .map(AppUserRole::getRoleName)
        .collect(Collectors.joining(",")));
    claims.put("email", user.getEmail());
    
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(user.getUsername())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
}
```

### Token Versioning (Future Enhancement)

**Problem**: User role changed, but existing JWT valid for 24 hours

**Solution**: Token version field in database
```java
@Entity
public class AppUser {
    @Column(name = "token_version")
    private Integer tokenVersion = 0;
    
    public void incrementTokenVersion() {
        this.tokenVersion++;
    }
}
```

**Validation**:
```java
if (tokenVersion != user.getTokenVersion()) {
    throw new TokenInvalidException("Token version mismatch");
}
```

**Result**: All tokens invalidated immediately when roles change

---

## Security Considerations

### Password Storage

**Never Store Plain Text**:
```java
// ❌ Wrong
user.setPassword(request.getPassword());

// ✅ Correct
user.setPassword(passwordEncoder.encode(request.getPassword()));
```

### JWT Secret Key

**Configuration**:
```yaml
jwt:
  secret: ${JWT_SECRET}  # Injected from environment variable or Vault
  expiration: 86400000  # 24 hours in milliseconds
```

**Security**:
- Secret key stored in Vault (not in code)
- Minimum 256 bits for HS256
- Shared with API Gateway for local validation

### Role Hierarchy

**Implementation**: ADMIN inherits USER permissions
```java
public boolean hasRole(String role) {
    if (roles.contains("ADMIN")) {
        return true;  // ADMIN satisfies any role check
    }
    return roles.contains(role);
}
```

---

## Development

### Run Locally

```bash
cd tourni-identity-service
mvn spring-boot:run

# Requires MySQL on port 3306
# Database: identity_db_dev
```

### Test Credentials (Development Only)

| Username | Password | Roles |
|----------|----------|-------|
| `admin` | `admin@4789` | ADMIN, USER |
| `user` | `admin@4789` | USER |

**Note**: Passwords are BCrypt hashed in database

### Dependencies

```xml
<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- JWT -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>

<!-- JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- MySQL -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency>
```

---

## Configuration

### Database

```yaml
spring:
  datasource:
    url: jdbc:mysql://${MYSQL_HOST:localhost}:3306/identity_db_dev
    username: identity_admin
    password: ${MYSQL_PASSWORD}
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update  # Dev: update, Prod: validate
    show-sql: true  # Dev only
    properties:
      hibernate:
        format_sql: true
```

### JWT

```yaml
jwt:
  secret: ${JWT_SECRET}
  expiration: 86400000  # 24 hours
```

---

## Monitoring

### Health Check

```bash
curl http://localhost:8082/actuator/health
```

### Metrics

```bash
# Prometheus metrics
curl http://localhost:8082/actuator/prometheus

# Key metrics to monitor:
# - authentication_requests_total
# - authentication_failures_total
# - jwt_generation_duration_seconds
# - password_encoding_duration_seconds
```

---

## Future Enhancements

- **Refresh Tokens**: Long-lived refresh tokens for better UX
- **Token Revocation**: Blacklist for compromised tokens
- **Multi-Factor Authentication**: TOTP/SMS verification
- **OAuth2 Integration**: Social login (Google, GitHub)
- **Password Reset**: Email-based password recovery

---

[← Back to Main Documentation](../README.md)
