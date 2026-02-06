# Inventory Management API (Spring Boot)

RESTful backend for supermarket products, categories, users, and stock movements. Built with Spring Boot, PostgreSQL, and JWT-based authentication. This service powers the Vue 3 frontend in `../Web`.

## Features
- CRUD for products with SKU-based codes, pricing, images, and category links.
- CRUD for categories with counts and pagination.
- Stock-in / stock-out endpoints with movement history and negative-stock prevention.
- JWT authentication with Admin and Staff roles; role-based access control on routes.
- Dashboard summary stats endpoint (totals, low stock, stock value).
- OpenAPI/Swagger UI for quick testing.

## Tech Stack
- Java 21, Spring Boot 3.x (Web, Data JPA, Validation, Security)
- PostgreSQL, Hibernate 6
- JWT for auth
- Gradle 8 (wrapper included)

## Project Structure (API)
```
src/main/java/com/.../api/
  ├─ auth/           # login, JWT filters
  ├─ product/        # controllers, services, repositories, DTOs
  ├─ category/       # controllers, services, repositories, DTOs
  ├─ stock/          # stock movement logic
  └─ config/         # security, CORS, swagger
src/main/resources/
  ├─ application.properties
  └─ static/uploads/ # product images (if enabled)
```

## Prerequisites
- Java 21+
- PostgreSQL 12+
- (Optional) Docker for running Postgres locally

## Configuration
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/supermarket
spring.datasource.username=your_user
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

server.port=8080

# JWT
app.jwt.secret=change_me
app.jwt.expiration=86400000

# CORS (adjust origin to your frontend)
app.cors.allowed-origins=http://localhost:5173
```

## Run
```bash
./gradlew clean bootRun
```
API available at `http://localhost:8080/api` and Swagger UI at `http://localhost:8080/swagger-ui.html`.

## Key Endpoints
- `POST /api/login` – obtain JWT
- `GET /api/products` – paginated list (keyword, categoryId, page, size, sortBy, sortDir)
- `POST /api/products` – create product (multipart with image)
- `PUT /api/products/{code}` – update product
- `DELETE /api/products/{code}` – delete product
- `GET /api/categories` – paginated categories
- `POST /api/categories` / `PUT` / `DELETE` – manage categories
- `POST /api/stock/in` – register stock-in
- `POST /api/stock/out` – register stock-out (fails if balance would go negative)
- `GET /api/stock/history/{productCode}` – movement history
- `GET /api/reports/summary` – dashboard stats (auth required)

## Roles
- **ADMIN**: manage users, products, categories, stock; view reports.
- **STAFF**: manage products, categories, stock; view reports.

## Tests
```bash
./gradlew test
```

## Common Issues
- **JWT unauthorized (401)**: re-login to refresh token; confirm `app.jwt.secret` consistent.
- **CORS errors**: ensure `app.cors.allowed-origins` matches frontend origin.
- **DB connection**: verify PostgreSQL running and credentials are correct.

## Related Docs
- Frontend guide: `../API/frontend-guide.md`
- Frontend app README: `../Web/README.md`

## License
Educational sample; adapt before production use.
