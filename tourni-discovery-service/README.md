# 🔍 Discovery Service (Eureka)

> Netflix Eureka-based service registry enabling dynamic service discovery and load balancing.

[![Spring Cloud Netflix](https://img.shields.io/badge/Spring%20Cloud%20Netflix-2023.0.0-brightgreen.svg)](https://spring.io/projects/spring-cloud-netflix)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-brightgreen.svg)](https://spring.io/projects/spring-boot)

---

## What It Does

Provides a service registry where all microservices register themselves and discover other services dynamically, eliminating hardcoded URLs and enabling automatic load balancing.

**Key Capabilities:**
- **Service Registration**: All services register with hostname and port on startup
- **Health Monitoring**: Periodic heartbeats (30s) to detect unhealthy instances
- **Service Discovery**: Services query Eureka to find other services dynamically
- **Load Balancing**: Client-side load balancing via `lb://SERVICE-NAME` URLs
- **Self-Preservation Mode**: Protects against network partition false positives
- **Web Dashboard**: Visual monitoring at `http://localhost:8761`

**Business Value:**
- **Zero Configuration**: Services discover each other automatically
- **Auto-Scaling**: New instances automatically join the pool
- **Fault Tolerance**: Dead instances removed within 90 seconds
- **No Hard-Coded URLs**: Services communicate via logical names

---

## Quick Start

### Run Locally

```bash
cd tourni-discovery-service
mvn spring-boot:run

# Eureka server runs on port 8761 (industry standard)
```

### Access Dashboard

Open browser: http://localhost:8761

You'll see registered services:
```
Application             Status          Availability Zones
TOURNI-GATEWAY          UP (1)          zone1
TOURNI-IDENTITY-SERVICE UP (1)          zone1
TOURNI-MANAGEMENT       UP (1)          zone1
TOURNI-CONFIG-SERVER    UP (1)          zone1
```

### Test Service Discovery API

```bash
# Get all registered services
curl http://localhost:8761/eureka/apps

# Get specific service instances
curl http://localhost:8761/eureka/apps/TOURNI-IDENTITY-SERVICE

# Check Eureka health
curl http://localhost:8761/actuator/health
```

**Expected Response:**
```json
{
  "status": "UP",
  "components": {
    "eureka": {
      "status": "UP"
    }
  }
}
```

---

## Architecture

```
┌──────────────────────────────────────────────────┐
│    Eureka Server (Port 8761)                     │
│                                                  │
│    Registry:                                     │
│    ┌─────────────────────────────────────┐      │
│    │ TOURNI-GATEWAY → [instance-1]       │      │
│    │ TOURNI-IDENTITY-SERVICE → [inst-1]  │      │
│    │ TOURNI-MANAGEMENT → [instance-1]    │      │
│    │ TOURNI-AI → [instance-1]            │      │
│    └─────────────────────────────────────┘      │
│                                                  │
│    Heartbeat every 30s ❤️                       │
│    Eviction if 3 missed heartbeats (90s)        │
└──────────────────────────────────────────────────┘
         ↑                              ↑
         │ Register (on startup)        │ Discover (runtime)
         │                              │
    ┌─────────┐                   ┌─────────┐
    │Service  │                   │Gateway  │
    │Startup  │                   │Routing  │
    └─────────┘                   └─────────┘
```

**How It Works:**
1. **Service Registration**: Each service registers on startup with name, hostname, port
2. **Heartbeat**: Services send heartbeat every 30s to prove they're alive
3. **Discovery**: Gateway/Services fetch registry (cached for 30s)
4. **Load Balancing**: Multiple instances of same service load-balanced automatically
5. **Eviction**: Services removed if 3 consecutive heartbeats missed (90s)

**Why Eureka Over DNS?**
- **Health Aware**: Only routes to healthy instances
- **Fast Failover**: 90s vs DNS TTL (minutes to hours)
- **Metadata**: Services can include tags, versions, availability zones
- **Client-Side LB**: No external load balancer needed

---

## Service Registration Example

### How Services Register

```yaml
# In service's application.yml (loaded from Config Server)
spring:
  application:
    name: tourni-identity-service  # Service name in registry

eureka:
  client:
    service-url:
      defaultZone: http://tourni-discovery-service:8761/eureka/
  instance:
    prefer-ip-address: true  # Use IP instead of hostname
    lease-renewal-interval-in-seconds: 30  # Heartbeat interval
```

No code required - Spring Boot auto-configures everything!

### Load-Balanced Service Calls

```java
// In Gateway application.yml
spring:
  cloud:
    gateway:
      routes:
        - id: identity-service
          uri: lb://TOURNI-IDENTITY-SERVICE  # lb:// = load-balanced via Eureka
          predicates:
            - Path=/api/v1/auth/**
```

Gateway automatically:
1. Queries Eureka for `TOURNI-IDENTITY-SERVICE` instances
2. Round-robin load balances across healthy instances
3. Retries on another instance if one fails

---

## Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| **Spring Boot** | 3.2.1 | Application framework |
| **Spring Cloud Netflix Eureka** | 2023.0.0 | Service registry |
| **Actuator** | 3.2.1 | Health monitoring |
| **Prometheus** | - | Metrics export |
| **Micrometer** | - | Observability |

---

## Configuration

### application.yml

```yaml
server:
  port: 8761  # Standard Eureka port

spring:
  application:
    name: tourni-discovery-service

eureka:
  client:
    # Eureka server should NOT register itself
    register-with-eureka: false
    fetch-registry: false
  server:
    # Disable self-preservation in dev (enable in production)
    enable-self-preservation: false
```

### Environment Variables

```bash
# Kubernetes/Docker deployment
EUREKA_HOST=tourni-discovery-service
EUREKA_PORT=8761
```

---

## Production Considerations

### High Availability
- **Multiple Eureka Instances**: Run 3+ instances in production
- **Peer Replication**: Eureka instances replicate registry to each other
- **Client Caching**: Services cache registry for 30s (survives short Eureka outages)
- **Resilience**: If Eureka down, services use cached registry

**Production Setup:**
```yaml
# Eureka instance 1
eureka:
  client:
    service-url:
      defaultZone: http://eureka2:8761/eureka/,http://eureka3:8761/eureka/

# Eureka instance 2
eureka:
  client:
    service-url:
      defaultZone: http://eureka1:8761/eureka/,http://eureka3:8761/eureka/
```

### Security
- **Dashboard Access**: Restrict to admins only (Spring Security)
- **Network Policy**: Eureka accessible only within cluster
- **No Sensitive Data**: Never put secrets in instance metadata
- **HTTPS**: Enable SSL in production

### Performance
- **Registry Caching**: Clients cache for 30s (reduces Eureka load)
- **Heartbeat Interval**: 30s default (balance between detection speed and network overhead)
- **Eviction**: 90s timeout (balance between false positives and detection speed)
- **Capacity**: Single Eureka handles 10,000+ services

### Monitoring
- **Health Check**: `/actuator/health` (checks internal state)
- **Metrics**: `/actuator/prometheus` (registered instances, heartbeats)
- **Dashboard**: `/` (visual service status)
- **Logs**: OpenTelemetry traces to Tempo

---

## Eureka Dashboard

### Real-Time Monitoring

Access: http://localhost:8761

**Displays:**
- **Instances Currently Registered**: All active services
- **General Info**: Uptime, environment, memory
- **Instance Info**: Each service's hostname, port, status, metadata
- **DS Replicas**: Peer Eureka servers (in HA setup)

**Service Status:**
- 🟢 **UP**: Service healthy (heartbeat received < 30s ago)
- 🔴 **DOWN**: Service unhealthy (heartbeat missed > 90s)
- 🟡 **STARTING**: Service registered but not ready

### Self-Preservation Mode

**What It Is:**
When Eureka detects network partition (e.g., 15%+ services stop heartbeating), it assumes the network is faulty, not the services. It stops evicting instances to prevent mass eviction.

**Why It Matters:**
Prevents false positives during network issues.

**Production Recommendation:**
- **Enable in production** (default behavior)
- **Disable in dev/test** (for faster feedback)

---

## Interview Highlights

**Architecture:**
- Why use Eureka over Consul/Zookeeper? (Java ecosystem, Netflix OSS, client-side LB)
- How does service discovery enable microservices? (Dynamic URLs, auto-scaling)
- Eureka AP vs Consul CP trade-offs (Availability vs Consistency)

**Scalability:**
- How many services can Eureka handle? (10,000+ instances per server)
- Client-side caching strategy (30s cache, reduces Eureka load)
- Horizontal scaling (peer replication across multiple Eureka instances)

**Fault Tolerance:**
- What happens if Eureka crashes? (Clients use cached registry)
- Self-preservation mode explained (prevents mass eviction during network partitions)
- Heartbeat tuning (30s interval vs 90s eviction timeout)

---

## Future Enhancements

| Feature | Priority | Impact | Effort |
|---------|----------|--------|--------|
| High Availability (3+ instances) | 🔴 High | Eliminate single point of failure | 1-2 days |
| Dashboard Authentication | 🔴 High | Secure admin access | 1 day |
| Service Metadata Tags | 🟡 Medium | Blue-green deployments | 2-3 days |
| Health Check Customization | 🟡 Medium | Better health detection | 1-2 days |
| Availability Zones | 🟢 Low | Multi-region support | 3-5 days |

---

## 🚀 What's Next?

### Key Concepts
- **Service Registry**: Central directory of all service instances
- **Heartbeat Mechanism**: Services prove they're alive every 30 seconds
- **Load Balancing**: `lb://SERVICE-NAME` automatically balances across instances

### Related Services
- [Gateway](../tourni-gateway/README.md) - Uses Eureka for routing
- [Config Server](../tourni-config-server/README.md) - Registers with Eureka
- [All Microservices](../README.md#microservices) - Register with Eureka

### Development Commands

```bash
# Check registered services
curl http://localhost:8761/eureka/apps | jq

# Monitor heartbeats (watch mode)
watch -n 5 'curl -s http://localhost:8761/eureka/apps | grep status'

# Test service discovery from container
docker exec -it gateway-container curl http://eureka:8761/eureka/apps/TOURNI-IDENTITY-SERVICE
```

---

**[← Back to Main README](../README.md)**
