# API Gateway Service

Spring Cloud Gateway implementation that serves as the entry point for the DevOps Microservices Project, providing request routing, authentication, and cross-cutting concerns.

## 🎯 Purpose

The API Gateway acts as a single entry point for all client requests, providing:
- **Request Routing** - Routes requests to appropriate microservices
- **Authentication** - JWT token validation and user authentication
- **CORS Handling** - Cross-origin resource sharing configuration
- **Load Balancing** - Distributes requests across service instances
- **Centralized Logging** - Request/response logging and monitoring

## 🛠️ Technology Stack

- **Java 17** - Programming language
- **Spring Boot 3.2.6** - Application framework
- **Spring Cloud Gateway 4.1.4** - Gateway implementation
- **Spring WebFlux** - Reactive web framework
- **JWT (Auth0 & JJWT)** - Token authentication
- **Lombok** - Code generation
- **Maven** - Build tool

## 📁 Project Structure

```
gateway/gate/
├── src/main/java/com/gateway/
│   ├── GatewayApplication.java     # Main application class
│   ├── config/                    # Configuration classes
│   ├── filter/                    # Custom gateway filters
│   └── security/                  # Authentication filters
├── src/main/resources/
│   ├── application.yml            # Gateway configuration
│   └── routes.yml                # Route definitions
├── pom.xml                       # Dependencies and build config
└── README.md                     # This file
```

## 🚀 Getting Started

### Prerequisites
- **Java 17** or higher
- **Maven 3.6+**
- Running microservices (User Service, Item Service)

### Running the Gateway

#### Option 1: Maven (Development)
```bash
cd gateway/gate
mvn spring-boot:run
```

#### Option 2: JAR (Production)
```bash
mvn clean package
java -jar target/gate-1.0-SNAPSHOT.jar
```

#### Option 3: Docker
```bash
# Build Docker image
docker build -t gateway-service .

# Run container
docker run -p 8080:8080 gateway-service
```

The gateway will be available at http://localhost:8080

## 🌐 Route Configuration

### Service Routes
The gateway routes requests based on path patterns:

```yaml
# Example route configuration
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: http://localhost:8081
          predicates:
            - Path=/api/auth/**
          filters:
            - AuthenticationFilter
            
        - id: item-service
          uri: http://localhost:8082
          predicates:
            - Path=/api/items/**
          filters:
            - AuthenticationFilter
```

### Route Definitions
- **User Service Routes** (`/api/auth/**`) → User authentication and management
- **Item Service Routes** (`/api/items/**`) → Item/schedule management
- **Health Checks** (`/actuator/**`) → Service monitoring

## 🔒 Security & Authentication

### JWT Authentication Flow
1. **Token Validation** - Validates JWT tokens from Authorization header
2. **User Context** - Extracts user information from valid tokens
3. **Request Forwarding** - Forwards authenticated requests to target services
4. **Error Handling** - Returns appropriate HTTP status codes for invalid tokens

### Security Features
- **Stateless Authentication** - No server-side sessions
- **Token Expiration** - Automatic token validation
- **CORS Support** - Configurable cross-origin policies
- **Rate Limiting** - Request throttling capabilities

### Configuration
```yaml
# JWT Configuration
jwt:
  secret: ${JWT_SECRET:your-secret-key}
  expiration: 86400000  # 24 hours

# CORS Configuration
spring:
  cloud:
    gateway:
      globalcors:
        corsConfigurations:
          '[/**]':
            allowedOrigins: "http://localhost:3000"
            allowedMethods: "*"
            allowedHeaders: "*"
```

## 📡 API Endpoints

### Health & Monitoring
```http
GET /actuator/health        # Gateway health status
GET /actuator/info         # Gateway information
GET /actuator/metrics      # Performance metrics
```

### Proxied Endpoints
All service endpoints are accessible through the gateway:

#### Authentication (User Service)
```http
POST /api/auth/register    # User registration
POST /api/auth/login      # User authentication
GET  /api/auth/users      # List users (dev only)
```

#### Business Logic (Item Service)
```http
GET    /api/items         # List items
POST   /api/items         # Create item
PUT    /api/items/{id}    # Update item
DELETE /api/items/{id}    # Delete item
```

## 🔧 Configuration

### Environment Variables
```bash
# JWT Configuration
JWT_SECRET=your-jwt-secret-key

# Service URLs
USER_SERVICE_URL=http://user-service:8080
ITEM_SERVICE_URL=http://item-service:8080

# Server Configuration
SERVER_PORT=8080
```

### Application Properties
```yaml
server:
  port: 8080

spring:
  application:
    name: api-gateway
  
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true
          lower-case-service-id: true
```



## 📊 Monitoring & Observability

### Actuator Endpoints
- `/actuator/health` - Service health status
- `/actuator/info` - Build and application information
- `/actuator/metrics` - Performance metrics
- `/actuator/gateway/routes` - Active route definitions

### Logging
The gateway provides comprehensive logging for:
- Request/response details
- Route matching and forwarding
- Authentication events
- Error conditions

### Metrics
Key metrics monitored:
- Request throughput
- Response times
- Error rates
- Active connections





---

**Default Port**: 8080  
**Framework**: Spring Cloud Gateway  
**Authentication**: JWT-based  
**Routing**: Path-based with filters