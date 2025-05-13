# Experiment Service

## Overview

The Experiment Service is a core component of the SmartFox system, responsible for managing experiments. It provides functionalities for creating, updating, running, and analyzing experiments.

## Features

- Experiment management (CRUD operations)
- Experiment execution
- Result collection and analysis
- Sharing experiments with other users

## Architecture

The service follows a layered architecture:

- **Controller Layer**: Handles HTTP requests and responses
- **Service Layer**: Contains business logic
- **Repository Layer**: Interfaces with the database
- **Model Layer**: Defines domain entities
- **DTO Layer**: Data transfer objects for API communication
- **Executor Component**: Handles experiment execution
- **Analyzer Component**: Analyzes experiment results

## API Endpoints

### Experiment Management

- `POST /api/experiments` - Create a new experiment
- `GET /api/experiments/{id}` - Get experiment by ID
- `PUT /api/experiments/{id}` - Update experiment
- `DELETE /api/experiments/{id}` - Delete experiment
- `GET /api/experiments/my` - Get current user's experiments
- `GET /api/experiments/search` - Search experiments
- `POST /api/experiments/{id}/share` - Share experiment
- `GET /api/experiments/published` - Get published experiments

### Experiment Execution and Results

- `POST /api/experiments/{experimentId}/run` - Run an experiment
- `DELETE /api/experiments/executions/{executionId}` - Cancel execution
- `GET /api/experiments/{experimentId}/results` - Get experiment results
- `GET /api/experiments/results/{id}` - Get result by ID
- `GET /api/experiments/executions/{executionId}` - Get result by execution ID
- `GET /api/experiments/results/{id}/analysis` - Analyze result
- `GET /api/experiments/results/{id}/summary` - Get result summary

## Getting Started

### Prerequisites

- Java 21
- Maven 3.8+
- MySQL 8.0+

### Running the Service

1. Configure database connection in `application.properties`
2. Build the project: `mvn clean package`
3. Run the service: `java -jar target/experiment-service-0.0.1-SNAPSHOT.jar`

### API Documentation

Once the service is running, the API documentation is available at:

- Swagger UI: `http://localhost:8084/swagger-ui.html`
- OpenAPI Docs: `http://localhost:8084/api-docs`

## Development

### Technology Stack

- Spring Boot 3.4.5
- Spring Data JPA
- Spring Security
- MySQL
- Swagger/OpenAPI
- JUnit 5 for testing

### Testing

Run the tests with:

```bash
mvn test
```
