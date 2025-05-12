# HealthGig Platform Architecture

This document describes the architecture and technical choices for the HealthGig Platform.

## System Overview

HealthGig is a healthcare gig platform that connects healthcare professionals with healthcare facilities for temporary staffing needs. The application consists of three main components:

1. **Backend**: A modular monolith built with Spring Boot
2. **Frontend**: A Next.js application with TypeScript and Tailwind CSS
3. **CMS**: A Sanity Studio instance for managing static content

## Architecture Diagram

```
┌────────────────────┐     ┌────────────────────┐     ┌────────────────────┐
│                    │     │                    │     │                    │
│   Next.js Frontend │────▶│   Spring Boot API  │◀────│   Sanity CMS       │
│                    │     │                    │     │                    │
└────────────────────┘     └──────────┬─────────┘     └────────────────────┘
                                      │
                                      ▼
                           ┌────────────────────┐     ┌────────────────────┐
                           │                    │     │                    │
                           │   PostgreSQL DB    │     │   Stripe API       │
                           │                    │     │                    │
                           └────────────────────┘     └────────────────────┘
```

## Backend Architecture

The backend follows a modular monolith architecture, which provides a good balance between the simplicity of a monolith and the modularity of microservices.

### Domain Modules

1. **Auth**: Authentication and authorization
   - JWT-based authentication
   - Role-based access control (Worker, Employer, Admin)

2. **Users**: User management
   - User entities (Worker, Employer, Admin)
   - Profile management

3. **Jobs**: Job posting and discovery
   - CRUD operations for job listings
   - Search and filtering

4. **Bookings**: Shift booking management
   - Booking workflow (confirm, check-in, check-out, complete)
   - Scheduling

5. **Payments**: Payment processing
   - Stripe integration
   - Payment tracking

6. **Reviews**: Rating and feedback system
   - Worker and employer reviews
   - Rating calculations

7. **Notifications**: In-app notification system
   - System notifications
   - Real-time alerts

8. **Admin**: Administration dashboard
   - System monitoring
   - User management

### Module Coupling

Modules communicate via internal interfaces, but they are packaged together in a single deployable unit. This approach:

- Simplifies deployment and testing
- Reduces operational complexity compared to microservices
- Allows for future decomposition if needed
- Maintains clear domain boundaries

### Database Design

The application uses PostgreSQL with the following key tables:

- users
- worker_profiles
- employer_profiles
- jobs
- bookings
- payments
- reviews
- notifications

Each module has its own set of tables with clear relationships between them.

## Frontend Architecture

The frontend is built with Next.js, utilizing modern React patterns and TypeScript for type safety.

### Key Features

- **Server-side rendering** for improved SEO and performance
- **Static site generation** for content pages
- **TypeScript** for type safety and improved developer experience
- **Tailwind CSS** for responsive design and styling
- **React Context API** for state management
- **Protected routes** based on user roles

### Page Structure

- Public pages (home, login, register, about, etc.)
- Worker dashboard and related pages
- Employer dashboard and related pages
- Admin dashboard and related pages
- Content pages (blog, FAQ, etc.) from Sanity CMS

### Authentication Flow

1. User logs in via the frontend
2. JWT token is received from the backend
3. Token is stored in localStorage (or cookies for production)
4. Protected routes check for valid token and role
5. Token refresh mechanism for extended sessions

## CMS Integration

The platform uses Sanity as a headless CMS for managing content.

### Content Types

- Blog posts
- Static pages (About, Terms, Privacy Policy, etc.)
- FAQs
- Testimonials
- Site settings

### Integration Points

- Next.js frontend fetches content from Sanity using GROQ queries
- Content is rendered using Portable Text
- Images use Sanity's image pipeline for optimization

## Development Workflow

### Local Development

1. Docker Compose for local environment setup
2. Spring Boot backend with dev profile
3. Next.js frontend in development mode
4. Sanity Studio for content management

### CI/CD Pipeline

1. GitHub Actions for CI/CD
2. Separate workflows for backend, frontend, and CMS
3. Test, build, and deploy stages
4. Docker containers for deployment

## Security Considerations

- JWT-based authentication with proper expiration
- Role-based access control
- Request validation and sanitization
- HTTPS for all traffic
- Secure storage of sensitive information using environment variables
- CSRF protection
- Content Security Policy (CSP)

## Scalability Strategy

The platform is designed to scale horizontally:

- Stateless backend can be deployed behind a load balancer
- Database can be scaled with read replicas
- Frontend can be deployed to a CDN
- Modular monolith can be decomposed into microservices if needed

## Monitoring and Logging

- Actuator endpoints for health and metrics
- Structured logging with correlation IDs
- Performance monitoring
- Error tracking

## Future Enhancements

- Real-time chat between workers and employers
- Advanced matching algorithm for jobs
- Mobile app using React Native
- Geolocation-based check-in/out verification
- AI-powered job recommendations