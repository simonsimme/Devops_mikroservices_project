Write-Host "Starting backend services..." -ForegroundColor Green

# Get the script's directory and navigate to project root
$scriptPath = Split-Path -Parent $MyInvocation.MyCommand.Path
$projectRoot = Split-Path -Parent (Split-Path -Parent $scriptPath)
Set-Location $projectRoot

Write-Host "Project root: $projectRoot" -ForegroundColor Yellow

# Start databases
Write-Host "Starting databases..." -ForegroundColor Yellow
Set-Location "services\user-service"
docker-compose up -d

Set-Location "$projectRoot\services\item-service\devops_mirco_project"
docker-compose up -d

# Wait for databases
Write-Host "Waiting for databases to start..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

# Start services in new windows
Write-Host "Starting services..." -ForegroundColor Yellow

# User Service
Set-Location "$projectRoot\services\user-service\devops_mirco_project"
Start-Process powershell -ArgumentList "-NoExit", "-Command", "Set-Location '$projectRoot\services\user-service\devops_mirco_project'; mvn spring-boot:run"

# Scheduler Service  
Set-Location "$projectRoot\services\item-service\devops_mirco_project"
Start-Process powershell -ArgumentList "-NoExit", "-Command", "Set-Location '$projectRoot\services\item-service\devops_mirco_project'; mvn spring-boot:run"

# Gateway
Set-Location "$projectRoot\gateway\gate"
Start-Process powershell -ArgumentList "-NoExit", "-Command", "Set-Location '$projectRoot\gateway\gate'; mvn spring-boot:run"