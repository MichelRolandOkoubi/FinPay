[![Author](https://img.shields.io/badge/Author-Michel%20Okoubi%20%E2%80%93%20Staff%20Engineer-blueviolet)](https://github.com/pulsar-jvm/pulsar)

# FinPay — Multi-Channel Payment Gateway

FinPay is a high-performance, asynchronous payment orchestration platform designed for modern fintech applications. It provides a secure bridge between merchants and payment providers (like Stripe), featuring real-time analytics and robust webhook management.

![FinPay Architecture](https://mermaid.ink/img/pako:eNptkctuwjAQRX_F8qp-gCURLSDRD6Ati7oZshCHeGqcxI86KCH-vXZCUKjKzp3reubO8AalFpXUUNzX_XmDVpU2GlN7q8Gv50XpB_6M_p8f0Zis6K0E1X_Wp5YvNKiNq_7vW3v6XkPr_2_756NnDRpXvW7N81eDRreir_97Y7p6h-7f_vW4Nal9o-Gva9M_OFT_Z5X7m7U_v_Xn9-4W_0_3zydvLSpTq8Y8z_qjQ6X7XjdmZ0G9at26vve_V5v7v2_t-fuDBvVvNK_N-eWFRtVtNKvN-fmV-6_f_vno6V29Sms1FNcX_Z7n8W0MhS9A)

## 🚀 Key Features

- **Multi-Service Architecture**: Decoupled services for Payments, Webhooks, and Analytics.
- **Stripe Integration**: Out-of-the-box support for Visa, Mastercard, and generic cards with secure tokenization.
- **Event-Driven**: Powered by Kafka for reliable, asynchronous status updates.
- **Idempotency Guarantee**: Built-in protection against double payments using Redis & PostgreSQL.
- **Reliable Webhooks**: HMAC-SHA256 signed callbacks with automatic retry logic capabilities.
- **Merchant Dashboard**: Premium real-time monitoring built with Next.js 16 and Tailwind CSS v4.

## 🛠️ Tech Stack

- **Backend**: Spring Boot 3.2.5 (Java 21)
- **Frontend**: Next.js 16 (React 19)
- **Database**: PostgreSQL 16
- **Cache**: Redis 7.2
- **Messaging**: Apache Kafka 3.7
- **Integation**: Stripe SDK

## 📁 Project Structure

```text
FinPay/
├── apps/
│   ├── api/             # Spring Boot Monorepo
│   │   ├── common       # Shared entities, repositories, events
│   │   ├── payment      # Core payment orchestration service
│   │   ├── webhook      # Event-driven callback dispatcher
│   │   └── analytics    # Real-time data processing (In development)
│   └── web/             # Next.js Merchant Dashboard
├── docs/                # Deep technical documentation
├── infra/               # Docker Compose & Infrastructure config
└── README.md            # You are here
```

## 🚥 Quick Start

### 1. Requirements
- Docker & Docker Compose
- Java 21+
- Node.js 20+

### 2. Infrastructure
```bash
cd infra
docker-compose up -d
```

### 3. Running the Backend
```bash
cd apps/api
./mvnw clean install
./mvnw spring-boot:run -pl payment-service
```

### 4. Running the Dashboard
```bash
cd apps/web
npm install
npm run dev
```

## 📖 Further Documentation

For more specialized details, please refer to:
- [Architecture Overview](docs/ARCHITECTURE.md)
- [API Reference](docs/API_REFERENCE.md)
- [Infrastructure Setup](docs/INFRASTRUCTURE.md)

## 📄 License
This project is licensed under the MIT License.
