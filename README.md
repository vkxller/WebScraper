# 🛒 WebScraper

<div align="center">

![Java](https://img.shields.io/badge/Java-21-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![OpenAPI / Swagger](https://img.shields.io/badge/OpenAPI-Swagger_UI-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)
![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Jsoup](https://img.shields.io/badge/Jsoup-HTML%20Parser-5A29E4?style=for-the-badge)
![TypeScript](https://img.shields.io/badge/TypeScript-Frontend-3178C6?style=for-the-badge&logo=typescript&logoColor=white)
![Vite](https://img.shields.io/badge/Vite-Frontend%20Build-646CFF?style=for-the-badge&logo=vite&logoColor=white)
![JUnit 5](https://img.shields.io/badge/JUnit-5-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![Mockito](https://img.shields.io/badge/Mockito-Testing-78A641?style=for-the-badge)
![JaCoCo](https://img.shields.io/badge/JaCoCo-Coverage-brightgreen?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)

</div>

---

## 📖 Overview

**WebScraper** is an enterprise-grade Java 21 microservice designed to extract, persist, and visualize product information from Falabella Chile in real time.


The project is built on **Clean Architecture** and **Domain-Driven Design (DDD)** principles, keeping business domain logic strictly independent from frameworks, persistence technologies, and external infrastructure.

Starting with **Milestone 4**, the backend is powered by **Spring Boot 3**, featuring:
- **Semantic REST API**: Full CRUD and scraping endpoints (`GET`, `POST`, `DELETE`).
- **Centralized Error Handling**: `@RestControllerAdvice` interceptor returning unified JSON error payloads (`ErrorResponseDto`).
- **Real Persistence & Virtualization**: PostgreSQL 16 database orchestrated via `docker-compose.yml` with Spring Data JPA and Hibernate ORM.
- **OpenAPI 3 / Swagger-UI**: Interactive API documentation restricted hermetically to the development profile (`dev`) and locked in production (`prod`).
- **Universal Live Search & Image Extraction**: Search any product across all of Falabella Chile and view responsive product images.
- **TypeScript + Vite Web Frontend**: Reactive client interface with real-time search, image rendering, and state management.

The current implementation extracts and persists:

- Unique product identifier (`id`)
- Store name (`store`)
- Product name (`name`)
- Current price (`price`)
- Previous price (`previousPrice`), when available
- Discount percentage (`discount`), when available
- Product source URL (`sourceUrl`), when available
- Product image URL (`imageUrl`), when available

---

## ✨ Features

<table>
  <tr>
    <td>🌐</td>
    <td><strong>HTML Download & Parsing</strong></td>
    <td>Retrieves web pages via Jsoup with custom browser headers, timeout resilience, and image resolution.</td>
  </tr>
  <tr>
    <td>🔍</td>
    <td><strong>Universal Live Search</strong></td>
    <td>Queries any keyword across Falabella Chile in real time (e.g., sneakers, laptops, toys, phones).</td>
  </tr>
  <tr>
    <td>🧱</td>
    <td><strong>Clean Architecture & DDD</strong></td>
    <td>Pure domain model with Aggregate Root Entity (`Product`) and self-validating Value Objects (`record`).</td>
  </tr>
  <tr>
    <td>💾</td>
    <td><strong>PostgreSQL & Spring Data JPA</strong></td>
    <td>Persistent relational storage with JPA entity mapping, automated migrations, and transactions.</td>
  </tr>
  <tr>
    <td>🐳</td>
    <td><strong>Docker Compose Virtualization</strong></td>
    <td>Pre-configured PostgreSQL 16 Alpine container with persistent volume and automatic healthchecks.</td>
  </tr>
  <tr>
    <td>🔌</td>
    <td><strong>Semantic REST Endpoints</strong></td>
    <td>RESTful routes for querying, scraping, retrieving by ID, and deleting products.</td>
  </tr>
  <tr>
    <td>🛡️</td>
    <td><strong>Centralized Error Interceptor</strong></td>
    <td><code>@RestControllerAdvice</code> capturing domain and infrastructure exceptions with unified JSON responses.</td>
  </tr>
  <tr>
    <td>📑</td>
    <td><strong>Swagger UI & OpenAPI 3</strong></td>
    <td>Interactive API documentation enabled in <code>dev</code> profile and strictly blocked in <code>prod</code>.</td>
  </tr>
  <tr>
    <td>💻</td>
    <td><strong>TypeScript + Vite Web UI</strong></td>
    <td>Fast, reactive frontend consuming the REST API with dynamic catalog rendering.</td>
  </tr>
  <tr>
    <td>🧪</td>
    <td><strong>Automated Testing & JaCoCo</strong></td>
    <td>178 unit and integration tests with JUnit 5, Mockito, MockMvc, and 100% domain coverage.</td>
  </tr>
</table>

---

## 🛠️ Technologies

| Technology | Purpose |
|---|---|
| Java 21 | Core programming language (LTS) |
| Spring Boot 3.4+ | Microservice framework and dependency injection |
| Spring Data JPA / Hibernate | Object-Relational Mapping (ORM) and persistence |
| PostgreSQL 16 | Relational database engine |
| Docker & Docker Compose | Container virtualization for local database |
| Springdoc OpenAPI / Swagger UI | OpenAPI 3 specification and interactive documentation |
| Jsoup | HTML downloading and DOM selector parsing |
| JUnit 5 | Unit and integration testing |
| Mockito & MockMvc | Test doubles, mocking, and REST endpoint testing |
| H2 Database | Fast, isolated in-memory database for automated tests |
| JaCoCo | Automated code coverage auditing and enforcement |
| TypeScript | Strongly-typed frontend logic in strict mode |
| Vite | Frontend development server and production bundler |
| Maven 3.9+ | Build and lifecycle management |

---

## 📁 Project Structure

```text
src
├── main
│   ├── java
│   │   └── org.diegoreyes.webscraper
│   │       ├── WebScraperApplication.java            // @SpringBootApplication Entry Point
│   │       │
│   │       ├── application
│   │       │   ├── usecase
│   │       │   │   ├── DeleteProductByIdUseCase.java
│   │       │   │   ├── GetAllProductsUseCase.java
│   │       │   │   ├── GetProductByIdUseCase.java
│   │       │   │   └── ScrapeAndSaveProductsUseCase.java
│   │       │   └── ProductScraperService.java
│   │       │
│   │       ├── domain
│   │       │   ├── exception
│   │       │   │   ├── InvalidProductException.java
│   │       │   │   └── ProductNotFoundException.java
│   │       │   ├── model
│   │       │   │   └── Product.java                  // DDD Aggregate Root Entity
│   │       │   ├── repository
│   │       │   │   └── ProductRepository.java        // Domain Repository Interface
│   │       │   └── valueobject
│   │       │       ├── Discount.java
│   │       │       ├── Price.java
│   │       │       ├── ProductId.java
│   │       │       ├── ProductName.java
│   │       │       ├── ProductUrl.java
│   │       │       └── StoreName.java
│   │       │
│   │       ├── infrastructure
│   │       │   ├── client
│   │       │   │   └── JsoupHtmlClient.java
│   │       │   ├── configuration
│   │       │   │   ├── BeanConfiguration.java        // Spring Bean Wiring
│   │       │   │   └── OpenApiConfiguration.java     // @Profile("dev") Swagger Config
│   │       │   ├── parser
│   │       │   │   └── FalabellaProductParser.java
│   │       │   ├── persistence
│   │       │   │   ├── entity
│   │       │   │   │   └── ProductJpaEntity.java     // JPA @Entity & @Table
│   │       │   │   ├── mapper
│   │       │   │   │   └── ProductJpaMapper.java     // Domain <-> JPA Entity Mapper
│   │       │   │   ├── repository
│   │       │   │   │   └── SpringDataProductRepository.java // JpaRepository
│   │       │   │   └── PostgreSqlProductRepository.java     // Implements ProductRepository
│   │       │   ├── repository
│   │       │   │   └── InMemoryProductRepository.java
│   │       │   └── web
│   │       │       ├── controller
│   │       │       │   └── ProductRestController.java   // Semantic REST Controller
│   │       │       ├── dto
│   │       │       │   ├── ErrorResponseDto.java        // Unified Error JSON
│   │       │       │   ├── ProductResponseDto.java      // Product JSON Response
│   │       │       │   └── ScrapeRequestDto.java        // Scrape Request Payload
│   │       │       └── exception
│   │       │           └── GlobalExceptionHandler.java  // Centralized @RestControllerAdvice
│   │       │
│   │       └── port
│   │           ├── HtmlClient.java
│   │           └── ProductParser.java
│   │
│   └── resources
│       ├── application.yml                           // Default Configuration (dev profile active)
│       ├── application-dev.yml                       // Dev Profile (PostgreSQL + Swagger Enabled)
│       └── application-prod.yml                      // Prod Profile (PostgreSQL + Swagger Disabled)
│
└── test
    ├── java
    │   └── org.diegoreyes.webscraper
    │       ├── application
    │       │   ├── usecase
    │       │   │   ├── DeleteProductByIdUseCaseTest.java
    │       │   │   ├── GetAllProductsUseCaseTest.java
    │       │   │   ├── GetProductByIdUseCaseTest.java
    │       │   │   └── ScrapeAndSaveProductsUseCaseTest.java
    │       │   └── ProductScraperServiceTest.java
    │       ├── domain
    │       │   ├── exception
    │       │   │   ├── InvalidProductExceptionTest.java
    │       │   │   └── ProductNotFoundExceptionTest.java
    │       │   ├── model
    │       │   │   └── ProductTest.java
    │       │   └── valueobject
    │       │       ├── DiscountTest.java
    │       │       ├── PriceTest.java
    │       │       ├── ProductIdTest.java
    │       │       ├── ProductNameTest.java
    │       │       ├── ProductUrlTest.java
    │       │       └── StoreNameTest.java
    │       └── infrastructure
    │           ├── client
    │           │   └── JsoupHtmlClientTest.java
    │           ├── configuration
    │           │   ├── BeanConfigurationTest.java
    │           │   └── OpenApiConfigurationTest.java
    │           ├── parser
    │           │   └── FalabellaProductParserTest.java
    │           ├── persistence
    │           │   ├── mapper
    │           │   │   └── ProductJpaMapperTest.java
    │           │   └── PostgreSqlProductRepositoryTest.java
    │           ├── repository
    │           │   └── InMemoryProductRepositoryTest.java
    │           └── web
    │               ├── controller
    │               │   └── ProductRestControllerTest.java
    │               └── exception
    │                   └── GlobalExceptionHandlerTest.java
    │
    └── resources
        └── application.yml                           // In-memory H2 Database for fast isolated tests

docker-compose.yml                                    // PostgreSQL 16 Container Orchestration

frontend
├── src
│   ├── components
│   │   └── ProductCard.ts
│   ├── models
│   │   └── product.ts
│   ├── main.ts
│   └── style.css
├── index.html
├── package.json
└── vite.config.ts
```

---

## 🏛️ Clean Architecture & DDD Flow

The application isolates business invariants from delivery mechanisms and data storage:

```text
┌────────────────────────────────────────────────────────┐
│               Delivery / Presentation                  │
│       ProductRestController (Spring Web MVC)           │
│       GlobalExceptionHandler (@RestControllerAdvice)   │
│       Swagger UI (OpenAPI 3 / Dev Profile Only)        │
└───────────────────────────┬────────────────────────────┘
                            │
                            ▼
┌────────────────────────────────────────────────────────┐
│                   Application Layer                    │
│   ScrapeAndSaveProductsUseCase / GetAllProductsUseCase │
│   GetProductByIdUseCase / DeleteProductByIdUseCase     │
│                 ProductScraperService                  │
└───────────────┬────────────────────────┬───────────────┘
                │                        │
                ▼                        ▼
       ┌────────────────┐       ┌─────────────────┐       ┌───────────────────┐
       │   HtmlClient   │       │  ProductParser  │       │ ProductRepository │
       │      Port      │       │      Port       │       │  Domain Contract  │
       └───────┬────────┘       └────────┬────────┘       └─────────┬─────────┘
               │                         │                          │
               ▼                         ▼                          ▼
     ┌───────────────────┐     ┌──────────────────────────┐ ┌───────────────────────┐
     │ JsoupHtmlClient   │     │ FalabellaProductParser   │ │PostgreSqlProductRepo..│
     │  Infrastructure   │     │      Infrastructure      │ │    Infrastructure     │
     └───────────────────┘     └────────────┬─────────────┘ └───────────┬───────────┘
                                            │                           │
                                            ▼                           ▼
                                ┌───────────────────────┐   ┌───────────────────────┐
                                │     Product Entity    │   │ SpringDataProductRepo │
                                │  Value Objects (DDD)  │   │     (PostgreSQL)      │
                                │     Domain Core       │   │    Infrastructure     │
                                └───────────────────────┘   └───────────────────────┘
```

---

## 🔌 REST API Specification

### Endpoints

| Verb | Path | Description | Status Codes |
|---|---|---|:---:|
| `GET` | `/api/products` | Retrieve all products or search live (`?search=zapatillas`) | `200 OK`, `500` |
| `GET` | `/api/products/{id}` | Retrieve a single product by UUID identifier | `200 OK`, `400`, `404` |
| `POST` | `/api/products/scrape` | Trigger a scraping operation with custom keyword/URL | `201 Created`, `400`, `502` |
| `DELETE` | `/api/products/{id}` | Delete a single product by UUID identifier | `204 No Content`, `404` |
| `DELETE` | `/api/products` | Clear all stored products | `204 No Content` |

### Unified Error JSON (`ErrorResponseDto`)

All domain and technical exceptions are captured by `@RestControllerAdvice` and transformed into consistent JSON responses:

```json
{
  "timestamp": "2026-08-16T23:45:00",
  "status": 404,
  "error": "Not Found",
  "message": "Product with ID 550e8400-e29b-41d4-a716-446655440000 was not found",
  "path": "/api/products/550e8400-e29b-41d4-a716-446655440000"
}
```

---

## 📑 OpenAPI Documentation & Profile Segregation

- **Development Profile (`dev`)**:
  - Swagger UI is fully enabled and accessible at:
    👉 **`http://localhost:8080/swagger-ui.html`**
  - OpenAPI 3 JSON docs available at: **`http://localhost:8080/v3/api-docs`**
- **Production Profile (`prod`)**:
  - Swagger UI and `/v3/api-docs` endpoints are **hermetically disabled** to protect microservice security.

To start with the production profile:
```bash
mvn spring-boot:run "-Dspring-boot.run.profiles=prod"
```

---

## 🐳 Virtualization with Docker Compose

To start the local PostgreSQL 16 database:

```bash
docker compose up -d
```

To stop the container:

```bash
docker compose down
```

### PostgreSQL Credentials:
- **Host:** `localhost:5432`
- **Database:** `webscraper_db`
- **User:** `postgres`
- **Password:** `postgrespassword`

---

## 🚀 Running the Project

### 1. Start PostgreSQL (Docker)

```bash
docker compose up -d
```

### 2. Compile and Verify Tests & JaCoCo Coverage

```bash
mvn clean verify
```

### 3. Run the Spring Boot Microservice

```bash
mvn spring-boot:run
```
> The API will start at `http://localhost:8080` with the `dev` profile active by default.

### 4. Run the TypeScript + Vite Frontend

In a separate terminal:

```bash
cd frontend
npm install
npm run dev
```
> Open `http://localhost:5173` in your browser.

---

## 🧪 Testing & Code Quality

<div align="center">

| Metric | Result | Target |
|---|:---:|:---:|
| **Total Automated Tests** | **178 passing** | 100% |
| **Domain Model & Entities Coverage** | **100%** | 100% |
| **Domain Value Objects Coverage** | **100%** | 100% |
| **Domain Exceptions Coverage** | **100%** | 100% |
| **Domain Repository Contracts Coverage** | **100%** | 100% |

</div>

Generate the JaCoCo HTML report:
```bash
mvn clean test jacoco:report
```
Report location:
`target/site/jacoco/index.html`

---

## 🎯 Milestone History

### Milestone 1: Java Scraper & Clean Architecture
- Java 21, Maven, Jsoup, Ports & Adapters, JUnit 5, Mockito, 100% JaCoCo coverage.

### Milestone 2: Java HTTP API & TypeScript Frontend
- HTTP API, TypeScript + Vite web interface, reactive catalog, search bar.

### Milestone 3: DDD Tactical Patterns & Universal Search
- Entity with unique identity (`ProductId`), self-validating Value Objects (`record`), pure English repository contract (`ProductRepository`), live universal search across Falabella, product images.

### Milestone 4: Spring Boot Microservice, JPA/PostgreSQL, Error Advice & OpenAPI
| Requirement | Status |
|---|:---:|
| **1. REST Endpoints & Error Advice (3-4 Pts)**: Semantic endpoints (`GET`, `POST`, `DELETE`) with `@RestControllerAdvice` returning unified JSON error payloads | ✅ |
| **2. Real Persistence & Virtualization (3-4 Pts)**: `docker-compose.yml` for PostgreSQL 16, JPA entity mapping (`@Entity`), and `JpaRepository` interface in infrastructure | ✅ |
| **3. OpenAPI Documentation & Profiles (4 Pts)**: Swagger-UI interactive documentation hermetically restricted to `dev` profile and locked in `prod` | ✅ |
| **Total Test Suite**: 178 tests with 100% JaCoCo domain line/branch coverage | ✅ |

---

<div align="center">

## 👨‍💻 Author

**Diego Reyes**

WebScraper Enterprise Microservice developed with Java 21, Spring Boot 3, Clean Architecture, PostgreSQL, Docker Compose, and OpenAPI 3.

</div>
