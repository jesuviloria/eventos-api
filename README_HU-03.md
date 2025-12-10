# Module 6 - Week 3  
## Hexagonal Refactor with Functional Equivalence (Ports & Adapters)

## User Story

**User Story Name:** Hexagonal Refactor with Functional Equivalence (Ports & Adapters) [file:4]

**User Story Goal**

As a development team, we want to restructure the current project toward a **hexagonal architecture**, separating the business core from frameworks (Quarkus, JPA, etc.) to achieve technological independence, better maintainability, and easier unit testing without a database [file:4].

The goal is to maintain the same functional behavior while gaining architectural flexibility for future changes and testing strategies.

---

## Week 3 Scope

### TASK 1: Analysis and Package Reorganization

**Description:**

- Create a base structure with the following packages [file:4]:
  - `domain/` → Pure business logic and entities (no framework dependencies)
  - `domain/ports/in/` → Input ports (use cases interfaces)
  - `domain/ports/out/` → Output ports (repository interfaces)
  - `application/usecase/` → Use case implementations
  - `infrastructure/adapter/in/web/` → REST controllers
  - `infrastructure/adapter/out/persistence/` → JPA persistence adapters
  - `infrastructure/config/` → Configuration beans

- Eliminate cross-layer dependencies (domain must not import from infrastructure) [file:4].
- Maintain functional equivalence with the existing CRUD from Week 2.

### TASK 2: Port Creation (Ports)

**Description:**

- Create interfaces in `domain/ports/in/` and `domain/ports/out/` [file:4].
  - **Input ports:** Define the use cases the system exposes (e.g., `CreateEventUseCase`, `FindEventUseCase`).
  - **Output ports:** Define the dependencies needed (e.g., `EventRepositoryPort`, `VenueRepositoryPort`).

- Inject dependencies in use cases through **interfaces, not implementations** [file:4].
- Implement use cases in `application/usecase/` following **SOLID principles** [file:4].

### TASK 3: Adapters and Mapping with MapStruct

**Description:**

- Implement concrete adapters for the ports [file:4]:
  - `EventJpaAdapter` (implements `EventRepositoryPort`)
  - `VenueJpaAdapter` (implements `VenueRepositoryPort`)
  - `EventController` (REST adapter for input)
  - `VenueController` (REST adapter for input)

- Integrate **MapStruct** to convert between persistence entity and domain model [file:4].
- The domain **must not have JPA annotations or framework dependencies** [file:4].
- Use DTOs in the REST layer (`EventRequest`, `EventResponse`, `VenueRequest`, `VenueResponse`).

---

## Architecture Overview

### Hexagonal Architecture Layers

┌─────────────────────────────────────────────────────────┐
│ Infrastructure │
│ ┌─────────────────┐ ┌─────────────────┐ │
│ │ REST Adapters │ │ JPA Adapters │ │
│ │ (Controllers) │ │ (Repositories) │ │
│ └────────┬────────┘ └────────┬────────┘ │
│ │ │ │
└───────────┼────────────────────────────────┼────────────┘
│ │
│ ┌──────────────┐ │
└───────►│ Use Cases │◄───────┘
│ (Application) │
└───────┬───────┘
│
┌───────▼────────┐
│ Domain Core │
│ (Business Logic)│
└─────────────────┘

text

### Package Structure

src/main/java/com/tiquetera/
├── domain/ # Pure business logic
│ ├── model/
│ │ ├── Event.java # Domain model (no JPA annotations)
│ │ └── Venue.java
│ └── ports/
│ ├── in/ # Input ports (use cases)
│ │ ├── CreateEventUseCase.java
│ │ ├── FindEventUseCase.java
│ │ ├── UpdateEventUseCase.java
│ │ ├── DeleteEventUseCase.java
│ │ ├── CreateVenueUseCase.java
│ │ ├── FindVenueUseCase.java
│ │ ├── UpdateVenueUseCase.java
│ │ └── DeleteVenueUseCase.java
│ └── out/ # Output ports (repositories)
│ ├── EventRepositoryPort.java
│ └── VenueRepositoryPort.java
├── application/ # Application logic
│ └── usecase/
│ ├── event/
│ │ ├── CreateEventUseCaseImpl.java
│ │ ├── FindEventUseCaseImpl.java
│ │ ├── UpdateEventUseCaseImpl.java
│ │ └── DeleteEventUseCaseImpl.java
│ └── venue/
│ ├── CreateVenueUseCaseImpl.java
│ ├── FindVenueUseCaseImpl.java
│ ├── UpdateVenueUseCaseImpl.java
│ └── DeleteVenueUseCaseImpl.java
└── infrastructure/ # Framework adapters
├── adapter/
│ ├── in/
│ │ └── web/
│ │ ├── EventController.java
│ │ ├── VenueController.java
│ │ └── dto/
│ │ ├── EventRequest.java
│ │ ├── EventResponse.java
│ │ ├── VenueRequest.java
│ │ └── VenueResponse.java
│ └── out/
│ └── persistence/
│ ├── EventJpaAdapter.java
│ ├── VenueJpaAdapter.java
│ ├── entity/
│ │ ├── EventEntity.java
│ │ └── VenueEntity.java
│ ├── repository/
│ │ ├── EventJpaRepository.java
│ │ └── VenueJpaRepository.java
│ └── mapper/
│ ├── EventMapper.java
│ └── VenueMapper.java
└── config/
├── BeanConfiguration.java
└── GlobalExceptionHandler.java

text

---

## Key Concepts

### Domain Layer
- **Pure business logic** with no external dependencies.
- Contains domain models (`Event`, `Venue`) without JPA annotations.
- Defines **ports** (interfaces) that other layers must implement.
- Business validation rules are in the domain models.

### Application Layer
- Contains **use case implementations**.
- Orchestrates domain logic and calls repository ports.
- Each use case has a single responsibility (SRP from SOLID).

### Infrastructure Layer
- Contains **adapters** that connect the application to external systems.
- **Input adapters**: REST controllers that receive HTTP requests.
- **Output adapters**: JPA adapters that persist data to PostgreSQL.
- Uses **MapStruct** to map between domain models and JPA entities.

---

## Endpoints (Week 3 - Same as Week 2)

The API maintains the same functional behavior as Week 2 [file:4]:

### Events

- `POST /api/v1/events` - Create a new event
- `GET /api/v1/events` - List events with pagination and filters
- `GET /api/v1/events/{id}` - Get event by ID
- `PUT /api/v1/events/{id}` - Update event
- `DELETE /api/v1/events/{id}` - Delete event

### Venues

- `POST /api/v1/venues` - Create a new venue
- `GET /api/v1/venues` - List venues with pagination and filters
- `GET /api/v1/venues/{id}` - Get venue by ID
- `PUT /api/v1/venues/{id}` - Update venue
- `DELETE /api/v1/venues/{id}` - Delete venue

---

## Acceptance Criteria

- The application maintains the same functional behavior as before the refactor [file:4].
- The domain is completely decoupled from frameworks and persistence technology [file:4].
- Correct use of **ports and adapters** pattern is evident [file:4].
- **MapStruct** performs conversion between entity and domain [file:4].
- The REST API continues working without breaking existing endpoints [file:4].
- Project documentation reflects the new architecture [file:4].

---

## Technologies Used

- **Java 21**
- **Quarkus 3.6.4**
- **Hibernate ORM with Panache**
- **PostgreSQL 16**
- **MapStruct 1.5.5** (for domain ↔ entity mapping)
- **Lombok** (for reducing boilerplate)
- **Bean Validation**
- **OpenAPI/Swagger**
- **Docker Compose**

---

## Running the Project (Week 3)

### Requirements

- Java 21
- Maven 3.9+
- Docker + Docker Compose
- PostgreSQL 16 (via Docker)

### Commands

1. Start PostgreSQL
docker-compose up -d

2. Build and run
./mvnw clean compile
./mvnw quarkus:dev

3. Access Swagger UI
http://localhost:8080/swagger-ui

4. Test endpoints (same as Week 2)
curl -X POST http://localhost:8080/api/v1/venues
-H "Content-Type: application/json"
-d '{
"nombre": "Movistar Arena",
"ciudad": "Bogotá",
"capacidad": 15000,
"direccion": "Calle 63 # 48-45"
}'

text

---

## Key Differences from Week 2

| Aspect | Week 2 | Week 3 (Hexagonal) |
|--------|--------|-------------------|
| **Architecture** | Layered (Controller → Service → Repository) | Hexagonal (Ports & Adapters) |
| **Domain** | Coupled to JPA (entities with `@Entity`) | Pure domain models (no annotations) |
| **Business Logic** | Mixed in services | Isolated in domain + use cases |
| **Dependencies** | Direct implementation dependencies | Interface-based (ports) |
| **Testability** | Requires database for tests | Can test use cases without database |
| **Flexibility** | Framework changes require refactoring | Easy to swap frameworks/adapters |
| **Mapping** | Manual or mixed | MapStruct for clean separation |

---

## Benefits of Hexagonal Architecture

✅ **Technology Independence:** Domain logic doesn't depend on frameworks  
✅ **Testability:** Easy to write unit tests without database  
✅ **Maintainability:** Clear separation of concerns  
✅ **Flexibility:** Easy to change persistence or presentation layers  
✅ **SOLID Principles:** Each use case has single responsibility  
✅ **Clean Code:** Domain models are pure and readable  

---

## Testing Strategy

### Unit Tests (without database)
// Test use case with mocked repository
@Test
void shouldCreateEvent() {
EventRepositoryPort mockRepo = mock(EventRepositoryPort.class);
CreateEventUseCaseImpl useCase = new CreateEventUseCaseImpl(mockRepo, mockVenueRepo);

text
Event event = Event.builder()
    .nombre("Test Event")
    .venueId(1L)
    .build();

useCase.execute(event);

verify(mockRepo).save(event);
}

text

### Integration Tests (with database)
Same as Week 2 - test through REST endpoints.

---

## Git Workflow for Week 3

Create branch
git checkout -b feature/semana-3-arquitectura-hexagonal

After implementation and testing
git add .
git commit -m "feat(semana-3): implement hexagonal architecture

Separate domain, application, and infrastructure layers

Create 8 use cases (4 Event + 4 Venue)

Implement input ports (use cases) and output ports (repositories)

Add JPA adapters with MapStruct

Maintain functional equivalence with Week 2

Domain models are pure without framework dependencies"

git push origin feature/semana-3-arquitectura-hexagonal

text

---

## Story Points: 10 [file:4]

---

## Conclusion

Week 3 successfully refactors the application to hexagonal architecture while maintaining all functionality from Week 2 [file:4]. The domain is now completely decoupled from frameworks, making the codebase more maintainable, testable, and flexible for future changes.

The clear separation between business logic (domain), application orchestration (use cases), and technical adapters (infrastructure) demonstrates proper implementation of the **Ports & Adapters** pattern [file:4].