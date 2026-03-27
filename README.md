# Ecommerce Backend API

A full-featured Spring Boot ecommerce backend with:

- JWT authentication and role-based authorization
- Product and category catalog management
- Cart management and checkout flow
- Order management for customers and admins
- H2 database for local development
- Swagger UI for API exploration

## Stack

- Java 8
- Spring Boot 2.7
- Spring Security
- Spring Data JPA
- H2
- springdoc-openapi

## Run

This project uses Maven. If Maven is installed locally:

```bash
mvn spring-boot:run
```

Once running:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- H2 Console: `http://localhost:8080/h2-console`

## Seeded Accounts

- Admin: `admin@shop.local` / `Admin@123`
- Customer: `customer@shop.local` / `Customer@123`

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

## Database

By default the app uses in-memory H2. You can override it with environment variables:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET`
- `JWT_EXPIRATION_MILLIS`

