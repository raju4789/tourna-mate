# TOURNA-MATE

## Overview

TOURNA-MATE is a comprehensive tournament management system designed to streamline the orchestration and execution of tournaments. With a suite of dedicated microservices, TOURNA-MATE ensures a seamless user experience. The system features a user-centric interface built with React, supports secure user authentication, and offers intuitive tournament management functionality. Additionally, TOURNA-MATE integrates advanced AI capabilities to provide smart insights, enhancing the strategic aspect of tournament planning.

## Architecture

The architecture diagram below illustrates how each microservice within TOURNA-MATE interacts to deliver a cohesive user experience:

![Tournamate_Design_F (1)](https://github.com/raju4789/tourna-mate/assets/9479811/6a9b1fef-e624-44a6-9c6b-e8f50a9fe05d)

## Database Design

<img width="623" alt="Screenshot 2567-05-30 at 10 20 41" src="https://github.com/raju4789/tourna-mate/assets/9479811/f062c36f-7f68-4826-a5fe-39e72e3bf287">


## Key Highlights

### Core Architecture
1. **Microservice Architecture**: Built using Java Spring Boot 3.x for the backend.
2. **Modern Frontend**: Developed with React JS, TypeScript, and Material UI with Vite.
3. **Secure Authentication & Authorization**: 
   - JWT tokens with embedded roles for stateless authentication
   - **Role-Based Access Control (RBAC)** with two-layer authorization:
     - Gateway-level route authorization (coarse-grained)
     - Service-level method authorization (fine-grained)
   - 98% faster than traditional service-based authorization
   - See [RBAC Documentation](docs/RBAC_IMPLEMENTATION.md) for details
4. **Service Discovery**: Utilizes Netflix Eureka server.
5. **API Gateway**: Managed by Spring Cloud Gateway.
6. **Database Operations**: Handled by Spring Data JPA with MySQL.
7. **Configuration Management**: Centralized using Spring Config Server with GitHub backend.
8. **Secret Management**: HashiCorp Vault for secure credential storage.
9. **API Documentation**: Provided by Swagger.

### Observability Stack
10. **Comprehensive Monitoring**: 
    - **Grafana**: Analytics and monitoring visualization dashboards
    - **Loki**: Centralized log aggregation
    - **Prometheus**: Metrics collection and alerting
    - **Tempo**: Distributed tracing with OpenTelemetry

### Production-Ready Features
11. **Multi-Environment Support**: Isolated configurations for Development, Staging, and Production
12. **Optimized Docker Images**: 
    - Reduced image sizes by ~25% (JDK → JRE transition)
    - Multi-stage builds for efficient compilation
    - Non-root users for enhanced security
    - Health checks for reliability
13. **Security Best Practices**:
    - Non-root containers
    - Secrets management with Vault
    - Security-enhanced Nginx configuration
    - No-new-privileges security option
14. **Performance Optimization**:
    - JVM container tuning (MaxRAMPercentage)
    - Optimized layer caching
    - Resource limits and reservations
    - Graceful shutdown handling

## User Interface Screenshots

![Screenshot from 2024-04-01 21-03-33](https://github.com/raju4789/tourna-mate/assets/9479811/ec8ad075-182a-443f-8455-c382791b893b)
![Screenshot from 2024-04-01 21-02-35](https://github.com/raju4789/tourna-mate/assets/9479811/5dd5c7fc-768e-4616-9824-9b2315bc8661)
![Screenshot from 2024-04-01 21-01-58](https://github.com/raju4789/tourna-mate/assets/9479811/8c7d8cba-e109-4893-9770-6a2544baf064)
![Screenshot from 2024-04-01 21-01-32](https://github.com/raju4789/tourna-mate/assets/9479811/406f78d1-26ea-4834-902d-5c9a04e5552a)
![Screenshot from 2024-04-01 21-00-54](https://github.com/raju4789/tourna-mate/assets/9479811/4e366d35-572f-4279-9c00-1b953a0a5342)

## Microservices

### TOURNI-UI

- A React application that leverages Hooks for state management and Material-UI for styling. It utilizes TypeScript for type safety and Axios for making HTTP requests.

### TOURNI-GATEWAY

- Routes incoming HTTP requests to the appropriate backend service, using Spring Cloud Gateway for intelligent routing and load balancing.

### TOURNI-CONFIG-SERVER

- Centralizes and manages external configurations for microservices, pulling from the TOURNI-CONFIG repository on GitHub.

### TOURNI-IDENTITY-SERVICE

- Handles user registration, login, and secure endpoint access with JWT tokens for stateless authentication.

### TOURNI-MANAGEMENT

- Facilitates various operations such as team and tournament addition, match result management, and points table management.

### TOURNI-AI (IN-PROGRESS)

- Enhances the tournament experience by providing strategic insights using OpenAI's GPT technology.

## Security & Authorization

### Role-Based Access Control (RBAC)

TOURNA-MATE implements a **production-grade RBAC system** with defense-in-depth:

#### **Available Roles**
- **ADMIN**: Full access to all resources and operations
- **USER**: Read access and limited write operations

#### **Authorization Layers**
1. **Gateway Layer** (Coarse-Grained)
   - Validates JWT tokens locally (no service calls)
   - Enforces route-level authorization
   - Propagates user context to downstream services
   - **Performance**: 1-2ms per request (98% faster than service-based validation)

2. **Service Layer** (Fine-Grained)
   - Method-level authorization using Spring Security
   - Custom annotations: `@RequiresUser`, `@RequiresAdmin`
   - Resource-level access control
   - Full user context available in business logic

#### **Quick Example**

```java
// User or Admin can view tournaments
@RequiresUser
@GetMapping("/tournaments")
public ResponseEntity<List<Tournament>> getAllTournaments() { ... }

// Only Admin can add match results
@RequiresAdmin
@PostMapping("/addMatchResult")
public ResponseEntity<String> addMatchResult(@RequestBody MatchRequest req) { ... }
```

#### **Documentation**
- 📚 [Complete RBAC Implementation Guide](docs/RBAC_IMPLEMENTATION.md)
- 🚀 [Quick Start Guide](docs/RBAC_QUICKSTART.md)
- ⚡ [Production Enhancements](docs/RBAC_ENHANCEMENTS.md) - Role hierarchy, multiple roles, token versioning
- 📊 [Architecture Diagrams](docs/RBAC_ARCHITECTURE_DIAGRAM.md)
- 📋 [Implementation Summary](docs/RBAC_SUMMARY.md)

---

## Observability

Our platform uses Grafana for analytics and monitoring visualization, Loki for log aggregation, Prometheus for system monitoring, and Tempo for distributed tracing to maintain a high level of performance and reliability.

## Prerequisites

- Docker & Docker Compose (v2.0+)
- 8GB+ RAM recommended for running all services
- 20GB+ disk space

## Quick Start

TOURNA-MATE supports three isolated environments: **Development**, **Staging**, and **Production**. Each environment runs on different ports to allow simultaneous operation.

### 🚀 Start Development Environment

```bash
./start-dev.sh
```

**Access URLs:**
- UI: http://localhost:8002
- API Gateway: http://localhost:8080
- Service Discovery: http://localhost:8761
- Grafana: http://localhost:3000
- Prometheus: http://localhost:9090

### 🧪 Start Staging Environment

```bash
./start-staging.sh
```

**Access URLs:**
- UI: http://localhost:9002
- API Gateway: http://localhost:9080
- Service Discovery: http://localhost:9761
- Grafana: http://localhost:3001
- Prometheus: http://localhost:9091

### 🔒 Start Production Environment

```bash
./start-prod.sh
```

**Access URLs:**
- UI: http://localhost:10002
- API Gateway: http://localhost:10080
- Service Discovery: http://localhost:10761
- Grafana: http://localhost:3002
- Prometheus: http://localhost:9092

> ⚠️ **Note**: Production startup requires confirmation and uses strong passwords. Edit `docker/secrets/.env.prod` before first run.

## Environment Configuration

### Port Allocation

| Service | Development | Staging | Production |
|---------|------------|---------|------------|
| UI | 8002 | 9002 | 10002 |
| API Gateway | 8080 | 9080 | 10080 |
| Service Discovery | 8761 | 9761 | 10761 |
| Config Server | 8888 | 9888 | 10888 |
| Identity Service | 8082 | 9082 | 10082 |
| Management Service | 8083 | 9083 | 10083 |
| AI Service | 8084 | 9084 | 10084 |
| MySQL | 3306 | 3307 | 3308 |
| Grafana | 3000 | 3001 | 3002 |
| Prometheus | 9090 | 9091 | 9092 |
| Tempo | 3200, 9411 | 3201, 9412 | 3202, 9413 |
| Loki | 3100 | 3101 | 3102 |
| Vault | 8200 | 8201 | 8202 |

### Environment Variables

Each environment uses its own `.env` file located in `docker/secrets/`:
- Development: `.env.dev`
- Staging: `.env.staging`
- Production: `.env.prod`

**Key Variables:**
```bash
# Database
MYSQL_ROOT_PASSWORD=<secure_password>
MYSQL_PASSWORD=<secure_password>

# Authentication
JWT_SECRET=<strong_secret_key>

# Monitoring
GRAFANA_PASSWORD=<admin_password>
ADMIN_PASSWORD=<admin_password>
```

## Docker Architecture

### Optimized Docker Images

All services use production-ready Docker images with:
- **Multi-stage builds**: Separate build and runtime stages
- **Minimal base images**: Eclipse Temurin JRE (not JDK) for Java services
- **Non-root users**: Enhanced security with dedicated user accounts
- **Health checks**: Automatic health monitoring for all services
- **Resource limits**: Configured CPU and memory constraints

## Management Commands

### View Service Status

```bash
# Development
cd docker/dev && docker-compose ps

# Staging
cd docker/staging && docker-compose ps

# Production
cd docker/prod && docker-compose ps
```

### View Logs

```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f tourni-gateway-dev
```

### Stop Environment

```bash
# Development
cd docker/dev && docker-compose down

# Staging
cd docker/staging && docker-compose down

# Production
cd docker/prod && docker-compose down

# Remove volumes (destructive)
docker-compose down -v
```

## Monitoring & Observability

### Grafana Dashboards

Access Grafana at:
- Dev: http://localhost:3000
- Staging: http://localhost:3001
- Prod: http://localhost:3002

**Default Credentials:**
- Username: `admin`
- Password: Check `docker/secrets/.env.<environment>` for `GRAFANA_PASSWORD`

### Metrics & Tracing

- **Prometheus**: Metrics collection and querying
- **Tempo**: Distributed tracing visualization
- **Loki**: Log aggregation and search
- **Spring Boot Actuator**: Application health and metrics endpoints

## Security

### Credentials Management

All sensitive credentials are stored in:
1. **Vault**: Runtime secrets (MySQL passwords, JWT secrets)
2. **Environment Files**: Configuration variables (in `docker/secrets/`)

### Production Security Checklist

- [ ] Change all default passwords in `.env.prod`
- [ ] Use strong JWT secrets (min 64 characters)
- [ ] Enable SSL/TLS for external access
- [ ] Configure firewall rules
- [ ] Use secrets management service (AWS Secrets Manager, Azure Key Vault)
- [ ] Regular security audits and updates

## Troubleshooting

### Services Not Starting

```bash
# Check service logs
docker-compose logs <service-name>

# Restart specific service
docker-compose restart <service-name>

# Rebuild and restart
docker-compose up -d --build <service-name>
```

### Database Connection Issues

```bash
# Verify MySQL is healthy
docker-compose ps tourni-mysql-dev

# Check MySQL logs
docker-compose logs tourni-mysql-dev

# Verify credentials in .env file
cat docker/secrets/.env.dev | grep MYSQL
```

### Port Already in Use

If ports are already occupied, either:
1. Stop the conflicting service
2. Modify ports in the docker-compose file
3. Use a different environment (dev/staging/prod)

## Project Structure

```
tourna-mate/
├── tourni-ui/                    # React frontend application
├── tourni-gateway/               # API Gateway service
├── tourni-config-service/        # Configuration server
├── tourni-discovery-service/     # Eureka service discovery
├── tourni-identity-service/      # Authentication service
├── tourni-management/            # Tournament management service
├── tourni-ai/                    # AI insights service
├── docker/
│   ├── dev/
│   │   └── docker-compose.yml    # Development environment
│   ├── staging/
│   │   └── docker-compose.yml    # Staging environment
│   ├── prod/
│   │   └── docker-compose.yml    # Production environment
│   ├── common-config.yml         # Shared configuration
│   ├── secrets/
│   │   ├── .env.dev             # Development secrets
│   │   ├── .env.staging         # Staging secrets
│   │   └── .env.prod            # Production secrets
│   ├── scripts/
│   │   └── init-vault.sh        # Vault initialization
│   └── observability/
│       ├── grafana/             # Grafana configs
│       ├── prometheus/          # Prometheus configs
│       ├── tempo/               # Tempo configs
│       └── loki/                # Loki configs
├── start-dev.sh                 # Start development environment
├── start-staging.sh             # Start staging environment
└── start-prod.sh                # Start production environment
```

## Recent Improvements (2024)

### Docker Optimization
- ✅ Reduced Docker image sizes by ~25% (699MB total savings)
- ✅ Switched from JDK to JRE for runtime images
- ✅ Implemented multi-stage builds for all services
- ✅ Added comprehensive health checks
- ✅ Optimized layer caching for faster builds

### Security Enhancements
- ✅ All services run as non-root users
- ✅ Integrated HashiCorp Vault for secrets management
- ✅ Added security options (no-new-privileges)
- ✅ Environment-specific credential isolation

### Environment Management
- ✅ Separated configurations for Dev, Staging, and Production
- ✅ Port isolation for simultaneous environment operation
- ✅ Simplified startup scripts for each environment
- ✅ Consistent environment variable management

### Observability
- ✅ Full observability stack integration (Grafana, Prometheus, Loki, Tempo)
- ✅ Distributed tracing with OpenTelemetry
- ✅ Centralized logging
- ✅ Real-time metrics and alerting

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contributing
We welcome contributions to TOURNA-MATE! If you have suggestions for improvements or have found a bug,
please open an issue or submit a pull request. For major changes, please open an issue first to discuss what you would like to change.

### Steps to Contribute
1. Fork the repository.
2. Create a new branch (git checkout -b feature-branch).
3. Make your changes.
4. Commit your changes (git commit -m 'Add some feature').
5. Push to the branch (git push origin feature-branch).
6. Open a pull request.
