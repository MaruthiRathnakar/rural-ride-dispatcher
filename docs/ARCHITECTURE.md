# Architecture Plan

## 1. Architecture Principle

Use Uber-style platform ideas without copying Uber-scale complexity too early.

The product needs clear boundaries:

- Mobile apps for passenger and bike owner workflows.
- Backend APIs for identity, rides, matching, and trips.
- Admin console for verification and operations.
- Data layer that can support geospatial search and future analytics.

## 2. High-Level System

```text
Android App
  Passenger mode
  Rider mode
      |
      v
Backend API
  Auth
  Users
  Rider verification
  Ride offers
  Ride requests
  Trips
  Ratings
  Complaints
      |
      v
PostgreSQL + PostGIS
Redis
Object Storage
Notification Provider
      ^
      |
Admin Web Console
```

## 3. Android App

Recommended stack:

- Kotlin.
- Jetpack Compose.
- MVVM.
- Coroutines and Flow.
- Retrofit or Ktor client.
- Room for local persistence.
- Firebase Cloud Messaging.
- Maps SDK based on India coverage and cost.

Initial app modules:

- `auth`
- `profile`
- `passenger`
- `rider`
- `rides`
- `trips`
- `support`

## 4. Backend

Recommended stack:

- NestJS with TypeScript, or Spring Boot with Kotlin/Java.
- PostgreSQL with PostGIS.
- Redis.
- S3-compatible object storage.
- Firebase Cloud Messaging for push notifications.

Suggested service boundaries at MVP:

- Auth module.
- User module.
- Rider verification module.
- Ride offer module.
- Ride request module.
- Trip module.
- Admin module.

Start as a modular monolith. Split into microservices only after real scale or team growth demands it.

## 5. Matching Model

MVP matching:

- Match by start and end area.
- Add distance radius around pickup/drop later.
- Filter by departure time window.
- Filter by verified rider status.
- Filter by available seats.
- Sort by distance, departure time, fare, and rider rating.

Future matching:

- Route corridor matching.
- Live rider location.
- Batch pooling.
- Reliability score.

## 6. Trip State Machine

```text
RideOffer: draft -> active -> full -> started -> completed
RideRequest: requested -> accepted -> rejected -> cancelled
Trip: confirmed -> rider_on_way -> started -> completed -> disputed
```

Every transition should be timestamped.

## 7. Security And Safety

- Phone OTP login.
- JWT access tokens with refresh tokens.
- Admin role-based access.
- Store documents in private object storage.
- Do not expose personal phone numbers before a match.
- Keep audit logs for verification, complaints, and user suspensions.

## 8. Offline And Rural Constraints

Rural usage needs low-friction behavior:

- Low bandwidth screens.
- Local cache for recent trips.
- Retry failed network requests.
- SMS/phone fallback later.
- Kannada-first language support if launching around Bellary.

## 9. Development Sequence

1. Wireframes.
2. API contract.
3. Android static prototype.
4. Backend auth and profile APIs.
5. Ride offer and request APIs.
6. Admin verification console.
7. Notifications.
8. Field pilot instrumentation.

Current planning artifacts:

- `docs/WIREFRAMES.md`
- `docs/API_CONTRACT.md`
- `docs/openapi.mvp.yaml`

## 10. What Not To Build Yet

- Surge pricing.
- Complex microservices.
- In-app wallet.
- AI matching.
- Full route optimization.
- Multi-city setup.
- Native iOS app.
