# API Reference

FinPay exposes a RESTful API for payment orchestration.

## Base URL
`http://localhost:8081/v1`

## Authentication
Currently: `permitAll()` (In development).
Planned: `X-API-KEY` header verification.

## Endpoints

### 1. Create Payment
`POST /payments`

**Request Body:**
```json
{
  "amount": 5000,
  "currency": "eur",
  "method": "CARD",
  "merchantId": "uuid-here",
  "idempotencyKey": "unique-client-key",
  "description": "Premium Plan"
}
```

**Status Codes:**
- `201 Created`: Transaction initiated.
- `400 Bad Request`: Validation error.
- `409 Conflict`: Idempotency key already used with different parameters.

### 2. Get Transaction Details
`GET /payments/{id}`

**Response:**
```json
{
  "id": "uuid",
  "status": "SUCCEEDED",
  "amount": 5000,
  "currency": "eur",
  "idempotencyKey": "unique-client-key",
  "createdAt": "2026-04-21T..."
}
```

## Webhooks

When a transaction status changes, FinPay sends a POST request to your configured webhook URL.

**Headers:**
- `FinPay-Signature`: HMAC-SHA256 signature of the payload.
- `FinPay-Event-Id`: Unique event ID.

**Payload:**
```json
{
  "transactionId": "uuid",
  "merchantId": "uuid",
  "amount": 5000,
  "currency": "eur",
  "status": "SUCCEEDED"
}
```
