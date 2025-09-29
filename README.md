# RAG Chat Storage Microservice

A lightweight REST API for managing chat sessions and messages. Built with Spring Boot 3, PostgreSQL, and Spring Data JPA. Includes pagination, validation, optional Swagger/OpenAPI docs, health checks, and a simple Docker-based local setup.

---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Requirements](#requirements)
- [Quick Start](#quick-start)
    - [1) Clone & Configure Environment](#1-clone--configure-environment)
    - [2) Start Infrastructure with Docker (optional)](#2-start-infrastructure-with-docker-optional)
    - [3) Run the App](#3-run-the-app)
    - [4) Verify it’s Up](#4-verify-its-up)
- [API Overview](#api-overview)
    - [Global Request Header](#global-request-header)
    - [Base Path](#base-path)
    - [Endpoints](#endpoints)
- [cURL Examples](#curl-examples)
- [Swagger / OpenAPI](#swagger--openapi)

## Features

- CRUD-style endpoints for chat **messages** within a **session**
- Request validation and consistent HTTP status codes
- Pagination & sorting for listing messages
- Optional **Swagger UI** / OpenAPI spec
- **Actuator health** endpoints
- Docker Compose for Postgres (+ optional pgAdmin & Redis)
- Unit tests with JUnit 5 + Mockito
- Configurable **CORS**
- `.env` support via `spring-dotenv`


---

## Tech Stack

- Java 17 (works with Java 17+)
- Spring Boot 3.5.x (Web, Validation, Data JPA, Actuator)
- PostgreSQL
- springdoc-openapi (Swagger UI)
- JUnit 5, Mockito
- Docker / Docker Compose (for local infra)

---
## Requirements

- Java 17+
- Maven 3.9+
- Docker & Docker Compose
## Quick Start

### 1) Clone & Configure Environment

```bash
git clone https://github.com/ShashiLakshan/chat-management.git
cd chat-management
```

### 2) Start Infrastructure with Docker
Spin up **Postgres** (and optionally **pgAdmin** and **Redis**) locally:

```bash
docker compose up -d
```
- Postgres → `localhost:5432`
- pgAdmin → http://localhost:5050 (login: `admin@local.test` / `admin`)
- Redis → `localhost:6379`


### 3) Run the App
The app runs on **http://localhost:8080** by default (configurable via `SERVER_PORT`).

### 4) Verify it’s Up

- Health: `GET http://localhost:8080/actuator/health`
- Swagger UI (if enabled): `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

---

## API Overview

### Global Request Header

All message endpoints require these headers:

```
X-User-Id: <string>      # ID of the end user, required
X-API-Key: <string>      # API key for simple auth; default dev-secret (see .env)
```

> In development, the default API key is `dev-secret` unless overridden by `API_KEY` in your environment.

### Base URL & Paths

- **Base URL:** `http://localhost:8080`
- **Base Path (API version):** `/v1`
- **Resource Path (messages):** `/sessions/{sessionId}/messages`

### Endpoints

#### Sessions

**1) Create session**  
`POST /v1/sessions`  
**Headers**
- `X-User-Id` (required)
- `X-API-Key` (required)

**2) List sessions**  
`GET /v1/sessions`  
**Headers**
- `X-User-Id`
- `X-API-Key`

**3) Get a session**  
`GET /v1/sessions/{sessionId}`  
**Headers**
- `X-User-Id`
- `X-API-Key`

**4) Delete a session**  
`DELETE /v1/sessions/{sessionId}`  
**Headers**
- `X-User-Id`
- `X-API-Key`

#### Messages (per session)

**1) Create message**  
`POST /v1/sessions/{sessionId}/messages`  
**Headers**
- `X-User-Id` (required)
- `X-API-Key` (required)  
  **Body (JSON)**
```json
{
  "role": "USER",     // or "ASSISTANT"
  "content": "hello there",
  "tokenUsage": 25    // optional
}
```

**2) List messages (paged)**  
`GET /v1/sessions/{sessionId}/messages`  
**Headers**
- `X-User-Id`
- `X-API-Key`  
  **Query params (optional)**
- `page` (default `0`)
- `size` (default `50`)
- `sort` (default `createdAt,ASC`)

**3) Delete message**  
`DELETE /v1/sessions/{sessionId}/messages/{messageId}`  
**Headers**
- `X-User-Id`
- `X-API-Key`


---

## cURL Examples

Below are copy‑pasteable **cURL** examples. Replace placeholder values like `{sessionId}` and `{messageId}` (or set the env vars shown).


---

## Quick env vars (optional)

```bash
BASE_URL=http://localhost:8080
API_KEY=dev-secret
USER_ID=demo-user-123
SESSION_ID=11111111-1111-1111-1111-111111111111   # replace after create
MESSAGE_ID=22222222-2222-2222-2222-222222222222   # replace after create
```
All requests include these headers:
```bash
-H "Content-Type: application/json" \
-H "X-API-Key: $API_KEY" \
-H "X-User-Id: $USER_ID"
```

---

## Sessions

### Create session (POST /v1/sessions)
**Body**
```json
{
  "title": "Production incident",
  "favourite": true
}
```
**cURL**
```bash
curl -i -X POST "$BASE_URL/v1/sessions" \
  -H "Content-Type: application/json" \
  -H "X-API-Key: $API_KEY" \
  -H "X-User-Id: $USER_ID" \
  -d '{
    "title": "Production incident",
    "favourite": true
  }'
```

### Rename session (PATCH /v1/sessions/{sessionId}/rename)
Send only the fields you want to change.

**Body**
```json
{
  "title": "Production incident - RCA"
}
```
**cURL**
```bash
curl -i -X PATCH "$BASE_URL/v1/sessions/$SESSION_ID/rename" \
  -H "Content-Type: application/json" \
  -H "X-API-Key: $API_KEY" \
  -H "X-User-Id: $USER_ID" \
  -d '{
    "title": "Production incident - RCA"
  }'
```

---

## Messages

### Create message (POST /v1/sessions/{sessionId}/messages)
**Body**
```json
{
  "role": "USER",
  "content": "What happened after the deploy?",
  "tokenUsage": 12
}
```
**cURL**
```bash
curl -i -X POST "$BASE_URL/v1/sessions/$SESSION_ID/messages" \
  -H "Content-Type: application/json" \
  -H "X-API-Key: $API_KEY" \
  -H "X-User-Id: $USER_ID" \
  -d '{
    "role": "USER",
    "content": "What happened after the deploy?",
    "tokenUsage": 12
  }'
```

### List messages (GET /v1/sessions/{sessionId}/messages)
**cURL**
```bash
curl -s "$BASE_URL/v1/sessions/$SESSION_ID/messages?page=0&size=50&sort=createdAt,asc" \
  -H "X-API-Key: $API_KEY" \
  -H "X-User-Id: $USER_ID"
```

### Delete message (DELETE /v1/sessions/{sessionId}/messages/{messageId})
**cURL**
```bash
curl -i -X DELETE "$BASE_URL/v1/sessions/$SESSION_ID/messages/$MESSAGE_ID" \
  -H "X-API-Key: $API_KEY" \
  -H "X-User-Id: $USER_ID"
```

---

## Extra snippets

### Create session (alternative, no env vars)
```bash
curl -i -X POST "http://localhost:8080/v1/sessions" \
  -H "Content-Type: application/json" \
  -H "X-API-Key: dev-secret" \
  -H "X-User-Id: demo-user-123" \
  -d '{"title":"Production incident","favourite":true}'
```

### Update session (alternative, no env vars)
```bash
curl -i -X PATCH "http://localhost:8080/v1/sessions/11111111-1111-1111-1111-111111111111" \
  -H "Content-Type: application/json" \
  -H "X-API-Key: dev-secret" \
  -H "X-User-Id: demo-user-123" \
  -d '{"title":"Production incident - RCA","favourite":false}'
```
