# Rural Bike Pool Android App PRD

## 1. Product Summary

Rural Bike Pool is a peer-to-peer Android mobility platform for rural and semi-urban routes. It helps bike owners share planned or on-demand rides with passengers traveling between villages, towns, bus stands, colleges, hospitals, markets, and work locations.

The product should feel operationally similar to Quick Ride's carpool/ride-sharing behavior, but optimized for rural bike pooling. The long-term architecture can borrow proven ideas from Uber-style ride platforms: location-aware matching, trip lifecycle, safety, payments, ratings, and scalable backend services.

## 2. Product Positioning

This is not a pure bike taxi app at the start. The stronger rural positioning is:

> A trusted bike pool network where verified local bike owners can share rides with nearby passengers on common rural routes.

This matters because peer-to-peer pooling can be more acceptable, cheaper, and easier to build trust around than anonymous commercial bike taxi dispatch.

## 3. Problem

Rural passengers often face:

- Infrequent buses.
- Long wait times.
- Poor first-mile and last-mile access.
- Expensive autos for single passengers.
- Difficulty reaching hospitals, colleges, markets, bus stands, and work locations at the right time.

Bike owners often travel with empty seats or can accept nearby passengers on regular routes, but there is no trusted matching layer for rural users.

## 4. Goals

- Build an Android-first app for passengers and bike owners.
- Support peer-to-peer bike pooling for rural routes.
- Verify riders before they can accept passengers.
- Match passengers with riders based on pickup, drop, route, time, and availability.
- Support fixed or suggested fare sharing.
- Provide safety and trust features from the first release.
- Collect route demand data before expanding villages.

## 5. Non-Goals For MVP

- iOS app.
- Full Uber-scale microservices.
- Real-time surge pricing.
- In-app wallet.
- Advanced driver incentives.
- Complex route optimization across many passengers.
- Fully automated legal/commercial compliance handling.
- Anonymous unverified riders.

## 6. Target Users

### Passenger

A rural or semi-urban user who needs a ride from village to town or town to village.

Common trips:

- Village to Bellary bus stand.
- Village to hospital.
- Village to college.
- Market and work commute.
- Return trip from town to village.

### Bike Owner / Pool Rider

A verified local bike owner willing to share a ride for fuel-cost contribution or additional income.

Common patterns:

- Daily commute.
- Regular village-to-town route.
- Accept nearby one-off requests.
- Offer planned ride at a fixed time.

### Admin / Operations Team

The business operator who verifies riders, monitors trips, handles complaints, sets route/fare policies, and manages local growth.

## 7. MVP Scope

### Passenger App

- Sign up with phone OTP.
- Create profile with name, gender optional, emergency contact optional.
- Search ride by pickup, drop, date/time, and passenger count.
- See available bike pool rides.
- Request a ride.
- Receive rider details after confirmation.
- Call or message rider.
- Track trip status.
- Rate rider after trip.
- Report issue.

### Bike Owner App

- Sign up with phone OTP.
- Submit verification documents:
  - Name.
  - Phone.
  - Bike number.
  - Driving license.
  - RC.
  - Insurance.
  - Profile photo.
- Set availability.
- Create planned ride:
  - Start point.
  - End point.
  - Departure time.
  - Seats available.
  - Fare contribution.
- Accept passenger requests.
- Start trip.
- Complete trip.
- Rate passenger.

### Admin Console

MVP admin can start as a web console.

- View users.
- Approve or reject bike owners.
- View rides.
- View trips.
- Handle complaints.
- Configure service routes.
- Configure fare guidance.

### Matching

MVP matching should be simple:

- Exact or nearby pickup area.
- Exact or nearby drop area.
- Departure time window.
- Rider availability.
- Seat availability.
- Rider verification status.

### Payments

MVP should support cash or UPI outside the app.

The app records:

- Fare amount.
- Payment mode.
- Payment status manually confirmed by rider/passenger.

In-app payments can come later.

## 8. Core Workflows

### Planned Pool Ride

1. Bike owner posts a ride from village to Bellary at 8:00 AM.
2. Passenger searches the same route.
3. Passenger requests seat.
4. Rider accepts.
5. Passenger receives rider and bike details.
6. Rider starts trip.
7. Rider completes trip.
8. Both users rate each other.

### On-Demand Ride Request

1. Passenger requests a ride now.
2. Nearby verified riders receive request.
3. One rider accepts.
4. Passenger confirms.
5. Trip starts and completes.

MVP can launch with planned pool rides first, then add on-demand matching.

## 9. Fare Model

The fare should be a contribution model, not just taxi pricing.

Initial guidance:

- Minimum fare: Rs 40 to Rs 50.
- Suggested fare: Rs 20 base + Rs 8 to Rs 12 per km.
- If bus fare is Rs 40, target pooled bike contribution is usually Rs 60 to Rs 90.
- Regular commute subscriptions can be cheaper per ride.

Admin should be able to set route-based fare guidance, and riders should not exceed configured maximums.

## 10. Trust And Safety

MVP must include:

- Phone OTP.
- Rider document verification.
- Rider profile photo.
- Bike number visible to passenger after match.
- Emergency contact support.
- Trip status trail.
- User ratings.
- Complaint reporting.
- Admin ability to suspend users.

Later:

- Live location sharing.
- SOS button.
- Women-preferred rider options.
- Trusted route captains.
- Ride PIN before trip start.

## 11. Functional Requirements

| ID | Requirement | Priority |
| --- | --- | --- |
| FR-1 | Passenger can register/login using phone OTP. | Must |
| FR-2 | Rider can register/login using phone OTP. | Must |
| FR-3 | Rider can submit verification documents. | Must |
| FR-4 | Admin can approve verified riders. | Must |
| FR-5 | Rider can create a planned bike pool ride. | Must |
| FR-6 | Passenger can search available rides. | Must |
| FR-7 | Passenger can request a seat. | Must |
| FR-8 | Rider can accept or reject passenger request. | Must |
| FR-9 | App can manage trip statuses. | Must |
| FR-10 | Users can call/message after match. | Must |
| FR-11 | Passenger and rider can rate each other. | Should |
| FR-12 | Admin can view rides, users, and complaints. | Should |
| FR-13 | App can suggest fare by route or distance. | Should |
| FR-14 | App can support cash/UPI status tracking. | Should |

## 12. Suggested MVP Tech Stack

### Android

- Kotlin.
- Jetpack Compose.
- MVVM architecture.
- Coroutines and Flow.
- Retrofit or Ktor client.
- Room for local cache.
- Google Maps SDK or Mappls/MapmyIndia depending on cost and India coverage.
- Firebase Cloud Messaging for notifications.

### Backend

- Node.js with NestJS or Java/Kotlin with Spring Boot.
- PostgreSQL with PostGIS for location queries.
- Redis for live availability and short-lived matching state.
- REST API first; WebSockets later for live trip updates.
- Object storage for documents and profile photos.

### Admin Web

- React or Next.js.
- Role-based admin login.
- Document review and trip monitoring.

### Infrastructure

- Start simple on one cloud provider.
- Dockerized backend.
- Managed PostgreSQL.
- Managed Redis.
- CI/CD from GitHub.

## 13. MVP Data Model

### User

- `id`
- `phone`
- `name`
- `role`
- `profilePhotoUrl`
- `status`
- `createdAt`

### RiderProfile

- `userId`
- `bikeNumber`
- `licenseUrl`
- `rcUrl`
- `insuranceUrl`
- `verificationStatus`
- `baseVillage`
- `rating`

### Route

- `id`
- `startArea`
- `endArea`
- `distanceKm`
- `suggestedFare`
- `maxFare`

### RideOffer

- `id`
- `riderId`
- `startArea`
- `endArea`
- `startLat`
- `startLng`
- `endLat`
- `endLng`
- `departureTime`
- `availableSeats`
- `fare`
- `status`

### RideRequest

- `id`
- `passengerId`
- `rideOfferId`
- `pickupArea`
- `dropArea`
- `requestedSeats`
- `status`

### Trip

- `id`
- `rideOfferId`
- `passengerId`
- `riderId`
- `status`
- `fare`
- `paymentMode`
- `paymentStatus`
- `startedAt`
- `completedAt`

## 14. Milestones

### Milestone 0: Product Foundation

- Finalize PRD.
- Define launch geography.
- Define first 10 routes.
- Define legal/compliance assumptions.
- Create UX wireframes.

### Milestone 1: Android Prototype

- Kotlin Compose app shell.
- Passenger/rider role selection.
- Static route search.
- Mock ride offers.
- Mock request/accept flow.

### Milestone 2: Backend MVP

- Auth.
- User profiles.
- Rider verification upload.
- Ride offer creation.
- Ride request and acceptance APIs.

### Milestone 3: Admin Console

- Rider approval.
- Ride/trip list.
- Complaint list.
- Route/fare configuration.

### Milestone 4: Field Pilot

- Launch with 20 to 50 verified riders.
- Launch on 5 to 10 routes.
- Track completed trips, cancellations, complaints, and repeat usage.

### Milestone 5: Live Matching And Safety

- Push notifications.
- Live rider availability.
- SOS and trip sharing.
- Better route matching.

## 15. Success Metrics

- Verified riders onboarded.
- Ride offers created per day.
- Passenger searches per day.
- Request-to-acceptance rate.
- Completed trips per day.
- Cancellation rate.
- Repeat passenger rate.
- Average rider earnings.
- Complaints per 100 trips.

## 16. Open Questions

- Should MVP start with planned bike pool rides only, or planned plus on-demand?
- What exact Bellary routes should launch first?
- Should women passengers be able to choose women-preferred or trusted riders?
- Should fare be fixed by platform or chosen by rider within a limit?
- What documents are legally required for this model in Karnataka?
- Should the first app support Kannada first, then English/Hindi/Telugu?
- How will offline users book if they do not use the app?
