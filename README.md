# Ecommerce Backend API

A Spring Boot ecommerce application focused on secure backend workflows for authentication, catalog management, cart operations, and order processing.

## Overview

This project follows a layered `Controller -> Service -> Repository` architecture and uses Spring Security, JWT, JPA, and PostgreSQL to model a practical ecommerce backend. It includes role-based access control, structured error handling, OpenAPI documentation, and a companion frontend for local testing.

## Core Features

- Authentication and authorization with JWT
- Role-based access for `ADMIN` and `USER`
- Product and category management APIs
- Cart management and checkout workflow
- Order tracking and admin order updates
- Filtering and pagination with JPA Specifications
- RFC 7807 style `application/problem+json` error responses
- Swagger UI for API exploration and testing
- PostgreSQL configuration with optional local H2 profile

## Tech Stack

- Java 8
- Spring Boot 2.7
- Spring Security
- Spring Data JPA
- PostgreSQL
- React
- Vite
- Docker Compose
- springdoc-openapi

## Architecture

- `controller` layer for request handling and response mapping
- `service` layer for business logic and workflow orchestration
- `repository` layer for persistence operations
- `model` layer for JPA entities and relationships
- `dto` layer for request and response contracts

## Run The Backend

1. Start PostgreSQL:

```bash
docker compose up -d
```

2. Run the backend:

```bash
mvn spring-boot:run
```

Once running:

- Swagger UI: `http://localhost:8080/swagger-ui.html`

If you want the local H2 profile for quick testing:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

## Run The Frontend

From the `frontend` folder:

```bash
npm.cmd install
npm.cmd run dev
```

Then open:

- Frontend: `http://localhost:5173`

## Seeded Accounts

- Admin: `admin@shop.local` / `Admin@123`
- User: `customer@shop.local` / `Customer@123`

## Key API Endpoints

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/products`
- `GET /api/categories`
- `GET /api/cart`
- `POST /api/cart/items`
- `POST /api/orders/checkout`
- `GET /api/orders/mine`
- `GET /api/orders/admin`

## Project Structure

- `src/main/java/...` Spring Boot backend
- `frontend/` React + Vite frontend
- `docker-compose.yml` PostgreSQL service
- `.env.example` backend and frontend environment values
- `ecommerce-api.http` IntelliJ HTTP client requests

## Environment Variables

Main backend settings:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET`
- `JWT_EXPIRATION_MILLIS`

Frontend:

- `VITE_API_BASE_URL`

## Error Format

Validation and business errors now return RFC 7807 style responses with:

- `type`
- `title`
- `status`
- `detail`
- `instance`
- optional `errors`
