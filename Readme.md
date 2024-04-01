# TOURNA-MATE

## Overview

TOURNA-MATE is an all-encompassing tournament management system tailored for effortless orchestration and execution of tournaments. With a constellation of dedicated microservices, TOURNA-MATE ensures a seamless user experience. The system flaunts a user-centric interface crafted using React, supports secure user authentication, and provides intuitive tournament management functionality. Additionally, TOURNA-MATE incorporates state-of-the-art AI capabilities to deliver smart insights, augmenting the strategic aspect of tournament planning.

## Architecture

The architecture diagram below provides a visual representation of how each microservice within TOURNA-MATE interacts to deliver a cohesive user experience:

![Tournamate_Design_F (1)](https://github.com/raju4789/tourna-mate/assets/9479811/6a9b1fef-e624-44a6-9c6b-e8f50a9fe05d)

## Key Highlights

1. Built with Microservice architecture pattern using Java Spring Boot 3.x for backend.
2. Used React JS , Typescript and Material UI for front end development.
3. Implemented authentication using JWT token and Spring Security.
4. Netflix Eureka server for Service discovery.
5. Spring cloud Gateway for API gateway.
6. Spring Data JPA for database operations.
7. Centralize and manage external configurations for microservices, by using Spring Config Server.
8. Our platform uses Grafana for analytics and monitoring visualization, Loki for log aggregation, Prometheus for system monitoring, and Tempo for distributed tracing to maintain a high level of performance and reliability.
9. Docker for building microservices.
10. Swagger for documentation.



## Microservices

### TOURNI-UI :

- A React application that leverages Hooks for state management and Material-UI for styling. It utilizes TypeScript for type safety and Axios for making HTTP requests.


### TOURNI-GATEWAY :

- Routes incoming HTTP requests to the appropriate backend service, using Spring Cloud Gateway for intelligent routing and load balancing.


### TOURNI-CONFIG-SERVER :

- Centralizes and manages external configurations for microservices, pulling from the TOURNI-CONFIG repository on GitHub.


### TOURNI-IDENTITY-SERVICE :

- Handles user registration, login, and secure endpoint access with JWT tokens for stateless authentication.


### TOURNI-MANAGEMENT :

- Facilitates various operations such as team and tournament addition, match result management, and points table management.


### TOURNI-AI (IN-PROGRESS) :

-  Enhances the tournament experience by providing strategic insights using OpenAI's GPT technology.


## Observability

Our platform uses Grafana for analytics and monitoring visualization, Loki for log aggregation, Prometheus for system monitoring, and Tempo for distributed tracing to maintain a high level of performance and reliability.


### Prerequisites

- Docker & Docker Compose

### Building the Micro Services in local :

```

cd docker

docker-compose up -d

```

