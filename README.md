# TOURNA-MATE

## Overview

TOURNA-MATE is a comprehensive tournament management system designed to streamline the orchestration and execution of tournaments. With a suite of dedicated microservices, TOURNA-MATE ensures a seamless user experience. The system features a user-centric interface built with React, supports secure user authentication, and offers intuitive tournament management functionality. Additionally, TOURNA-MATE integrates advanced AI capabilities to provide smart insights, enhancing the strategic aspect of tournament planning.

## Architecture

The architecture diagram below illustrates how each microservice within TOURNA-MATE interacts to deliver a cohesive user experience:

![Tournamate_Design_F (1)](https://github.com/raju4789/tourna-mate/assets/9479811/6a9b1fef-e624-44a6-9c6b-e8f50a9fe05d)

## Database Design

<img width="623" alt="Screenshot 2567-05-30 at 10 20 41" src="https://github.com/raju4789/tourna-mate/assets/9479811/f062c36f-7f68-4826-a5fe-39e72e3bf287">


## Key Highlights

1. **Microservice Architecture**: Built using Java Spring Boot 3.x for the backend.
2. **Modern Frontend**: Developed with React JS, TypeScript, and Material UI.
3. **Secure Authentication**: Implemented using JWT tokens and Spring Security with role-based authorization.
4. **Service Discovery**: Utilizes Netflix Eureka server.
5. **API Gateway**: Managed by Spring Cloud Gateway.
6. **Database Operations**: Handled by Spring Data JPA.
7. **Configuration Management**: Centralized using Spring Config Server.
8. **Caching**: Enabled caching to improve performance
9. **Observability**: 
   - **Grafana**: For analytics and monitoring visualization.
   - **Loki**: For log aggregation.
   - **Prometheus**: For system monitoring.
   - **Tempo**: For distributed tracing.
10. **Containerization**: Docker is used for building microservices.
11. **API Documentation**: Provided by Swagger.

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

## Observability

Our platform uses Grafana for analytics and monitoring visualization, Loki for log aggregation, Prometheus for system monitoring, and Tempo for distributed tracing to maintain a high level of performance and reliability.

## Prerequisites

- Docker & Docker Compose

## Building the Microservices Locally

```bash
cd docker
docker-compose up -d

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
