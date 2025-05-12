"use client";

import { useAuth } from '@/contexts/AuthContext';
import { useRouter, usePathname } from 'next/navigation';
import { useEffect } from 'react';

type ProtectedRouteProps = {
  children: React.ReactNode;
  allowedRoles?: string[];
};

export default function ProtectedRoute({ 
  children, 
  allowedRoles = []
}: ProtectedRouteProps) {
  const { user, isLoading, isAuthenticated } = useAuth();
  const router = useRouter();
  const pathname = usePathname();

  useEffect(() => {
    if (!isLoading && !isAuthenticated) {
      // Not authenticated, redirect to login
      router.push(`/auth/login?from=${encodeURIComponent(pathname)}`);
      return;
    }

    if (!isLoading && user && allowedRoles.length > 0) {
      // Check if user has the required role
      if (!allowedRoles.includes(user.role)) {
        // User doesn't have the required role, redirect based on their role
        if (user.role === 'WORKER') {
          router.push('/worker/dashboard');
        } else if (user.role === 'EMPLOYER') {
          router.push('/employer/dashboard');
        } else if (user.role === 'ADMIN') {
          router.push('/admin/dashboard');
        } else {
          router.push('/');
        }
      }
    }
  }, [isLoading, isAuthenticated, user, router, pathname, allowedRoles]);

  // Show loading while checking authentication
  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
      </div>
    );
  }

  // If authentication check is done and user is authenticated with correct role, render children
  if (!isLoading && isAuthenticated && (allowedRoles.length === 0 || allowedRoles.includes(user?.role || ''))) {
    return <>{children}</>;
  }

  // This will briefly show before redirect happens
  return null;
}