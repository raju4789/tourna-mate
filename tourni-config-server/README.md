# Config Server

> Centralized configuration management with Git backend

[![Spring Cloud Config](https://img.shields.io/badge/Spring%20Cloud%20Config-2023.0.0-brightgreen.svg)](https://spring.io/projects/spring-cloud-config)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-brightgreen.svg)](https://spring.io/projects/spring-boot)

---

## Purpose

Externalized configuration management for all microservices using Git repository as backend. Enables environment-specific configurations, runtime updates, and version-controlled settings.

### Core Responsibilities

- **Centralized Configuration**: Single source of truth for all service configurations
- **Environment Profiles**: Separate configs for dev/staging/prod
- **Git Integration**: Version-controlled configurations with audit trail
- **Runtime Refresh**: Update configurations without service restarts (using `/refresh` endpoint)
- **Encryption**: Supports encrypted properties for sensitive data

---

## Architecture

```
┌─────────────────────────────────────────────┐
│   GitHub Repository                         │
│   raju4789/tourni-config                    │
│                                             │
│   /application.yml          (common)        │
│   /application-dev.yml      (development)   │
│   /application-staging.yml  (staging)       │
│   /application-prod.yml     (production)    │
│                                             │
│   /tourni-gateway.yml                       │
│   /tourni-identity-service.yml              │
│   /tourni-management.yml                    │
└──────────────────┬──────────────────────────┘
                   │ HTTP(S) clone/pull
                   ▼
┌─────────────────────────────────────────────┐
│   Config Server (Port 8888)                 │
│                                             │
│   On startup:                               │
│   1. Clone Git repo to local filesystem     │
│   2. Cache configurations in memory         │
│                                             │
│   On request:                               │
│   1. Service requests config                │
│   2. Return YAML/JSON for profile           │
│   3. Merge application.yml + service.yml    │
└──────────────────┬──────────────────────────┘
                   │
        ┌──────────┼───────────┐
        │          │           │
        ▼          ▼           ▼
   ┌────────┐ ┌────────┐ ┌────────┐
   │Gateway │ │Identity│ │  Mgmt  │
   │(8080)  │ │ (8082) │ │ (8083) │
   └────────┘ └────────┘ └────────┘
```

---

## Configuration

### Config Server (`application.yml`)

```yaml
server:
  port: 8888

spring:
  application:
    name: tourni-config-server
  cloud:
    config:
      server:
        git:
          uri: https://github.com/raju4789/tourni-config.git
          default-label: main
          clone-on-start: true  # Clone on startup for faster first request
          timeout: 10  # Seconds
```

### Client Configuration (Services)

**Example**: `tourni-gateway/application.yml`

```yaml
spring:
  application:
    name: tourni-gateway
  config:
    import: "optional:configserver:http://tourni-config-dev:8888"
  profiles:
    active: dev  # or staging, prod
```

**Behavior**:
1. Gateway starts
2. Connects to Config Server at `http://tourni-config-dev:8888`
3. Requests configuration for `tourni-gateway` with profile `dev`
4. Config Server returns merged configuration:
   - `application.yml` (common to all services)
   - `application-dev.yml` (environment-specific)
   - `tourni-gateway.yml` (service-specific)
   - `tourni-gateway-dev.yml` (service + environment specific)
5. Gateway applies configuration

---

## Git Repository Structure

**Repository**: `https://github.com/raju4789/tourni-config`

```
tourni-config/
├── application.yml              # Common configuration for all services
├── application-dev.yml          # Development environment overrides
├── application-staging.yml      # Staging environment overrides
├── application-prod.yml         # Production environment overrides
│
├── tourni-gateway.yml           # Gateway-specific configuration
├── tourni-gateway-dev.yml       # Gateway dev overrides
├── tourni-gateway-staging.yml   # Gateway staging overrides
├── tourni-gateway-prod.yml      # Gateway prod overrides
│
├── tourni-identity-service.yml
├── tourni-management.yml
└── ... (other services)
```

### Priority Order (Highest to Lowest)

1. `{service-name}-{profile}.yml` (e.g., `tourni-gateway-prod.yml`)
2. `{service-name}.yml` (e.g., `tourni-gateway.yml`)
3. `application-{profile}.yml` (e.g., `application-prod.yml`)
4. `application.yml`

**Example Merge**:
```yaml
# application.yml (common)
logging:
  level:
    root: INFO

# application-dev.yml (dev override)
logging:
  level:
    root: DEBUG  # Overrides INFO

# tourni-gateway-dev.yml (service + env specific)
logging:
  level:
    root: DEBUG
    com.tournament: TRACE  # Additional config
```

---

## API Endpoints

### Get Configuration for Service

**GET** `/{service-name}/{profile}`

```bash
# Get gateway configuration for dev environment
curl http://localhost:8888/tourni-gateway/dev

# Get identity service configuration for prod
curl http://localhost:8888/tourni-identity-service/prod
```

**Response**:
```json
{
  "name": "tourni-gateway",
  "profiles": ["dev"],
  "label": null,
  "version": "abc123",
  "state": null,
  "propertySources": [
    {
      "name": "https://github.com/raju4789/tourni-config.git/tourni-gateway-dev.yml",
      "source": {
        "server.port": 8080,
        "logging.level.root": "DEBUG"
      }
    },
    {
      "name": "https://github.com/raju4789/tourni-config.git/application-dev.yml",
      "source": {
        "common.property": "value"
      }
    }
  ]
}
```

### Health Check

```bash
curl http://localhost:8888/actuator/health
```

---

## Benefits

### 1. GitOps Workflow

**Configuration Changes**:
```bash
# 1. Make change in local repo
vim tourni-gateway-prod.yml

# 2. Commit and push
git add .
git commit -m "Increase connection pool size"
git push origin main

# 3. Config Server pulls changes automatically (or on next request)
# Services refresh configuration (manual or automatic with Spring Cloud Bus)
```

**Audit Trail**: Every configuration change tracked in Git history

### 2. Environment Parity

Same configuration structure across all environments:
- Development: `application-dev.yml`
- Staging: `application-staging.yml`
- Production: `application-prod.yml`

Easy to compare: `git diff application-dev.yml application-prod.yml`

### 3. Rollback Capability

```bash
# Rollback to previous version
git revert <commit-hash>
git push

# Config Server picks up previous version
# Services refresh and use rolled-back config
```

---

## Runtime Configuration Refresh

### Using Spring Cloud Bus (Future)

1. Update configuration in Git
2. POST to `/actuator/bus-refresh`
3. All services receive refresh event via message bus (Kafka/RabbitMQ)
4. Services reload configuration without restart

### Manual Refresh (Current)

```bash
# 1. Update configuration in Git
git push

# 2. Trigger refresh on specific service
curl -X POST http://localhost:8082/actuator/refresh \
  -H "Content-Type: application/json"

# 3. Service reloads configuration marked with @RefreshScope
```

---

## Security

### Encrypted Properties

**Encrypt Sensitive Values**:
```bash
# Using Config Server encryption endpoint
curl http://localhost:8888/encrypt \
  -d "mysql_password_123"

# Returns: {cipher}AQA2j3kdh8f...
```

**Use in Configuration**:
```yaml
spring:
  datasource:
    password: '{cipher}AQA2j3kdh8f...'
```

**Decryption**: Config Server decrypts before sending to services

### Access Control

**Production**: Restrict Config Server access
- Private network only
- Authentication required (Spring Security)
- Git repository with SSH keys or personal access tokens

---

## Development

### Run Locally

```bash
cd tourni-config-server
mvn spring-boot:run

# Server starts on port 8888
# Clones Git repository on startup
```

### Test Configuration Retrieval

```bash
# Get gateway dev configuration
curl http://localhost:8888/tourni-gateway/dev | jq

# Get identity service prod configuration
curl http://localhost:8888/tourni-identity-service/prod | jq
```

---

## Monitoring

### Health Check

```bash
curl http://localhost:8888/actuator/health

# Response
{
  "status": "UP",
  "components": {
    "configServer": {
      "status": "UP",
      "details": {
        "repository": "https://github.com/raju4789/tourni-config.git",
        "label": "main"
      }
    }
  }
}
```

### Metrics

```bash
curl http://localhost:8888/actuator/prometheus

# Key metrics:
# - config_server_requests_total
# - config_server_git_clone_duration
# - config_server_config_fetch_duration
```

---

## Future Enhancements

- **Spring Cloud Bus**: Automatic configuration refresh across all services
- **Vault Integration**: Dynamic secrets from HashiCorp Vault
- **Multi-Repository**: Different repos for different teams/services
- **Configuration Versioning**: Rollback to specific Git commits via API

---

[← Back to Main Documentation](../README.md)
