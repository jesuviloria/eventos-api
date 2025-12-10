# Module 6 - Week 2  
## Persistent Catalog with Validation and Pagination (JPA + PostgreSQL)

## User Story

**User Story Name:** Persistent Catalog with Validation and Pagination (Spring Data JPA → adapted to Quarkus + Panache) [file:2]

**User Story Goal**

As a backend team, we want to replace the in-memory storage with a real database using JPA, add proper validations, and support pagination and filtering in the Events and Venues catalog, so that the API behaves closer to a production-like environment [file:2].
Scope and Tasks
text
## Week 2 Scope

### TASK 1: Persistence with JPA

- Replace in-memory services with real persistence using:
  - JPA + Hibernate ORM with Panache
  - PostgreSQL as the database.
- Create entities:
  - `EventEntity`
  - `VenueEntity`
- Map fields and relationships:
  - One `VenueEntity` has many `EventEntity` (OneToMany).
  - One `EventEntity` belongs to one `VenueEntity` (ManyToOne).
- Add basic constraints:
  - Unique event name.
  - Not null fields for required attributes.
- Configure the datasource in `application.properties` to point to PostgreSQL.
- Use `drop-and-create` for schema generation in dev profile.

### TASK 2: Validations

- Apply Bean Validation on entities and/or DTOs:
  - `@NotBlank`, `@NotNull`, `@Size`, `@Min`, `@Future`, etc.
- Validate:
  - Event name is required and unique.
  - Venue name is required and unique.
  - Capacity is at least 1.
  - Start date is before end date and both are in the future.
- Ensure validation errors return readable messages to the client [file:2].

### TASK 3: Pagination and Filters

- Add pagination to list endpoints:
  - `GET /api/v1/events?page=&size=&sort=`
  - `GET /api/v1/venues?page=&size=&sort=`
- Support optional filters on Events:
  - `city`
  - `category`
  - `venueId`
- Support optional filters on Venues:
  - `city`
  - `minCapacity`
- Make sure pagination and filters work together and are documented in Swagger [file:2].

### TASK 4: Basic Error Handling

- Implement a global error handler to return consistent error responses:
  - Translate `NotFoundException` into `404`.
  - Translate validation errors into `400`.
  - Translate business conflicts (like duplicate names) into `409`.
- Return clear error payloads with:
  - `error`
  - `message`
  - `status`
  - `timestamp` [file:2].
Endpoints (Week 2 Behavior)
text
## Endpoints (with persistence + pagination)

### Events

- `POST /api/v1/events`
  - Creates a new event stored in PostgreSQL.
  - Validations:
    - Name required and unique.
    - Description required.
    - Start and end dates required and in the future.
    - `startDate < endDate`.
    - `venueId` must exist in the database.

- `GET /api/v1/events`
  - Returns a paginated list of events.
  - Query parameters:
    - `page` (default: 0)
    - `size` (default: 10)
    - `sort` (default: `fechaInicio`)
    - Optional filters:
      - `city`
      - `category`
      - `venueId`

- `GET /api/v1/events/{id}`
  - Returns a single event by id.
  - Returns `404` if the event does not exist.

- `PUT /api/v1/events/{id}`
  - Updates an existing event.
  - Validates unique name (excluding the current record).
  - Validates venue existence and dates.

- `DELETE /api/v1/events/{id}`
  - Deletes an event by id.
  - Returns `404` if not found.

### Venues

- `POST /api/v1/venues`
  - Creates a new venue stored in PostgreSQL.
  - Validations:
    - Name required and unique.
    - City required.
    - Capacity >= 1.

- `GET /api/v1/venues`
  - Returns a paginated list of venues.
  - Query parameters:
    - `page` (default: 0)
    - `size` (default: 10)
    - `sort` (default: `nombre`)
    - Optional filters:
      - `city`
      - `minCapacity`

- `GET /api/v1/venues/{id}`
  - Returns a single venue by id.
  - Returns `404` if not found.

- `PUT /api/v1/venues/{id}`
  - Updates an existing venue.
  - Validates unique name (excluding the current record).

- `DELETE /api/v1/venues/{id}`
  - Deletes a venue by id.
  - Returns `404` if not found.
Acceptance Criteria
text
## Acceptance Criteria

- The API uses a real database for persistence:
  - Events and Venues are stored in PostgreSQL (or H2 in test profile) [file:2].
- CRUD operations for Events and Venues work end-to-end through JPA entities and repositories.
- Bean Validation is active:
  - Invalid payloads result in HTTP 400 with meaningful messages.
- Pagination is implemented and functional on list endpoints:
  - `page`, `size`, and `sort` parameters are supported and documented.
- Filters for city, category, venue, and minimum capacity work correctly.
- Error responses are handled globally and return:
  - HTTP 400 for validation issues.
  - HTTP 404 when resources are not found.
  - HTTP 409 for business conflicts (like duplicate names).
- Swagger/OpenAPI documentation reflects:
  - Request/response models.
  - Query parameters for pagination and filters.
  - Possible HTTP status codes per endpoint.
Project Structure (Week 2)
text
## Project Structure (Week 2)

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
├── entity/
│ ├── EventEntity.java
│ └── VenueEntity.java
├── repository/
│ ├── EventRepository.java
│ └── VenueRepository.java
└── exception/
└── GlobalExceptionHandler.java

src/main/resources/
├── application.properties
└── import.sql (optional initial data)

text
undefined
Running the Project (Week 2)
text
## Running the Project (Week 2)

### Requirements

- Java 21
- Maven 3.9+
- Docker + Docker Compose
- PostgreSQL 16+ Docker image (configured via `docker-compose.yml`)

### Steps

1. Start PostgreSQL
docker-compose up -d

2. Build and run the application
./mvnw clean compile
./mvnw quarkus:dev

3. Access Swagger UI
http://localhost:8080/swagger-ui

4. Check OpenAPI spec
http://localhost:8080/swagger

text

### Example Requests

Create a venue
curl -X POST http://localhost:8080/api/v1/venues
-H "Content-Type: application/json"
-d '{
"nombre": "Movistar Arena",
"ciudad": "Bogotá",
"capacidad": 15000,
"direccion": "Calle 63 # 48-45"
}'

Create an event
curl -X POST http://localhost:8080/api/v1/events
-H "Content-Type: application/json"
-d '{
"nombre": "Rock Festival 2025",
"descripcion": "Big rock festival",
"fechaInicio": "2025-12-30T18:00:00",
"fechaFin": "2025-12-30T23:00:00",
"venueId": 1,
"ciudad": "Bogotá",
"categoria": "Music"
}'

List events with pagination
curl "http://localhost:8080/api/v1/events?page=0&size=10&sort=fechaInicio"

text
undefined