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

interface JobSummary {
  open: number;
  filled: number;
  completed: number;
}

export default function EmployerDashboard() {
  const { user } = useAuth();
  const [bookings, setBookings] = useState<BookingSummary>({
    upcoming: 0,
    completed: 0,
    cancelled: 0,
  });
  const [jobs, setJobs] = useState<JobSummary>({
    open: 0,
    filled: 0,
    completed: 0,
  });
  const [upcomingBookings, setUpcomingBookings] = useState([]);
  const [openJobs, setOpenJobs] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        // In a real application, these would be actual API calls
        // const bookingsResponse = await apiClient.get('/bookings/my-employer-bookings/summary');
        // const jobsResponse = await apiClient.get('/jobs/my-jobs/summary');
        // const upcomingBookingsResponse = await apiClient.get('/bookings/my-employer-bookings/upcoming');
        // const openJobsResponse = await apiClient.get('/jobs/my-jobs/open');
        
        // Simulate API responses for demo
        setBookings({
          upcoming: 5,
          completed: 18,
          cancelled: 2,
        });
        
        setJobs({
          open: 3,
          filled: 2,
          completed: 10,
        });
        
        setUpcomingBookings([
          {
            id: 1,
            jobTitle: 'ICU Registered Nurse',
            workerName: 'Mary Johnson',
            date: '2025-05-15',
            startTime: '08:00',
            endTime: '16:00',
            status: 'CONFIRMED',
          },
          {
            id: 2,
            jobTitle: 'ER Nurse Practitioner',
            workerName: 'Robert Chen',
            date: '2025-05-16',
            startTime: '19:00',
            endTime: '07:00',
            status: 'CONFIRMED',
          },
          {
            id: 3,
            jobTitle: 'ICU Registered Nurse',
            workerName: 'Sarah Adams',
            date: '2025-05-17',
            startTime: '08:00',
            endTime: '16:00',
            status: 'CONFIRMED',
          },
        ]);

        setOpenJobs([
          {
            id: 1,
            title: 'ICU Registered Nurse',
            startDateTime: '2025-05-20T08:00:00',
            endDateTime: '2025-05-20T16:00:00',
            hourlyRate: 50.00,
            bookings: 0,
            maxApplicants: 2,
          },
          {
            id: 2,
            title: 'ER Physician',
            startDateTime: '2025-05-22T07:00:00',
            endDateTime: '2025-05-22T19:00:00',
            hourlyRate: 90.00,
            bookings: 0,
            maxApplicants: 1,
          },
          {
            id: 3,
            title: 'Physical Therapist',
            startDateTime: '2025-05-24T09:00:00',
            endDateTime: '2025-05-24T17:00:00',
            hourlyRate: 45.00,
            bookings: 0,
            maxApplicants: 1,
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
    <ProtectedRoute allowedRoles={['EMPLOYER']}>
      <div className="min-h-screen bg-gray-50">
        <Navbar />
        
        <main className="container mx-auto px-4 py-6">
          <header className="mb-8">
            <h1 className="text-3xl font-bold text-gray-900">Employer Dashboard</h1>
            <p className="text-gray-600 mt-2">Manage your healthcare job postings and bookings.</p>
          </header>
          
          {isLoading ? (
            <div className="flex justify-center py-12">
              <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
            </div>
          ) : (
            <div className="grid md:grid-cols-3 gap-6 mb-8">
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
                  <Link href="/employer/bookings" className="text-sm text-blue-600 hover:text-blue-800">
                    View all bookings
                  </Link>
                </div>
              </div>
              
              <div className="bg-white rounded-lg shadow p-6">
                <h2 className="text-lg font-semibold mb-4">Jobs</h2>
                <div className="flex justify-between items-center">
                  <div className="text-center">
                    <p className="text-2xl font-bold text-blue-600">{jobs.open}</p>
                    <p className="text-sm text-gray-500">Open</p>
                  </div>
                  <div className="text-center">
                    <p className="text-2xl font-bold text-orange-600">{jobs.filled}</p>
                    <p className="text-sm text-gray-500">Filled</p>
                  </div>
                  <div className="text-center">
                    <p className="text-2xl font-bold text-green-600">{jobs.completed}</p>
                    <p className="text-sm text-gray-500">Completed</p>
                  </div>
                </div>
                <div className="mt-4 text-center">
                  <Link href="/employer/jobs" className="text-sm text-blue-600 hover:text-blue-800">
                    View all jobs
                  </Link>
                </div>
              </div>
              
              <div className="bg-white rounded-lg shadow p-6">
                <h2 className="text-lg font-semibold mb-4">Quick Actions</h2>
                <div className="space-y-3">
                  <Link href="/employer/jobs/create" className="block w-full py-2 px-4 text-center border border-transparent rounded-md shadow-sm text-white bg-blue-600 hover:bg-blue-700">
                    Post New Job
                  </Link>
                  <Link href="/employer/bookings/pending" className="block w-full py-2 px-4 text-center border border-gray-300 rounded-md shadow-sm text-gray-700 bg-white hover:bg-gray-50">
                    Review Pending Bookings
                  </Link>
                  <Link href="/employer/profile" className="block w-full py-2 px-4 text-center border border-gray-300 rounded-md shadow-sm text-gray-700 bg-white hover:bg-gray-50">
                    Update Profile
                  </Link>
                </div>
              </div>
            </div>
          )}
          
          {/* Upcoming Bookings */}
          <div className="mb-8">
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-xl font-bold">Upcoming Bookings</h2>
              <Link href="/employer/bookings" className="text-sm text-blue-600 hover:text-blue-800">
                View all
              </Link>
            </div>
            
            {upcomingBookings.length > 0 ? (
              <div className="bg-white rounded-lg shadow overflow-hidden">
                <table className="min-w-full divide-y divide-gray-200">
                  <thead className="bg-gray-50">
                    <tr>
                      <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Job Title
                      </th>
                      <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Healthcare Professional
                      </th>
                      <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Date
                      </th>
                      <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Time
                      </th>
                      <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Status
                      </th>
                      <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Actions
                      </th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-200">
                    {upcomingBookings.map((booking: any) => (
                      <tr key={booking.id}>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="text-sm font-medium text-gray-900">{booking.jobTitle}</div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="text-sm text-gray-900">{booking.workerName}</div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="text-sm text-gray-900">{booking.date}</div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="text-sm text-gray-900">{booking.startTime} - {booking.endTime}</div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <span className="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800">
                            {booking.status}
                          </span>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                          <Link href={`/employer/bookings/${booking.id}`} className="text-blue-600 hover:text-blue-900 mr-4">
                            View
                          </Link>
                          <Link href={`/employer/bookings/${booking.id}/details`} className="text-blue-600 hover:text-blue-900">
                            Message
                          </Link>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            ) : (
              <div className="bg-white rounded-lg shadow p-6 text-center">
                <p className="text-gray-600">You have no upcoming bookings.</p>
              </div>
            )}
          </div>
          
          {/* Open Jobs */}
          <div className="mb-8">
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-xl font-bold">Open Jobs</h2>
              <Link href="/employer/jobs/create" className="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700">
                Post New Job
              </Link>
            </div>
            
            {openJobs.length > 0 ? (
              <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
                {openJobs.map((job: any) => {
                  const startDate = new Date(job.startDateTime);
                  const endDate = new Date(job.endDateTime);
                  const formattedDate = startDate.toLocaleDateString('en-US', {
                    month: 'short',
                    day: 'numeric',
                    year: 'numeric'
                  });
                  const formattedStartTime = startDate.toLocaleTimeString('en-US', {
                    hour: '2-digit',
                    minute: '2-digit'
                  });
                  const formattedEndTime = endDate.toLocaleTimeString('en-US', {
                    hour: '2-digit',
                    minute: '2-digit'
                  });

                  return (
                    <div key={job.id} className="bg-white rounded-lg shadow overflow-hidden">
                      <div className="p-5">
                        <h3 className="text-lg font-semibold mb-2">{job.title}</h3>
                        <div className="mb-4 space-y-2">
                          <div className="flex items-center text-sm">
                            <svg className="h-4 w-4 text-gray-500 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                            </svg>
                            <span>{formattedDate}</span>
                          </div>
                          <div className="flex items-center text-sm">
                            <svg className="h-4 w-4 text-gray-500 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                            </svg>
                            <span>{formattedStartTime} - {formattedEndTime}</span>
                          </div>
                          <div className="flex items-center text-sm">
                            <svg className="h-4 w-4 text-gray-500 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 9V7a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2m2 4h10a2 2 0 002-2v-6a2 2 0 00-2-2H9a2 2 0 00-2 2v6a2 2 0 002 2zm7-5a2 2 0 11-4 0 2 2 0 014 0z" />
                            </svg>
                            <span>${job.hourlyRate.toFixed(2)}/hr</span>
                          </div>
                          <div className="flex items-center text-sm">
                            <svg className="h-4 w-4 text-gray-500 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
                            </svg>
                            <span>{job.bookings} / {job.maxApplicants} booked</span>
                          </div>
                        </div>
                        <div className="flex space-x-2">
                          <Link href={`/employer/jobs/${job.id}`} className="flex-1 inline-flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700">
                            View Details
                          </Link>
                          <Link href={`/employer/jobs/${job.id}/edit`} className="flex-1 inline-flex justify-center py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50">
                            Edit
                          </Link>
                        </div>
                      </div>
                    </div>
                  );
                })}
              </div>
            ) : (
              <div className="bg-white rounded-lg shadow p-6 text-center">
                <p className="text-gray-600">You have no open jobs.</p>
                <Link href="/employer/jobs/create" className="mt-4 inline-block px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700">
                  Post Your First Job
                </Link>
              </div>
            )}
          </div>
          
          {/* Analytics Summary */}
          <div className="bg-white rounded-lg shadow p-6">
            <h2 className="text-xl font-bold mb-4">Analytics Summary</h2>
            <div className="grid md:grid-cols-3 gap-4">
              <div className="border rounded-lg p-4">
                <h3 className="text-lg font-medium text-gray-900 mb-2">Fill Rate</h3>
                <p className="text-3xl font-bold text-blue-600">87%</p>
                <p className="text-sm text-gray-500 mt-1">Jobs filled successfully</p>
              </div>
              <div className="border rounded-lg p-4">
                <h3 className="text-lg font-medium text-gray-900 mb-2">Average Response Time</h3>
                <p className="text-3xl font-bold text-blue-600">3.5 hrs</p>
                <p className="text-sm text-gray-500 mt-1">To fill open positions</p>
              </div>
              <div className="border rounded-lg p-4">
                <h3 className="text-lg font-medium text-gray-900 mb-2">Worker Reliability</h3>
                <p className="text-3xl font-bold text-blue-600">95%</p>
                <p className="text-sm text-gray-500 mt-1">On-time check-in rate</p>
              </div>
            </div>
            <div className="mt-4 text-center">
              <Link href="/employer/analytics" className="text-sm text-blue-600 hover:text-blue-800">
                View detailed analytics
              </Link>
            </div>
          </div>
        </main>
      </div>
    </ProtectedRoute>
  );
}