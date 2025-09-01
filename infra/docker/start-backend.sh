#!/bin/bash

echo -e "\e[32mStarting backend services...\e[0m"

# Get the script's directory and navigate to project root
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$(dirname "$SCRIPT_DIR")")"
cd "$PROJECT_ROOT" || exit

echo -e "\e[33mProject root: $PROJECT_ROOT\e[0m"

# Start databases
echo -e "\e[33mStarting databases...\e[0m"
cd "$PROJECT_ROOT/services/user-service" || exit
docker compose up -d

cd "$PROJECT_ROOT/services/item-service/devops_mirco_project" || exit
docker compose up -d

# Wait for databases
echo -e "\e[33mWaiting for databases to start...\e[0m"
sleep 10

# Start services in new terminals
echo -e "\e[33mStarting services...\e[0m"

# User Service
gnome-terminal -- bash -c "cd '$PROJECT_ROOT/services/user-service/devops_mirco_project'; mvn spring-boot:run; exec bash"

# Scheduler Service
gnome-terminal -- bash -c "cd '$PROJECT_ROOT/services/item-service/devops_mirco_project'; mvn spring-boot:run; exec bash"

# Gateway
gnome-terminal -- bash -c "cd '$PROJECT_ROOT/gateway/gate'; mvn spring-boot:run; exec bash"