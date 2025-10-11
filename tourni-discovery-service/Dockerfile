# ========== Build Stage ==========
FROM maven:3.9-eclipse-temurin-17 AS build

ARG SERVICE_DIR=tourni-discovery-service

WORKDIR /build

# Copy parent POM for dependency management
COPY pom.xml .

# Copy only this service's POM for better layer caching
COPY ${SERVICE_DIR}/pom.xml ${SERVICE_DIR}/pom.xml

# Download dependencies (this layer will be cached)
WORKDIR /build/${SERVICE_DIR}
RUN mvn dependency:go-offline -B || true

# Copy only this service's source code
COPY ${SERVICE_DIR}/src ./src

# Build the service
RUN mvn clean package -DskipTests -B && \
    mv target/*.jar target/app.jar

# ========== Runtime Stage ==========
FROM eclipse-temurin:17-jre

# Add metadata labels
LABEL maintainer="tourna-mate-team" \
      version="1.0" \
      description="Eureka Discovery Service" \
      java.version="17"

# Install dumb-init for proper signal handling and curl for health checks
RUN apt-get update && \
    apt-get install -y --no-install-recommends dumb-init curl && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Create non-root user for security
RUN groupadd -g 1001 appuser && \
    useradd -r -u 1001 -g appuser appuser

# Copy JAR from build stage with proper ownership
ARG SERVICE_DIR=tourni-discovery-service
COPY --from=build --chown=appuser:appuser /build/${SERVICE_DIR}/target/app.jar app.jar

# Switch to non-root user
USER appuser

EXPOSE 8080

# Health check using Spring Boot Actuator
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Use dumb-init to handle signals properly for graceful shutdown
ENTRYPOINT ["dumb-init", "--"]

# JVM tuning for containerized environments
CMD ["java", \
     "-XX:+UseContainerSupport", \
     "-XX:MaxRAMPercentage=75.0", \
     "-XX:InitialRAMPercentage=50.0", \
     "-XX:+HeapDumpOnOutOfMemoryError", \
     "-XX:HeapDumpPath=/app/heapdump.hprof", \
     "-XX:+ExitOnOutOfMemoryError", \
     "-Djava.security.egd=file:/dev/./urandom", \
     "-Dserver.shutdown=graceful", \
     "-jar", "app.jar"]