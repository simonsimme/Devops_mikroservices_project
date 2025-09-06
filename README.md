# DevOps Microservices Project

A comprehensive microservices architecture project demonstrating modern DevOps practices with Spring Boot, React, and containerization.

## 🏗️ Architecture Overview

This project implements a distributed microservices architecture with:
- **API Gateway** for request routing and authentication
- **User Service** for authentication and user management
- **Item Service** for business logic and data management
- **React Frontend** for user interface
- **PostgreSQL** databases for data persistence
- **Docker** for containerization and development environment

## 📦 Project Structure

```
📦 Devops_mikroservices_project/
├── 🌐 gateway/                    # Spring Cloud Gateway (Port 8080)
│   └── gate/                     # Gateway service implementation
├── 🔧 services/                  # Microservices
│   ├── 👤 user-service/          # User Management & Auth (Port 8080)
│   │   └── devops_mirco_project/ # Service implementation
│   └── 📦 item-service/          # Item/Schedule Management (Port 8080)
│       └── devops_mirco_project/ # Service implementation
├── 🎨 frontend/                  # Frontend Applications
│   ├── README.md                # Frontend overview
│   └── devops-frontend/         # React Web App (Port 3000)
└── 🚀 infra/                     # Infrastructure & DevOps
    ├── 🐳 docker/               # Docker development environment
    ├── ☸️  k8s/                 # Kubernetes manifests
    └── 🔄 ci-cd/               # CI/CD pipeline configurations
```

## 🛠️ Technology Stack

### Backend Services
- **Java 17** - Programming language
- **Spring Boot 3.x** - Application framework
- **Spring Cloud Gateway** - API gateway and routing
- **Spring Security + JWT** - Authentication and authorization
- **Spring Data JPA** - Data persistence layer
- **PostgreSQL** - Primary database
- **Flyway** - Database migrations
- **Maven** - Build tool and dependency management

### Frontend
- **React 19** - UI framework
- **React Router DOM** - Client-side routing
- **Modern JavaScript/ES6+** - Programming language
- **npm** - Package management

### DevOps & Infrastructure
- **Docker & Docker Compose** - Containerization
- **Kubernetes** - Container orchestration
- **GitHub Actions** - CI/CD pipelines

## 🚀 Quick Start

### Prerequisites
- **Java 17** or higher
- **Node.js 16+** and npm
- **Docker** and Docker Compose
- **Maven 3.6+**
- **Git**

### 1. Clone the Repository
```bash
git clone https://github.com/simonsimme/Devops_mikroservices_project.git
cd Devops_mikroservices_project
```

### 2. Start Backend Services (Automated)
```bash
# Navigate to docker directory
cd infra/docker

# Run the startup script (Linux/Mac)
./start-backend.sh

# Or for Windows
start-backend.bat
```

This script will:
- Start PostgreSQL databases with Docker Compose
- Launch User Service on port 8080
- Launch Item Service on port 8080  
- Launch API Gateway on port 8080

### 3. Start Frontend
```bash
# Navigate to frontend
cd frontend/devops-frontend

# Install dependencies
npm install

# Start development server
npm start
```

The application will be available at:
- **Frontend**: http://localhost:3000
- **API Gateway**: http://localhost:8080
- **User Service**: http://localhost:8080 (via gateway)
- **Item Service**: http://localhost:8080 (via gateway)

## 📡 API Endpoints

### Authentication (via Gateway)
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User authentication
- `GET /api/auth/users` - List users (development only)

### Health Monitoring
- `GET /actuator/health` - Service health status
- `GET /actuator/info` - Service information

## 🧪 Testing

Each service includes comprehensive test suites:

```bash
# Test User Service
cd services/user-service/devops_mirco_project
mvn test

# Test Item Service  
cd services/item-service/devops_mirco_project
mvn test

# Test Frontend
cd frontend/devops-frontend
npm test
```

## 🔧 Development

### Service Ports
- **User Service**: 8080 (with database on 5433)
- **Item Service**: 8080 (with database on 5434)
- **Gateway**: 8080
- **Frontend**: 3000

### Database Connections
- **User Service DB**: PostgreSQL on localhost:5433
- **Item Service DB**: PostgreSQL on localhost:5434

### Environment Variables
Key environment variables for production:
- `JWT_SECRET` - JWT signing secret
- `POSTGRES_URL` - Database connection URL
- `POSTGRES_USER` - Database username
- `POSTGRES_PASSWORD` - Database password

## 📁 Component Documentation

Each component has detailed documentation in its respective directory:

- **[Gateway](./gateway/README.md)** - API Gateway configuration and routing
- **[User Service](./services/user-service/README.md)** - Authentication and user management
- **[Item Service](./services/item-service/README.md)** - Business logic and data management
- **[Frontend](./frontend/README.md)** - React application and UI components
- **[Infrastructure](./infra/docker/README.md)** - Docker and deployment setup

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is part of a DevOps learning initiative demonstrating microservices architecture and modern development practices.

---

**Project Status**: 🟢 Active Development  
**Last Updated**: 2024  
**Maintainer**: DevOps Team