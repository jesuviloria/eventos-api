# Module 6 - Week 1  
## In-Memory Catalog of Events and Venues (REST + Layered Architecture)

## User Story

**User Story Name:** In-Memory Catalog of Events and Venues (REST + Layered Architecture) [file:5]

**User Story Goal**

As a backend team, we want to expose a REST API to create, read, update, and delete Events and Venues using in-memory storage, following a layered architecture and documenting the endpoints with OpenAPI/Swagger [file:5]. This first version will serve as the foundation for future iterations with real persistence and more advanced architecture [file:5].

---

## Week 1 Scope

### TASK 1: Model and Layered Structure

- Create DTOs:
  - `EventDTO`
  - `VenueDTO` [file:5]
- Create in-memory services using `List`/`Map` to simulate persistence:
  - `EventService`
  - `VenueService`
- Create REST controllers:
  - `EventController`
  - `VenueController`
- Handle HTTP status codes properly using `Response`:
  - 200 OK, 201 Created, 204 No Content, 400 Bad Request, 404 Not Found, 409 Conflict [file:5]
- Configure at least two profiles in `application.properties`:
  - `dev`
  - `test`

### TASK 2: REST CRUD for Events and Venues

- Implement endpoints for **Events** [file:5]:
  - `POST /api/v1/events`
  - `GET /api/v1/events`
  - `GET /api/v1/events/{id}`
  - `PUT /api/v1/events/{id}`
  - `DELETE /api/v1/events/{id}`
- Implement endpoints for **Venues** [file:5]:
  - `POST /api/v1/venues`
  - `GET /api/v1/venues`
  - `GET /api/v1/venues/{id}`
  - `PUT /api/v1/venues/{id}`
  - `DELETE /api/v1/venues/{id}`
- Implement basic filters:
  - Events:
    - `?city=...`
    - `?category=...`
    - `?venueId=...`
  - Venues:
    - `?city=...`
    - `?minCapacity=...`
- Add Bean Validation on DTOs:
  - `@NotBlank`, `@Size`, `@NotNull`, `@Min`, `@Future`, etc. [file:5]

### TASK 3: Documentation and Basic Error Handling

- Configure OpenAPI/Swagger UI in `application.properties`:
  - `quarkus.smallrye-openapi.path=/swagger`
  - `quarkus.swagger-ui.path=/swagger-ui`
- Document each endpoint using OpenAPI annotations:
  - `@Operation`, `@APIResponse`, `@Schema`, `@Parameter`
- Provide examples of request/response in the documentation where possible [file:5].
- Implement basic error handling:
  - 400 for validation errors or bad input.
  - 404 when an Event or Venue is not found.
  - 409 for business conflicts (for example, duplicated names).

---

## Endpoints

### Events

- `POST /api/v1/events`  
  Creates a new Event (in-memory).  
  Validations:
  - Name required and unique.
  - Description required.
  - Start/end dates must be in the future.
  - `startDate` must be before `endDate`.
  - `venueId` required.

- `GET /api/v1/events`  
  Returns all Events.  
  Optional filters:
  - `?city=...`
  - `?category=...`
  - `?venueId=...`

- `GET /api/v1/events/{id}`  
  Returns a single Event by id.

- `PUT /api/v1/events/{id}`  
  Updates an existing Event.

- `DELETE /api/v1/events/{id}`  
  Deletes an Event by id.

### Venues

- `POST /api/v1/venues`  
  Creates a new Venue (in-memory).  
  Validations:
  - Name required and unique.
  - City required.
  - Capacity >= 1.

- `GET /api/v1/venues`  
  Returns all Venues.  
  Optional filters:
  - `?city=...`
  - `?minCapacity=...`

- `GET /api/v1/venues/{id}`  
  Returns a single Venue by id.

- `PUT /api/v1/venues/{id}`  
  Updates an existing Venue.

- `DELETE /api/v1/venues/{id}`  
  Deletes a Venue by id.

---

## Acceptance Criteria

- The catalog supports full CRUD for **Events** and **Venues** using in-memory storage (no real database yet) [file:5].
- The API follows REST principles:
  - Proper use of HTTP methods.
  - Coherent HTTP status codes (200, 201, 204, 400, 404, 409).
- Swagger UI exposes the documentation in a clear and navigable way [file:5].
- The project structure is organized at least by:
  - Controller
  - Service
  - DTO
- Data is kept in memory during the application runtime.
- Input is validated using Bean Validation on DTOs.

---

## Project Structure (Week 1)

src/main/java/com/tiquetera/
├── controller/
│ ├── EventController.java
│ └── VenueController.java
├── service/
│ ├── EventService.java
│ └── VenueService.java
├── dto/
│ ├── EventDTO.java
│ └── VenueDTO.java
└── config/
└── DataLoader.java (optional sample data)

text

---

## Running the Project (Week 1)

### Requirements

- Java 21
- Maven 3.9+
- Quarkus

### Commands

Run in dev mode
./mvnw quarkus:dev

Build
./mvnw clean package

Run tests
./mvnw test

text

### Swagger / OpenAPI

- OpenAPI JSON/YAML:
  - `http://localhost:8080/swagger`
- Swagger UI:
  - `http://localhost:8080/swagger-ui`