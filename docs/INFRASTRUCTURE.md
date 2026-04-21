# Infrastructure & Deployment

FinPay relies on a robust containerized infrastructure for local development and production.

## Technology Stack

| Component | Technology | Version |
|-----------|------------|---------|
| Runtime | Java (Corretto) | 21 |
| Framework | Spring Boot | 3.2.5 |
| Database | PostgreSQL | 16 |
| Cache/Idempotency | Redis | 7.2 |
| Messaging | Kafka | 3.7 (Kraft) |
| Frontend | Next.js | 16 (React 19) |

## Running Locally

### 1. Infrastructure (Docker)
Start the required services using Docker Compose:
```bash
cd infra
docker-compose up -d
```
This starts PostgreSQL, Redis, and Kafka.

### 2. Backend (API)
Run the services from the `apps/api` directory:
```bash
# In separate terminals
./mvnw spring-boot:run -pl payment-service
./mvnw spring-boot:run -pl webhook-service
```

### 3. Frontend (Web)
Run the dashboard:
```bash
cd apps/web
npm install
npm run dev
```

## Environment Variables

### Backend (`application.properties`)
- `STRIPE_API_KEY`: Secret key for Stripe integration.
- `SPRING_DATASOURCE_URL`: PostgreSQL connection string.
- `SPRING_KAFKA_BOOTSTRAP_SERVERS`: Kafka broker location.

### Frontend (`.env.local`)
- `NEXT_PUBLIC_API_URL`: Backend API base URL.
