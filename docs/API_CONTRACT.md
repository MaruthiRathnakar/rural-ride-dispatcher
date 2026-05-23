# API Contract

MVP API contract for the Rural Bike Pool Android app and admin console.

The backend should start as a modular monolith. These endpoints are grouped by module but can live in one service.

## 1. Conventions

Base URL:

```text
https://api.ruralbikepool.example.com/v1
```

Authentication:

```http
Authorization: Bearer <access_token>
```

Response envelope:

```json
{
  "data": {},
  "meta": {},
  "error": null
}
```

Error envelope:

```json
{
  "data": null,
  "meta": {},
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Invalid request",
    "details": []
  }
}
```

## 2. Domain Enums

```text
UserRole: passenger, rider, admin
UserStatus: active, suspended, deleted
VerificationStatus: not_submitted, pending, approved, rejected
RideOfferStatus: draft, active, full, started, completed, cancelled
RideRequestStatus: requested, accepted, rejected, cancelled, expired
TripStatus: confirmed, rider_on_way, started, completed, cancelled, disputed
PaymentMode: cash, upi_external
PaymentStatus: pending, paid, disputed
ComplaintStatus: open, reviewing, resolved, dismissed
```

## 3. Auth

### Send OTP

```http
POST /auth/otp
```

Request:

```json
{
  "phone": "9876543210"
}
```

Response:

```json
{
  "data": {
    "otpRequestId": "otp_123",
    "expiresInSeconds": 300
  },
  "meta": {},
  "error": null
}
```

### Verify OTP

```http
POST /auth/otp/verify
```

Request:

```json
{
  "otpRequestId": "otp_123",
  "otp": "123456"
}
```

Response:

```json
{
  "data": {
    "accessToken": "jwt",
    "refreshToken": "refresh",
    "user": {
      "id": "usr_123",
      "phone": "9876543210",
      "name": null,
      "roles": ["passenger"],
      "status": "active"
    }
  },
  "meta": {},
  "error": null
}
```

### Refresh Token

```http
POST /auth/refresh
```

Request:

```json
{
  "refreshToken": "refresh"
}
```

## 4. Users And Profiles

### Get Current User

```http
GET /me
```

### Update Profile

```http
PATCH /me
```

Request:

```json
{
  "name": "Suresh",
  "gender": "male",
  "preferredLanguage": "kn",
  "emergencyContactPhone": "9876500000"
}
```

### Switch Active Role

```http
POST /me/active-role
```

Request:

```json
{
  "role": "rider"
}
```

Rules:

- Rider role can be active only after rider profile exists.
- Accepting rides requires approved verification.

## 5. Rider Verification

### Get Rider Profile

```http
GET /rider-profile
```

### Create Or Update Rider Profile

```http
PUT /rider-profile
```

Request:

```json
{
  "bikeNumber": "KA34AB1234",
  "baseVillage": "Village X",
  "profilePhotoFileId": "file_profile",
  "licenseFileId": "file_license",
  "rcFileId": "file_rc",
  "insuranceFileId": "file_insurance"
}
```

### Submit Rider Profile For Review

```http
POST /rider-profile/submit
```

Response:

```json
{
  "data": {
    "verificationStatus": "pending"
  },
  "meta": {},
  "error": null
}
```

## 6. Files

### Create Upload URL

```http
POST /files/upload-url
```

Request:

```json
{
  "purpose": "rider_license",
  "contentType": "image/jpeg"
}
```

Response:

```json
{
  "data": {
    "fileId": "file_123",
    "uploadUrl": "https://storage.example.com/signed-url",
    "expiresInSeconds": 600
  },
  "meta": {},
  "error": null
}
```

## 7. Routes And Fare Guidance

### Search Areas

```http
GET /areas?query=Bellary
```

Response:

```json
{
  "data": [
    {
      "id": "area_bellary_bus_stand",
      "name": "Bellary Bus Stand",
      "type": "bus_stand",
      "lat": 15.1394,
      "lng": 76.9214
    }
  ],
  "meta": {},
  "error": null
}
```

### Get Fare Estimate

```http
GET /fare-estimates?pickupAreaId=area_village_x&dropAreaId=area_bellary_bus_stand&seats=1
```

Response:

```json
{
  "data": {
    "distanceKm": 10,
    "suggestedFare": 80,
    "minFare": 60,
    "maxFare": 100,
    "currency": "INR"
  },
  "meta": {},
  "error": null
}
```

## 8. Ride Offers

### Create Ride Offer

```http
POST /ride-offers
```

Request:

```json
{
  "startAreaId": "area_village_x",
  "endAreaId": "area_bellary_bus_stand",
  "pickupNote": "Village X temple",
  "dropNote": "Main bus stand gate",
  "departureTime": "2026-05-24T08:10:00+05:30",
  "availableSeats": 1,
  "fare": 80
}
```

Response:

```json
{
  "data": {
    "id": "offer_123",
    "status": "active"
  },
  "meta": {},
  "error": null
}
```

Rules:

- Rider must be approved.
- Fare must be within route configured limits unless admin override exists.

### Search Ride Offers

```http
GET /ride-offers/search?startAreaId=area_village_x&endAreaId=area_bellary_bus_stand&departureFrom=2026-05-24T07:30:00+05:30&departureTo=2026-05-24T09:00:00+05:30&seats=1
```

Response:

```json
{
  "data": [
    {
      "id": "offer_123",
      "startArea": {
        "id": "area_village_x",
        "name": "Village X"
      },
      "endArea": {
        "id": "area_bellary_bus_stand",
        "name": "Bellary Bus Stand"
      },
      "departureTime": "2026-05-24T08:10:00+05:30",
      "availableSeats": 1,
      "fare": 80,
      "rider": {
        "id": "usr_rider_1",
        "name": "Ramesh",
        "rating": 4.8,
        "verificationStatus": "approved",
        "bikeNumberMasked": "KA34 ** 1234"
      }
    }
  ],
  "meta": {
    "count": 1
  },
  "error": null
}
```

Privacy:

- Do not return rider phone before accepted request.

### Get Ride Offer Details

```http
GET /ride-offers/{offerId}
```

### Cancel Ride Offer

```http
POST /ride-offers/{offerId}/cancel
```

## 9. Ride Requests

### Request Seat

```http
POST /ride-offers/{offerId}/requests
```

Request:

```json
{
  "pickupNote": "Village X temple",
  "dropNote": "Bellary bus stand main gate",
  "requestedSeats": 1
}
```

Response:

```json
{
  "data": {
    "id": "req_123",
    "status": "requested"
  },
  "meta": {},
  "error": null
}
```

### List My Ride Requests

```http
GET /ride-requests?scope=mine&status=requested
```

### Accept Ride Request

```http
POST /ride-requests/{requestId}/accept
```

Response:

```json
{
  "data": {
    "requestId": "req_123",
    "status": "accepted",
    "tripId": "trip_123"
  },
  "meta": {},
  "error": null
}
```

### Reject Ride Request

```http
POST /ride-requests/{requestId}/reject
```

Request:

```json
{
  "reason": "not_on_route"
}
```

### Cancel Ride Request

```http
POST /ride-requests/{requestId}/cancel
```

## 10. Trips

### Get My Trips

```http
GET /trips?scope=mine&status=confirmed
```

### Get Trip Details

```http
GET /trips/{tripId}
```

Accepted trip response includes contact details:

```json
{
  "data": {
    "id": "trip_123",
    "status": "confirmed",
    "fare": 80,
    "paymentMode": "cash",
    "paymentStatus": "pending",
    "passenger": {
      "id": "usr_passenger_1",
      "name": "Suresh",
      "phone": "9876543210",
      "rating": 4.6
    },
    "rider": {
      "id": "usr_rider_1",
      "name": "Ramesh",
      "phone": "9876500000",
      "bikeNumber": "KA34AB1234",
      "rating": 4.8
    },
    "route": {
      "startAreaName": "Village X",
      "endAreaName": "Bellary Bus Stand",
      "pickupNote": "Village X temple",
      "dropNote": "Bellary bus stand main gate"
    }
  },
  "meta": {},
  "error": null
}
```

### Mark Rider On Way

```http
POST /trips/{tripId}/rider-on-way
```

### Start Trip

```http
POST /trips/{tripId}/start
```

### Complete Trip

```http
POST /trips/{tripId}/complete
```

Request:

```json
{
  "paymentMode": "cash",
  "paymentStatus": "paid"
}
```

### Cancel Trip

```http
POST /trips/{tripId}/cancel
```

Request:

```json
{
  "reason": "passenger_no_show"
}
```

## 11. Ratings

### Create Rating

```http
POST /ratings
```

Request:

```json
{
  "tripId": "trip_123",
  "ratedUserId": "usr_rider_1",
  "score": 5,
  "comment": "On time and polite"
}
```

## 12. Complaints

### Create Complaint

```http
POST /complaints
```

Request:

```json
{
  "tripId": "trip_123",
  "category": "safety",
  "description": "Rider did not follow agreed route"
}
```

### List My Complaints

```http
GET /complaints?scope=mine
```

## 13. Admin API

Admin endpoints require `admin` role.

### List Pending Rider Verifications

```http
GET /admin/rider-verifications?status=pending
```

### Approve Rider

```http
POST /admin/rider-verifications/{userId}/approve
```

### Reject Rider

```http
POST /admin/rider-verifications/{userId}/reject
```

Request:

```json
{
  "reason": "Insurance document is unclear"
}
```

### List Trips

```http
GET /admin/trips?date=2026-05-24&status=completed
```

### List Complaints

```http
GET /admin/complaints?status=open
```

### Create Route

```http
POST /admin/routes
```

Request:

```json
{
  "startAreaId": "area_village_x",
  "endAreaId": "area_bellary_bus_stand",
  "distanceKm": 10,
  "suggestedFare": 80,
  "minFare": 60,
  "maxFare": 100
}
```

## 14. Open API Decisions

- OTP provider selection.
- Whether admin auth is phone OTP or email/password.
- Whether live trip updates should use polling first or WebSockets.
- Whether image upload should go directly to object storage or through backend.
- Whether location matching starts with area IDs only or lat/lng radius search.
