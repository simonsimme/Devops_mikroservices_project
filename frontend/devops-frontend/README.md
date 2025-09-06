# DevOps Frontend Application

A React-based web application providing the user interface for the DevOps Microservices Project. This application features user authentication and a company schedule management system.

## 🎯 Application Overview

This is the main frontend application that provides:
- **User Authentication** - Login and registration interface
- **Company Schedule Interface** - Landing page for schedule management
- **Responsive Design** - Mobile-friendly user interface
- **API Integration** - Connects to backend microservices via API Gateway

## 🛠️ Technology Stack

- **React 19.1.1** - Core UI framework
- **React Router DOM 7.8.2** - Client-side routing and navigation
- **React Scripts 5.0.1** - Build tools and development server
- **Testing Library** - Comprehensive testing utilities
- **Web Vitals** - Performance monitoring

## 📁 Project Structure

```
devops-frontend/
├── public/
│   ├── index.html             # HTML template
│   ├── favicon.ico           # Application icon
│   └── manifest.json         # PWA manifest
├── src/
│   ├── App.js               # Main application component
│   ├── Login.js             # Authentication component
│   ├── landingpage.js       # Schedule management interface
│   ├── *.css               # Component styles
│   └── assets/             # Images and static resources
├── package.json            # Dependencies and scripts
└── README.md              # This file
```

## 🚀 Getting Started

### Prerequisites
- **Node.js 16+** and npm
- Backend services running (User Service, Item Service, Gateway)

### Installation
```bash
# Install dependencies
npm install
```

### Development
```bash
# Start development server
npm start
```

The application will open at http://localhost:3000 and automatically reload when you make changes.

### Building for Production
```bash
# Create optimized production build
npm run build
```

The build artifacts will be stored in the `build/` directory, ready for deployment.

## 🔌 API Integration

The frontend communicates with backend services through the API Gateway:

### Authentication Endpoints
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration

### Configuration
Update API base URL in source files as needed:
- **Development**: http://localhost:8080 (API Gateway)
- **Production**: Configure via environment variables



## 🎨 Styling

The application uses:
- **CSS3** - Custom component styles
- **Responsive Design** - Mobile-first approach
- **Component-Scoped CSS** - Modular styling approach

Key style files:
- `App.css` - Main application styles
- `Login.css` - Authentication interface styles
- `landingpage.css` - Schedule interface styles

## 🔧 Development Scripts

```bash
# Start development server with hot reload
npm start

# Run all tests in watch mode
npm test

# Build production-ready application
npm run build

# Eject from Create React App (one-way operation)
npm run eject
```

## 📱 Features

### Authentication Flow
1. **Login Page** (`/`) - User authentication interface
2. **Registration** - New user account creation
3. **Protected Routes** - Authenticated user access

### Schedule Management
- **Landing Page** (`/landing`) - Main application interface
- **Company Schedule** - Schedule viewing and management
- **User Dashboard** - Personalized user experience

## 🌐 Routing

The application uses React Router for navigation:

```javascript
Route Structure:
├── / (Login page)
└── /landing (Schedule interface)
```

## 🔒 Security

- **JWT Token Management** - Secure authentication tokens
- **Protected Routes** - Authorization-based access control
- **CORS Configuration** - Cross-origin request handling

## 📦 Dependencies

### Core Dependencies
- `react` - UI framework
- `react-dom` - DOM rendering
- `react-router-dom` - Client-side routing
- `web-vitals` - Performance monitoring

### Development Dependencies
- `@testing-library/*` - Testing utilities
- `react-scripts` - Build tools and dev server



## 🔍 Performance

The application is optimized for:
- **Fast Loading** - Code splitting and lazy loading
- **Responsive UI** - Smooth user interactions
- **Bundle Size** - Optimized production builds
- **Web Vitals** - Core performance metrics

---

**Development Server**: http://localhost:3000  
**Build Command**: `npm run build`  
**Test Command**: `npm test`  
**Framework**: React 19 with Create React App
