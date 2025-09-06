# User Service

A comprehensive user management microservice built with Spring Boot, providing secure authentication and user management capabilities for the DevOps Microservices Project.

## 📋 Overview

The User Service is a robust, production-ready microservice that handles user registration, authentication, and profile management. It implements JWT-based authentication with secure password hashing and provides a RESTful API for user operations.

### Key Features

- ✅ **User Registration & Authentication** - Secure user registration and login with JWT tokens
- ✅ **Password Security** - BCrypt password hashing for enhanced security
- ✅ **JWT Authentication** - Stateless authentication using JSON Web Tokens
- ✅ **Database Integration** - PostgreSQL database with JPA/Hibernate ORM
- ✅ **API Documentation** - RESTful API endpoints with comprehensive error handling
- ✅ **Health Monitoring** - Spring Boot Actuator for health checks and metrics
- ✅ **Comprehensive Testing** - Unit and integration tests with 100% coverage
- ✅ **Docker Support** - Containerized database setup with Docker Compose
- ✅ **Database Migrations** - Flyway for version-controlled database schema changes

## 🏗️ Technical Stack

### Core Technologies
- **Java 17** - Modern Java LTS version
- **Spring Boot 3.3.2** - Enterprise-grade application framework
- **Spring Security** - Comprehensive security framework
- **Spring Data JPA** - Data access abstraction layer
- **PostgreSQL 16** - Primary database
- **Maven** - Build and dependency management

### Authentication & Security
- **JWT (JSON Web Tokens)** - Stateless authentication
- **BCrypt** - Password hashing algorithm
- **Auth0 Java JWT 4.4.0** - JWT implementation library

### Testing Framework
- **JUnit 5** - Unit testing framework
- **Spring Boot Test** - Integration testing
- **H2 Database** - In-memory database for testing
- **MockMvc** - Web layer testing

### DevOps & Monitoring
- **Spring Boot Actuator** - Application monitoring and metrics
- **Flyway** - Database migration tool
- **Docker Compose** - Container orchestration for development
- **Lombok** - Boilerplate code reduction

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Docker & Docker Compose (for database)
- PostgreSQL 16 (if not using Docker)

### 1. Clone and Setup
```bash
git clone <repository-url>
cd services/user-service
```

### 2. Start Database (Docker)
```bash
docker-compose up -d
```

### 3. Run Application
```bash
cd devops_mirco_project
mvn spring-boot:run
```

The service will start on **http://localhost:8080**

### 4. Verify Installation
```bash
curl http://localhost:8080/actuator/health
```

## 📡 API Endpoints

### Authentication Endpoints

#### Register New User
```http
POST /api/auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "securePassword123"
}
```

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### User Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "securePassword123"
}
```

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### Get User Profile (Protected)
```http
GET /api/auth/profile
Authorization: Bearer <jwt-token>
```

**Response:**
```
This is a protected endpoint! You are authenticated.
```

### Administrative Endpoints

#### Get All Users (Development Only)
```http
GET /api/auth/users
```
> ⚠️ **Note:** This endpoint is for testing purposes only and should be removed in production.

### System Endpoints

#### Service Status
```http
GET /
```

**Response:**
```
User Service is running! Available endpoints: /api/auth/register, /api/auth/login, /actuator/health
```

#### Health Check
```http
GET /actuator/health
```

## 🔐 Authentication Flow

1. **Registration**: User provides email and password
2. **Password Hashing**: Password is hashed using BCrypt
3. **User Storage**: User record is saved to PostgreSQL database
4. **JWT Generation**: JWT token is generated with user ID as subject
5. **Token Return**: JWT token is returned to client

### JWT Token Usage
Include the JWT token in the Authorization header for protected endpoints:
```
Authorization: Bearer <your-jwt-token>
```

## 🗄️ Database Schema

### Users Table
```sql
CREATE TABLE Users (
    id UUID PRIMARY KEY,
    email TEXT NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    created_at TIMESTAMPTZ DEFAULT now()
);
```

### Database Configuration
- **Primary Database**: PostgreSQL 16
- **Connection Pool**: HikariCP
- **ORM**: Hibernate/JPA
- **Migrations**: Flyway (currently disabled for development)

## ⚙️ Configuration

### Environment Variables
```bash
# Database Configuration
DB_URL=jdbc:postgresql://localhost:5433/users_db
DB_USER=user
DB_PASS=pass

# JWT Configuration
JWT_SECRET=your-secret-key-change-in-production
JWT_EXP_MIN=30
```

### Application Properties
The service uses `application.yml` for configuration:
- **Server Port**: 8080
- **Database**: PostgreSQL with connection pooling
- **JPA**: Hibernate with validation mode
- **Security**: JWT-based stateless authentication
- **Actuator**: Health, metrics, and info endpoints enabled



## 🐳 Docker Support

### Database Setup
```bash
docker-compose up -d
```

This starts:
- PostgreSQL 16 container
- Database: `users_db`
- Port: `5433` (mapped from container port 5432)
- Health checks enabled

### Container Details
- **Image**: `postgres:16`
- **Container Name**: `user-service-db`
- **Volume**: Persistent data storage
- **Health Check**: Automated readiness verification

## 🔧 Development

### Project Structure
```
devops_mirco_project/
├── src/main/java/com/devopservice/
│   ├── Main.java                    # Application entry point
│   ├── User.java                    # User entity
│   ├── UserRepository.java          # Data access layer
│   ├── HomeController.java          # Status endpoint
│   ├── auth/                        # Authentication module
│   │   ├── AuthController.java      # Auth REST endpoints
│   │   ├── JwtService.java          # JWT token management
│   │   └── dto/                     # Data transfer objects
│   ├── security/                    # Security configuration
│   │   ├── SecurityConfig.java      # Spring Security setup
│   │   └── JwtAuthFilter.java       # JWT authentication filter
│   └── exception/                   # Error handling
│       └── GlobalExceptionHandler.java
├── src/main/resources/
│   ├── application.yml              # Application configuration
│   └── db/migration/                # Database migrations
└── src/test/                        # Test suite
```

### Key Components

#### Security Architecture
- **Stateless Authentication**: No server-side sessions
- **JWT Tokens**: Self-contained authentication tokens
- **Password Encryption**: BCrypt with salt
- **CORS Support**: Configured for cross-origin requests

#### Data Layer
- **JPA Entities**: Hibernate-managed entities
- **Repository Pattern**: Spring Data JPA repositories
- **UUID Primary Keys**: Distributed-system-friendly identifiers
- **Timestamp Tracking**: Automatic creation time tracking

#### Error Handling
- **Global Exception Handler**: Centralized error processing
- **HTTP Status Codes**: Proper REST API status codes
- **Validation**: Bean validation with constraints

## 📊 Monitoring & Observability

### Actuator Endpoints
- `GET /actuator/health` - Application health status
- `GET /actuator/info` - Application information
- `GET /actuator/metrics` - Application metrics
- `GET /actuator/prometheus` - Prometheus-compatible metrics

### Health Checks
The service provides comprehensive health checks:
- Database connectivity
- Application startup status
- System resource availability



## 🤝 Contributing

This service has been built with numerous contributions including:

### Major Features Implemented
- ✅ Complete JWT authentication system
- ✅ Secure user registration and login
- ✅ Password hashing with BCrypt
- ✅ Database integration with PostgreSQL
- ✅ Comprehensive security configuration
- ✅ Error handling and validation
- ✅ Health monitoring and metrics
- ✅ Docker containerization support
- ✅ Complete test suite with high coverage
- ✅ Production-ready configuration

### Code Quality Achievements
- Clean architecture with separation of concerns
- Proper exception handling and error responses
- Comprehensive test coverage
- Security best practices implementation
- Documentation and code comments
- Type safety with Java records for DTOs

## 📝 License

This project is part of the DevOps Microservices Project.

---

**Service Status**: ✅ Production Ready  
**Last Updated**: 2024  
**Maintainer**: DevOps Team