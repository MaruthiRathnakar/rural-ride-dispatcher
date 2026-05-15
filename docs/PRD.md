# Rural Ride Dispatcher PRD

## 1. Product Summary

Rural Ride Dispatcher is a lightweight web tool for managing bike ride bookings between Bellary and nearby villages. The first version supports a manual operations model where passengers book through phone or WhatsApp, an operator records the ride, assigns a verified rider, and sends WhatsApp-ready messages to riders and passengers.

## 2. Problem

Rural passengers often need reliable village-to-town transport for bus stands, hospitals, markets, colleges, and work trips. Existing public transport is cheaper but may be slow, infrequent, crowded, or unavailable at the exact time needed.

For an early transport business, the biggest operational problem is not building a full app. It is coordinating requests quickly and reliably when multiple passengers and riders contact one central number.

## 3. Goals

- Let one dispatcher handle incoming passenger requests quickly.
- Track available riders and assign them to rides.
- Generate consistent WhatsApp messages for rider groups, passengers, and assigned riders.
- Keep the system simple enough to operate from a laptop or shared counter.
- Store early data without requiring a backend.
- Help validate demand before investing in a full customer app or rider app.

## 4. Non-Goals

- Real-time GPS tracking.
- Online payments.
- Automated WhatsApp API integration.
- Dynamic surge pricing.
- Multi-dispatcher permissions.
- Native Android or iOS apps.
- Production-grade database or authentication.

## 5. Target Users

### Dispatcher

The operator who receives bookings through phone or WhatsApp and assigns riders.

Needs:

- Create rides fast.
- See open rides.
- Know which riders are available.
- Copy accurate messages.
- Export ride records.

### Rider

A verified local bike owner who accepts rides from the dispatcher.

Needs:

- Clear pickup/drop details.
- Fixed fare before accepting.
- Passenger contact only after assignment.
- Simple completion instructions.

### Passenger

A local passenger booking a ride through phone or WhatsApp.

Needs:

- Quick confirmation.
- Rider name and phone.
- Fare clarity.
- Trust that the rider is verified.

## 6. MVP Scope

### Passenger Booking

- Add passenger name and phone number.
- Add pickup, drop, time, distance, fare, passenger count, and notes.
- Auto-create a ride ID.
- Default new rides to `New` status.

### Rider Management

- Add rider name, phone, base area, and vehicle number.
- Mark riders available or unavailable.
- Only available riders should appear in new assignment dropdowns.

### Dispatch Board

- Show all rides in a live board.
- Filter rides by all, new, assigned, and completed.
- Assign a rider to a ride.
- Mark ride as completed.
- Mark ride as cancelled.

### WhatsApp Support

- Copy rider group message.
- Copy passenger confirmation message.
- Copy assigned rider message.
- Open WhatsApp passenger link with the generated message.

### Data

- Store data locally in the browser using `localStorage`.
- Export rides as CSV.
- Include demo data for training and testing.

## 7. Core Workflow

1. Passenger calls or sends WhatsApp message to the central booking number.
2. Dispatcher creates a ride request in the app.
3. Dispatcher copies the rider group message and posts it to the rider WhatsApp group.
4. Rider replies with the ride ID, for example `ACCEPT R104`.
5. Dispatcher assigns that rider in the app.
6. Dispatcher sends confirmation to passenger.
7. Dispatcher sends passenger details to rider.
8. Rider completes trip and reports `DONE R104`.
9. Dispatcher marks ride as completed.

## 8. Pricing Assumption

Initial fare guidance:

- Minimum fare: Rs 40 to Rs 50.
- Normal rural ride: Rs 20 base + Rs 8 to Rs 12 per km.
- If bus fare is Rs 40, bike ride target fare is usually Rs 70 to Rs 100 depending on distance, urgency, and return-empty risk.

Fare calculation in the MVP is a helper only. Dispatcher can override fare manually.

## 9. Success Metrics

### Operational Metrics

- Number of rides created per day.
- Percentage of rides assigned.
- Average time from booking to assignment.
- Completed rides per active rider.
- Cancelled rides.

### Business Metrics

- Repeat passengers.
- Daily gross booking value.
- Dispatcher commission per ride.
- Rider earnings per day.
- Top pickup and drop points.

### Quality Metrics

- Complaints per 100 rides.
- Rider cancellation frequency.
- Passenger no-show frequency.
- Wrong fare or wrong route incidents.

## 10. Functional Requirements

| ID | Requirement | Priority |
| --- | --- | --- |
| FR-1 | Dispatcher can create a new ride request. | Must |
| FR-2 | Dispatcher can add and manage riders. | Must |
| FR-3 | Dispatcher can mark riders available or unavailable. | Must |
| FR-4 | Dispatcher can assign a rider to a ride. | Must |
| FR-5 | Dispatcher can mark ride status as new, assigned, completed, or cancelled. | Must |
| FR-6 | App can generate WhatsApp-ready messages. | Must |
| FR-7 | App can export rides to CSV. | Should |
| FR-8 | App can provide demo data for training. | Should |
| FR-9 | App can calculate suggested fare from distance. | Should |
| FR-10 | App supports mobile layout. | Should |

## 11. Data Model

### Ride

- `id`
- `createdAt`
- `passengerName`
- `passengerPhone`
- `pickup`
- `drop`
- `time`
- `distance`
- `fare`
- `seats`
- `notes`
- `status`
- `riderId`

### Rider

- `id`
- `name`
- `phone`
- `base`
- `vehicle`
- `available`

## 12. Risks

- Browser-local storage can be lost if the browser is cleared.
- One dispatcher may struggle with high ride volume.
- WhatsApp message copy/paste can cause human mistakes.
- Passenger and rider privacy needs careful handling.
- Legal requirements for bike taxi operations vary by location and must be checked before launch.

## 13. Next Product Milestones

### Milestone 1: Manual Dispatcher MVP

- Current static web app.
- Local storage.
- CSV export.
- WhatsApp message helpers.

### Milestone 2: Persistent Operations

- Backend database.
- Login for dispatcher.
- Daily ride reports.
- Rider performance view.

### Milestone 3: WhatsApp Automation

- WhatsApp Business API integration.
- Booking templates.
- Rider acceptance flow.
- Automated passenger updates.

### Milestone 4: Rider App or Rider Web View

- Rider receives assigned rides.
- Rider accepts or rejects.
- Rider marks pickup and completion.

### Milestone 5: Passenger Booking Experience

- Public booking form.
- Fare estimate.
- Saved passenger history.
- Repeat route bookings.

## 14. Open Questions

- What are the first 5 villages to support?
- What are the fixed fares for each route?
- What hours will the service operate?
- Will the first model support women-only rider assignment or family-preferred riders?
- Will riders pay commission per trip or weekly subscription?
- Who settles cash and when?
- What exact legal structure is needed before launch?
