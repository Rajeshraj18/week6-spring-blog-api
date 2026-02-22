# Blog Management REST API

A comprehensive RESTful API for a blog management system built with Spring Boot 3.x. Provides CRUD operations for blog posts, categories, and comments with proper validation, error handling, and documentation.

## Project Overview
This project provides a robust backend solution for managing a blogging platform. 
**Goals:**
- Provide a fully functional REST API to manage Posts, Categories, and Comments.
- Demonstrate a production-ready application architecture using modern tools and practices.

**Objectives:**
- Model clear relational data using JPA entities with One-To-Many and Many-To-One mappings.
- Deliver secure, paginated, and strictly validated RESTful endpoints.
- Abstract database environments between development (H2) and production (PostgreSQL) using Spring Profiles.
- Generate comprehensive API documentation via OpenAPI/Swagger.

## Features
- Complete RESTful endpoints for Blog Posts, Categories, and Comments.
- Deep relational modeling natively tracking Entities mapping smoothly to DTOs.
- Exhaustive request validation (Jakarta constraints) and centralized global error handling.
- Optimized data retrieval using Spring Data Pagination and Sorting for list endpoints.
- Integrated Comprehensive SLF4J Logging across Controllers, Services and exception handlers.
- **Monitoring & Metrics**: Integrated Spring Boot Actuator exposing `/actuator/health`, `/actuator/metrics`, etc.
- Auto-generated interactive Swagger API Documentation.
- Pre-configured Postman collection ready for immediate integration testing.

## Setup Instructions
**Prerequisites:**
- Java 17 or higher
- Maven 3.6+
- PostgreSQL (if running in the `prod` profile)

**Step-by-step Installation and Configuration Guide:**

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd week6-spring-blog-api
   ```
2. **Configuration (application.properties):**
   - The application uses `application-dev.properties` by default, which configures an in-memory H2 database (`jdbc:h2:mem:blogdb`). No external database setup is required for local dev.
   - For production, configure the `application-prod.properties` with your PostgreSQL credentials.
3. **Launch the application with Maven:**
   ```bash
   mvn spring-boot:run
   ```
4. **Alternatively, build and run the executable JAR:**
   ```bash
   mvn clean package
   java -jar target/blog-api-0.0.1-SNAPSHOT.jar
   ```
5. **Access Points:**
   - **API Base URL:** `http://localhost:8080`
   - **Swagger UI (Documentation):** `http://localhost:8080/swagger-ui.html`
   - **Actuator Health Metrics:** `http://localhost:8080/actuator/health`

## Code Structure
The application adopts a layered monolithic architecture, ensuring a clean separation of concerns.

```
week6-spring-blog-api/
│── src/main/java/com/blogapi/
│   ├── BlogApiApplication.java   # Main application entry point
│   ├── config/                   # Swagger UI and general configuration
│   ├── controller/               # REST Controllers exposing HTTP endpoints mapped to services
│   ├── exception/                # Custom exceptions and @ControllerAdvice GlobalExceptionHandler
│   ├── model/        
│   │   ├── dto/                  # Data Transfer Objects for strict API request/response shaping
│   │   └── entity/               # JPA Entities mirroring database schemas
│   ├── repository/               # Spring Data JPA interfaces for database I/O
│   └── service/                  # Core Business logic operations and transaction management
│── src/main/resources/           # Environment property files (dev, prod)
│── src/test/java/com/blogapi/    # Unit and Integration test suites
│── docs/                         # Postman collections
└── pom.xml                       # Maven dependency configuration
```

## Visual Documentation
Screenshots demonstrating the functionality of the application, including endpoint successful responses, pagination, and Swagger UI interactions, are provided in the `/screenshots` directory.

> **Note:** Please refer to the `screenshots/` folder in the repository root to view demonstrations of:
> - Successful CRUD operations via Postman
> - Validation constraints triggering `400 Bad Request` responses
> - Swagger UI interface and automated documentation
> - Actuator health checks

## Technical Details

### Architecture
- **Multi-tier MVC Pattern:** Separates concerns into Web (Controllers), Business (Services), and Persistence (Repositories) layers for high maintainability.
- **Global Error Handling:** Implements an innovative `@ControllerAdvice` (`GlobalExceptionHandler.java`) to cleanly intercept Exceptions (like `ResourceNotFoundException` or `MethodArgumentNotValidException`), normalizing all errors down to a predictable `ApiResponse` JSON format regardless of where the failure occurred.

### Data Structures
- **DTO Projection:** Natively uses Data Transfer Objects (DTOs) heavily, mapping complex JPA relational graphs into flat JSON responses using the Builder pattern, preventing infinite recursion or lazy-loading serialization errors.
- **Pagination Models:** Leverages Spring Boot's internal `org.springframework.data.domain.Page` abstractions, converting `java.util.List` arrays into optimized chunks for database queries, vastly improving memory footprint and network I/O limits.

### Algorithms
- **Dynamic Sorting and Paging:** Under the hood, List endpoints use dynamic query formation (Hibernate Criteria API abstractions) based on incoming Request parameters (`sort=createdAt,desc`) to efficiently order database reads via algorithmic B-Tree index scanning rather than sorting data in Java memory.

## Testing Evidence
Rigorous automated testing ensures software durability, yielding 18 passing tests via Maven.

- **Unit & Integration Testing Strategy:** Validated directly inside the codebase covering Controllers through Services logic using JUnit 5 and Mockito.
```bash
# Execute the test suite
mvn clean test
```

### Examples of Test Cases and Validation
- **Controller Unit Tests (`PostControllerTest.java`):** 
  - Validates HTTP response codes (`201 Created`, `200 OK`, `204 No Content`).
  - Verifies that `MockMvc` successfully serializes and deserializes generic JSON objects to internal DTO structures.
  - *Example:* Testing `createPost()` checks if valid JSON inputs correctly map to `postService.createPost()` and return a modeled `$.title`.

- **Service Unit Tests (`CategoryServiceTest.java`):**
  - Uses strictly mock-injected Repositories (`@Mock CategoryRepository`) to prevent modifying active databases during testing.
  - *Example:* When `categoryRepository.findById()` is called, it correctly outputs an `Optional.empty()` response which throws a predictable `ResourceNotFoundException`, satisfying the exact exception validation pipeline expected.

- **Postman Support**: A dedicated JSON collection for Postman is located within `docs/postman_collection.json` which has mapped sample requests to ensure endpoints function correctly from external validation tools.

