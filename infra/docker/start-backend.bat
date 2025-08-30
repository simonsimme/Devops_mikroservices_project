@echo off
echo Starting backend services...

echo Starting databases...
cd services\user-service
docker-compose up -d
cd ..\item-service\devops_mirco_project
docker-compose up -d

echo Waiting for databases to start...
timeout /t 10

echo Starting services...
cd ..\..\user-service\devops_mirco_project
start "User Service" mvn spring-boot:run

cd ..\..\item-service\devops_mirco_project
start "Scheduler Service" mvn spring-boot:run

cd ..\..\gateway\gate
start "Gateway" mvn spring-boot:run

echo Backend services are starting...
echo User Service: http://localhost:8082
echo Scheduler Service: http://localhost:8081
echo Gateway: http://localhost:8080 (default)
echo.
echo Check the individual windows for startup progress.
pause