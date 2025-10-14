# 🔐 Identity Service

> JWT-based authentication and authorization microservice providing user registration, login, and token management for the TOURNA-MATE platform.

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-6.2.1-brightgreen.svg)](https://spring.io/projects/spring-security)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)

---

## What It Does

Handles user authentication, JWT token generation, role management, and provides a secure foundation for all TOURNA-MATE services.

**Key Capabilities:**
- **User Registration**: Create accounts with BCrypt password hashing (cost factor 12)
- **JWT Token Generation**: Stateless tokens valid for 24 hours
- **Multi-Role Support**: ADMIN, USER, MANAGER, VIEWER roles (expandable)
- **Token Versioning**: Incremental version invalidates old tokens on role changes
- **Automatic Audit Trail**: Who created/modified users and when (JPA Auditing)
- **Optimistic Locking**: Prevents concurrent modification conflicts (@Version)

**Security Metrics:**
- **Password Hashing**: BCrypt (cost 12) = 2^12 iterations (~200ms per hash)
- **Token Expiration**: 24 hours (configurable)
- **Stateless**: No session storage, scales horizontally

---

## Quick Start

### Run Locally

```bash
cd tourni-identity-service
mvn spring-boot:run

# Service runs on port 8082
```

### Register a User

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

**Response:**
```json
{
  "status": "SUCCESS",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huX2RvZSIsInJvbGVzIjpbIlVTRVIiXSwiaWF0IjoxNzA2MTIzNDU2LCJleHAiOjE3MDYyMDk4NTZ9...",
    "expiryTime": "24 Hours",
    "username": "john_doe",
    "email": "john@example.com",
    "roles": ["USER"]
  }
}
```

### Login (Authenticate)

```bash
curl -X POST http://localhost:8080/api/v1/auth/authenticate \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin@4789"
  }'
```

### Test Credentials (Development Only)

```
Username: admin
Password: admin@4789
Roles: ADMIN, USER

Username: user
Password: admin@4789
Roles: USER
```

---

## Architecture

```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │
       │ 1. POST /api/v1/auth/register or /authenticate
       ↓
┌──────────────────┐
│  API Gateway     │ (forwards to Identity Service)
└──────┬───────────┘
       │
       │ 2. Validate credentials
       ↓
┌──────────────────────────────────────────────┐
│        Identity Service (Port 8082)          │
│                                              │
│  ┌────────────────────────────────────┐    │
│  │ AppAuthenticationController        │    │
│  └────────────┬───────────────────────┘    │
│               ↓                            │
│  ┌────────────────────────────────────┐    │
│  │ AppAuthenticationService           │    │
│  │  - loadUserByUsername()            │    │
│  │  - BCrypt password validation      │    │
│  └────────────┬───────────────────────┘    │
│               ↓                            │
│  ┌────────────────────────────────────┐    │
│  │ MySQL Database                     │    │
│  │  Tables: app_user, user_roles      │    │
│  └────────────┬───────────────────────┘    │
│               ↓                            │
│  ┌────────────────────────────────────┐    │
│  │ JWTService                         │    │
│  │  - generateToken()                 │    │
│  │  - Include: username, roles, email │    │
│  │  - Expiration: 24 hours            │    │
│  └────────────────────────────────────┘    │
└──────────────────────────────────────────────┘
       │
       │ 3. Return JWT token
       ↓
┌──────────────────┐
│   Client Stores  │
│   JWT Token      │
└──────────────────┘
```

**Design Decisions:**
- **Stateless Tokens**: No session storage → horizontal scaling without sticky sessions
- **Token Versioning**: Role changes increment version → invalidates old tokens
- **BCrypt Cost 12**: Balance between security (2^12 iterations) and performance (~200ms)
- **Separate Service**: Security isolation, independent scaling, bounded context

---

## API Endpoints

### Authentication Endpoints

| Method | Endpoint | Description | Auth Required | Roles |
|--------|----------|-------------|---------------|-------|
| POST | `/api/v1/auth/register` | Create new user | No | - |
| POST | `/api/v1/auth/authenticate` | Login, get JWT token | No | - |
| POST | `/api/v1/auth/validateToken` | Validate JWT token | No | - |
| GET | `/api/v1/auth/generateToken?username={username}` | Generate token for user | No | - |

### Examples

#### Register

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "alice",
    "password": "SecurePass456",
    "firstName": "Alice",
    "lastName": "Smith",
    "email": "alice@example.com",
    "roles": ["USER", "ADMIN"]
  }'
```

#### Authenticate (Login)

```bash
curl -X POST http://localhost:8080/api/v1/auth/authenticate \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin@4789"
  }'
```

#### Validate Token

```bash
curl -X POST http://localhost:8080/api/v1/auth/validateToken \
  -H "Content-Type: application/json" \
  -d '{"token": "eyJhbGciOiJIUzI1NiJ9..."}'
```

---

## Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| **Spring Boot** | 3.2.1 | Application framework |
| **Spring Security** | 6.2.1 | Authentication & authorization |
| **Spring Data JPA** | 3.2.1 | Database abstraction (ORM) |
| **JWT (jjwt)** | 0.11.5 | Token generation & parsing |
| **MySQL** | 8.0 | User persistence |
| **Flyway** | - | Database migrations (future) |
| **BCrypt** | - | Password hashing (cost factor 12) |
| **Lombok** | - | Boilerplate reduction |
| **OpenAPI 3.0** | 2.3.0 | API documentation |

---

## Database Schema

### app_user Table

```sql
CREATE TABLE app_user (
    username VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    token_version INT DEFAULT 0 NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    version BIGINT,  -- Optimistic locking
    record_created_date TIMESTAMP NOT NULL,
    record_created_by VARCHAR(255) NOT NULL,
    record_updated_date TIMESTAMP,
    record_updated_by VARCHAR(255)
);
```

### user_roles Table (Many-to-Many)

```sql
CREATE TABLE user_roles (
    username VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY (username, role),
    FOREIGN KEY (username) REFERENCES app_user(username)
);
```

**Sample Data:**
```sql
-- User: admin (password: admin@4789)
username: admin
roles: ADMIN, USER

-- User: user (password: admin@4789)
username: user
roles: USER
```

---

## Configuration

### External Configuration (from Config Server)

```yaml
# Loaded from: https://github.com/raju4789/tourni-config/tourni-identity-service.yml

jwt:
  secret: ${VAULT_JWT_SECRET}  # Stored in Vault for security
  expiration: 86400000          # 24 hours in milliseconds

spring:
  datasource:
    url: jdbc:mysql://mysql:3306/identity_db
    username: ${VAULT_DB_USERNAME}
    password: ${VAULT_DB_PASSWORD}
    
  jpa:
    hibernate:
      ddl-auto: update           # Creates/updates tables automatically
    show-sql: false              # Disable SQL logging in production
    auditing:
      enabled: true              # Enable JPA auditing

  security:
    password-encoder: BCrypt     # BCrypt with cost factor 12
```

### Local Bootstrap (application.yml)

```yaml
server:
  port: 8082

spring:
  application:
    name: tourni-identity-service
  config:
    import: "optional:configserver:http://tourni-config-server:8888"
```

---

## Security Implementation

### Password Hashing (BCrypt)

**Why BCrypt with Cost Factor 12?**
- **Adaptive**: Cost factor increases as hardware improves
- **Salt**: Random salt per password (prevents rainbow tables)
- **Slow**: 2^12 iterations (~200ms) makes brute-force attacks impractical

```java
// Automatically handled by Spring Security
BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
String hashedPassword = encoder.encode("plainPassword");
// Result: $2a$12$2GgsW45hrAFhmbNbojj3m./wpbEud9YK/LgX1SmkfHIQxpCUE4M/O
```

### Token Versioning

**Problem**: How to immediately invalidate tokens when user roles change?

**Solution**: Token versioning

```java
// AppUser entity
private Integer tokenVersion = 0;  // Incremented on role/password changes

// When roles change
user.addRole(AppUserRole.ADMIN);
user.incrementTokenVersion();  // Old tokens now invalid
```

**JWT Payload:**
```json
{
  "sub": "admin",
  "roles": ["ADMIN", "USER"],
  "email": "admin@tournimate.com",
  "tokenVersion": 5,  // Must match database
  "iat": 1706123456,
  "exp": 1706209856
}
```

### JPA Auditing

Automatically tracks who/when for all changes:

```java
@EntityListeners(AuditingEntityListener.class)
public class AppUser {
    @CreatedDate
    private LocalDateTime recordCreatedDate;
    
    @CreatedBy
    private String recordCreatedBy;
    
    @LastModifiedDate
    private LocalDateTime recordUpdatedDate;
    
    @LastModifiedBy
    private String recordUpdatedBy;
}
```

**Example Data:**
```
username: john_doe
record_created_date: 2024-01-25 10:30:00
record_created_by: admin
record_updated_date: 2024-01-26 14:15:00
record_updated_by: john_doe
```

---

## Production Considerations

### Security
- ✅ **Passwords Hashed**: BCrypt cost factor 12 (~200ms per hash)
- ✅ **JWT Secrets in Vault**: Never commit secrets to Git
- ✅ **Token Versioning**: Immediate role revocation
- ⚠️ **Recommended**: Add refresh tokens (15min access + 7d refresh)
- ⚠️ **Recommended**: Account lockout after 5 failed attempts
- ⚠️ **Recommended**: Email verification on registration

### Performance
- **Stateless**: No session storage, scales horizontally
- **Connection Pool**: HikariCP with 10 connections (configurable)
- **Query Optimization**: Username and email indexed (unique constraints)
- **BCrypt Overhead**: ~200ms per login (acceptable for security)

### Monitoring
- **Metrics**: `/actuator/prometheus`
  - Login attempts per second
  - Failed login rate
  - Token generation time
- **Health**: `/actuator/health` (checks database connectivity)
- **Tracing**: OpenTelemetry → Tempo (distributed traces)
- **Logs**: Structured JSON → Loki

---

## Interview Highlights

**System Design:**
- Why separate identity service? (Bounded context, security isolation, independent scaling)
- Stateless vs stateful auth trade-offs (scalability vs immediate revocation)
- How to handle role changes with JWT? (Token versioning, refresh tokens)
- Why JWT over session cookies? (Stateless, cross-domain, mobile apps)

**Security:**
- BCrypt cost factor selection (balance security vs performance)
- JWT expiration strategy (24h access token, 7d refresh token recommended)
- OWASP Top 10 mitigations (SQL injection via JPA, XSS prevention, CSRF protection)
- How to prevent token theft? (HTTPS, short expiration, refresh rotation)

**Scalability:**
- Horizontal scaling without sticky sessions (stateless JWT)
- Database read replicas for user lookups
- Redis for token blacklist (immediate revocation)
- How to handle 10,000 concurrent logins? (Connection pooling, async processing)

---

## Future Enhancements

| Feature | Priority | Impact | Effort |
|---------|----------|--------|--------|
| Refresh Token Rotation | 🔴 High | Reduced token theft risk | 2-3 days |
| Account Lockout (5 attempts) | 🔴 High | Brute force prevention | 1-2 days |
| OAuth2/OIDC (Google, GitHub) | 🟡 Medium | Social login support | 5-7 days |
| 2FA/MFA (TOTP) | 🟡 Medium | Additional security layer | 3-5 days |
| Email Verification | 🟡 Medium | Validate email ownership | 2-3 days |
| Password Reset via Email | 🟡 Medium | User self-service | 2-3 days |
| Rate Limiting (login attempts) | 🟢 Low | DDoS prevention | 1-2 days |

---

## 🚀 What's Next?

### Development Commands

```bash
# Run tests
mvn test

# Build Docker image
docker build -t tourni-identity-service:latest .

# Run with Docker Compose
cd ../../docker/dev
docker-compose up identity-service mysql

# Check database
docker exec -it mysql mysql -u root -p
mysql> USE identity_db;
mysql> SELECT username, email, is_active FROM app_user;
```

### Key Concepts
- **JWT Generation**: Tokens include username, roles, email, expiration (24h)
- **Password Security**: BCrypt (cost 12) with automatic salting
- **Audit Trail**: JPA Auditing tracks who created/modified users
- **Token Versioning**: Incremental version invalidates old tokens

### Related Services
- [Gateway](../tourni-gateway/README.md) - Validates JWT tokens
- [Management Service](../tourni-management/README.md) - Uses user context
- [Config Server](../tourni-config-server/README.md) - Provides configuration
- [Discovery Service](../tourni-discovery-service/README.md) - Service registration

---

**[← Back to Main README](../README.md)**
