# Ticket Sales API

A full-stack ticket sales platform built with Spring Boot, featuring real-time seat availability updates, concurrent booking protection, and email notifications.

## Tech Stack

- **Java 17** + **Spring Boot 4.0.6**
- **Spring Security** + **JWT** authentication
- **Spring Data JPA** + **PostgreSQL** — data persistence
- **WebSocket** (STOMP) — real-time seat status broadcast
- **Optimistic Locking** — concurrent booking protection
- **Redis** — temporary seat hold with TTL
- **JavaMail** — booking confirmation emails

## Features

- User registration and login with JWT tokens
- Browse events and view interactive seat maps
- Real-time seat availability updates via WebSocket
- 10-minute seat hold using Redis TTL
- Concurrent booking protection with Optimistic Locking
- Email confirmation after successful booking

## Architecture

Controller → Service → Repository → PostgreSQL
↓
WebSocket Broadcast (STOMP)
↓
Redis (seat hold TTL)
↓
Email Notification

## Running Locally

### Prerequisites
- Java 17
- Docker

### Start dependencies
```bash
docker run --name tickets-db -e POSTGRES_DB=ticketsales -e POSTGRES_USER=tickets -e POSTGRES_PASSWORD=tickets123 -p 5432:5432 -d postgres:16

docker run --name tickets-redis -p 6379:6379 -d redis:7
```

### Run the application
```bash
./mvnw spring-boot:run
```

API runs on `http://localhost:8080`

## API Endpoints

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | /api/auth/register | Register new user | No |
| POST | /api/auth/login | Login | No |
| GET | /api/events | List active events | Yes |
| GET | /api/events/{id} | Get event details | Yes |
| GET | /api/events/{id}/seats | Get seats for event | Yes |
| POST | /api/bookings/hold | Hold seat for 10 min | Yes |
| POST | /api/bookings | Create booking | Yes |
| GET | /api/bookings/my | Get user bookings | Yes |

## Key Technical Decisions

**Optimistic Locking** — `@Version` field on `Seat` entity detects concurrent modifications without database-level locking, returning 409 Conflict when two users attempt to book the same seat simultaneously.

**Redis TTL** — Seat holds use `setIfAbsent` (atomic operation) with 10-minute expiry. No scheduled jobs needed — Redis handles cleanup automatically.

**WebSocket** — STOMP over SockJS broadcasts seat status changes to all subscribers of `/topic/events/{id}` on every hold and booking action.