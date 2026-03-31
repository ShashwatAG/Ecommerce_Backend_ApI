# Ecommerce Backend API

Spring Boot backend for an e-commerce system with authentication, catalog management, cart flows, and order processing.

## Features

- JWT-based authentication and authorization
- Role-based access control for `ADMIN` and `USER`
- Product and category management APIs
- Cart operations and checkout workflow
- Order tracking and admin order management
- Filtering and pagination with JPA Specifications
- RFC 7807 style `application/problem+json` error responses
- Swagger UI for API testing and exploration
- PostgreSQL configuration with optional local H2 profile

## Tech Stack

- Java 8
- Spring Boot 2.7
- Spring Security
- Spring Data JPA
- PostgreSQL
- H2
- Docker Compose
- springdoc-openapi

## Architecture

- `controller` layer for request handling
- `service` layer for business logic
- `repository` layer for persistence operations
- `model` layer for JPA entities and relationships
- `dto` layer for request and response contracts

## Run Locally

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

If you want to use the local H2 profile for quick testing:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

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

- `src/main/java/...` Spring Boot backend source
- `docker-compose.yml` PostgreSQL service
- `.env.example` backend environment values
- `ecommerce-api.http` IntelliJ HTTP client requests

## Environment Variables

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET`
- `JWT_EXPIRATION_MILLIS`

## Error Format

Validation and business errors return RFC 7807 style responses with:

- `type`
- `title`
- `status`
- `detail`
- `instance`
- optional `errors`
