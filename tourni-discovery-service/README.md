# Discovery Service

> Netflix Eureka server for service registration and discovery

[![Spring Cloud Netflix](https://img.shields.io/badge/Spring%20Cloud%20Netflix-2023.0.0-brightgreen.svg)](https://spring.io/projects/spring-cloud-netflix)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-brightgreen.svg)](https://spring.io/projects/spring-boot)

---

## Purpose

Service registry enabling dynamic service discovery and client-side load balancing for all microservices in the TOURNA-MATE platform.

### Core Responsibilities

- **Service Registration**: Microservices register themselves on startup
- **Service Discovery**: Services find each other by logical name
- **Health Monitoring**: Periodic heartbeats detect unhealthy instances
- **Load Balancing**: Client-side load balancing via registered instances
- **Self-Preservation Mode**: Protects registry during network partitions

---

## Architecture

```
┌────────────────────────────────────────────┐
│   Eureka Server (Port 8761)                │
│                                            │
│   Service Registry:                        │
│   ┌────────────────────────────────────┐  │
│   │ TOURNI-GATEWAY      → 2 instances  │  │
│   │   - 192.168.1.10:8080 (UP)         │  │
│   │   - 192.168.1.11:8080 (UP)         │  │
│   │                                    │  │
│   │ TOURNI-IDENTITY-SERVICE → 1 inst  │  │
│   │   - 192.168.1.12:8082 (UP)         │  │
│   │                                    │  │
│   │ TOURNI-MANAGEMENT → 1 instance    │  │
│   │   - 192.168.1.13:8083 (UP)         │  │
│   └────────────────────────────────────┘  │
└────────────────────────────────────────────┘
         ▲              │
         │ register     │ discover
         │ heartbeat    │ query
         │              ▼
    ┌────────┐      ┌─────────┐
    │Service │      │Service  │
    │Instance│      │Client   │
    └────────┘      └─────────┘
```

### How It Works

**Service Registration**:
```
1. Service starts up
2. Reads own hostname, port from configuration
3. Registers with Eureka Server
4. Sends heartbeat every 30 seconds
5. If heartbeat stops → Eureka marks as DOWN after 90 seconds
```

**Service Discovery**:
```
1. Service needs to call another service
2. Queries Eureka for service by name: lb://tourni-management
3. Eureka returns list of healthy instances
4. Client-side load balancer (Ribbon/LoadBalancer) picks instance
5. Service makes HTTP call to selected instance
```

---

## Configuration

### Eureka Server (`application.yml`)

```yaml
server:
  port: 8761

spring:
  application:
    name: tourni-discovery-service

eureka:
  client:
    register-with-eureka: false  # Server doesn't register itself
    fetch-registry: false         # Server doesn't fetch registry
  server:
    enable-self-preservation: true  # Protects during network issues
    eviction-interval-timer-in-ms: 4000  # Check for expiration every 4s
```

### Eureka Client (Services)

```yaml
eureka:
  client:
    service-url:
      defaultZone: http://tourni-discovery-dev:8761/eureka/
    register-with-eureka: true   # Register this service
    fetch-registry: true         # Fetch other services
  instance:
    prefer-ip-address: true      # Use IP instead of hostname
    lease-renewal-interval-in-seconds: 30  # Heartbeat every 30s
    lease-expiration-duration-in-seconds: 90  # Evict after 90s no heartbeat
```

---

## Dashboard

### Access

```bash
# Open in browser
http://localhost:8761

# Or via Docker network
http://tourni-discovery-dev:8761
```

### Dashboard Information

**General Info**:
- Environment (dev/staging/prod)
- Number of registered instances
- Number of available zones
- Registered replicas

**Instance Registry**:
- Application names
- Instance IDs
- Status (UP, DOWN, STARTING)
- Zone information
- Metadata

**Example**:
```
Application          AMIs  Availability Zones  Status
TOURNI-GATEWAY       n/a   (1) (1)             UP (1) - 192.168.1.10:8080
TOURNI-IDENTITY-SERVICE n/a (1) (1)           UP (1) - 192.168.1.12:8082
TOURNI-MANAGEMENT    n/a   (1) (1)             UP (1) - 192.168.1.13:8083
```

---

## Service Discovery in Action

### Gateway Routing Example

**Configuration**:
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: tourni-identity-service
          uri: lb://tourni-identity-service  # lb:// = load-balanced via Eureka
          predicates:
            - Path=/api/v1/auth/**
```

**Flow**:
```
1. Request: GET /api/v1/auth/validateToken
2. Gateway matches route: tourni-identity-service
3. Gateway queries Eureka: "Give me instances of tourni-identity-service"
4. Eureka returns: ["192.168.1.12:8082"]
5. Gateway forwards to: http://192.168.1.12:8082/api/v1/auth/validateToken
```

### Load Balancing with Multiple Instances

**Scenario**: 3 Gateway instances running

```
Eureka Registry:
- tourni-gateway: 
  - 192.168.1.10:8080 (UP)
  - 192.168.1.11:8080 (UP)
  - 192.168.1.12:8080 (UP)

Request 1 → 192.168.1.10:8080 (Round-robin)
Request 2 → 192.168.1.11:8080
Request 3 → 192.168.1.12:8080
Request 4 → 192.168.1.10:8080 (cycle repeats)
```

**Load Balancer**: Spring Cloud LoadBalancer (replaces Netflix Ribbon)

---

## Health Monitoring

### Heartbeat Mechanism

**Default Timing**:
- Services send heartbeat every **30 seconds**
- Eureka waits **90 seconds** before eviction
- Allows 3 missed heartbeats before marking DOWN

**Calculation**:
```
Eviction Threshold = 90s / 30s = 3 missed heartbeats
```

### Self-Preservation Mode

**Purpose**: Prevent mass evictions during network partitions

**Trigger**: When < 85% of services sending heartbeats

**Behavior**:
- Eureka stops evicting instances
- Assumes network issue, not service failure
- Displays warning in dashboard: "EMERGENCY! EUREKA MAY BE INCORRECTLY CLAIMING INSTANCES ARE UP WHEN THEY'RE NOT."

**Exit**: When heartbeats resume > 85% threshold

---

## Development

### Run Locally

```bash
cd tourni-discovery-service
mvn spring-boot:run

# Server starts on port 8761
# Dashboard: http://localhost:8761
```

### Dependencies

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

### Enable Eureka Server

```java
@SpringBootApplication
@EnableEurekaServer  // Single annotation enables server
public class TourniDiscoveryService {
    public static void main(String[] args) {
        SpringApplication.run(TourniDiscoveryService.class, args);
    }
}
```

---

## Monitoring

### Health Check

```bash
curl http://localhost:8761/actuator/health

# Response
{
  "status": "UP"
}
```

### Eureka REST API

```bash
# Get all registered applications
curl http://localhost:8761/eureka/apps -H "Accept: application/json" | jq

# Get specific application
curl http://localhost:8761/eureka/apps/TOURNI-GATEWAY -H "Accept: application/json" | jq

# Get instance info
curl http://localhost:8761/eureka/apps/TOURNI-GATEWAY/192.168.1.10:8080 \
  -H "Accept: application/json" | jq
```

---

## Production Considerations

### High Availability

**Problem**: Eureka Server is single point of failure

**Solution**: Run multiple Eureka Server instances

```yaml
# eureka-server-1
eureka:
  client:
    service-url:
      defaultZone: http://eureka-server-2:8761/eureka/,http://eureka-server-3:8761/eureka/

# eureka-server-2
eureka:
  client:
    service-url:
      defaultZone: http://eureka-server-1:8761/eureka/,http://eureka-server-3:8761/eureka/
```

**Result**: Eureka servers replicate registry between themselves

### DNS-Based Discovery (Alternative)

**For Kubernetes**: Use Kubernetes Service Discovery instead of Eureka
- CoreDNS provides service discovery
- Kubernetes Services provide load balancing
- Simpler for cloud-native deployments

---

## Comparison with Alternatives

| Feature | Eureka | Consul | Kubernetes DNS |
|---------|--------|--------|---------------|
| **Client-side LB** | ✅ Yes | ✅ Yes | ❌ Server-side |
| **Health Checks** | ✅ Heartbeat | ✅ HTTP/TCP/Script | ✅ Liveness/Readiness |
| **Dashboard** | ✅ Built-in | ✅ UI | ❌ Use K8s Dashboard |
| **Spring Integration** | ✅ Native | ⚠️ External | ✅ Spring Cloud K8s |
| **Self-Preservation** | ✅ Yes | ❌ No | N/A |
| **Complexity** | Low | Medium | High (requires K8s) |

**Why Eureka for this project**:
- Native Spring Cloud integration
- Simplest for local Docker Compose deployment
- Demonstrates microservices patterns
- For production K8s deployment, would use Kubernetes Service Discovery

---

## Future Enhancements

- **Zone Awareness**: Prefer instances in same availability zone
- **Metadata-Based Routing**: Route based on custom metadata (version, canary)
- **Metrics Collection**: Track registry size, heartbeat latency
- **Integration with Service Mesh**: Istio for advanced traffic management

---

[← Back to Main Documentation](../README.md)
