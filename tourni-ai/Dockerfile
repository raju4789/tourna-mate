FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /build

# Copy the entire multi-module project into the build context
COPY ../../ .

# Set build argument for the service directory
ARG SERVICE_DIR

WORKDIR /build/${SERVICE_DIR}
RUN mvn clean package -DskipTests

# --------- Stage 2: Create the runtime image ---------
FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y curl && apt-get clean && rm -rf /var/lib/apt/lists/*
WORKDIR /app

# Use the same build argument to copy the correct JAR
ARG SERVICE_DIR
COPY --from=build /build/${SERVICE_DIR}/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]