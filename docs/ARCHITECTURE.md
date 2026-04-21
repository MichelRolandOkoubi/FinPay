# FinPay Architecture Overview

FinPay is a high-performance, asynchronous multi-channel payment gateway built with a microservices-modular architecture.

## System Architecture

```mermaid
graph TD
    Client[Client Browser/Mobile]
    Proxy[Nginx/Gateway]
    
    subgraph "API Layer (Spring Boot)"
        PS[Payment Service]
        WS[Webhook Service]
        AS[Analytics Service]
    end
    
    PS -- "Publishes events" --> Kafka[Kafka Topic: payment.events]
    Kafka -- "Consumes events" --> WS
    Kafka -- "Consumes events" --> AS
    
    PS -- "Idempotency & Cache" --> Redis[(Redis)]
    PS -- "Persistence" --> Postgres[(PostgreSQL)]
    WS -- "Logs & Config" --> Postgres
    
    PS -- "External Gateway" --> Stripe[Stripe API]
    WS -- "Callbacks" --> Merchant[Merchant Webhook URL]
```

## Key Components

### 1. Payment Service (`apps/api/payment-service`)
- Handles transaction initiation and processing.
- Integrates with external payment providers (Stripe).
- Implements strict idempotency using Redis and PostgreSQL.
- Publishes status updates to Kafka.

### 2. Webhook Service (`apps/api/webhook-service`)
- Consumes Kafka events.
- Dispatches signed HTTP callbacks to merchants.
- Implements HMAC-SHA256 signing for security.
- Maintains exhaustive logs of delivery attempts.

### 3. Common Module (`apps/api/common`)
- Shares JPA entities, Repositories, DTOs, and Kafka Event objects.
- Contains the Flyway migration scripts.

### 4. Merchant Dashboard (`apps/web`)
- Next.js 16 application.
- Real-time display of transaction metrics.
- Modern dark-mode UI with Glassmorphism.
