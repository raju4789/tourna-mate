# ⚙️ Config Server

> Centralized external configuration management for all TOURNA-MATE microservices.

[![Spring Cloud Config](https://img.shields.io/badge/Spring%20Cloud%20Config-2023.0.0-brightgreen.svg)](https://spring.io/projects/spring-cloud-config)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-brightgreen.svg)](https://spring.io/projects/spring-boot)

---

## What It Does

Acts as a central configuration server that delivers environment-specific configurations to all microservices from a Git repository, enabling configuration changes without redeploying services.

**Key Capabilities:**
- Git-backed configuration storage ([GitHub repo](https://github.com/raju4789/tourni-config))
- Environment-specific profiles (dev, staging, prod)
- Dynamic configuration refresh without service restarts
- Version-controlled configuration history via Git
- Configuration encryption support (Vault integration ready)
- Service discovery registration via Eureka

**Business Value:**
- **Zero Downtime**: Update configs without redeploying (saves ~15 minutes per deploy)
- **Audit Trail**: Every config change tracked in Git with commit history
- **Consistency**: Single source of truth for all service configurations
- **Security**: Sensitive data externalized (database passwords, JWT secrets)

---

## Quick Start

### Run Locally

```bash
cd tourni-config-server
mvn spring-boot:run

# Config server runs on port 8888
```

### Test Configuration Retrieval

```bash
# Get default configuration for identity service
curl http://localhost:8888/tourni-identity-service/default

# Get production configuration
curl http://localhost:8888/tourni-identity-service/prod

# Get specific file
curl http://localhost:8888/tourni-identity-service/default/main/application.yml
```

**Expected Response:**
```json
{
  "name": "tourni-identity-service",
  "profiles": ["default"],
  "label": "main",
  "version": "abc123...",
  "state": null,
  "propertySources": [...]
}
```

---

## Architecture

```
┌─────────────────────────────────────────────────┐
│         Config Server (Port 8888)               │
│                                                 │
│  1. Service requests config                    │
│     GET /tourni-identity-service/default       │
│                                                 │
│  2. Fetch from Git repository                  │
│     https://github.com/raju4789/tourni-config  │
│                                                 │
│  3. Cache locally (filesystem)                 │
│     ~/.spring-cloud-config/                    │
│                                                 │
│  4. Return configuration as JSON               │
│                                                 │
└─────────────────────────────────────────────────┘
                        │
         ┌──────────────┼──────────────┬────────────────┐
         ↓              ↓              ↓                ↓
   ┌─────────┐   ┌─────────┐   ┌─────────┐   ┌──────────────┐
   │Identity │   │Gateway  │   │Management│   │Discovery     │
   │Service  │   │Service  │   │Service   │   │Service       │
   └─────────┘   └─────────┘   └─────────┘   └──────────────┘
```

**Why Git-Backed Configuration?**
- **Version Control**: Every change tracked with who, what, when
- **Rollback**: Revert to previous config via Git revert
- **Branching**: Test configs in feature branches before merging
- **Code Review**: Configuration changes reviewed via Pull Requests

**URL Pattern:**
```
http://config-server:8888/{service-name}/{profile}/{label}

Examples:
- /tourni-identity-service/default/main  (development)
- /tourni-identity-service/prod/main     (production)
- /tourni-gateway/staging/main           (staging)
```

---

## Configuration Repository Structure

```
raju4789/tourni-config (GitHub)
├── application.yml                    # Shared config (all services)
├── tourni-identity-service.yml        # Identity service base config
├── tourni-identity-service-prod.yml   # Identity production overrides
├── tourni-gateway.yml                 # Gateway base config
├── tourni-management.yml              # Management service config
└── ... (other service configs)
```

**Configuration Priority** (lowest to highest):
1. `application.yml` (shared)
2. `{service-name}.yml` (service-specific)
3. `{service-name}-{profile}.yml` (environment-specific)

---

## Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| **Spring Boot** | 3.2.1 | Application framework |
| **Spring Cloud Config Server** | 2023.0.0 | Configuration management |
| **Spring Cloud Eureka Client** | 2023.0.0 | Service registration |
| **Git (GitHub)** | - | Configuration repository |
| **Actuator** | 3.2.1 | Health monitoring |
| **Prometheus** | - | Metrics export |

---

## Configuration

### application.yml

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
          uri: https://github.com/raju4789/tourni-config
          # Public repo - no credentials required
          # For private repos, add username/password or SSH key

eureka:
  client:
    service-url:
      defaultZone: http://tourni-discovery-service:8761/eureka/
```

### Environment Variables

```bash
# Docker/Kubernetes deployment
CONFIG_SERVICE_HOST=tourni-config-server  # Service discovery hostname
EUREKA_HOST=tourni-discovery-service      # Eureka server hostname
```

---

## Production Considerations

### Security
- **Encryption**: Use `{cipher}` prefix for encrypted values (requires encryption key)
- **Vault Integration**: Store secrets in HashiCorp Vault (JWT secret, DB passwords)
- **Private Repository**: Use SSH keys or GitHub tokens for private repos
- **Network Security**: Config server accessible only within cluster (not public)

### Performance
- **Local Caching**: Git repo cloned locally (~/.spring-cloud-config/)
- **Lazy Loading**: Configs fetched only when requested
- **Refresh Interval**: Services refresh every 30s (configurable via `/actuator/refresh`)

### Monitoring
- **Health Check**: `/actuator/health` (checks Git repo connectivity)
- **Metrics**: `/actuator/prometheus` (request counts, response times)
- **Logging**: OpenTelemetry traces sent to Tempo

### Disaster Recovery
- **Git Backup**: Config repo backed up automatically via GitHub
- **Local Fallback**: Services use embedded configs if Config Server unavailable
- **High Availability**: Run multiple Config Server instances (active-active)

---

## How Services Use Config Server

### 1. Service Startup Sequence

```
Service Start
    ↓
Contact Config Server (port 8888)
    ↓
Request config: /{service-name}/{profile}
    ↓
Config Server fetches from Git
    ↓
Service receives configuration
    ↓
Service starts with loaded config
```

### 2. Application Code (No Code Required)

Services automatically connect using `spring.config.import`:

```yaml
# In service's application.yml
spring:
  config:
    import: "optional:configserver:http://tourni-config-server:8888"
```

`optional:` means: "Use Config Server if available, otherwise use local config."

### 3. Dynamic Refresh

```bash
# Update config in Git repository
git commit -m "Update database pool size"
git push

# Refresh service without restart
curl -X POST http://service:8080/actuator/refresh
```

---

## Interview Highlights

**Architecture:**
- Why externalize configuration? (Separation of concerns, security, dynamic updates)
- Config Server vs environment variables trade-offs
- Git-backed vs database-backed configuration comparison

**Deployment:**
- How to handle Config Server downtime? (`optional:` prefix, local fallbacks)
- Configuration precedence (application → service → profile)
- Blue-green deployments with configuration versioning

**Security:**
- How to store secrets securely? (Vault integration, {cipher} encryption)
- Preventing unauthorized config access (Spring Security, network policies)
- Audit trail for configuration changes (Git commit history)

---

## Future Enhancements

| Feature | Priority | Impact | Effort |
|---------|----------|--------|--------|
| HashiCorp Vault Integration | 🔴 High | Secure secrets management | 2-3 days |
| Configuration Encryption | 🔴 High | Encrypted sensitive values | 1-2 days |
| Config Change Webhooks | 🟡 Medium | Auto-refresh services on Git push | 2-3 days |
| Multi-Repository Support | 🟡 Medium | Separate repos per team | 1-2 days |
| Config Validation | 🟢 Low | Validate configs before deployment | 3-5 days |

---

## 🚀 What's Next?

### Key Concepts
- **Centralized Configuration**: All service configs managed from single Git repository
- **Profile-Based**: Different configs for dev, staging, production environments
- **Dynamic Refresh**: Update configurations without service restarts

### Related Services
- [Eureka Discovery Service](../tourni-discovery-service/README.md) - Service registration
- [All Microservices](../README.md#microservices) - Services using Config Server

### Development Setup

```bash
# Clone configuration repository
git clone https://github.com/raju4789/tourni-config.git

# Make config changes locally
cd tourni-config
vim tourni-identity-service.yml

# Test locally before committing
curl http://localhost:8888/tourni-identity-service/default
```

---

**[← Back to Main README](../README.md)**
