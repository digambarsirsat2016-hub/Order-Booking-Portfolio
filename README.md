# FYNXT Java Developer Assignment

## Overview

This project implements the Order Booking & Portfolio API described in the FYNXT backend engineering assignment.

The application allows traders to:

- Place BUY and SELL orders
- Fill orders
- Cancel orders
- Maintain stock holdings
- View portfolio summaries grouped by sector
- Analyze portfolio overlap against benchmark baskets
- Calculate portfolio risk level

The application is built using:

- Java 17
- Spring Boot 3
- Spring Data JPA
- H2 Database
- Maven

---

## Project Structure

```text
src/main/java/com/fynxt/orderbooking

├── controller
│   ├── OrderController
│   └── PortfolioController
│
├── dto
│   ├── AddPortfolioRequest
│   ├── PlaceOrderRequest
│   ├── PortfolioResponse
│   ├── BasketOverlapDto
│   ├── OverlapResponse
│   └── ErrorResponse
│
├── entity
│   ├── Order
│   ├── Portfolio
│   ├── OrderSide
│   └── OrderStatus
│
├── exception
│   ├── BusinessException
│   ├── ResourceNotFoundException
│   └── GlobalExceptionHandler
│
├── repository
│   ├── OrderRepository
│   └── PortfolioRepository
│
├── service
│   ├── OrderService
│   ├── PortfolioService
│   └── OverlapAnalysisService
│
└── OrderBookingApplication
```

---

## Running the Application

### Prerequisites

- Java 17+
- Maven 3.9+

### Start Application

```bash
mvn clean install
mvn spring-boot:run
```

Application starts on:

```text
http://localhost:8080
```

---

## H2 Database Console

Open:

```text
http://localhost:8080/h2-console
```

Connection Details:

```text
JDBC URL:
jdbc:h2:mem:testdb

Username:
sa

Password:
(empty)
```

---

## API Endpoints

### Add Holdings

POST

```text
/api/portfolio/{traderId}/holdings
```

Request

```json
{
  "stock": "NVDA",
  "sector": "TECH",
  "quantity": 100
}
```

---

### Place Order

POST

```text
/api/orders
```

Request

```json
{
  "traderId": "T001",
  "stock": "AAPL",
  "sector": "TECH",
  "quantity": 50,
  "side": "BUY"
}
```

---

### Fill Order

POST

```text
/api/orders/{orderId}/fill
```

Example

```text
/api/orders/1/fill
```

---

### Cancel Order

POST

```text
/api/orders/{orderId}/cancel
```

Example

```text
/api/orders/1/cancel
```

---

### Get Portfolio

GET

```text
/api/portfolio/{traderId}
```

Response

```json
{
  "traderId": "T001",
  "positions": {
    "AAPL": 150,
    "TSLA": 80
  },
  "sectorBreakdown": {
    "TECH": 230
  }
}
```

---

### Portfolio Overlap Analysis

GET

```text
/api/portfolio/{traderId}/overlap
```

Response

```json
{
  "overlaps": [
    {
      "basket": "TECH_HEAVY",
      "overlap": "75.00%"
    }
  ],
  "dominantBasket": "TECH_HEAVY",
  "riskFlag": "HIGH"
}
```

---

## Business Rules

### Pending Order Limit

A trader cannot have more than three orders in PENDING state.

Attempting to create a fourth pending order results in a validation error.

---

### SELL Validation

A SELL order is rejected when the trader does not own enough shares.

Example:

Current Holdings

```text
AAPL = 50
```

SELL Request

```text
80 shares
```

Result

```text
Rejected
```

---

### Order State Transitions

Valid transitions:

```text
PENDING -> FILLED
PENDING -> CANCELLED
```

Invalid transitions:

```text
FILLED -> CANCELLED
CANCELLED -> FILLED
FILLED -> FILLED
```

Invalid transitions return a descriptive error response.

---

## Sector Overlap Logic

Benchmark baskets:

### TECH_HEAVY

```text
AAPL
MSFT
GOOGL
TSLA
NVDA
```

### FINANCE_HEAVY

```text
JPM
GS
BAC
MS
WFC
```

### BALANCED

```text
AAPL
JPM
XOM
JNJ
TSLA
```

Formula:

```text
Overlap =
(2 × Common Stocks)
/
(Portfolio Size + Basket Size)
× 100
```

Example:

Portfolio

```text
AAPL
TSLA
NVDA
```

Common Stocks with TECH_HEAVY:

```text
AAPL
TSLA
NVDA
```

Overlap:

```text
75%
```

---

## Risk Flag Calculation

```text
Overlap >= 60%  -> HIGH
Overlap >= 40%  -> MEDIUM
Overlap < 40%   -> LOW
```

---

## Design Decisions

### Layered Architecture

The project follows a standard layered Spring Boot architecture:

```text
Controller
   ↓
Service
   ↓
Repository
   ↓
Database
```

Business logic is isolated inside the service layer.

---

### Optimistic Locking

Portfolio uses:

```java
@Version
private Long version;
```

This provides optimistic locking and helps prevent lost updates during concurrent modifications.

Optimistic locking was chosen because:

- Simpler implementation
- Better performance under low write contention
- Easier scalability
- Reduced database locking

---

### Pure Java Overlap Logic

Portfolio overlap calculations are implemented entirely in Java.

No repository or database access is performed inside the overlap analysis component.

This satisfies the assignment requirement for framework-independent overlap logic.

---

### Validation

Validation is implemented using:

```java
@NotBlank
@NotNull
@Min
```

Invalid requests return HTTP 400 responses.

---

## Error Handling

Centralized exception handling is implemented using:

```java
@RestControllerAdvice
```

Supported error scenarios:

- Resource not found
- Invalid state transitions
- Insufficient shares
- Maximum pending orders exceeded
- Request validation failures
- Unexpected server errors

---

## Assumptions

- Trader IDs are unique.
- Stock symbols are case-sensitive.
- Portfolio quantity cannot become negative.
- Order quantity must always be greater than zero.
- Portfolio records are unique per trader and stock combination.
- Authentication and authorization are out of scope.

---

## Trade-Offs

For simplicity and assignment scope:

- H2 is used instead of PostgreSQL.
- No authentication layer.
- No pagination support.
- No audit history.
- No asynchronous order execution.
- Single application deployment model.

---

## Possible Future Improvements

- PostgreSQL integration
- Swagger/OpenAPI documentation
- Authentication and authorization
- Audit logging
- Distributed locking
- Event-driven order processing
- Docker deployment
- Metrics and monitoring
- Integration testing with Testcontainers

---

## Build

```bash
mvn clean install
```

## Run

```bash
mvn spring-boot:run
```

## Test

```bash
mvn test
```
