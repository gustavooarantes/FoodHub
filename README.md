# FoodHub - Restaurant Management API
## Developer: Gustavo Arantes

A robust RESTful API to simulate a restaurant's order and management system, incorporating a modern architecture with microservices, messaging, caching, and a full CI/CD pipeline.

---
## Project Structure
<pre>
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── gustavooarantes
    │   │           └── foodhub
    │   │               ├── config
    │   │               ├── consumer
    │   │               ├── controller
    │   │               ├── domain
    │   │               │   └── enums
    │   │               ├── dto
    │   │               ├── repository
    │   │               ├── security
    │   │               └── service
    │   │                   └── notificacao
    │   └── resources
    └── test
        └── java
            └── com
                └── gustavooarantes
                    └── foodhub
                        ├── controller
                        └── service
</pre>
---

## Features

-   **User Management & Security (RBAC)**:
    -   Role-based access control (`ROLE_ADMIN`, `ROLE_CHEF`, `ROLE_CLIENT`).
    -   Secure authentication via **JWT (JSON Web Tokens)** with endpoints for registration and login.
-   **Dynamic Menu**: Full CRUD operations to manage menu products.
-   **Order Management System**:
    -   Authenticated clients can create complex orders with multiple items.
    -   Endpoints for admins and clients to view and manage order history.
    -   Admins/Chefs can update an order's status (e.g., "In Preparation", "Ready").
-   **Asynchronous Processing with RabbitMQ**:
    -   Newly created orders are sent to a queue for **simulated payment processing**, decoupling the API response.
    -   Status updates trigger events to a **notification queue**, informing clients asynchronously.
-   **Performance Caching with Redis**:
    -   Menu queries (`/api/produtos`) are cached to reduce database load and dramatically increase response speed.
-   **Design Pattern (Factory)**:
    -   The notification system uses the **Factory Pattern** to create different types of "notifiers" (`Email`, `SMS`), making the code more flexible and extensible.
-   **API Documentation**: Interactive and automatic API documentation generated with **Swagger (OpenAPI)**.
-   **Automated Testing**: Unit testing coverage (with Mockito) for business logic and integration testing for controllers.
-   **DevOps**:
    -   **Dockerfile** with a multi-stage build to create an optimized application image.
    -   **Kubernetes** manifests to orchestrate the deployment of the entire service architecture.

---

## Tech Stack

-   **Backend**: Java 17, Spring Boot 3
-   **Data Persistence**: Spring Data JPA, PostgreSQL
-   **Security**: Spring Security, JSON Web Token (JWT)
-   **Messaging**: RabbitMQ
-   **Caching**: Redis
-   **Containerization**: Docker
-   **Orchestration**: Kubernetes (Minikube)
-   **API Documentation**: SpringDoc (Swagger/OpenAPI)
-   **Testing**: JUnit 5 & Mockito
-   **Build Tool**: Maven
-   **Design Patterns**: Factory

---

## API Endpoints Overview

### Authentication (`/api/auth`)

-   `POST /register`: Register a new user (`ROLE_CLIENT`).
-   `POST /login`: Authenticates a user and returns a JWT.

### Products (`/api/produtos`)

-   `GET /`: List all products (Public).
-   `GET /{id}`: Get a product by ID (Public).
-   `POST /`: Create a new product (Requires `ROLE_ADMIN`).
-   `PUT /{id}`: Update an existing product (Requires `ROLE_ADMIN`).
-   `DELETE /{id}`: Delete a product (Requires `ROLE_ADMIN`).

### Orders (`/api/pedidos`)

-   `POST /`: Client creates a new order (Requires `ROLE_CLIENT`).
-   `GET /`: List all orders in the system (Requires `ROLE_ADMIN` or `ROLE_CHEF`).
-   `GET /meus-pedidos`: List the authenticated client's orders (Requires `ROLE_CLIENT`).
-   `PATCH /{id}/status`: Update an order's status (Requires `ROLE_ADMIN` or `ROLE_CHEF`).
