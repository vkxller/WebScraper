# 🛒 WebScraper

<div align="center">

![Java](https://img.shields.io/badge/Java-21-007396?style=for-the-badge&logo=openjdk&logoColor=white)
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

**WebScraper** is a Java 21 application designed to extract product information from the Falabella Chile website.

The project follows **Clean Architecture** principles, keeping business logic independent from external libraries and infrastructure components. This results in a solution that is maintainable, testable and prepared for future expansion.

Starting with **Milestone 2**, the project was extended with a lightweight HTTP API and a **TypeScript + Vite** web frontend, allowing the scraped products to be browsed and searched directly from the browser.

The current implementation extracts:

- Store name
- Product name
- Current price
- Previous price, when available
- Discount, when available
- Source URL, when available

The long-term objective is to evolve the project into a complete product and price comparison platform.

---

## ✨ Features

<table>
  <tr>
    <td>🌐</td>
    <td><strong>HTML download</strong></td>
    <td>Retrieves product pages using Jsoup.</td>
  </tr>
  <tr>
    <td>🔍</td>
    <td><strong>Product parsing</strong></td>
    <td>Extracts relevant product information from Falabella HTML.</td>
  </tr>
  <tr>
    <td>🧱</td>
    <td><strong>Clean Architecture</strong></td>
    <td>Separates domain, application, ports and infrastructure.</td>
  </tr>
  <tr>
    <td>✅</td>
    <td><strong>Domain validation</strong></td>
    <td>Prevents the creation of invalid products.</td>
  </tr>
  <tr>
    <td>🧪</td>
    <td><strong>Unit testing</strong></td>
    <td>Uses JUnit 5 and Mockito for isolated tests.</td>
  </tr>
  <tr>
    <td>📊</td>
    <td><strong>Code coverage</strong></td>
    <td>Generates coverage reports with JaCoCo.</td>
  </tr>
  <tr>
    <td>🔌</td>
    <td><strong>HTTP API</strong></td>
    <td>Exposes the scraped products through a lightweight Java API.</td>
  </tr>
  <tr>
    <td>💻</td>
    <td><strong>Web interface</strong></td>
    <td>Consumes the API from a TypeScript + Vite frontend.</td>
  </tr>
  <tr>
    <td>🔎</td>
    <td><strong>Search</strong></td>
    <td>Allows filtering products by name directly in the browser.</td>
  </tr>
</table>

---

## 🛠️ Technologies

| Technology | Purpose |
|---|---|
| Java 21 | Main programming language / backend |
| Maven | Build and dependency management |
| Jsoup | HTML download and parsing |
| JUnit 5 | Unit testing |
| Mockito | Test doubles and dependency isolation |
| JaCoCo | Code coverage reports |
| TypeScript | Frontend logic |
| Vite | Frontend development and build tool |
| HTML5 | Frontend markup |
| CSS | Frontend styling |
| Node.js / npm | Frontend tooling and package management |
| Git | Version control |
| GitHub | Source code hosting |

---

## 📁 Project Structure

```text
src
├── main
│   ├── java
│   │   └── org.diegoreyes.webscraper
│   │       ├── api
│   │       │   └── ProductApiApplication.java
│   │       │
│   │       ├── application
│   │       │   └── ProductScraperService.java
│   │       │
│   │       ├── domain
│   │       │   ├── exception
│   │       │   └── model
│   │       │       └── Product.java
│   │       │
│   │       ├── infrastructure
│   │       │   ├── client
│   │       │   │   └── JsoupHtmlClient.java
│   │       │   └── parser
│   │       │       └── FalabellaProductParser.java
│   │       │
│   │       ├── port
│   │       │   ├── HtmlClient.java
│   │       │   └── ProductParser.java
│   │       │
│   │       └── WebScraperApplication.java
│   │
│   └── resources
│
└── test
    └── java
        └── org.diegoreyes.webscraper

frontend
├── src
│   ├── components
│   │   └── ProductCard.ts
│   │
│   ├── models
│   │   └── product.ts
│   │
│   ├── main.ts
│   └── style.css
│
├── index.html
├── package.json
├── package-lock.json
├── tsconfig.json
└── vite.config.ts
```

---

## 🏛️ Architecture

The application uses a Clean Architecture approach based on dependency inversion.

```text
┌───────────────────────────────────────────┐
│           WebScraperApplication           │
│              Entry point                  │
└────────────────────┬──────────────────────┘
                     │
                     ▼
┌───────────────────────────────────────────┐
│          ProductScraperService            │
│            Application logic              │
└───────────────┬───────────────┬───────────┘
                │               │
                ▼               ▼
       ┌────────────────┐ ┌─────────────────┐
       │   HtmlClient   │ │ ProductParser   │
       │      Port      │ │      Port       │
       └───────┬────────┘ └────────┬────────┘
               │                   │
               ▼                   ▼
     ┌───────────────────┐ ┌──────────────────────────┐
     │ JsoupHtmlClient   │ │ FalabellaProductParser  │
     │ Infrastructure    │ │ Infrastructure           │
     └───────────────────┘ └────────────┬─────────────┘
                                        │
                                        ▼
                              ┌─────────────────┐
                              │     Product     │
                              │     Domain      │
                              └─────────────────┘
```

### Dependency flow

```text
Infrastructure → Ports ← Application → Domain
```

Business rules remain completely independent from the infrastructure layer.
The domain layer does not depend on Jsoup, Maven, frameworks or infrastructure implementations.

### Frontend / Backend communication

Starting with Milestone 2, the API reuses the existing `ProductScraperService`, avoiding any duplication of the scraping logic.

```text
Falabella
    ↓
Jsoup
    ↓
ProductScraperService
    ↓
Java HTTP API
    ↓
fetch()
    ↓
TypeScript
    ↓
Web Interface
```

During development, Vite uses a proxy for `/api` requests:

```text
Frontend
http://localhost:5173
        │
        │ /api/products
        ↓
Vite Proxy
        │
        ↓
Java API
http://localhost:8080
```

This allows the frontend to call `/api/products` directly, without hard-coding the backend address in the code.

---

## 📜 Business Rules

The application currently follows these business rules:

1. Every product must have a valid store name.
2. Every product must have a valid product name.
3. Every product must have a valid current price.
4. The current price cannot be negative.
5. The previous price is optional.
6. The previous price cannot be negative when present.
7. Discount information is optional.
8. The product source URL is optional.
9. Invalid products are rejected through domain validation.
10. Business rules must remain independent from infrastructure components.

These rules are centralized in the domain model instead of being distributed across parsers or clients.

---

## 🧠 Design Decisions

### Clean Architecture

Clean Architecture was selected to keep the project independent from specific frameworks and technical implementations.

This allows infrastructure components to be replaced without modifying business logic.

### Ports and Adapters

The application defines ports such as:

- `HtmlClient`
- `ProductParser`

Their implementations are located in the infrastructure layer:

- `JsoupHtmlClient`
- `FalabellaProductParser`

This design allows future clients and parsers to be added without changing the application service.

### Constructor Injection

Dependencies are provided through constructors.

This avoids hidden dependencies and simplifies unit testing.

### Domain Validation

Product validation is performed when domain objects are created.

This guarantees that an invalid `Product` cannot exist inside the application.

### Optional Product Information

Some Falabella products do not contain:

- Previous price
- Discount
- Product URL in the initial HTML response

For that reason, these fields are optional.

Falabella may inject some links dynamically through JavaScript after the initial HTML document is loaded. Jsoup does not execute JavaScript, so the source URL may not always be available.

### Immutable Results

The application service returns product collections that cannot be modified externally.

This protects the scraper result and reduces unintended state changes.

### API reuse

The HTTP API exposed in Milestone 2 wraps the existing `ProductScraperService` instead of duplicating scraping logic, keeping a single source of truth for how products are extracted.

---

## 🧩 Architecture Principles

The project follows these software engineering principles:

- Clean Architecture
- Separation of Concerns
- Dependency Inversion Principle
- Single Responsibility Principle
- Open/Closed Principle
- Low coupling
- High cohesion
- Explicit dependencies
- Testability by design
- Maintainability
- Extensibility

---

## 🚀 Getting Started

### Prerequisites

Make sure the following tools are installed:

```text
Java 21
Maven 3.9 or newer
Node.js
npm
Git
```

Verify the installations:

```bash
java -version
mvn -version
node -v
npm -v
git --version
```

---

## ▶️ Running the Project

### Compile the application

```bash
mvn clean compile
```

### Run all tests

```bash
mvn clean test
```

### Validate the complete project

```bash
mvn clean verify
```

### Generate the JaCoCo report

```bash
mvn clean test jacoco:report
```

The generated report can be found at:

```text
target/site/jacoco/index.html
```

### Run the scraper

```bash
mvn exec:java
```

### Run the backend API

```bash
mvn exec:java "-Dexec.mainClass=org.diegoreyes.webscraper.api.ProductApiApplication"
```

The API will be available at:

```text
http://localhost:8080
```

Main endpoint:

```text
http://localhost:8080/api/products
```

### Install frontend dependencies

In a separate terminal:

```bash
cd frontend
npm install
```

### Run the frontend

From the `frontend` directory:

```bash
npm run dev
```

Vite will start at:

```text
http://localhost:5173
```

Open that address in the browser.

### Build the frontend for production

```bash
npm run build
```

This generates the production bundle in:

```text
frontend/dist/
```

The build step verifies that TypeScript compiles correctly before generating the bundle with Vite.

---

## 🧪 Testing

The project includes unit tests for the main components and behaviors.

| Test area | Validations |
|---|---|
| Domain model | Required attributes and invalid values |
| Product equality | `equals()` and `hashCode()` behavior |
| Application service | Downloading, parsing and returning products |
| Product parser | HTML extraction and optional attributes |
| HTTP client | HTML retrieval and error handling |
| Domain exceptions | Validation failure behavior |

JUnit 5 is used as the testing framework, while Mockito isolates dependencies in application-level tests. JaCoCo is used to measure code coverage.

---

## 📊 Code Quality

The project emphasizes code quality through:

- Unit tests with JUnit 5
- Dependency isolation with Mockito
- Code coverage reports with JaCoCo
- Complete domain validation coverage
- Full branch coverage in the domain layer
- Constructor dependency injection
- Consistent package organization
- Descriptive class and method names
- Separation between business and infrastructure logic
- Automated Maven verification
- TypeScript strict mode on the frontend

### Current domain coverage

<div align="center">

| Layer | Instruction Coverage | Branch Coverage |
|---|:---:|:---:|
| Domain Model | 100% | 100% |
| Domain Exceptions | 100% | 100% |

</div>

---

## 🎯 Milestone 1

| Requirement | Status |
|---|:---:|
| Java 21 | ✅ |
| Maven project | ✅ |
| Jsoup | ✅ |
| Product extraction | ✅ |
| Clean Architecture | ✅ |
| Ports and adapters | ✅ |
| Domain validation | ✅ |
| Unit tests | ✅ |
| JUnit 5 | ✅ |
| Mockito | ✅ |
| JaCoCo | ✅ |
| Domain coverage | ✅ 100% |
| Domain branch coverage | ✅ 100% |

---

## 🌐 Milestone 2

During Milestone 2, a web interface was added to consume the products obtained by the scraper, together with a lightweight HTTP API on the Java backend.

### Frontend

Built using:

- TypeScript
- Vite
- HTML5
- CSS
- TypeScript strict mode

The frontend includes:

- A typed product model
- Components to represent products
- Asynchronous consumption of the API (`async`/`await`)
- Error handling via `try`/`catch`
- HTTP response validation
- Validation of the data received from the API
- Loading state
- Empty state
- Error messages
- Dynamic rendering
- Search by product name
- Form handling using `preventDefault()`

### API

A lightweight HTTP API was added in Java to serve the products to the frontend.

Main endpoint:

```text
GET /api/products
```

The API returns the products in JSON format.

### 🔎 Search

The application includes a search bar that filters the products loaded from the API.

The search is performed on the product name.

```text
User types
      ↓
Form
      ↓
preventDefault()
      ↓
Filter products
      ↓
Render results
```

### ✅ Milestone 2 Status

| Requirement | Status |
|---|:---:|
| TypeScript | ✅ |
| Vite | ✅ |
| TypeScript strict mode | ✅ |
| Typed model | ✅ |
| DOM manipulation | ✅ |
| DOM element validation | ✅ |
| Form handling | ✅ |
| `preventDefault()` | ✅ |
| `async`/`await` | ✅ |
| `try`/`catch` | ✅ |
| `response.ok` validation | ✅ |
| API data validation | ✅ |
| Loading state | ✅ |
| Error state | ✅ |
| Empty state | ✅ |
| Dynamic rendering | ✅ |
| Product search | ✅ |
| Frontend components | ✅ |
| Java HTTP API | ✅ |
| Frontend/API integration | ✅ |
| Production build | ✅ |

> **Note:** The visual interface is deliberately kept simple in this milestone. The main goal was to correctly implement the communication between the TypeScript frontend and the Java backend, along with the required states, validations and functionality.

### 📌 Quick verification

**Backend**

```bash
mvn clean test
mvn exec:java "-Dexec.mainClass=org.diegoreyes.webscraper.api.ProductApiApplication"
```

**Frontend**

In another terminal:

```bash
cd frontend
npm install
npm run build
npm run dev
```

Finally, open:

```text
http://localhost:5173
```

The page should display the products retrieved from `/api/products` and allow searching by name.

---

## 💻 Current Output

The scraper prints each product in a readable format.

Example:

```text
Products found: 27

Store: Falabella
Name: Notebook Lenovo LOQ
Price: $699.990
Discount: -17%
Source URL: Not available
```

The number of products and their information depend on the HTML returned by Falabella at execution time.

---

## 🌟 Project Vision

The long-term goal of WebScraper is to evolve from a single-store scraper into a complete product and price intelligence platform.

The platform is intended to collect, store, compare and analyze products from multiple online retailers.

Users will eventually be able to:

- Explore products from different categories
- Search and filter available products
- Compare prices across multiple stores
- Review historical price changes
- Identify genuine discounts
- Discover the best current offers
- Receive intelligent product recommendations

---

## 🗺️ Future Improvements

### 🔍 General Scraping

- Support any Falabella product category.
- Allow users to provide any compatible category URL.
- Automatically discover and process products from category pages.
- Add pagination support.
- Extract product page URLs.
- Extract product image URLs.
- Extract additional product specifications.
- Improve parser resilience against HTML structure changes.
- Prevent duplicated products in the final result.

### 💾 Data Persistence

- Integrate a relational database.
- Persist products and store information.
- Maintain a complete price history for every product.
- Register the date and time of each detected price.
- Detect price increases and reductions.
- Identify the lowest historical price.
- Avoid duplicate records during repeated scraping operations.

### 🌱 Web Platform Enhancements

- Migrate the application entry point to Spring Boot.
- Add category and store filters.
- Add price-based sorting.
- Display richer product details.
- Show price-history charts.
- Create dashboards with product and market statistics.
- Improve the visual design of the frontend.

### 🏪 Multi-Store Comparison

- Add scrapers for additional e-commerce websites.
- Normalize products collected from different stores.
- Identify equivalent products across retailers.
- Compare prices between stores.
- Automatically highlight the best available offer.
- Generate product rankings based on prices and discounts.
- Track availability and stock when possible.

### 🤖 Artificial Intelligence

- Integrate AI-powered product recommendations.
- Suggest alternatives according to price and characteristics.
- Analyze historical price behavior.
- Detect potentially misleading discounts.
- Identify relevant buying opportunities.
- Generate summaries of product characteristics.
- Recommend products based on user preferences.
- Provide intelligent insights for purchasing decisions.

---

## 👨‍💻 Author

<div align="center">

**Diego Reyes**

Developed as a Java web scraping project focused on software architecture, automated testing and future scalability, later extended with a TypeScript + Vite frontend to visualize the scraped data.

</div>
