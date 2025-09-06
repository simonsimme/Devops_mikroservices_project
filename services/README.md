# Microservices

This directory contains all backend microservices for the DevOps Microservices Project, implementing domain-specific business logic and data management.

## 📦 Services Overview

### 👤 User Service
**Location**: `./user-service/`  
**Port**: 8080  
**Database**: PostgreSQL (Port 5433)  
**Purpose**: User authentication, registration, and user management

**Key Features**:
- JWT-based authentication system
- User registration and login
- Password encryption with BCrypt
- PostgreSQL integration with Flyway migrations
- Comprehensive test suite
- Spring Security configuration
- Health monitoring with Actuator

### 📦 Item Service  
**Location**: `./item-service/`  
**Port**: 8080  
**Database**: PostgreSQL (Port 5434)  
**Purpose**: Item/schedule management and business logic

**Key Features**:
- Item data management
- Database integration with PostgreSQL
- RESTful API endpoints
- Spring Boot architecture
- Docker Compose support

## 🛠️ Technology Stack

### Common Technologies
- **Java 17** - Programming language
- **Spring Boot 3.x** - Application framework
- **Spring Data JPA** - Data persistence layer
- **PostgreSQL** - Primary database
- **Flyway** - Database migrations
- **Maven** - Build tool and dependency management
- **Docker Compose** - Database containerization
- **Spring Boot Actuator** - Health monitoring
- **Lombok** - Code generation

### Service-Specific Technologies

#### User Service
- **Spring Security** - Authentication and authorization
- **JWT (Auth0)** - Token-based authentication
- **BCrypt** - Password hashing
- **H2** - Test database
- **JUnit 5** - Testing framework

#### Item Service
- **Spring Cloud Gateway** - Gateway integration
- **Spring WebFlux** - Reactive web framework

## 🚀 Quick Start

### Prerequisites
- **Java 17** or higher
- **Maven 3.6+**
- **Docker** and Docker Compose
- **PostgreSQL** (via Docker)

### Start All Services
Use the automated startup script:
```bash
# From project root
cd infra/docker
./start-backend.sh
```

This will:
1. Start PostgreSQL databases for both services
2. Launch User Service in a new terminal
3. Launch Item Service in a new terminal
4. Launch API Gateway in a new terminal

### Manual Service Startup

#### 1. Start Databases
```bash
# User Service Database
cd services/user-service
docker compose up -d

# Item Service Database  
cd services/item-service/devops_mirco_project
docker compose up -d
```

#### 2. Start Services
```bash
# User Service
cd services/user-service/devops_mirco_project
mvn spring-boot:run

# Item Service
cd services/item-service/devops_mirco_project
mvn spring-boot:run
```

## 📡 API Endpoints

### User Service (`/api/auth/`)
```http
POST /api/auth/register    # User registration
POST /api/auth/login      # User authentication
GET  /api/auth/users      # List users (development)
GET  /actuator/health     # Health check
```

### Item Service (`/api/items/`)
```http
GET    /api/items         # List items
POST   /api/items         # Create item
PUT    /api/items/{id}    # Update item
DELETE /api/items/{id}    # Delete item
GET    /actuator/health   # Health check
```

## 🗄️ Database Configuration

### User Service Database
- **Host**: localhost:5433
- **Database**: users_db
- **Username**: user
- **Password**: pass
- **Container**: user-service-db

### Item Service Database
- **Host**: localhost:5434
- **Database**: scheduler_db
- **Username**: postgres
- **Password**: password
- **Container**: item-service-db

## 🧪 Testing

### Run All Tests
```bash
# Test User Service
cd services/user-service/devops_mirco_project
mvn test

# Test Item Service
cd services/item-service/devops_mirco_project
mvn test
```

### Test Coverage
Both services include:
- **Unit Tests** - Entity and service logic testing
- **Integration Tests** - API endpoint testing
- **Database Tests** - Data layer testing with H2
- **Security Tests** - Authentication and authorization

## 🔧 Development

### Adding a New Service
1. Create service directory under `services/`
2. Set up Spring Boot project with Maven
3. Configure database connection
4. Add Docker Compose for database
5. Implement REST endpoints
6. Add comprehensive tests
7. Update documentation

### Service Communication
Services communicate through:
- **API Gateway** - Single entry point for external requests
- **Direct HTTP calls** - Inter-service communication when needed
- **Database isolation** - Each service has its own database

### Environment Variables
```bash
# Database Configuration
POSTGRES_URL=jdbc:postgresql://localhost:5433/users_db
POSTGRES_USER=user
POSTGRES_PASSWORD=pass

# JWT Configuration (User Service)
JWT_SECRET=your-secret-key

# Application Configuration
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=dev
```

## 📊 Monitoring

### Health Checks
All services expose health endpoints:
```bash
# Check User Service
curl http://localhost:8080/actuator/health

# Check Item Service  
curl http://localhost:8080/actuator/health
```

### Database Monitoring
```bash
# Check database containers
docker ps | grep -E "(user-service-db|item-service-db)"

# View database logs
docker logs user-service-db
docker logs item-service-db
```

## 📁 Service Documentation

For detailed information about each service:

- **[User Service](./user-service/README.md)** - Complete authentication service documentation
- **[Item Service](./item-service/README.md)** - Item management service documentation

## 🔍 Troubleshooting

### Common Issues
1. **Port Conflicts** - Check if ports 5433/5434 are available
2. **Database Connection** - Ensure Docker containers are running
3. **Service Startup** - Check Java version and Maven configuration
4. **Authentication** - Verify JWT secret configuration

### Debug Commands
```bash
# Check running services
ps aux | grep java

# Check database connectivity
docker exec -it user-service-db psql -U user -d users_db

# View service logs
mvn spring-boot:run | tee service.log
```

---

**Total Services**: 2 (User Service, Item Service)  
**Database Engine**: PostgreSQL  
**Architecture**: Domain-driven microservices  
**Communication**: REST APIs via API Gateway