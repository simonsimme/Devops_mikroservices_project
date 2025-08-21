# DevOps Microservices Project

A comprehensive microservices architecture project with proper DevOps practices.

## Project Structure

```
📦 Devops_mikroservices_project/
├── 🌐 gateway/                    # API Gateway (Port 3000)
├── 🔧 services/
│   ├── 👤 user-service/          # User Management API (Port 3001)
│   └── 📦 item-service/          # Item Management API (Port 3002)
├── 🎨 frontend/                  # React Web Application (Port 80)
└── 🚀 infra/                     # Infrastructure & DevOps
    ├── 🐳 docker/               # Docker Compose files
    ├── ☸️  k8s/                 # Kubernetes manifests
    └── 🔄 ci-cd/               # Deployment scripts
```

## Components

### Services
- **Gateway**: API Gateway that routes requests to appropriate microservices
- **User Service**: Handles user management and authentication
- **Item Service**: Manages items/products in the system
- **Frontend**: React-based web application for user interface

### Infrastructure
- **Docker**: Containerization and local development environment
- **Kubernetes**: Container orchestration for production deployment
- **CI/CD**: Automated testing, building, and deployment pipelines

## Getting Started

Each component has its own README with specific setup instructions. Navigate to the respective directories for detailed information.