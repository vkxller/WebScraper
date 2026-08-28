# 💻 WebScraper Frontend Client

<div align="center">

![TypeScript](https://img.shields.io/badge/TypeScript-Strict_5.0-3178C6?style=for-the-badge&logo=typescript&logoColor=white)
![Vite](https://img.shields.io/badge/Vite-7.3-646CFF?style=for-the-badge&logo=vite&logoColor=white)
![HTML5](https://img.shields.io/badge/HTML5-Semantic-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-Responsive-1572B6?style=for-the-badge&logo=css3&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)

</div>

---

## 📖 Overview

The **WebScraper Frontend** is a modern, reactive single-page application built with **Vanilla TypeScript** and **Vite**. It provides a clean, user-friendly web UI to perform live product searches, visualize dynamic store cards, and inspect direct store purchase links from Falabella Chile.

The client architecture follows modular frontend design principles:
- **Strict Typing (`src/models/`)**: Zero usage of `any`, leveraging TypeScript interfaces and type guards.
- **Asynchronous Service Layer (`src/services/`)**: Centralized `fetch` network calls decoupled from DOM event controllers.
- **Visual Components (`src/components/`)**: Modular HTML generators for reactive card rendering.
- **DOM & Event Management (`src/main.ts`)**: Reactive form processing and state management.

---

## 📁 Directory Structure

```text
frontend
├── src
│   ├── components
│   │   └── ProductCard.ts        // Modular HTML generator for product cards
│   ├── models
│   │   └── product.ts            // Strict TypeScript Product domain interface
│   ├── services
│   │   └── productApi.ts         // Asynchronous REST API network service (fetch)
│   ├── main.ts                   // DOM controller, state handling, and event listeners
│   └── style.css                 // Responsive CSS styles with modern flex & grid layout
├── index.html                    // Main SPA HTML container
├── package.json                  // Dependencies and build scripts
├── tsconfig.json                 // Strict TypeScript configuration
└── vite.config.ts                // Vite dev server & API proxy configuration
```

---

## 🛠️ Getting Started

### 1. Install Dependencies

```bash
cd frontend
npm install
```

### 2. Start Development Server

```bash
npm run dev
```
> The application will be accessible at: **`http://localhost:5173`**

### 3. Build for Production

```bash
npm run build
```
> Compiles TypeScript with strict type-checking (`tsc`) and builds optimized production bundles using Vite.

---

## 🔌 API Integration

The frontend proxy communicates seamlessly with the Spring Boot backend microservice:
- **Base Endpoint**: `/api/products` (Proxied to `http://localhost:8080` in dev mode)
- **Live Search**: `/api/products?search={query}`
- **Store Redirection**: Direct canonical links to product detail pages on Falabella.

---

<div align="center">

Developed with ❤️ using Vanilla TypeScript, Vite, and Clean Frontend Architecture.

</div>
