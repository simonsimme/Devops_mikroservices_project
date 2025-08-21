#!/bin/bash

# Validation script for Copilot Instructions
# This script tests the key commands mentioned in the instructions

echo "=== Validating DevOps Mikroservices Project Setup ==="
echo

# Test 1: Basic repository structure
echo "1. Testing repository structure..."
if [ -d "gateway" ] && [ -d "services" ] && [ -d "frontend" ] && [ -d "infra" ]; then
    echo "✅ Directory structure is correct"
else
    echo "❌ Directory structure is missing"
    exit 1
fi

# Test 2: Node.js availability
echo "2. Testing Node.js installation..."
if command -v node > /dev/null 2>&1; then
    NODE_VERSION=$(node --version)
    echo "✅ Node.js is installed: $NODE_VERSION"
else
    echo "❌ Node.js is not installed"
    exit 1
fi

# Test 3: Docker availability  
echo "3. Testing Docker installation..."
if command -v docker > /dev/null 2>&1; then
    DOCKER_VERSION=$(docker --version)
    echo "✅ Docker is installed: $DOCKER_VERSION"
else
    echo "❌ Docker is not installed"
    exit 1
fi

# Test 4: Docker Compose availability
echo "4. Testing Docker Compose installation..."
if command -v docker-compose > /dev/null 2>&1; then
    COMPOSE_VERSION=$(docker-compose --version)
    echo "✅ Docker Compose is installed: $COMPOSE_VERSION"
else
    echo "❌ Docker Compose is not installed"
    exit 1
fi

# Test 5: Package.json validation
echo "5. Testing package.json configuration..."
if [ -f "package.json" ]; then
    if npm run test > /dev/null 2>&1; then
        echo "✅ Package.json is configured correctly"
    else
        echo "❌ Package.json test script failed"
        exit 1
    fi
else
    echo "❌ Package.json is missing"
    exit 1
fi

# Test 6: Docker Compose configuration
echo "6. Testing Docker Compose configuration..."
cd infra
if docker-compose config > /dev/null 2>&1; then
    echo "✅ Docker Compose configuration is valid"
else
    echo "❌ Docker Compose configuration is invalid"
    exit 1
fi
cd ..

# Test 7: Port availability
echo "7. Testing port availability..."
for port in 3000 3001 3002; do
    if ! lsof -i :$port > /dev/null 2>&1; then
        echo "✅ Port $port is available"
    else
        echo "⚠️  Port $port is in use"
    fi
done

# Test 8: Git repository status
echo "8. Testing Git repository status..."
if git status > /dev/null 2>&1; then
    echo "✅ Git repository is working"
else
    echo "❌ Git repository has issues"
    exit 1
fi

echo
echo "=== Validation Complete ==="
echo "✅ All core requirements are met"
echo "📝 Repository is ready for microservices development"