# CI/CD Pipeline

Continuous Integration and Continuous Deployment configurations for the DevOps Microservices Project, enabling automated testing, building, and deployment.

## 🎯 Purpose

This directory is designed to contain CI/CD pipeline configurations for:
- **Automated Testing** - Unit tests, integration tests, and end-to-end testing
- **Code Quality** - Linting, security scanning, and code coverage
- **Build Automation** - Docker image building and artifact generation
- **Deployment Automation** - Automated deployment to development, staging, and production
- **Infrastructure as Code** - Terraform or similar for cloud resources
- **Monitoring & Alerts** - Pipeline status monitoring and notifications

## 📁 Planned Structure

```
infra/ci-cd/
├── README.md                     # This file - CI/CD documentation
├── github-actions/               # GitHub Actions workflows
│   ├── ci.yml                   # Continuous Integration pipeline
│   ├── cd-dev.yml               # Development deployment
│   ├── cd-staging.yml           # Staging deployment
│   ├── cd-production.yml        # Production deployment
│   └── security-scan.yml        # Security scanning pipeline
├── docker/                       # Docker build configurations
│   ├── Dockerfile.user-service  # User service container
│   ├── Dockerfile.scheduler     # Scheduler service container
│   ├── Dockerfile.gateway       # Gateway service container
│   └── Dockerfile.frontend      # Frontend application container
├── terraform/                    # Infrastructure as Code
│   ├── dev/                     # Development environment
│   ├── staging/                 # Staging environment
│   └── production/              # Production environment
├── scripts/                      # Deployment and utility scripts
│   ├── build.sh                # Build all services
│   ├── test.sh                 # Run all tests
│   ├── deploy.sh               # Deployment script
│   └── rollback.sh             # Rollback script
└── monitoring/                   # Pipeline monitoring
    ├── alerts.yml              # Pipeline failure alerts
    └── dashboards/             # CI/CD metrics dashboards
```

## 🔄 CI/CD Pipeline Overview

### Continuous Integration (CI)
```yaml
# Example GitHub Actions CI Pipeline
name: Continuous Integration

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest
    
    services:
      postgres:
        image: postgres:16
        env:
          POSTGRES_PASSWORD: postgres
        options: >-
          --health-cmd pg_isready
          --health-interval 10s
          --health-timeout 5s
          --health-retries 5
    
    steps:
    - uses: actions/checkout@v4
    
    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
    
    - name: Set up Node.js
      uses: actions/setup-node@v4
      with:
        node-version: '18'
    
    - name: Test User Service
      run: |
        cd services/user-service/devops_mirco_project
        mvn clean test
    
    - name: Test Scheduler Service
      run: |
        cd services/item-service/devops_mirco_project
        mvn clean test
    
    - name: Test Gateway
      run: |
        cd gateway/gate
        mvn clean test
    
    - name: Test Frontend
      run: |
        cd frontend/devops-frontend
        npm ci
        npm test -- --coverage --watchAll=false
    
    - name: Upload Coverage Reports
      uses: codecov/codecov-action@v3
```

### Continuous Deployment (CD)
```yaml
# Example deployment pipeline
name: Deploy to Development

on:
  push:
    branches: [ develop ]

jobs:
  deploy:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v4
    
    - name: Build Docker Images
      run: |
        docker build -t user-service:${{ github.sha }} -f docker/Dockerfile.user-service .
        docker build -t scheduler-service:${{ github.sha }} -f docker/Dockerfile.scheduler .
        docker build -t gateway:${{ github.sha }} -f docker/Dockerfile.gateway .
        docker build -t frontend:${{ github.sha }} -f docker/Dockerfile.frontend .
    
    - name: Push to Registry
      run: |
        echo ${{ secrets.DOCKER_PASSWORD }} | docker login -u ${{ secrets.DOCKER_USERNAME }} --password-stdin
        docker push user-service:${{ github.sha }}
        docker push scheduler-service:${{ github.sha }}
        docker push gateway:${{ github.sha }}
        docker push frontend:${{ github.sha }}
    
    - name: Deploy to Kubernetes
      run: |
        echo ${{ secrets.KUBECONFIG }} | base64 -d > kubeconfig
        export KUBECONFIG=kubeconfig
        kubectl set image deployment/user-service user-service=user-service:${{ github.sha }}
        kubectl set image deployment/scheduler-service scheduler-service=scheduler-service:${{ github.sha }}
        kubectl set image deployment/gateway gateway=gateway:${{ github.sha }}
        kubectl set image deployment/frontend frontend=frontend:${{ github.sha }}
        kubectl rollout status deployment/user-service
        kubectl rollout status deployment/scheduler-service
        kubectl rollout status deployment/gateway
        kubectl rollout status deployment/frontend
```

## 🏗️ Build Process

### Multi-Service Build
```bash
#!/bin/bash
# build.sh - Build all services

set -e

echo "Building User Service..."
cd services/user-service/devops_mirco_project
mvn clean package -DskipTests
cd ../../..

echo "Building Scheduler Service..."
cd services/item-service/devops_mirco_project
mvn clean package -DskipTests
cd ../../..

echo "Building Gateway..."
cd gateway/gate
mvn clean package -DskipTests
cd ../..

echo "Building Frontend..."
cd frontend/devops-frontend
npm ci
npm run build
cd ../..

echo "All services built successfully!"
```

### Docker Image Building
```dockerfile
# Example Dockerfile for User Service
FROM openjdk:17-jre-slim

WORKDIR /app

COPY services/user-service/devops_mirco_project/target/user-service-*.jar app.jar

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
```

## 🧪 Testing Strategy

### Test Pyramid
1. **Unit Tests** - Individual component testing
2. **Integration Tests** - Service integration testing
3. **Contract Tests** - API contract validation
4. **End-to-End Tests** - Full system testing
5. **Performance Tests** - Load and stress testing

### Automated Testing Pipeline
```yaml
# Testing stages
test-stages:
  unit-tests:
    - User Service: mvn test
    - Scheduler Service: mvn test
    - Gateway: mvn test
    - Frontend: npm test
  
  integration-tests:
    - Database connectivity
    - Service-to-service communication
    - API endpoint validation
  
  e2e-tests:
    - User registration flow
    - Authentication workflow
    - Schedule management operations
    - Full application workflow
  
  performance-tests:
    - Load testing with JMeter
    - Stress testing
    - Database performance testing
```

## 🔒 Security & Quality Gates

### Code Quality Checks
```yaml
# Example quality gates
quality-gates:
  code-coverage:
    minimum: 80%
    tools: [JaCoCo, Jest]
  
  security-scanning:
    tools:
      - Snyk (dependency vulnerabilities)
      - SonarQube (code quality)
      - Trivy (container scanning)
      - OWASP ZAP (dynamic security testing)
  
  code-style:
    java: Checkstyle
    javascript: ESLint
    
  license-compliance:
    tools: [FOSSA, WhiteSource]
```

### Security Pipeline
```yaml
# Security scanning workflow
name: Security Scan

on:
  schedule:
    - cron: '0 2 * * *'  # Daily at 2 AM
  push:
    branches: [ main ]

jobs:
  security:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v4
    
    - name: Run Snyk Security Scan
      uses: snyk/actions/maven@master
      env:
        SNYK_TOKEN: ${{ secrets.SNYK_TOKEN }}
    
    - name: Container Security Scan
      run: |
        docker run --rm -v $(pwd):/workspace aquasec/trivy fs /workspace
    
    - name: OWASP ZAP Security Scan
      uses: zaproxy/action-baseline@v0.7.0
      with:
        target: 'http://localhost:8080'
```

## 🚀 Deployment Strategies

### Blue-Green Deployment
```yaml
# Blue-Green deployment strategy
deployment-strategy:
  type: blue-green
  steps:
    1. Deploy to green environment
    2. Run smoke tests on green
    3. Switch traffic to green
    4. Monitor for issues
    5. Keep blue as rollback option
```

### Rolling Deployment
```yaml
# Kubernetes rolling deployment
strategy:
  type: RollingUpdate
  rollingUpdate:
    maxUnavailable: 1
    maxSurge: 1
```

### Canary Deployment
```yaml
# Canary deployment with Istio
canary:
  traffic-split:
    stable: 90%
    canary: 10%
  promotion-criteria:
    - error-rate < 1%
    - response-time < 500ms
    - success-rate > 99%
```

## 📊 Monitoring & Observability

### Pipeline Metrics
- **Build Success Rate** - Percentage of successful builds
- **Test Coverage** - Code coverage across all services
- **Deployment Frequency** - How often deployments occur
- **Lead Time** - Time from commit to production
- **Mean Time to Recovery** - Time to fix failed deployments

### Alerting
```yaml
# Example alerting configuration
alerts:
  build-failure:
    channels: [slack, email]
    threshold: any-failure
  
  deployment-failure:
    channels: [slack, pagerduty]
    threshold: any-failure
  
  test-coverage-drop:
    channels: [slack]
    threshold: below-80%
  
  security-vulnerability:
    channels: [slack, email, pagerduty]
    threshold: high-severity
```

## 🔧 Environment Management

### Environment Promotion
```
Development → Staging → Production
     ↓           ↓         ↓
  Feature     Integration  Release
  Testing      Testing     Testing
```

### Environment Configuration
```yaml
environments:
  development:
    cluster: dev-k8s-cluster
    database: dev-postgres
    replicas: 1
    resources: minimal
  
  staging:
    cluster: staging-k8s-cluster
    database: staging-postgres
    replicas: 2
    resources: production-like
  
  production:
    cluster: prod-k8s-cluster
    database: prod-postgres-cluster
    replicas: 3
    resources: production
```

## 🛠️ Development Workflow

### GitFlow Integration
```
main (production) ← merge ← release/v1.0.0 ← merge ← develop ← merge ← feature/new-feature
                                                        ↓
                                                   auto-deploy to dev
```

### Pull Request Pipeline
1. **Code Review** - Automated and manual review
2. **CI Pipeline** - Tests and quality checks
3. **Security Scan** - Vulnerability assessment
4. **Preview Environment** - Temporary deployment for testing
5. **Approval** - Stakeholder approval
6. **Merge** - Automatic merge and deployment

## 📚 Best Practices

### CI/CD Best Practices
- **Fail Fast** - Quick feedback on failures
- **Immutable Deployments** - Never modify running containers
- **Infrastructure as Code** - Version-controlled infrastructure
- **Automated Rollbacks** - Automatic rollback on failure
- **Monitoring-Driven** - Deploy with comprehensive monitoring

### Secret Management
```yaml
# Secret management strategy
secrets:
  storage: Azure Key Vault / AWS Secrets Manager
  rotation: automatic-30-days
  access: least-privilege
  encryption: at-rest-and-in-transit
```

## 📁 Related Documentation

- **[Main Project README](../../README.md)** - Project overview and architecture
- **[Docker Setup](../docker/README.md)** - Local development environment
- **[Kubernetes](../k8s/README.md)** - Container orchestration setup

---

**Status**: 🚧 **Under Development** - Pipeline configurations not yet implemented  
**CI Platform**: GitHub Actions (recommended)  
**Container Registry**: Docker Hub / GitHub Container Registry  
**Deployment Target**: Kubernetes clusters  
**Infrastructure**: Cloud-native (AWS/Azure/GCP)