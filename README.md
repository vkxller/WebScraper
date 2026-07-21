# WebScraper

![Java](https://img.shields.io/badge/Java-21-blue)
![Maven](https://img.shields.io/badge/Maven-3.9-red)
![JUnit 5](https://img.shields.io/badge/JUnit-5-green)
![JaCoCo](https://img.shields.io/badge/JaCoCo-Code%20Coverage-brightgreen)
![License](https://img.shields.io/badge/License-MIT-yellow)

## Overview

WebScraper is a Java 21 application that extracts product information from the Falabella Chile website.

The project was developed following Clean Architecture principles, separating business logic from infrastructure concerns to provide a maintainable, testable and extensible solution.

Currently, the scraper extracts:

- Store
- Product name
- Current price
- Previous price (when available)
- Discount (when available)

---

## Technologies

- Java 21
- Maven
- Jsoup
- JUnit 5
- Mockito
- JaCoCo

---

## Project Structure

```
src
├── main
│   ├── java
│   │   └── org.diegoreyes.webscraper
│   │       ├── application
│   │       ├── domain
│   │       │   ├── exception
│   │       │   └── model
│   │       ├── infrastructure
│   │       │   ├── client
│   │       │   └── parser
│   │       ├── port
│   │       └── WebScraperApplication.java
│   └── resources
└── test
    └── java
```

---

## Architecture

The project follows a layered architecture.

```
WebScraperApplication
        │
        ▼
ProductScraperService
        │
 ┌──────┴────────┐
 ▼               ▼
HtmlClient   ProductParser
 │               │
 ▼               ▼
JsoupHtmlClient  FalabellaProductParser
```

Business rules remain completely independent from the infrastructure layer.

---

## Features

- Download HTML using Jsoup
- Parse Falabella product cards
- Validate domain objects
- Handle optional product fields
- Clean Architecture
- Unit testing with JUnit 5
- Code coverage with JaCoCo

---

## Running the Project

Compile:

```bash
mvn clean compile
```

Run tests:

```bash
mvn clean test
```

Generate JaCoCo report:

```bash
mvn clean test jacoco:report
```

Run the scraper:

```bash
mvn exec:java
```

---

## Testing

The project contains unit tests covering:

- Domain validations
- Product equality
- Parser behavior
- Application service
- HTTP client

JaCoCo is used to measure code coverage.

---

## Hito 1 Requirements

| Requirement | Status |
|------------|--------|
| Java 21 | ✅ |
| Maven | ✅ |
| Clean Architecture | ✅ |
| Jsoup | ✅ |
| Unit Tests | ✅ |
| JUnit 5 | ✅ |
| Mockito | ✅ |
| JaCoCo | ✅ |
| Domain Coverage | ✅ 100% |
| Product Extraction | ✅ |

---

## Current Output

The scraper currently extracts:

- Store
- Product name
- Current price
- Previous price
- Discount

Example:

```
Store: Falabella
Name: Notebook Lenovo LOQ
Price: $699.990
Previous price: $819.990
Discount: -17%
Source URL: Not available
```

---

## Future Improvements

- Product URL extraction
- Pagination
- Multi-store support
- Price comparison
- Database integration
- REST API
- Frontend
- AI-powered recommendations

---

## Author

Diego Reyes
