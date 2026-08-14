# Coffee Shop Microservices

A microservices-based backend system for a coffee shop/bakery, built with Spring Boot and Spring Cloud — featuring independent services for menu, users, and orders, JWT-secured API Gateway, service discovery, centralized configuration, and full Docker deployment.

## Architecture

The system follows a microservices architecture with independent, loosely-coupled services communicating over REST and Feign clients, registered with a central service discovery server, and configured through a centralized config server.

## Technologies

- Java 21, Spring Boot
- Spring Cloud (Gateway, Eureka, Config Server, OpenFeign)
- Spring Security + JWT
- Spring Data JPA, MySQL
- Docker & Docker Compose
- Lombok

## Microservices

- **menu-service**
- **user-service**
- **order-service**
- **api-gateway**
- **eureka-server**
- **config-server**

## Features

- Synchronous inter-service communication via Feign clients (product availability checks, user validation, loyalty points updates)
- JWT authentication at the Gateway level with role-based authorization (ADMIN/USER)
- Order status transition validation and error handling (e.g., 409 Conflict for foreign key conflicts)
- Each service has an independent database, following the microservices data ownership pattern
- Full Docker deployment with Docker Compose
