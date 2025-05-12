#!/bin/bash

echo "🔧 Setting up HealthGig Platform for development..."

# Function to check if a command exists
command_exists() {
  command -v "$1" >/dev/null 2>&1
}

# Check for required tools
echo "📋 Checking for required tools..."

if ! command_exists docker; then
  echo "❌ Docker is not installed. Please install Docker first: https://docs.docker.com/get-docker/"
  exit 1
fi

if ! command_exists docker-compose; then
  echo "❌ Docker Compose is not installed. Please install Docker Compose first: https://docs.docker.com/compose/install/"
  exit 1
fi

if ! command_exists git; then
  echo "❌ Git is not installed. Please install Git first: https://git-scm.com/downloads"
  exit 1
fi

echo "✅ All required tools are installed."

# Create a .env file for local development if it doesn't exist
if [ ! -f ".env" ]; then
  echo "📝 Creating .env file for local development..."
  cat > .env << EOL
# Database Configuration
DB_HOST=postgres
DB_PORT=5432
DB_NAME=healthgig
DB_USERNAME=postgres
DB_PASSWORD=postgres

# JWT Configuration
JWT_SECRET=local_development_jwt_secret_key_change_in_production

# Stripe Configuration
STRIPE_API_KEY=your_stripe_test_key_here

# Sanity Configuration
SANITY_PROJECT_ID=your_sanity_project_id_here
SANITY_DATASET=production
EOL
  echo "✅ .env file created."
fi

# Build and start the development environment
echo "🚀 Starting development environment..."
docker-compose up -d

# Wait for the services to be ready
echo "⏳ Waiting for services to be ready..."
sleep 5

echo "\n✨ Development environment is ready!\n"
echo "📊 PostgreSQL database is running on localhost:5432"
echo "🔙 Backend API is running on http://localhost:8080/api"
echo "🌐 Frontend is running on http://localhost:3000"
echo "📚 API documentation is available at http://localhost:8080/api/swagger-ui.html"

echo "\nYou can start developing by:"
echo "1. Frontend: cd frontend && npm install && npm run dev"
echo "2. Backend: cd backend && ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev"
echo "3. CMS: cd cms/sanity && npm install && npm run dev"

echo "\n🎉 Happy coding!"