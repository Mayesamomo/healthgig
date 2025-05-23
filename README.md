# HealthGig Platform

A healthcare gig platform (similar to Instawork, but healthcare-focused) built as a modular monolith backend with a Next.js frontend.

## Project Status

This project is currently in development. The following components have been completed:

✅ Backend architecture and domain model design  
✅ Basic authentication flow with JWT  
✅ Core domain models and repositories  
✅ Frontend structure and authentication pages  
✅ CMS setup with Sanity  
✅ Docker and CI/CD configuration  
✅ Worker and employer dashboards (UI only)  

Still in progress:

⏳ Complete backend services and controllers  
⏳ Integration between frontend and backend  
⏳ Stripe payment processing  
⏳ Location services integration  
⏳ Advanced search and matching  

## Project Structure

### Backend (Modular Monolith)
- `auth`: Authentication and authorization
- `users`: User management (workers, employers, admins)
- `jobs`: Job posting and discovery
- `bookings`: Shift booking and management
- `payments`: Payment processing and tracking
- `reviews`: Reviews and ratings system
- `notifications`: In-app notification system
- `admin`: Admin dashboard and controls

### Frontend (Next.js with TypeScript)
- React components with Tailwind CSS
- Pages for workers, employers, and admins
- Integration with Sanity CMS for static content

### CMS (Sanity)
- Blog posts
- Static pages
- FAQs
- Testimonials

## Tech Stack

### Backend
- Spring Boot with modular structure
- PostgreSQL database
- JWT-based authentication
- Flyway for database migrations

### Frontend
- Next.js with TypeScript
- Tailwind CSS for styling
- Responsive UI

### Infrastructure
- Docker + Docker Compose for development
- GitHub Actions for CI/CD

### Third-party Integrations
- Stripe for payments
- Mapbox/Google Maps for location services
- Sanity for content management

## Documentation

- [Architecture Document](./ARCHITECTURE.md): Detailed architecture and design decisions
- [API Documentation](http://localhost:8080/api/swagger-ui.html): Interactive API documentation (when running locally)

## Getting Started

### Prerequisites
- Docker and Docker Compose
- Java 17 (for local development outside Docker)
- Node.js 18+ (for local development outside Docker)

### Running the Application

#### Using Docker Compose (Recommended)
1. Clone the repository
2. Run the setup script:
   ```
   # On Windows
   .\setup.bat
   
   # On Unix-based systems
   chmod +x setup.sh
   ./setup.sh
   ```
3. Access the frontend at: http://localhost:3000
4. Access the API at: http://localhost:8080/api
5. API documentation is available at: http://localhost:8080/api/swagger-ui.html

#### Local Development

**Backend:**
1. Start a PostgreSQL database:
   ```
   docker run --name healthgig-postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=healthgig -p 5432:5432 -d postgres:16
   ```

2. Navigate to the backend directory:
   ```
   cd backend
   ```

3. Run the Spring Boot application:
   ```
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
   ```
   
**Frontend:**
1. Navigate to the frontend directory:
   ```
   cd frontend
   ```

2. Install dependencies:
   ```
   npm install
   ```

3. Run the development server:
   ```
   npm run dev
   ```

**CMS:**
1. Navigate to the CMS directory:
   ```
   cd cms/sanity
   ```

2. Install dependencies:
   ```
   npm install
   ```

3. Run the Sanity Studio:
   ```
   npm run dev
   ```

### Test Users

When using the seed data, the following test users are available:

- Admin: admin@healthgig.com / admin123
- Employer: hospital@example.com / password123
- Employer: clinic@example.com / password123
- Worker: nurse@example.com / password123
- Worker: doctor@example.com / password123

## Development Workflow

1. Create a feature branch from main
2. Implement your changes
3. Write/update tests
4. Submit a pull request
5. CI pipeline will run tests
6. After approval and merge, CD pipeline will deploy



## License

This project is proprietary and confidential.
