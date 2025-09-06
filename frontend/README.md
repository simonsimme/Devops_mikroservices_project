# Frontend Applications

React-based web applications for the DevOps Microservices Project.

## 📁 Structure

```
frontend/
├── README.md                    # This file - Frontend overview
└── devops-frontend/            # Main React application
    ├── src/                    # Source code
    ├── public/                 # Static assets
    ├── package.json           # Dependencies and scripts
    └── README.md              # Detailed setup instructions
```

## 🎯 Applications

### DevOps Frontend
**Location**: `./devops-frontend/`  
**Port**: 3000 (development) / 80 (production)  
**Purpose**: Main user interface for the microservices system

**Features**:
- User authentication and login
- Company schedule management interface
- Responsive React-based UI
- Integration with backend microservices via API Gateway

## 🚀 Quick Start

### Prerequisites
- Node.js 16+ and npm
- Running backend services (see main README)

### Development Setup
```bash
# Navigate to the main application
cd devops-frontend

# Install dependencies
npm install

# Start development server
npm start
```

The application will be available at http://localhost:3000

### Production Build
```bash
# Build for production
npm run build

# The build artifacts will be in the build/ directory
```

## 🔧 Configuration

The frontend connects to backend services through the API Gateway at:
- **Development**: http://localhost:8080
- **Production**: Configure via environment variables

## 📚 Documentation

For detailed setup, development, and deployment instructions, see:
- **[DevOps Frontend README](./devops-frontend/README.md)** - Comprehensive application documentation



## 🛠️ Technology Stack

- **React 19** - UI framework
- **React Router DOM 7** - Client-side routing  
- **Modern JavaScript/ES6+** - Programming language
- **CSS3** - Styling
- **Jest** - Testing framework

---

**Port Information**:
- Development: http://localhost:3000
- Production: Port 80 (via Docker/K8s)