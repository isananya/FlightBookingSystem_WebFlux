# Flight Booking System – WebFlux + Reactive MongoDB

A fully reactive airline booking backend built using Spring WebFlux and Reactive MongoDB. The system supports admin-level airline & schedule management, flight search, booking, ticket management, and cancellation workflows.

## Features
This project handles:
- Adding flights & schedules (Admin only)
- Searching flights (one-way & round-trip)
- Creating bookings with seat validation
- Issuing tickets
- Viewing tickets by PNR or email
- Cancelling bookings with 24-hour rule enforcement

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /signup | Registers a new user. |
| POST | /airline/route/add | Adds a new flight route. Requires header: `Admin_key: Admin`. |
| POST | /airline/inventory/add | Adds a new flight schedule. Requires header: `Admin_key: Admin`. |
| POST | /flights/search | Searches flights (one-way or round-trip). |
| POST | /booking | Creates a new booking and generates PNR. |
| DELETE | /booking/cancel/{pnr} | Cancels a booking if allowed. |
| GET | /ticket/{pnr} | Retrieves all tickets for a given PNR. |
| GET | /booking/history/{emailId} | Retrieves all tickets for a given email. |

## Entity Relationship (ER) Diagram 

<img width="508" height="349" alt="ER Diagram" src="https://github.com/user-attachments/assets/f7af474b-2e47-4b74-b467-a5230f1d2304" />

