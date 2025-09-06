# Scheduler Service (Item Service)

A comprehensive scheduling management microservice built with Spring Boot, providing worker scheduling, shift management, and role-based assignment capabilities for the DevOps Microservices Project.

## 🎯 Service Overview

The Scheduler Service manages:
- **Worker Management** - Employee information and role assignments
- **Shift Scheduling** - Work shift creation and management
- **Role Management** - Job roles and permissions
- **Shift Assignments** - Assigning workers to specific shifts
- **Schedule Coordination** - Complex scheduling logic and conflicts resolution

## 🛠️ Technology Stack

### Core Technologies
- **Java 17** - Programming language
- **Spring Boot 3.1.5** - Application framework
- **Spring Data JPA** - Data persistence layer
- **PostgreSQL** - Primary database
- **Flyway** - Database migrations and versioning
- **Maven** - Build tool and dependency management

### Data & Persistence
- **Hibernate** - Object-relational mapping
- **PostgreSQL Dialect** - Database-specific optimizations
- **UUID Primary Keys** - Distributed-system-friendly identifiers
- **Connection Pooling** - HikariCP for efficient database connections

### DevOps & Monitoring
- **Spring Boot Actuator** - Application monitoring and metrics
- **Docker Compose** - Container orchestration for development
- **Flyway Migrations** - Version-controlled database schema
- **Lombok** - Boilerplate code reduction

## 📁 Project Structure

```
item-service/devops_mirco_project/
├── src/main/java/com/devopservice/
│   ├── Main.java                        # Application entry point
│   ├── HomeController.java              # Status endpoint
│   ├── entities/                        # JPA entities
│   │   ├── Worker.java                  # Worker entity
│   │   ├── Shift.java                   # Shift entity
│   │   ├── Roles.java                   # Role entity
│   │   └── ShiftAssignment.java         # Assignment entity
│   ├── controller/                      # REST controllers
│   │   ├── WorkerController.java        # Worker management API
│   │   ├── ShiftController.java         # Shift management API
│   │   ├── RolesController.java         # Role management API
│   │   └── ShiftAssignmentController.java # Assignment API
│   ├── repositories/                    # Data access layer
│   ├── dto/                            # Data transfer objects
│   └── exceptions/                     # Error handling
├── src/main/resources/
│   ├── application.yml                 # Application configuration
│   └── db/migration/                   # Database migrations
├── src/test/                          # Test suite
├── docker-compose.yml                 # Database container
├── pom.xml                           # Dependencies and build config
└── README.md                         # This file
```

## 🗄️ Database Schema

### Core Entities

#### Worker
- `id` (UUID) - Unique worker identifier
- `name` (String) - Worker full name
- `role` (String) - Worker's primary role

#### Shift
- `id` (UUID) - Unique shift identifier
- `date` (LocalDate) - Shift date
- `worker_id` (UUID) - Assigned worker reference

#### Roles
- Role definitions and permissions

#### ShiftAssignment
- Complex assignment logic and relationships

### Database Configuration
- **Host**: localhost:5434
- **Database**: scheduler_db
- **Username**: postgres
- **Password**: password
- **Container**: item-service-db

## 🚀 Getting Started

### Prerequisites
- **Java 17** or higher
- **Maven 3.6+**
- **Docker** and Docker Compose
- **PostgreSQL** (via Docker)

### 1. Start Database
```bash
cd services/item-service/devops_mirco_project
docker compose up -d
```

### 2. Run Service

#### Development Mode
```bash
mvn spring-boot:run
```

#### Production JAR
```bash
mvn clean package
java -jar target/devops_micro_project-1.0-SNAPSHOT.jar
```

The service will be available at http://localhost:8081

### 3. Verify Installation
```bash
# Check service health
curl http://localhost:8081/actuator/health

# Check service status
curl http://localhost:8081/
```

## 📡 API Endpoints

### Worker Management
```http
GET    /api/workers              # List all workers
POST   /api/workers              # Create new worker
GET    /api/workers/{id}         # Get worker by ID
PUT    /api/workers/{id}         # Update worker
DELETE /api/workers/{id}         # Delete worker
```

### Shift Management
```http
GET    /api/shifts               # List all shifts
POST   /api/shifts               # Create new shift
GET    /api/shifts/{id}          # Get shift by ID
PUT    /api/shifts/{id}          # Update shift
DELETE /api/shifts/{id}          # Delete shift
GET    /api/shifts/date/{date}   # Get shifts by date
```

### Role Management
```http
GET    /api/roles                # List all roles
POST   /api/roles                # Create new role
GET    /api/roles/{id}           # Get role by ID
PUT    /api/roles/{id}           # Update role
DELETE /api/roles/{id}           # Delete role
```

### Shift Assignment Management
```http
GET    /api/assignments          # List all assignments
POST   /api/assignments          # Create assignment
GET    /api/assignments/{id}     # Get assignment by ID
PUT    /api/assignments/{id}     # Update assignment
DELETE /api/assignments/{id}     # Delete assignment
```

### System Endpoints
```http
GET    /                         # Service status
GET    /actuator/health          # Health check
GET    /actuator/info           # Service information
GET    /actuator/metrics        # Performance metrics
```

## 📋 API Usage Examples

### Create Worker
```bash
curl -X POST http://localhost:8081/api/workers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "role": "Manager"
  }'
```

### Create Shift
```bash
curl -X POST http://localhost:8081/api/shifts \
  -H "Content-Type: application/json" \
  -d '{
    "date": "2024-01-15",
    "workerId": "550e8400-e29b-41d4-a716-446655440000"
  }'
```

### Get Shifts by Date
```bash
curl http://localhost:8081/api/shifts/date/2024-01-15
```

## 🔧 Configuration

### Application Properties
```yaml
server:
  port: 8081

spring:
  application:
    name: scheduler-service
  datasource:
    url: jdbc:postgresql://localhost:5434/scheduler_db
    username: postgres
    password: password
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
  flyway:
    enabled: true
    locations: classpath:db/migration
```

### Environment Variables
```bash
# Database Configuration
DB_URL=jdbc:postgresql://localhost:5434/scheduler_db
DB_USER=postgres
DB_PASS=password

# Application Configuration
SERVER_PORT=8081
SHOW_SQL=false

# Actuator Configuration
MANAGEMENT_ENDPOINTS=health,info,metrics,prometheus
```

## 🧪 Testing

### Run Tests
```bash
# Run all tests
mvn test

# Run with coverage
mvn test jacoco:report
```

### Test Categories
- **Unit Tests** - Entity logic and business rules
- **Integration Tests** - API endpoint testing
- **Repository Tests** - Database layer testing
- **Controller Tests** - REST API testing

### Test Database
Tests use H2 in-memory database for isolation and speed.

## 📊 Database Migrations

### Flyway Integration
Database schema is managed with Flyway migrations:

```sql
-- Example migration: V1__Create_worker_table.sql
CREATE TABLE worker (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);
```

### Migration Commands
```bash
# Run migrations
mvn flyway:migrate

# Check migration status
mvn flyway:info

# Baseline existing database
mvn flyway:baseline
```

## 📈 Monitoring & Observability

### Actuator Endpoints
- `GET /actuator/health` - Service health status
- `GET /actuator/info` - Build and application information
- `GET /actuator/metrics` - Performance metrics
- `GET /actuator/prometheus` - Prometheus-compatible metrics

### Health Checks
The service provides comprehensive health checks:
- Database connectivity
- Application startup status
- System resource availability

### Metrics
Key metrics monitored:
- Request throughput
- Response times
- Database connection pool status
- JVM memory usage

## 🔍 Business Logic

### Scheduling Features
- **Conflict Detection** - Prevents double-booking workers
- **Role-Based Assignment** - Ensures workers are assigned appropriate shifts
- **Date-Based Querying** - Efficient shift lookups by date
- **Worker Availability** - Tracks worker schedules and availability

### Data Validation
- **UUID Validation** - Ensures valid entity identifiers
- **Date Validation** - Prevents scheduling in the past
- **Role Consistency** - Validates worker-role assignments
- **Constraint Enforcement** - Database-level integrity checks

## 🚀 Deployment

### Docker Deployment
```dockerfile
FROM openjdk:17-jre-slim
COPY target/devops_micro_project-1.0-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Production Considerations
- **Connection Pooling** - Configure HikariCP for optimal performance
- **Database Migrations** - Ensure Flyway runs on startup
- **Health Monitoring** - Configure monitoring dashboards
- **Backup Strategy** - Regular PostgreSQL backups

## 🔍 Troubleshooting

### Common Issues
1. **Database Connection** - Check Docker container status
2. **Port Conflicts** - Ensure port 8081 is available
3. **Migration Failures** - Check Flyway migration scripts
4. **Memory Issues** - Configure JVM heap size appropriately

### Debug Commands
```bash
# Check database container
docker ps | grep scheduler

# View database logs
docker logs item-service-db

# Check service logs
mvn spring-boot:run | tee service.log

# Test database connection
docker exec -it item-service-db psql -U postgres -d scheduler_db
```

## 🤝 Contributing

### Development Workflow
1. Create feature branch
2. Implement changes with tests
3. Run full test suite
4. Create database migration if needed
5. Update API documentation
6. Submit pull request

### Code Standards
- Use Lombok for boilerplate reduction
- Follow Spring Boot best practices
- Include comprehensive tests
- Document API changes
- Use meaningful commit messages

---

**Service Name**: Scheduler Service  
**Port**: 8081  
**Database**: PostgreSQL (localhost:5434)  
**Framework**: Spring Boot 3.1.5  
**Primary Purpose**: Worker and shift scheduling management