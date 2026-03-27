# Ecommerce Platform

A resume-aligned full-stack ecommerce project with:

- Spring Boot REST API for products, cart, and orders
- JWT authentication with `ADMIN` and `USER` roles
- Dynamic filtering and pagination using JPA Specifications
- RFC 7807 style `application/problem+json` error responses
- PostgreSQL-ready configuration with Docker Compose
- React frontend for authentication, catalog, cart, and admin workflows
- Swagger UI for API exploration

## Stack

- Java 8
- Spring Boot 2.7
- Spring Security
- Spring Data JPA
- PostgreSQL
- React
- Vite
- Docker Compose
- springdoc-openapi

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

If you want the old H2-only mode for quick local testing:

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

## Main APIs

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/products`
- `GET /api/categories`
- `GET /api/cart`
- `POST /api/cart/items`
- `POST /api/orders/checkout`
- `GET /api/orders/mine`
- `GET /api/orders/admin`

## Project Layout

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
