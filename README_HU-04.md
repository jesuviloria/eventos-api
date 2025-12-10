# Module 6 - Week 4  
## JPA Optimization and Database Migrations with Flyway

## User Story

**User Story Name:** JPA Optimization and Database Version Control with Flyway

**User Story Goal**

As a development team, we want to implement database version control using Flyway, optimize JPA queries to avoid N+1 problems, add strategic indexes, and implement automatic auditing, so that the application is production-ready with better performance and maintainability.

---

## Week 4 Scope

### TASK 1: Flyway Integration and Database Migrations

**Description:**

- Integrate Flyway for database schema version control.
- Create versioned migration scripts:
  - `V1.0.0__create_initial_schema.sql` - Create tables, constraints, and indexes
  - `V1.0.1__add_audit_triggers.sql` - Add PostgreSQL triggers for automatic timestamp updates
  - `V1.0.2__add_sample_data.sql` - Load initial sample data for testing
- Configure Flyway to execute migrations automatically at application startup.
- Change Hibernate strategy from `drop-and-create` to `validate` or `none`.
- Ensure migration history is tracked in `flyway_schema_history` table.

### TASK 2: JPA Query Optimization

**Description:**

- Implement **Named Queries** using `@NamedQuery` annotation for frequently used queries.
- Add **JOIN FETCH** to prevent N+1 query problems.
- Configure `@BatchSize` on collections to optimize batch fetching.
- Use proper `FetchType.LAZY` for relationships.
- Create custom repository methods using JPQL for complex queries.
- Add query methods for:
  - Finding upcoming events
  - Searching by multiple criteria (city + category)
  - Finding events in a date range

### TASK 3: Database Indexes and Constraints

**Description:**

- Add strategic indexes on frequently queried columns:
  - Foreign keys (e.g., `venue_id` in events table)
  - Search fields (e.g., `ciudad`, `categoria`)
  - Sort fields (e.g., `fecha_inicio`, `capacidad`)
- Define indexes in both migration scripts and JPA entities using `@Index`.
- Add database-level constraints:
  - Unique constraints on business keys
  - Check constraints for data integrity (e.g., `fecha_fin > fecha_inicio`)
  - Foreign key constraints with cascade rules

### TASK 4: Automatic Auditing

**Description:**

- Implement automatic timestamp tracking for all entities:
  - `created_at` - Set automatically on entity creation
  - `updated_at` - Updated automatically on every modification
- Use JPA callbacks (`@PrePersist`, `@PreUpdate`) for Java-level auditing.
- Implement PostgreSQL triggers as a database-level backup for auditing.
- Ensure timestamps are immutable (`created_at`) or auto-updated (`updated_at`).

---

## Architecture Changes

### Before Week 4
- Schema managed by Hibernate (`drop-and-create`)
- No version control for database changes
- Manual data loading for testing
- No query optimization
- N+1 query problems present

### After Week 4
- Schema managed by Flyway (versioned migrations)
- Database changes tracked and versioned
- Sample data loaded automatically
- Named queries for common operations
- Optimized fetching strategies (no N+1)
- Strategic indexes for performance
- Automatic auditing with timestamps

---

## Package Structure (Unchanged from Week 3)

Week 4 maintains the hexagonal architecture from Week 3, only adding optimizations at the infrastructure layer:

src/main/
├── java/com/tiquetera/
│ ├── domain/ # No changes
│ ├── application/ # No changes
│ └── infrastructure/
│ └── adapter/out/persistence/
│ ├── entity/
│ │ ├── EventEntity.java # Added @NamedQuery, @Index, @BatchSize
│ │ └── VenueEntity.java # Added @NamedQuery, @Index, @BatchSize
│ ├── repository/
│ │ ├── EventJpaRepository.java # Added custom query methods
│ │ └── VenueJpaRepository.java # Added custom query methods
│ └── (rest unchanged)
└── resources/
├── application.properties # Flyway configuration added
└── db/migration/ # NEW: Flyway migrations
├── V1.0.0__create_initial_schema.sql
├── V1.0.1__add_audit_triggers.sql
└── V1.0.2__add_sample_data.sql

text

---

## Flyway Migrations

### V1.0.0__create_initial_schema.sql

Creates tables with:
- Primary keys (auto-increment)
- Unique constraints on business keys (`nombre`)
- Foreign keys with cascade delete
- Check constraints for data integrity
- Strategic indexes on search and sort columns
- Audit columns (`created_at`, `updated_at`)

**Key Indexes:**
- `idx_events_venue_id` - Foreign key lookup
- `idx_events_ciudad` - City filtering
- `idx_events_categoria` - Category filtering
- `idx_events_fecha_inicio` - Date sorting
- `idx_venues_ciudad` - Venue city filtering
- `idx_venues_capacidad` - Capacity filtering

### V1.0.1__add_audit_triggers.sql

Creates PostgreSQL triggers that:
- Automatically update `updated_at` timestamp on any UPDATE
- Work as a database-level backup for JPA callbacks
- Ensure audit trail even if application logic fails

### V1.0.2__add_sample_data.sql

Loads initial test data:
- 5 sample venues in different cities
- 5 sample events with different categories
- Realistic dates for future events
- Properly set audit timestamps

---

## New Endpoints (Week 4)

### Events - Optimized Queries

**`GET /api/v1/events/upcoming`**
- Returns all future events ordered by start date
- Uses named query for optimization
- No pagination (assumes reasonable number of upcoming events)

**`GET /api/v1/events/search?ciudad={ciudad}&categoria={categoria}`**
- Search events by city AND category
- Uses named query with parameters
- Optimized with indexes on both fields

**`GET /api/v1/events/range?start={ISO_DATE}&end={ISO_DATE}`**
- Find events within a date range
- Uses JPQL query with date comparisons
- Example: `/api/v1/events/range?start=2026-01-01T00:00:00&end=2026-12-31T23:59:59`

---

## JPA Optimizations Implemented

### 1. Named Queries

Defined in entity classes, compiled once, cached:

@NamedQueries({
@NamedQuery(
name = "EventEntity.findUpcomingEvents",
query = "SELECT e FROM EventEntity e WHERE e.fechaInicio > CURRENT_TIMESTAMP ORDER BY e.fechaInicio"
)
})

text

### 2. Batch Fetching

Configured with `@BatchSize` to load related entities in batches:

@ManyToOne(fetch = FetchType.LAZY)
@BatchSize(size = 10)
private VenueEntity venue;

text

**Result:** Instead of N+1 queries, only 2 queries (1 for events + 1 batch for venues)

### 3. JOIN FETCH

Custom repository method to eagerly load relationships when needed:

public Optional<EventEntity> findByIdWithVenue(Long id) {
return find("#EventEntity.findWithVenue", Parameters.with("id", id))
.firstResultOptional();
}

text

### 4. Strategic Indexes

All frequently queried columns have indexes:
- Foreign keys (always indexed)
- Filter fields (ciudad, categoria)
- Sort fields (fecha_inicio, capacidad)

---

## Acceptance Criteria

- ✅ Flyway executes migrations automatically at startup
- ✅ `flyway_schema_history` table tracks all applied migrations
- ✅ Database schema is created from versioned SQL scripts (not Hibernate)
- ✅ Sample data is loaded automatically (5 venues + 5 events)
- ✅ Named queries are used for common operations
- ✅ No N+1 query problems (verified in SQL logs)
- ✅ Indexes improve query performance on filtered/sorted columns
- ✅ Audit timestamps (`created_at`, `updated_at`) work automatically
- ✅ PostgreSQL triggers update timestamps on database level
- ✅ All Week 3 functionality continues working (no breaking changes)
- ✅ New optimized endpoints are documented in Swagger

---

## Technologies Used

- **Flyway 9.x** - Database version control
- **PostgreSQL 16** - Production database
- **Hibernate ORM with Panache** - JPA implementation
- **JPQL** - Java Persistence Query Language
- **Named Queries** - Compiled and cached queries
- **Batch Fetching** - Optimized collection loading
- **PostgreSQL Triggers** - Database-level auditing

---

## Configuration

### application.properties

Flyway Configuration
quarkus.flyway.migrate-at-start=true
quarkus.flyway.baseline-on-migrate=true
quarkus.flyway.baseline-version=1.0.0
quarkus.flyway.locations=db/migration

Hibernate (changed from drop-and-create to validate)
quarkus.hibernate-orm.database.generation=validate
quarkus.hibernate-orm.log.sql=true
quarkus.hibernate-orm.log.bind-parameters=true

text

---

## Running the Project (Week 4)

### Requirements

- Java 21
- Maven 3.9+
- Docker + Docker Compose
- PostgreSQL 16 (via Docker)

### First Time Setup

1. Start PostgreSQL
docker-compose up -d

2. Clean and compile
./mvnw clean compile

3. Run application (Flyway will execute migrations automatically)
./mvnw quarkus:dev

You should see in logs:
INFO [org.flywaydb.core.internal.command.DbMigrate] Successfully applied 3 migrations
text

### Verify Flyway Migrations

Connect to PostgreSQL
docker exec -it eventos-postgres psql -U postgres -d eventos_db

Check migration history
SELECT version, description, installed_on, success
FROM flyway_schema_history
ORDER BY installed_rank;

Verify sample data
SELECT * FROM venues;
SELECT * FROM events;

\q

text

---

## Testing the Optimizations

### Test Named Queries

Upcoming events (uses named query)
curl "http://localhost:8080/api/v1/events/upcoming"

Check logs - you should see:
/* EventEntity.findUpcomingEvents */ select ...
text

### Test Batch Fetching (No N+1)

List all events
curl "http://localhost:8080/api/v1/events?page=0&size=10"

Check SQL logs - you should see:
1. SELECT from events (1 query)
2. SELECT from venues WHERE id IN (?, ?, ...) (1 batch query)
NOT: 10 separate SELECT queries for each venue
text

### Test Search Optimization

Search by city and category (uses named query + indexes)
curl "http://localhost:8080/api/v1/events/search?ciudad=Bogotá&categoria=Música"

Should return 2 events quickly thanks to indexes
text

### Test Auditing

Create a new event
curl -X POST http://localhost:8080/api/v1/events
-H "Content-Type: application/json"
-d '{
"nombre": "Test Audit Event",
"descripcion": "Testing automatic timestamps",
"fechaInicio": "2026-08-15T20:00:00",
"fechaFin": "2026-08-15T23:00:00",
"venueId": 1,
"ciudad": "Bogotá",
"categoria": "Test"
}'

Verify created_at and updated_at in database
docker exec -it eventos-postgres psql -U postgres -d eventos_db
-c "SELECT nombre, created_at, updated_at FROM events WHERE nombre = 'Test Audit Event';"

Update the event
curl -X PUT http://localhost:8080/api/v1/events/6
-H "Content-Type: application/json"
-d '{ "nombre": "Test Audit Event UPDATED", ... }'

Verify updated_at changed
docker exec -it eventos-postgres psql -U postgres -d eventos_db
-c "SELECT nombre, created_at, updated_at FROM events WHERE id = 6;"

text

---

## Performance Improvements

| Metric | Before Week 4 | After Week 4 | Improvement |
|--------|---------------|--------------|-------------|
| **List 10 events with venues** | 11 queries (N+1) | 2 queries | **82% reduction** |
| **Search by ciudad** | Table scan | Index scan | **~10x faster** |
| **Find upcoming events** | Dynamic query | Named query | **Compiled/cached** |
| **Date range queries** | No index | Indexed | **Faster sorting** |
| **Schema updates** | Manual (risky) | Flyway (versioned) | **Zero-downtime** |

---

## Common Flyway Commands

### Create a New Migration

Create new migration file with next version
touch src/main/resources/db/migration/V1.0.3__add_new_feature.sql

text

### Rollback (Manual)

Flyway doesn't support automatic rollback. For rollback:

Create an undo migration
touch src/main/resources/db/migration/V1.0.4__rollback_feature.sql

Write SQL to reverse V1.0.3 changes
text

### Clean Database (Development Only)

WARNING: Deletes all data
docker exec -it eventos-postgres psql -U postgres -d eventos_db
-c "DROP SCHEMA public CASCADE; CREATE SCHEMA public;"

Restart application - Flyway will recreate everything
text

---

## Troubleshooting

### Issue: Flyway migration fails

Check Flyway history
docker exec -it eventos-postgres psql -U postgres -d eventos_db
-c "SELECT * FROM flyway_schema_history ORDER BY installed_rank;"

If migration marked as failed, fix SQL and:
docker exec -it eventos-postgres psql -U postgres -d eventos_db
-c "DELETE FROM flyway_schema_history WHERE version = 'X.X.X';"

text

### Issue: Sample data not loading

Insert data manually
docker exec -it eventos-postgres psql -U postgres -d eventos_db

Run INSERT statements from V1.0.2__add_sample_data.sql
text

### Issue: N+1 queries still appearing

- Verify `@BatchSize` is present on relationships
- Check `FetchType.LAZY` is configured
- Use JOIN FETCH for specific queries
- Enable SQL logging to verify batch queries

---

## Git Workflow for Week 4

git checkout -b feature/semana-4-optimizacion-jpa-flyway

After implementation
git add .
git commit -m "feat(semana-4): JPA optimization and Flyway migrations

Integrate Flyway for database version control

Create 3 versioned migrations (schema, triggers, data)

Implement named queries for optimization

Add @BatchSize to eliminate N+1 problems

Create strategic indexes on search/sort columns

Implement automatic auditing with triggers

Add optimized endpoints: /upcoming, /search, /range

Load 5 sample venues and 5 sample events"

git push origin feature/semana-4-optimizacion-jpa-flyway

text

---

## Story Points: 8

---

## Conclusion

Week 4 successfully optimizes the application for production readiness by implementing database version control with Flyway, eliminating N+1 query problems, adding strategic indexes, and implementing automatic auditing. The hexagonal architecture from Week 3 remains intact, with all optimizations added at the infrastructure layer without affecting the domain or application logic.

The application now has:
- ✅ Versioned database schema management
- ✅ Optimized query performance
- ✅ Automatic audit trails
- ✅ Production-ready data loading strategy
- ✅ Maintainable and traceable database changes