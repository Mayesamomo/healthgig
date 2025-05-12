"use client";

import { useEffect, useState } from 'react';
import { useAuth } from '@/contexts/AuthContext';
import apiClient from '@/lib/api';
import ProtectedRoute from '@/components/ProtectedRoute';
import Navbar from '@/components/Navbar';
import Link from 'next/link';

interface BookingSummary {
  upcoming: number;
  completed: number;
  cancelled: number;
}

interface EarningsSummary {
  current: number;
  pending: number;
  total: number;
}

export default function WorkerDashboard() {
  const { user } = useAuth();
  const [bookings, setBookings] = useState<BookingSummary>({
    upcoming: 0,
    completed: 0,
    cancelled: 0,
  });
  const [earnings, setEarnings] = useState<EarningsSummary>({
    current: 0,
    pending: 0,
    total: 0,
  });
  const [upcomingShifts, setUpcomingShifts] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        // In a real application, these would be actual API calls
        // const bookingsResponse = await apiClient.get('/worker/bookings/summary');
        // const earningsResponse = await apiClient.get('/worker/earnings/summary');
        // const upcomingShiftsResponse = await apiClient.get('/worker/bookings/upcoming');
        
        // Simulate API responses for demo
        setBookings({
          upcoming: 3,
          completed: 12,
          cancelled: 1,
        });
        
        setEarnings({
          current: 750.00,
          pending: 250.00,
          total: 3250.00,
        });
        
        setUpcomingShifts([
          {
            id: 1,
            jobTitle: 'Registered Nurse',
            facility: 'Memorial Hospital',
            date: '2025-05-15',
            startTime: '08:00',
            endTime: '16:00',
            hourlyRate: 45.00,
          },
          {
            id: 2,
            jobTitle: 'Licensed Practical Nurse',
            facility: 'Sunrise Care Center',
            date: '2025-05-18',
            startTime: '07:00',
            endTime: '15:00',
            hourlyRate: 35.00,
          },
          {
            id: 3,
            jobTitle: 'Nursing Assistant',
            facility: 'Golden Age Retirement Home',
            date: '2025-05-20',
            startTime: '16:00',
            endTime: '00:00',
            hourlyRate: 30.00,
          },
        ]);
      } catch (error) {
        console.error('Error fetching dashboard data:', error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchDashboardData();
  }, []);

  return (
    <ProtectedRoute allowedRoles={['WORKER']}>
      <div className="min-h-screen bg-gray-50">
        <Navbar />
        
        <main className="container mx-auto px-4 py-6">
          <header className="mb-8">
            <h1 className="text-3xl font-bold text-gray-900">Welcome back{user ? `, ${user.firstName}` : ''}!</h1>
            <p className="text-gray-600 mt-2">Here's an overview of your activity and upcoming shifts.</p>
          </header>
          
          {isLoading ? (
            <div className="flex justify-center py-12">
              <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
            </div>
          ) : (
            <div className="grid md:grid-cols-3 gap-6">
              {/* Summary Cards */}
              <div className="bg-white rounded-lg shadow p-6">
                <h2 className="text-lg font-semibold mb-4">Bookings</h2>
                <div className="flex justify-between items-center">
                  <div className="text-center">
                    <p className="text-2xl font-bold text-blue-600">{bookings.upcoming}</p>
                    <p className="text-sm text-gray-500">Upcoming</p>
                  </div>
                  <div className="text-center">
                    <p className="text-2xl font-bold text-green-600">{bookings.completed}</p>
                    <p className="text-sm text-gray-500">Completed</p>
                  </div>
                  <div className="text-center">
                    <p className="text-2xl font-bold text-red-600">{bookings.cancelled}</p>
                    <p className="text-sm text-gray-500">Cancelled</p>
                  </div>
                </div>
                <div className="mt-4 text-center">
                  <Link href="/worker/bookings" className="text-sm text-blue-600 hover:text-blue-800">
                    View all bookings
                  </Link>
                </div>
              </div>
              
              <div className="bg-white rounded-lg shadow p-6">
                <h2 className="text-lg font-semibold mb-4">Earnings</h2>
                <div className="space-y-2">
                  <div className="flex justify-between">
                    <p className="text-gray-600">Current Balance:</p>
                    <p className="font-medium">${earnings.current.toFixed(2)}</p>
                  </div>
                  <div className="flex justify-between">
                    <p className="text-gray-600">Pending:</p>
                    <p className="font-medium">${earnings.pending.toFixed(2)}</p>
                  </div>
                  <div className="flex justify-between pt-2 border-t">
                    <p className="text-gray-600">Total Earnings:</p>
                    <p className="font-bold">${earnings.total.toFixed(2)}</p>
                  </div>
                </div>
                <div className="mt-4 text-center">
                  <button className="text-sm text-blue-600 hover:text-blue-800">
                    Request Payout
                  </button>
                </div>
              </div>
              
              <div className="bg-white rounded-lg shadow p-6">
                <h2 className="text-lg font-semibold mb-4">Profile Completion</h2>
                <div className="relative pt-1">
                  <div className="flex mb-2 items-center justify-between">
                    <div>
                      <span className="text-xs font-semibold inline-block py-1 px-2 uppercase rounded-full text-blue-600 bg-blue-200">
                        85% Complete
                      </span>
                    </div>
                  </div>
                  <div className="overflow-hidden h-2 mb-4 text-xs flex rounded bg-blue-200">
                    <div style={{ width: '85%' }} className="shadow-none flex flex-col text-center whitespace-nowrap text-white justify-center bg-blue-500"></div>
                  </div>
                </div>
                <p className="text-sm text-gray-600 mb-3">Complete your profile to increase your chances of getting hired.</p>
                <div className="mt-2 text-center">
                  <Link href="/worker/profile" className="text-sm text-blue-600 hover:text-blue-800">
                    Complete profile
                  </Link>
                </div>
              </div>
            </div>
          )}
          
          {/* Upcoming Shifts */}
          <div className="mt-8">
            <h2 className="text-xl font-bold mb-4">Upcoming Shifts</h2>
            
            {upcomingShifts.length > 0 ? (
              <div className="bg-white rounded-lg shadow overflow-hidden">
                <table className="min-w-full divide-y divide-gray-200">
                  <thead className="bg-gray-50">
                    <tr>
                      <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Job
                      </th>
                      <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Facility
                      </th>
                      <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Date
                      </th>
                      <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Time
                      </th>
                      <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Rate
                      </th>
                      <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Actions
                      </th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-200">
                    {upcomingShifts.map((shift: any) => (
                      <tr key={shift.id}>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="text-sm font-medium text-gray-900">{shift.jobTitle}</div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="text-sm text-gray-900">{shift.facility}</div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="text-sm text-gray-900">{shift.date}</div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="text-sm text-gray-900">{shift.startTime} - {shift.endTime}</div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="text-sm text-gray-900">${shift.hourlyRate.toFixed(2)}/hr</div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                          <Link href={`/worker/bookings/${shift.id}`} className="text-blue-600 hover:text-blue-900 mr-4">
                            View Details
                          </Link>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            ) : (
              <div className="bg-white rounded-lg shadow p-6 text-center">
                <p className="text-gray-600">You have no upcoming shifts.</p>
                <Link href="/worker/jobs" className="mt-4 inline-block text-blue-600 hover:text-blue-800">
                  Find Jobs
                </Link>
              </div>
            )}
          </div>
          
          {/* Available Jobs */}
          <div className="mt-8">
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-xl font-bold">Recommended Jobs</h2>
              <Link href="/worker/jobs" className="text-sm text-blue-600 hover:text-blue-800">
                View all jobs
              </Link>
            </div>
            
            <div className="grid md:grid-cols-3 gap-6">
              <div className="bg-white rounded-lg shadow p-6">
                <div className="flex justify-between items-start">
                  <div>
                    <h3 className="text-lg font-semibold mb-1">ICU Registered Nurse</h3>
                    <p className="text-gray-600 text-sm mb-2">City General Hospital</p>
                  </div>
                  <span className="bg-green-100 text-green-800 text-xs font-semibold px-2.5 py-0.5 rounded">New</span>
                </div>
                <div className="mt-4 space-y-2">
                  <div className="flex items-center text-sm">
                    <svg className="h-4 w-4 text-gray-500 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                    </svg>
                    <span>May 25, 2025</span>
                  </div>
                  <div className="flex items-center text-sm">
                    <svg className="h-4 w-4 text-gray-500 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                    <span>12:00 PM - 8:00 PM</span>
                  </div>
                  <div className="flex items-center text-sm">
                    <svg className="h-4 w-4 text-gray-500 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                    </svg>
                    <span>Downtown, Metro City</span>
                  </div>
                  <div className="flex items-center text-sm font-semibold text-blue-600">
                    <svg className="h-4 w-4 text-blue-600 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                    <span>$50.00/hr</span>
                  </div>
                </div>
                <div className="mt-6">
                  <Link href="/worker/jobs/1" className="w-full inline-flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                    View Details
                  </Link>
                </div>
              </div>
              
              <div className="bg-white rounded-lg shadow p-6">
                <div className="flex justify-between items-start">
                  <div>
                    <h3 className="text-lg font-semibold mb-1">ER Nurse Practitioner</h3>
                    <p className="text-gray-600 text-sm mb-2">Mercy Medical Center</p>
                  </div>
                </div>
                <div className="mt-4 space-y-2">
                  <div className="flex items-center text-sm">
                    <svg className="h-4 w-4 text-gray-500 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                    </svg>
                    <span>May 26-27, 2025</span>
                  </div>
                  <div className="flex items-center text-sm">
                    <svg className="h-4 w-4 text-gray-500 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                    <span>7:00 PM - 7:00 AM</span>
                  </div>
                  <div className="flex items-center text-sm">
                    <svg className="h-4 w-4 text-gray-500 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                    </svg>
                    <span>North District, Metro City</span>
                  </div>
                  <div className="flex items-center text-sm font-semibold text-blue-600">
                    <svg className="h-4 w-4 text-blue-600 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                    <span>$55.00/hr</span>
                  </div>
                </div>
                <div className="mt-6">
                  <Link href="/worker/jobs/2" className="w-full inline-flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                    View Details
                  </Link>
                </div>
              </div>
              
              <div className="bg-white rounded-lg shadow p-6">
                <div className="flex justify-between items-start">
                  <div>
                    <h3 className="text-lg font-semibold mb-1">Home Health Nurse</h3>
                    <p className="text-gray-600 text-sm mb-2">Caring Hearts Home Health</p>
                  </div>
                </div>
                <div className="mt-4 space-y-2">
                  <div className="flex items-center text-sm">
                    <svg className="h-4 w-4 text-gray-500 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                    </svg>
                    <span>May 28, 2025</span>
                  </div>
                  <div className="flex items-center text-sm">
                    <svg className="h-4 w-4 text-gray-500 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                    <span>9:00 AM - 3:00 PM</span>
                  </div>
                  <div className="flex items-center text-sm">
                    <svg className="h-4 w-4 text-gray-500 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                    </svg>
                    <span>East Village, Metro City</span>
                  </div>
                  <div className="flex items-center text-sm font-semibold text-blue-600">
                    <svg className="h-4 w-4 text-blue-600 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                    <span>$40.00/hr</span>
                  </div>
                </div>
                <div className="mt-6">
                  <Link href="/worker/jobs/3" className="w-full inline-flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                    View Details
                  </Link>
                </div>
              </div>
            </div>
          </div>
        </main>
      </div>
    </ProtectedRoute>
  );
}