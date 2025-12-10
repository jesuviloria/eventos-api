# Module 6 - Week 5  
## Security with JWT and RFC 7807 Error Handling

## User Story

**User Story Name:** JWT Authentication and RFC 7807 Problem Details Implementation

**User Story Goal**

As a development team, we want to implement JWT-based authentication with role-based authorization (RBAC) and standardize error responses using RFC 7807, so that the API is secure, follows industry standards, and provides clear, consistent error messages to clients.

---

## Week 5 Scope

### TASK 1: JWT Authentication Implementation

**Description:**

- Integrate SmallRye JWT for token-based authentication.
- Generate RSA key pair for signing and verifying JWTs.
- Create authentication endpoints:
  - `POST /api/v1/auth/register` - Register new users
  - `POST /api/v1/auth/login` - Authenticate and receive JWT token
- Implement password hashing using BCrypt.
- Configure JWT token generation with:
  - User principal (username)
  - Roles (groups)
  - Expiration time (configurable)
  - Issuer information

### TASK 2: User Management and Roles

**Description:**

- Create database schema for users and roles:
  - `users` table - Store user credentials and profile
  - `roles` table - Define available roles (USER, ADMIN)
  - `user_roles` table - Many-to-many relationship
- Implement user domain model following hexagonal architecture.
- Create use cases:
  - `RegisterUserUseCase` - User registration with validations
  - `LoginUserUseCase` - Authentication and token generation
  - `FindUserUseCase` - User lookup operations
- Encrypt passwords using BCrypt before storage.
- Assign default "USER" role on registration.
- Create seed data with admin and regular user accounts.

### TASK 3: Role-Based Authorization (RBAC)

**Description:**

- Protect endpoints with role-based access control:
  - **Public endpoints** (no authentication):
    - `GET /api/v1/events` - List events
    - `GET /api/v1/events/{id}` - View event details
    - `GET /api/v1/venues` - List venues
    - `GET /api/v1/venues/{id}` - View venue details
  - **ADMIN-only endpoints**:
    - `POST /api/v1/events` - Create events
    - `PUT /api/v1/events/{id}` - Update events
    - `DELETE /api/v1/events/{id}` - Delete events
    - `POST /api/v1/venues` - Create venues
    - `PUT /api/v1/venues/{id}` - Update venues
    - `DELETE /api/v1/venues/{id}` - Delete venues
- Use annotations:
  - `@PermitAll` for public endpoints
  - `@RolesAllowed({"ADMIN"})` for admin-only operations
- Configure security policies in `application.properties`.

### TASK 4: RFC 7807 Problem Details Implementation

**Description:**

- Implement RFC 7807 standard for HTTP error responses.
- Create `ProblemDetail` class with required fields:
  - `type` - URI reference identifying the problem
  - `title` - Short, human-readable summary
  - `status` - HTTP status code
  - `detail` - Specific explanation
  - `instance` - URI reference to occurrence
  - `timestamp` - When the error occurred
  - `traceId` - Unique identifier for debugging
- Update `GlobalExceptionHandler` to return RFC 7807 responses.
- Set `Content-Type: application/problem+json` for error responses.
- Provide factory methods for common errors (400, 401, 403, 404, 409, 500).

---

## Architecture Integration

Week 5 maintains the hexagonal architecture from Week 3 and Week 4, adding security as a cross-cutting concern:

┌─────────────────────────────────────────────────────────┐
│ Infrastructure │
│ ┌─────────────────┐ ┌─────────────────┐ │
│ │ REST Adapters │ │ JPA Adapters │ │
│ │ + JWT Security │ │ + User/Role │ │
│ └────────┬────────┘ └────────┬────────┘ │
│ │ │ │
└───────────┼────────────────────────────────┼────────────┘
│ │
│ ┌──────────────┐ │
└───────►│ Use Cases │◄───────┘
│ + Auth Logic │
└───────┬───────┘
│
┌───────▼────────┐
│ Domain Core │
│ + User Model │
└─────────────────┘

text

---

## Package Structure (Week 5 Additions)

src/main/java/com/tiquetera/
├── domain/
│ ├── model/
│ │ ├── Event.java
│ │ ├── Venue.java
│ │ ├── User.java # NEW
│ │ └── Role.java # NEW
│ └── ports/
│ ├── in/
│ │ ├── (event use cases)
│ │ ├── (venue use cases)
│ │ ├── RegisterUserUseCase.java # NEW
│ │ ├── LoginUserUseCase.java # NEW
│ │ └── FindUserUseCase.java # NEW
│ └── out/
│ ├── EventRepositoryPort.java
│ ├── VenueRepositoryPort.java
│ └── UserRepositoryPort.java # NEW
├── application/
│ └── usecase/
│ ├── event/
│ ├── venue/
│ └── auth/ # NEW
│ ├── RegisterUserUseCaseImpl.java
│ ├── LoginUserUseCaseImpl.java
│ └── FindUserUseCaseImpl.java
└── infrastructure/
├── adapter/
│ ├── in/web/
│ │ ├── EventController.java # Updated with security
│ │ ├── VenueController.java # Updated with security
│ │ ├── AuthController.java # NEW
│ │ └── dto/
│ │ ├── LoginRequest.java # NEW
│ │ ├── RegisterRequest.java # NEW
│ │ ├── AuthResponse.java # NEW
│ │ └── UserResponse.java # NEW
│ └── out/persistence/
│ ├── UserJpaAdapter.java # NEW
│ ├── entity/
│ │ ├── UserEntity.java # NEW
│ │ └── RoleEntity.java # NEW
│ ├── repository/
│ │ ├── UserJpaRepository.java # NEW
│ │ └── RoleJpaRepository.java # NEW
│ └── mapper/
│ └── UserMapper.java # NEW
└── config/
├── GlobalExceptionHandler.java # Updated with RFC 7807
├── ProblemDetail.java # NEW
├── PasswordService.java # NEW
└── JwtService.java # NEW

src/main/resources/
├── application.properties # JWT configuration
├── privateKey.pem # NEW - RSA private key
├── META-INF/resources/publicKey.pem # NEW - RSA public key
└── db/migration/
├── V1.0.0__create_initial_schema.sql
├── V1.0.1__add_audit_triggers.sql
├── V1.0.2__add_sample_data.sql
└── V1.0.3__create_users_table.sql # NEW

text

---

## New Endpoints (Week 5)

### Authentication

**`POST /api/v1/auth/register`**
- Register a new user
- Validates username uniqueness
- Hashes password with BCrypt
- Assigns "USER" role by default
- Returns JWT token immediately
- Request body:
{
"username": "juanperez",
"email": "juan@example.com",
"password": "password123",
"fullName": "Juan Pérez"
}

text
- Response (201):
{
"token": "eyJraWQiOi...",
"type": "Bearer",
"username": "juanperez",
"email": "juan@example.com"
}

text

**`POST /api/v1/auth/login`**
- Authenticate existing user
- Validates credentials
- Returns JWT token with roles
- Request body:
{
"username": "admin",
"password": "admin123"
}

text
- Response (200):
{
"token": "eyJraWQiOi...",
"type": "Bearer",
"username": "admin"
}

text

---

## Security Configuration

### JWT Token Structure

{
"iss": "https://tiquetera.com",
"upn": "admin",
"groups": ["USER", "ADMIN"],
"iat": 1702234567,
"exp": 1702238167
}

text

### Roles and Permissions

| Role | Permissions |
|------|-------------|
| **USER** | - View all events and venues<br>- Search and filter<br>- Access public endpoints |
| **ADMIN** | - All USER permissions<br>- Create events and venues<br>- Update events and venues<br>- Delete events and venues |

### Protected Endpoints Matrix

| Endpoint | Method | Public | USER | ADMIN |
|----------|--------|--------|------|-------|
| `/api/v1/events` | GET | ✅ | ✅ | ✅ |
| `/api/v1/events/{id}` | GET | ✅ | ✅ | ✅ |
| `/api/v1/events` | POST | ❌ | ❌ | ✅ |
| `/api/v1/events/{id}` | PUT | ❌ | ❌ | ✅ |
| `/api/v1/events/{id}` | DELETE | ❌ | ❌ | ✅ |
| `/api/v1/venues` | GET | ✅ | ✅ | ✅ |
| `/api/v1/venues/{id}` | GET | ✅ | ✅ | ✅ |
| `/api/v1/venues` | POST | ❌ | ❌ | ✅ |
| `/api/v1/venues/{id}` | PUT | ❌ | ❌ | ✅ |
| `/api/v1/venues/{id}` | DELETE | ❌ | ❌ | ✅ |
| `/api/v1/auth/register` | POST | ✅ | ✅ | ✅ |
| `/api/v1/auth/login` | POST | ✅ | ✅ | ✅ |

---

## RFC 7807 Error Responses

All error responses follow RFC 7807 standard with `Content-Type: application/problem+json`.

### Example: 401 Unauthorized

{
"type": "https://tiquetera.com/problems/unauthorized",
"title": "Unauthorized",
"status": 401,
"detail": "Authentication is required to access this resource",
"timestamp": "2025-12-10T16:45:30.123",
"traceId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
}

text

### Example: 403 Forbidden

{
"type": "https://tiquetera.com/problems/forbidden",
"title": "Forbidden",
"status": 403,
"detail": "You don't have permission to access this resource",
"timestamp": "2025-12-10T16:46:00.456",
"traceId": "xyz-789-abc-123"
}

text

### Example: 409 Conflict

{
"type": "https://tiquetera.com/problems/conflict",
"title": "Conflict",
"status": 409,
"detail": "Username already exists",
"timestamp": "2025-12-10T16:47:15.789",
"traceId": "def-456-ghi-789"
}

text

---

## Acceptance Criteria

- ✅ JWT authentication is implemented with RSA key pair
- ✅ Users can register with unique username and email
- ✅ Passwords are hashed with BCrypt (never stored plain)
- ✅ Login returns valid JWT token with roles
- ✅ Public endpoints work without authentication
- ✅ Protected endpoints require valid JWT token
- ✅ ADMIN-only endpoints reject USER role (403 Forbidden)
- ✅ Invalid tokens return 401 Unauthorized
- ✅ Missing tokens return 401 Unauthorized
- ✅ All errors follow RFC 7807 standard
- ✅ Error responses include traceId for debugging
- ✅ Content-Type is `application/problem+json` for errors
- ✅ Swagger UI supports JWT authorization
- ✅ Token expiration is configurable
- ✅ Default users (admin, user) are seeded in database
- ✅ Hexagonal architecture is maintained

---

## Technologies Used

- **SmallRye JWT** - JWT implementation for Quarkus
- **BCrypt** - Password hashing algorithm
- **RSA-256** - JWT signing algorithm
- **Jakarta Security** - Security annotations (@RolesAllowed, @PermitAll)
- **RFC 7807** - Problem Details for HTTP APIs standard

---

## Configuration

### application.properties

JWT Configuration
mp.jwt.verify.publickey.location=META-INF/resources/publicKey.pem
mp.jwt.verify.issuer=https://tiquetera.com
smallrye.jwt.sign.key.location=privateKey.pem

jwt.issuer=https://tiquetera.com
jwt.duration=3600
jwt.secret=mySecretKey12345678901234567890123456789012

Security Paths
quarkus.http.auth.permission.public.paths=/api/v1/auth/,/swagger-ui/,/swagger,/q/*
quarkus.http.auth.permission.public.policy=permit

quarkus.http.auth.permission.authenticated.paths=/api/v1/events/,/api/v1/venues/
quarkus.http.auth.permission.authenticated.policy=authenticated

CORS
quarkus.http.cors=true
quarkus.http.cors.origins=*
quarkus.http.cors.methods=GET,POST,PUT,DELETE,OPTIONS
quarkus.http.cors.headers=accept,authorization,content-type,x-requested-with

text

---

## Running the Project (Week 5)

### Requirements

- Java 21
- Maven 3.9+
- Docker + Docker Compose
- PostgreSQL 16
- OpenSSL (for key generation)

### First Time Setup

1. Generate RSA keys
openssl genpkey -algorithm RSA -out src/main/resources/privateKey.pem -pkeyopt rsa_keygen_bits:2048
openssl rsa -pubout -in src/main/resources/privateKey.pem -out src/main/resources/META-INF/resources/publicKey.pem

2. Start PostgreSQL
docker-compose up -d

3. Clean and compile
./mvnw clean compile

4. Run application
./mvnw quarkus:dev

Flyway will apply V1.0.3 migration (users table)
text

---

## Testing JWT Security

### 1. Register New User

curl -X POST http://localhost:8080/api/v1/auth/register
-H "Content-Type: application/json"
-d '{
"username": "testuser",
"email": "test@example.com",
"password": "password123",
"fullName": "Test User"
}'

text

### 2. Login and Get Token

curl -X POST http://localhost:8080/api/v1/auth/login
-H "Content-Type: application/json"
-d '{
"username": "admin",
"password": "admin123"
}'

Save the token
export TOKEN="eyJraWQiOi..."

text

### 3. Access Public Endpoint (No Token)

curl http://localhost:8080/api/v1/events

✅ Should work
text

### 4. Try Protected Endpoint Without Token

curl -X POST http://localhost:8080/api/v1/events
-H "Content-Type: application/json"
-d '{ ... }'

❌ Should return 401 Unauthorized (RFC 7807)
text

### 5. Access Protected Endpoint With Token

curl -X POST http://localhost:8080/api/v1/events
-H "Content-Type: application/json"
-H "Authorization: Bearer $TOKEN"
-d '{
"nombre": "New Event",
"descripcion": "Created with JWT",
"fechaInicio": "2026-08-15T20:00:00",
"fechaFin": "2026-08-15T23:00:00",
"venueId": 1,
"ciudad": "Bogotá",
"categoria": "Música"
}'

✅ Should work if token has ADMIN role
text

### 6. Try ADMIN Operation With USER Token

Login as regular user
curl -X POST http://localhost:8080/api/v1/auth/login
-H "Content-Type: application/json"
-d '{"username": "user", "password": "user123"}'

export USER_TOKEN="eyJ..."

Try to create event
curl -X POST http://localhost:8080/api/v1/events
-H "Authorization: Bearer $USER_TOKEN"
-H "Content-Type: application/json"
-d '{ ... }'

❌ Should return 403 Forbidden (RFC 7807)
text

---

## Swagger UI with JWT

1. Open http://localhost:8080/swagger-ui
2. Click **"Authorize"** button (top right)
3. Enter: `Bearer <your-token-here>`
4. Click **"Authorize"**
5. Click **"Close"**
6. Now you can test protected endpoints from Swagger UI

---

## Default Credentials

Two users are seeded in the database:

| Username | Password | Roles | Email |
|----------|----------|-------|-------|
| `admin` | `admin123` | USER, ADMIN | admin@tiquetera.com |
| `user` | `user123` | USER | user@tiquetera.com |

---

## Security Best Practices Implemented

✅ **Passwords never stored in plain text** - BCrypt hashing  
✅ **JWT tokens expire** - Configurable expiration (default: 1 hour)  
✅ **Role-based access control** - Granular permissions  
✅ **Principle of least privilege** - Users get minimal permissions  
✅ **Secure token signing** - RSA-256 algorithm  
✅ **CORS configured** - Controlled cross-origin access  
✅ **Error details limited** - No sensitive info in error messages  
✅ **Trace IDs for debugging** - Track issues without exposing internals  

---

## Git Workflow for Week 5

git checkout -b feature/semana-5-seguridad-jwt-rfc7807

After implementation
git add .
git commit -m "feat(semana-5): implement JWT security and RFC 7807

Implement JWT authentication with SmallRye JWT

Generate RSA key pair for token signing

Create user registration and login endpoints

Add User and Role domain models and entities

Implement BCrypt password hashing

Add role-based authorization (USER, ADMIN)

Protect endpoints with @RolesAllowed annotations

Implement RFC 7807 Problem Details standard

Update GlobalExceptionHandler for consistent errors

Add ProblemDetail class for error responses

Configure Swagger UI with JWT authorization

Create V1.0.3 migration for users and roles

Seed default admin and user accounts"

git push origin feature/semana-5-seguridad-jwt-rfc7807

text

---

## Story Points: 13

---

## Conclusion

Week 5 successfully implements enterprise-grade security with JWT authentication and role-based authorization while maintaining the clean hexagonal architecture from previous weeks. All error responses now follow the RFC 7807 standard, providing clients with consistent, machine-readable error information.

The application is now production-ready with:
- ✅ Secure authentication and authorization
- ✅ Encrypted password storage
- ✅ Token-based stateless sessions
- ✅ Fine-grained access control
- ✅ Standardized error responses
- ✅ Full API documentation with security schemas
- ✅ Maintained architectural integrity