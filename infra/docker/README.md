# Docker Development Environment

Docker Compose configurations and startup scripts for local development of the DevOps Microservices Project.

## 🎯 Purpose

This directory provides:
- **Automated Backend Startup** - Single command to start all services
- **Database Containers** - PostgreSQL instances for each microservice  
- **Development Environment** - Consistent local development setup
- **Cross-Platform Scripts** - Support for Linux, macOS, and Windows

## 📁 Contents

```
infra/docker/
├── README.md                  # This file - Docker documentation
├── start-backend.sh          # Linux/macOS startup script
├── start-backend.ps1         # PowerShell script for Windows
└── start-backend.bat         # Windows batch script
```

## 🚀 Quick Start

### Prerequisites
- **Docker** and Docker Compose installed
- **Java 17** or higher
- **Maven 3.6+**
- **Terminal/Command Prompt** access

### Start All Services

#### Linux/macOS
```bash
cd infra/docker
chmod +x start-backend.sh
./start-backend.sh
```

#### Windows (PowerShell)
```powershell
cd infra/docker
.\start-backend.ps1
```

#### Windows (Command Prompt)
```cmd
cd infra\docker
start-backend.bat
```

## 🔧 What the Scripts Do

The startup scripts perform the following automated sequence:

### 1. Database Startup
```bash
# Start User Service database
cd services/user-service
docker compose up -d

# Start Scheduler Service database  
cd services/item-service/devops_mirco_project
docker compose up -d
```

### 2. Service Startup
After a 10-second wait for databases to initialize:
- **User Service** - Launched in new terminal on port 8080
- **Scheduler Service** - Launched in new terminal on port 8081  
- **API Gateway** - Launched in new terminal on port 8080

### 3. New Terminal Windows
Each service runs in its own terminal window for:
- **Independent logging** - Separate log streams per service
- **Easy debugging** - Individual service control
- **Development workflow** - Start/stop services independently

## 🗄️ Database Configuration

### User Service Database
- **Container Name**: user-service-db
- **Image**: postgres:16
- **Port**: 5433 → 5432 (host → container)
- **Database**: users_db
- **Username**: user
- **Password**: pass
- **Volume**: postgres_data

```yaml
# Docker Compose excerpt
services:
  postgres:
    image: postgres:16
    container_name: user-service-db
    environment:
      POSTGRES_DB: users_db
      POSTGRES_USER: user
      POSTGRES_PASSWORD: pass
    ports:
      - "5433:5432"
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U user -d users_db"]
      interval: 30s
      timeout: 10s
      retries: 3
```

### Scheduler Service Database
- **Container Name**: scheduler-service-db (or similar)
- **Image**: postgres:16  
- **Port**: 5434 → 5432 (host → container)
- **Database**: scheduler_db
- **Username**: postgres
- **Password**: password

## 🔍 Manual Database Operations

### Connect to Databases
```bash
# User Service database
docker exec -it user-service-db psql -U user -d users_db

# Scheduler Service database
docker exec -it scheduler-service-db psql -U postgres -d scheduler_db
```

### Check Database Status
```bash
# List running containers
docker ps | grep postgres

# Check database logs
docker logs user-service-db
docker logs scheduler-service-db

# Check database health
docker exec user-service-db pg_isready -U user -d users_db
```

### Database Management
```bash
# Stop databases
docker compose down

# Start databases only
docker compose up -d

# Remove database volumes (CAUTION: Data loss)
docker compose down -v
```

## 🛠️ Development Workflow

### Typical Development Session
1. **Start Infrastructure**: Run the startup script
2. **Frontend Development**: Navigate to `frontend/devops-frontend` and run `npm start`
3. **Backend Changes**: Edit code in service directories, Spring Boot auto-reloads
4. **Database Changes**: Add Flyway migrations as needed
5. **Testing**: Run tests in individual service directories

### Service URLs
- **Frontend**: http://localhost:3000
- **API Gateway**: http://localhost:8080
- **User Service**: http://localhost:8080 (via gateway)
- **Scheduler Service**: http://localhost:8081
- **User Database**: localhost:5433
- **Scheduler Database**: localhost:5434

## 🔧 Customization

### Environment Variables
You can customize the setup by setting environment variables:

```bash
# Database configuration
export USER_DB_PORT=5433
export SCHEDULER_DB_PORT=5434
export POSTGRES_PASSWORD=your-password

# Service configuration  
export USER_SERVICE_PORT=8080
export SCHEDULER_SERVICE_PORT=8081
export GATEWAY_PORT=8080
```

### Script Modifications
To modify the startup behavior:
1. Edit the appropriate script (`start-backend.sh`, `start-backend.ps1`, or `start-backend.bat`)
2. Adjust database startup commands
3. Modify service startup sequence
4. Change terminal/window creation logic

## 🧪 Testing the Setup

### Health Checks
```bash
# Check all databases
docker ps | grep postgres

# Check service endpoints
curl http://localhost:8080/actuator/health  # Gateway
curl http://localhost:8080/actuator/health  # User Service (via gateway)
curl http://localhost:8081/actuator/health  # Scheduler Service

# Check database connectivity
docker exec user-service-db pg_isready -U user -d users_db
```

### Smoke Test
```bash
# Test user registration
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@example.com","password":"password"}'

# Test scheduler service
curl http://localhost:8081/api/workers
```

## 🔍 Troubleshooting

### Common Issues

#### Port Conflicts
```bash
# Check what's using ports
lsof -i :5433  # User database
lsof -i :5434  # Scheduler database
lsof -i :8080  # Gateway/User service
lsof -i :8081  # Scheduler service

# Kill processes if needed
sudo kill -9 <PID>
```

#### Database Connection Issues
```bash
# Check container status
docker ps -a | grep postgres

# Restart databases
cd services/user-service && docker compose restart
cd services/item-service/devops_mirco_project && docker compose restart

# Check logs
docker logs user-service-db
```

#### Service Startup Issues
```bash
# Check Java version
java -version

# Check Maven
mvn -version

# Verify project structure
ls -la services/user-service/devops_mirco_project/pom.xml
ls -la services/item-service/devops_mirco_project/pom.xml
ls -la gateway/gate/pom.xml
```

### Clean Reset
```bash
# Stop all services
docker compose down

# Remove all containers and volumes
docker compose down -v --remove-orphans

# Clean Maven builds
cd services/user-service/devops_mirco_project && mvn clean
cd services/item-service/devops_mirco_project && mvn clean
cd gateway/gate && mvn clean

# Restart from scratch
./start-backend.sh
```

## 📚 Related Documentation

- **[Main Project README](../../README.md)** - Project overview and architecture
- **[User Service](../../services/user-service/README.md)** - Authentication service details
- **[Scheduler Service](../../services/item-service/README.md)** - Scheduling service details
- **[API Gateway](../../gateway/README.md)** - Gateway configuration and routing

---

**Purpose**: Local development environment setup  
**Database Engine**: PostgreSQL 16  
**Container Management**: Docker Compose  
**Startup Method**: Automated cross-platform scripts