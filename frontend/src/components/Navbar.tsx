"use client";

import Link from 'next/link';
import { useAuth } from '@/contexts/AuthContext';
import { useState } from 'react';

export default function Navbar() {
  const { user, logout, isAuthenticated } = useAuth();
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  const toggleMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };

  return (
    <nav className="bg-white shadow-sm">
      <div className="container mx-auto px-4">
        <div className="flex justify-between h-16">
          <div className="flex">
            <div className="flex-shrink-0 flex items-center">
              <Link href="/" className="text-2xl font-bold text-blue-600">
                HealthGig
              </Link>
            </div>
            <div className="hidden sm:ml-6 sm:flex sm:items-center sm:space-x-4">
              {isAuthenticated && user?.role === 'WORKER' && (
                <>
                  <Link href="/worker/dashboard" className="px-3 py-2 text-gray-700 hover:text-blue-600">
                    Dashboard
                  </Link>
                  <Link href="/worker/jobs" className="px-3 py-2 text-gray-700 hover:text-blue-600">
                    Find Jobs
                  </Link>
                  <Link href="/worker/bookings" className="px-3 py-2 text-gray-700 hover:text-blue-600">
                    My Bookings
                  </Link>
                </>
              )}
              {isAuthenticated && user?.role === 'EMPLOYER' && (
                <>
                  <Link href="/employer/dashboard" className="px-3 py-2 text-gray-700 hover:text-blue-600">
                    Dashboard
                  </Link>
                  <Link href="/employer/jobs" className="px-3 py-2 text-gray-700 hover:text-blue-600">
                    Manage Jobs
                  </Link>
                  <Link href="/employer/bookings" className="px-3 py-2 text-gray-700 hover:text-blue-600">
                    Bookings
                  </Link>
                </>
              )}
              {isAuthenticated && user?.role === 'ADMIN' && (
                <>
                  <Link href="/admin/dashboard" className="px-3 py-2 text-gray-700 hover:text-blue-600">
                    Dashboard
                  </Link>
                  <Link href="/admin/users" className="px-3 py-2 text-gray-700 hover:text-blue-600">
                    Users
                  </Link>
                  <Link href="/admin/jobs" className="px-3 py-2 text-gray-700 hover:text-blue-600">
                    Jobs
                  </Link>
                </>
              )}
            </div>
          </div>
          <div className="hidden sm:ml-6 sm:flex sm:items-center">
            {isAuthenticated ? (
              <div className="flex items-center space-x-4">
                <Link href={`/${user?.role.toLowerCase()}/profile`} className="px-3 py-2 text-gray-700 hover:text-blue-600">
                  My Profile
                </Link>
                <button
                  onClick={logout}
                  className="px-3 py-2 text-gray-700 hover:text-blue-600"
                >
                  Logout
                </button>
              </div>
            ) : (
              <div className="flex items-center space-x-4">
                <Link href="/auth/login" className="btn-secondary">
                  Login
                </Link>
                <Link href="/auth/register" className="btn-primary">
                  Register
                </Link>
              </div>
            )}
          </div>
          <div className="-mr-2 flex items-center sm:hidden">
            <button
              onClick={toggleMenu}
              className="inline-flex items-center justify-center p-2 rounded-md text-gray-400 hover:text-gray-500 hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-blue-500"
              aria-expanded="false"
            >
              <span className="sr-only">Open main menu</span>
              <svg
                className={`${isMenuOpen ? 'hidden' : 'block'} h-6 w-6`}
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
                aria-hidden="true"
              >
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 6h16M4 12h16M4 18h16" />
              </svg>
              <svg
                className={`${isMenuOpen ? 'block' : 'hidden'} h-6 w-6`}
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
                aria-hidden="true"
              >
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
        </div>
      </div>

      {/* Mobile menu */}
      <div className={`${isMenuOpen ? 'block' : 'hidden'} sm:hidden`}>
        <div className="pt-2 pb-3 space-y-1">
          {isAuthenticated && user?.role === 'WORKER' && (
            <>
              <Link href="/worker/dashboard" className="block px-3 py-2 text-gray-700 hover:text-blue-600 hover:bg-gray-50">
                Dashboard
              </Link>
              <Link href="/worker/jobs" className="block px-3 py-2 text-gray-700 hover:text-blue-600 hover:bg-gray-50">
                Find Jobs
              </Link>
              <Link href="/worker/bookings" className="block px-3 py-2 text-gray-700 hover:text-blue-600 hover:bg-gray-50">
                My Bookings
              </Link>
            </>
          )}
          {isAuthenticated && user?.role === 'EMPLOYER' && (
            <>
              <Link href="/employer/dashboard" className="block px-3 py-2 text-gray-700 hover:text-blue-600 hover:bg-gray-50">
                Dashboard
              </Link>
              <Link href="/employer/jobs" className="block px-3 py-2 text-gray-700 hover:text-blue-600 hover:bg-gray-50">
                Manage Jobs
              </Link>
              <Link href="/employer/bookings" className="block px-3 py-2 text-gray-700 hover:text-blue-600 hover:bg-gray-50">
                Bookings
              </Link>
            </>
          )}
          {isAuthenticated && user?.role === 'ADMIN' && (
            <>
              <Link href="/admin/dashboard" className="block px-3 py-2 text-gray-700 hover:text-blue-600 hover:bg-gray-50">
                Dashboard
              </Link>
              <Link href="/admin/users" className="block px-3 py-2 text-gray-700 hover:text-blue-600 hover:bg-gray-50">
                Users
              </Link>
              <Link href="/admin/jobs" className="block px-3 py-2 text-gray-700 hover:text-blue-600 hover:bg-gray-50">
                Jobs
              </Link>
            </>
          )}
        </div>
        <div className="pt-4 pb-3 border-t border-gray-200">
          {isAuthenticated ? (
            <div className="space-y-1">
              <Link href={`/${user?.role.toLowerCase()}/profile`} className="block px-3 py-2 text-gray-700 hover:text-blue-600 hover:bg-gray-50">
                My Profile
              </Link>
              <button
                onClick={logout}
                className="block w-full text-left px-3 py-2 text-gray-700 hover:text-blue-600 hover:bg-gray-50"
              >
                Logout
              </button>
            </div>
          ) : (
            <div className="space-y-1">
              <Link href="/auth/login" className="block px-3 py-2 text-gray-700 hover:text-blue-600 hover:bg-gray-50">
                Login
              </Link>
              <Link href="/auth/register" className="block px-3 py-2 text-gray-700 hover:text-blue-600 hover:bg-gray-50">
                Register
              </Link>
            </div>
          )}
        </div>
      </div>
    </nav>
  );
}