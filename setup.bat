@echo off
echo Setting up HealthGig Platform for development...

REM Check for required tools
echo Checking for required tools...

where docker >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
  echo Docker is not installed. Please install Docker first: https://docs.docker.com/get-docker/
  exit /b 1
)

where docker-compose >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
  echo Docker Compose is not installed. Please install Docker Compose first: https://docs.docker.com/compose/install/
  exit /b 1
)

where git >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
  echo Git is not installed. Please install Git first: https://git-scm.com/downloads
  exit /b 1
)

echo All required tools are installed.

REM Create a .env file for local development if it doesn't exist
if not exist ".env" (
  echo Creating .env file for local development...
  (
    echo # Database Configuration
    echo DB_HOST=postgres
    echo DB_PORT=5432
    echo DB_NAME=healthgig
    echo DB_USERNAME=postgres
    echo DB_PASSWORD=postgres
    echo.
    echo # JWT Configuration
    echo JWT_SECRET=local_development_jwt_secret_key_change_in_production
    echo.
    echo # Stripe Configuration
    echo STRIPE_API_KEY=your_stripe_test_key_here
    echo.
    echo # Sanity Configuration
    echo SANITY_PROJECT_ID=your_sanity_project_id_here
    echo SANITY_DATASET=production
  ) > .env
  echo .env file created.
)

REM Build and start the development environment
echo Starting development environment...
docker-compose up -d

REM Wait for the services to be ready
echo Waiting for services to be ready...
timeout /t 5 /nobreak >nul

echo.
echo Development environment is ready!
echo.
echo PostgreSQL database is running on localhost:5432
echo Backend API is running on http://localhost:8080/api
echo Frontend is running on http://localhost:3000
echo API documentation is available at http://localhost:8080/api/swagger-ui.html

echo.
echo You can start developing by:
echo 1. Frontend: cd frontend ^&^& npm install ^&^& npm run dev
echo 2. Backend: cd backend ^&^& .\mvnw spring-boot:run -Dspring-boot.run.profiles=dev
echo 3. CMS: cd cms/sanity ^&^& npm install ^&^& npm run dev

echo.
echo Happy coding!