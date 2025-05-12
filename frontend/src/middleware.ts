import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';

// Define the routes that don't require authentication
const publicRoutes = ['/', '/auth/login', '/auth/register', '/terms', '/privacy', '/about'];

// Define the role-specific routes
const workerRoutes = ['/worker'];
const employerRoutes = ['/employer'];
const adminRoutes = ['/admin'];

export function middleware(request: NextRequest) {
  const { pathname } = request.nextUrl;
  
  // Check if path is a public route
  if (publicRoutes.some(route => pathname === route || pathname.startsWith(`${route}/`))) {
    return NextResponse.next();
  }
  
  // Get the token from the cookies
  const token = request.cookies.get('token')?.value;
  const userRole = request.cookies.get('userRole')?.value;
  
  // If no token, redirect to login
  if (!token) {
    const url = new URL('/auth/login', request.url);
    url.searchParams.set('from', pathname);
    return NextResponse.redirect(url);
  }
  
  // Check role-based access
  if (userRole) {
    // Worker can only access worker routes
    if (userRole === 'WORKER' && employerRoutes.some(route => pathname.startsWith(route)) || 
        userRole === 'WORKER' && adminRoutes.some(route => pathname.startsWith(route))) {
      return NextResponse.redirect(new URL('/worker/dashboard', request.url));
    }
    
    // Employer can only access employer routes
    if (userRole === 'EMPLOYER' && workerRoutes.some(route => pathname.startsWith(route)) || 
        userRole === 'EMPLOYER' && adminRoutes.some(route => pathname.startsWith(route))) {
      return NextResponse.redirect(new URL('/employer/dashboard', request.url));
    }
    
    // Admin can access all routes, no restrictions needed
  }
  
  return NextResponse.next();
}

// Configure the middleware to run only on specific routes
export const config = {
  matcher: [
    /*
     * Match all request paths except for:
     * - _next/static (static files)
     * - _next/image (image optimization files)
     * - favicon.ico (favicon file)
     * - public folder
     */
    '/((?!_next/static|_next/image|favicon.ico|images|.*\.png$).*)',
  ],
};