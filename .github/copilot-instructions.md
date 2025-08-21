# DevOps Mikroservices Project

A comprehensive DevOps microservices project designed for scalable, containerized applications with modern DevOps practices including CI/CD, container orchestration, and monitoring.

**ALWAYS reference these instructions first and fallback to search or bash commands only when you encounter unexpected information that does not match the information here.**

## Current Repository State

This repository is currently in its initial state with only a README.md file. The instructions below guide you through the complete setup and development process for building a production-ready microservices architecture.

## Working Effectively - Bootstrap Process

### Prerequisites Installation
Before starting development, install the required tools:

```bash
# Install Node.js (required for all services and frontend)
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt-get install -y nodejs

# Verify installation
node --version  # Should show v18.x.x
npm --version   # Should show 8.x.x or higher

# Install Docker (required for containerization)
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh
sudo usermod -aG docker $USER

# Install Docker Compose (required for local development)
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Verify Docker installation
docker --version
docker-compose --version
```

**TIMING**: Prerequisites installation takes 3-5 minutes. NEVER CANCEL during package installation.

**MEASURED PERFORMANCE**: 
- Docker Compose configuration validation: ~0.04 seconds
- NPM install (empty project): ~0.4 seconds  
- Docker image pull (hello-world): ~0.8 seconds
- Complete validation script: ~1-2 seconds

### Initial Project Structure Setup

The project follows this microservices architecture:

```
├── README.md                    # Project documentation
├── .github/
│   ├── copilot-instructions.md  # This file
│   └── workflows/               # CI/CD pipelines (when created)
├── gateway/                     # API Gateway service
├── services/
│   ├── user-service/           # User management microservice
│   └── item-service/           # Item management microservice
├── frontend/                   # React frontend application
├── infra/
│   ├── docker-compose.yml      # Local development setup
│   ├── k8s/                    # Kubernetes manifests
│   └── terraform/              # Infrastructure as Code
└── docs/                       # Additional documentation
```

To create this structure, run:

```bash
mkdir -p gateway services/user-service services/item-service frontend infra/k8s infra/terraform docs scripts
```

## Current Files and Configuration

The repository includes these starter files to help with development:

- `.github/copilot-instructions.md` - This instruction file
- `package.json` - Root package configuration with placeholder scripts
- `infra/docker-compose.yml` - Docker Compose configuration template
- `scripts/validate-setup.sh` - Validation script for setup verification
- `gateway/README.md` - Gateway service documentation template
- `services/user-service/README.md` - User service documentation template

## Development Workflow

### Building the Project

**CRITICAL: Currently there is NO build system in place.** When microservices are implemented, the build process will be:

```bash
# Install dependencies for all services (when package.json files exist)
npm install                              # Root dependencies
cd gateway && npm install               # Gateway dependencies  
cd ../services/user-service && npm install  # User service dependencies
cd ../item-service && npm install      # Item service dependencies
cd ../../frontend && npm install       # Frontend dependencies
```

**TIMING**: Full dependency installation takes 2-3 minutes. NEVER CANCEL during npm install operations.

### Running Services Locally

**CRITICAL: Currently there are NO services to run.** When services are implemented:

```bash
# Start all services with Docker Compose (when docker-compose.yml exists)
docker-compose up -d

# Or start individual services for development:
cd gateway && npm run dev               # Start gateway (typically port 3000)
cd services/user-service && npm run dev     # Start user service (typically port 3001)  
cd services/item-service && npm run dev     # Start item service (typically port 3002)
cd frontend && npm start               # Start frontend (typically port 3001)
```

**TIMING**: Service startup takes 30-60 seconds per service. NEVER CANCEL during service initialization.

### Testing

**CRITICAL: Currently there are NO tests.** When test suites are implemented:

```bash
# Run all tests
npm test                                # Run root test suite
npm run test:services                   # Run all microservice tests  
npm run test:integration               # Run integration tests
npm run test:e2e                       # Run end-to-end tests
```

**TIMING**: Test suites typically take 2-5 minutes. NEVER CANCEL test runs. Set timeout to 10+ minutes for comprehensive test suites.

### Docker Operations

```bash
# Build all service images (when Dockerfiles exist)
docker-compose build

# Start entire application stack
docker-compose up -d

# View logs  
docker-compose logs -f [service-name]

# Stop all services
docker-compose down

# Clean up (removes containers, networks, volumes)
docker-compose down -v --remove-orphans
```

**TIMING**: Docker build operations take 3-10 minutes depending on service complexity. NEVER CANCEL during image builds. Set timeout to 15+ minutes.

## Validation Scenarios

**CRITICAL**: After making changes, ALWAYS run through these validation scenarios:

### Quick Validation
Run the comprehensive validation script:

```bash
./scripts/validate-setup.sh
```

This script validates all core requirements including Node.js, Docker, repository structure, and configuration files.

### Basic Health Check Validation
When services are implemented, test each service endpoint:

```bash
# Test gateway health
curl http://localhost:3000/health

# Test user service health  
curl http://localhost:3001/health

# Test item service health
curl http://localhost:3002/health

# Test frontend loads
curl http://localhost:3000
```

### End-to-End User Workflow
When the application is implemented, test complete user scenarios:

1. **User Registration & Login**:
   - Create a new user account
   - Log in with credentials
   - Verify authentication token

2. **Item Management**:
   - Create a new item
   - List all items
   - Update an item
   - Delete an item

3. **Gateway Routing**:
   - Verify gateway correctly routes requests to services
   - Test load balancing if implemented
   - Verify error handling and timeouts

### Performance & Monitoring
When monitoring is implemented:

```bash
# Check resource usage
docker stats

# Monitor logs for errors
docker-compose logs | grep -i error

# Test concurrent requests (when services exist)
curl -s "http://localhost:3000/health" & 
curl -s "http://localhost:3000/health" &
curl -s "http://localhost:3000/health" &
wait
```

## CI/CD Pipeline

**CRITICAL: Currently there are NO CI/CD pipelines.** When GitHub Actions workflows are implemented:

```bash
# Validate workflow syntax (when .github/workflows/*.yml exist)
# GitHub automatically validates on push

# Local testing of Docker builds that CI will run
docker build -t gateway ./gateway/
docker build -t user-service ./services/user-service/
docker build -t item-service ./services/item-service/
docker build -t frontend ./frontend/
```

**TIMING**: CI/CD pipeline execution takes 5-15 minutes. NEVER CANCEL GitHub Actions workflows. They include:
- Linting and code quality checks (1-2 minutes)
- Unit tests (2-3 minutes)  
- Integration tests (3-5 minutes)
- Docker image builds (3-7 minutes)
- Deployment to staging (2-3 minutes)

## Code Quality & Standards

Before committing code, ALWAYS run:

```bash
# Linting (when configured)
npm run lint                            # Lint all code
npm run lint:fix                        # Auto-fix linting issues

# Formatting (when configured)  
npm run format                          # Format all code
npm run format:check                    # Check formatting

# Type checking (when TypeScript is used)
npm run type-check                      # TypeScript type checking
```

**TIMING**: Code quality checks take 1-2 minutes. NEVER CANCEL linting operations.

## Common Commands Reference

### Repository Status
```bash
# Check current repository state
ls -la                                  # List all files and directories
git status                              # Check git status
git branch -a                           # List all branches
```

### Docker Commands  
```bash
# Clean up Docker system (when needed)
docker system prune -f                 # Remove unused containers, networks, images
docker volume prune -f                 # Remove unused volumes

# Monitor container resource usage
docker stats --format "table {{.Container}}\t{{.CPUPerc}}\t{{.MemUsage}}"
```

### Kubernetes Operations (when implemented)
```bash
# Apply Kubernetes manifests (when k8s/ directory has manifests)
kubectl apply -f infra/k8s/

# Check deployment status
kubectl get pods,services,deployments

# View logs
kubectl logs -f deployment/gateway
kubectl logs -f deployment/user-service  
kubectl logs -f deployment/item-service
```

## Troubleshooting

### Common Issues

1. **Port conflicts**: Services use ports 3000-3002. Check if ports are available:
   ```bash
   lsof -i :3000
   lsof -i :3001  
   lsof -i :3002
   ```

2. **Docker permission issues**:
   ```bash
   sudo usermod -aG docker $USER
   # Log out and back in for changes to take effect
   ```

3. **Node.js version issues**: Ensure Node.js 18+ is installed:
   ```bash
   node --version
   # If wrong version, reinstall using instructions above
   ```

4. **Docker Compose issues**:
   ```bash
   docker-compose down -v
   docker system prune -f
   docker-compose up --build
   ```

### Debug Mode
When services support debug mode:

```bash
# Start services in debug mode
DEBUG=* docker-compose up

# Or for specific service debugging
cd gateway && DEBUG=gateway:* npm run dev
```

## Technology Stack

- **Services**: Node.js + Express.js
- **Frontend**: React with TypeScript  
- **Database**: PostgreSQL (for user/item data)
- **Message Queue**: Redis (for inter-service communication)
- **API Gateway**: Custom Node.js gateway with routing
- **Containerization**: Docker + Docker Compose
- **Orchestration**: Kubernetes
- **CI/CD**: GitHub Actions
- **Infrastructure**: Terraform (for cloud resources)
- **Monitoring**: Prometheus + Grafana (when implemented)

## Important Notes

- **NEVER CANCEL** long-running operations like builds, tests, or deployments
- **ALWAYS** test locally before pushing to remote branches
- **ALWAYS** run the complete validation scenarios after making changes
- **Use explicit timeouts** of 15+ minutes for build operations and 10+ minutes for test suites
- **Monitor resource usage** during development to prevent system overload
- **Follow the microservices patterns** outlined in this document
- **Keep services loosely coupled** and independently deployable

## Next Steps for Development

When implementing the microservices architecture:

1. **Start with the Gateway**: Implement basic routing and health checks
2. **Implement User Service**: Basic CRUD operations for user management
3. **Implement Item Service**: Basic CRUD operations for item management  
4. **Add Frontend**: React application that consumes the gateway API
5. **Add Infrastructure**: Docker Compose for local development
6. **Add Kubernetes**: Production-ready container orchestration
7. **Add CI/CD**: Automated testing and deployment pipelines
8. **Add Monitoring**: Observability and alerting systems

Remember: This is a microservices project, so each service should be independently deployable and maintainable.