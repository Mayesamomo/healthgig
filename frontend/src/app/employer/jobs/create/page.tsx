"use client";

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { useAuth } from '@/contexts/AuthContext';
import ProtectedRoute from '@/components/ProtectedRoute';
import Navbar from '@/components/Navbar';
import apiClient from '@/lib/api';

export default function CreateJob() {
  const router = useRouter();
  const { user } = useAuth();
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');
  
  // Form state
  const [formData, setFormData] = useState({
    title: '',
    jobType: 'NURSE_RN',
    description: '',
    requiredExperienceYears: 1,
    startDateTime: '',
    endDateTime: '',
    hourlyRate: 40,
    location: '',
    address: '',
    city: '',
    state: '',
    zipCode: '',
    remotePossible: false,
    applicationDeadline: '',
    maxApplicants: 1,
    requiredSkills: '',
  });

  const jobTypes = [
    { value: 'NURSE_RN', label: 'Registered Nurse (RN)' },
    { value: 'NURSE_LPN', label: 'Licensed Practical Nurse (LPN)' },
    { value: 'NURSE_AIDE', label: 'Nursing Aide' },
    { value: 'PHYSICIAN', label: 'Physician' },
    { value: 'PHYSICIAN_ASSISTANT', label: 'Physician Assistant' },
    { value: 'DENTIST', label: 'Dentist' },
    { value: 'DENTAL_HYGIENIST', label: 'Dental Hygienist' },
    { value: 'PHYSICAL_THERAPIST', label: 'Physical Therapist' },
    { value: 'OCCUPATIONAL_THERAPIST', label: 'Occupational Therapist' },
    { value: 'RESPIRATORY_THERAPIST', label: 'Respiratory Therapist' },
    { value: 'PHARMACIST', label: 'Pharmacist' },
    { value: 'PHARMACY_TECHNICIAN', label: 'Pharmacy Technician' },
    { value: 'MEDICAL_ASSISTANT', label: 'Medical Assistant' },
    { value: 'MEDICAL_RECEPTIONIST', label: 'Medical Receptionist' },
    { value: 'OTHER', label: 'Other' },
  ];

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    const { name, value, type } = e.target;
    
    if (type === 'checkbox') {
      setFormData(prev => ({
        ...prev,
        [name]: (e.target as HTMLInputElement).checked
      }));
    } else {
      setFormData(prev => ({
        ...prev,
        [name]: value
      }));
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    setError('');

    try {
      // Transform form data for the API
      const jobData = {
        ...formData,
        requiredExperienceYears: parseInt(formData.requiredExperienceYears.toString()),
        hourlyRate: parseFloat(formData.hourlyRate.toString()),
        maxApplicants: parseInt(formData.maxApplicants.toString()),
        requiredSkills: formData.requiredSkills.split(',').map(skill => skill.trim()).filter(skill => skill),
      };

      // In a real app, we would submit to the API
      // const response = await apiClient.post('/jobs', jobData);
      // console.log('Job created:', response.data);
      
      // For now, we'll just simulate success
      console.log('Job data to submit:', jobData);
      
      // Navigate back to jobs list
      router.push('/employer/jobs');
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to create job');
      console.error('Error creating job:', err);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <ProtectedRoute allowedRoles={['EMPLOYER']}>
      <div className="min-h-screen bg-gray-50">
        <Navbar />
        
        <main className="container mx-auto px-4 py-6">
          <div className="max-w-3xl mx-auto">
            <header className="mb-6">
              <h1 className="text-3xl font-bold text-gray-900">Post a New Job</h1>
              <p className="text-gray-600 mt-2">Create a new job listing to find qualified healthcare professionals.</p>
            </header>
            
            {error && (
              <div className="bg-red-50 border-l-4 border-red-500 p-4 mb-6">
                <div className="flex">
                  <div className="flex-shrink-0">
                    <svg className="h-5 w-5 text-red-500" viewBox="0 0 20 20" fill="currentColor">
                      <path fillRule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clipRule="evenodd" />
                    </svg>
                  </div>
                  <div className="ml-3">
                    <p className="text-sm text-red-700">{error}</p>
                  </div>
                </div>
              </div>
            )}
            
            <form onSubmit={handleSubmit} className="bg-white rounded-lg shadow p-6">
              <div className="space-y-6">
                {/* Basic Job Information */}
                <div>
                  <h2 className="text-xl font-semibold mb-4 pb-2 border-b">Basic Job Information</h2>
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div className="col-span-2">
                      <label htmlFor="title" className="block text-sm font-medium text-gray-700 mb-1">Job Title *</label>
                      <input
                        type="text"
                        id="title"
                        name="title"
                        value={formData.title}
                        onChange={handleChange}
                        required
                        className="w-full p-2 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500"
                      />
                    </div>
                    
                    <div>
                      <label htmlFor="jobType" className="block text-sm font-medium text-gray-700 mb-1">Job Type *</label>
                      <select
                        id="jobType"
                        name="jobType"
                        value={formData.jobType}
                        onChange={handleChange}
                        required
                        className="w-full p-2 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500"
                      >
                        {jobTypes.map(type => (
                          <option key={type.value} value={type.value}>{type.label}</option>
                        ))}
                      </select>
                    </div>
                    
                    <div>
                      <label htmlFor="requiredExperienceYears" className="block text-sm font-medium text-gray-700 mb-1">
                        Required Experience (Years) *
                      </label>
                      <input
                        type="number"
                        id="requiredExperienceYears"
                        name="requiredExperienceYears"
                        value={formData.requiredExperienceYears}
                        onChange={handleChange}
                        min="0"
                        required
                        className="w-full p-2 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500"
                      />
                    </div>
                    
                    <div className="col-span-2">
                      <label htmlFor="requiredSkills" className="block text-sm font-medium text-gray-700 mb-1">
                        Required Skills (comma-separated)
                      </label>
                      <input
                        type="text"
                        id="requiredSkills"
                        name="requiredSkills"
                        value={formData.requiredSkills}
                        onChange={handleChange}
                        placeholder="e.g., Critical Care, Ventilator Management, EPIC EMR"
                        className="w-full p-2 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500"
                      />
                    </div>
                    
                    <div className="col-span-2">
                      <label htmlFor="description" className="block text-sm font-medium text-gray-700 mb-1">Job Description *</label>
                      <textarea
                        id="description"
                        name="description"
                        value={formData.description}
                        onChange={handleChange}
                        required
                        rows={5}
                        className="w-full p-2 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500"
                      />
                    </div>
                  </div>
                </div>
                
                {/* Schedule */}
                <div>
                  <h2 className="text-xl font-semibold mb-4 pb-2 border-b">Schedule</h2>
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                      <label htmlFor="startDateTime" className="block text-sm font-medium text-gray-700 mb-1">Start Date & Time *</label>
                      <input
                        type="datetime-local"
                        id="startDateTime"
                        name="startDateTime"
                        value={formData.startDateTime}
                        onChange={handleChange}
                        required
                        className="w-full p-2 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500"
                      />
                    </div>
                    
                    <div>
                      <label htmlFor="endDateTime" className="block text-sm font-medium text-gray-700 mb-1">End Date & Time *</label>
                      <input
                        type="datetime-local"
                        id="endDateTime"
                        name="endDateTime"
                        value={formData.endDateTime}
                        onChange={handleChange}
                        required
                        className="w-full p-2 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500"
                      />
                    </div>
                    
                    <div>
                      <label htmlFor="applicationDeadline" className="block text-sm font-medium text-gray-700 mb-1">Application Deadline</label>
                      <input
                        type="datetime-local"
                        id="applicationDeadline"
                        name="applicationDeadline"
                        value={formData.applicationDeadline}
                        onChange={handleChange}
                        className="w-full p-2 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500"
                      />
                    </div>
                    
                    <div>
                      <label htmlFor="maxApplicants" className="block text-sm font-medium text-gray-700 mb-1">Maximum Applicants *</label>
                      <input
                        type="number"
                        id="maxApplicants"
                        name="maxApplicants"
                        value={formData.maxApplicants}
                        onChange={handleChange}
                        min="1"
                        required
                        className="w-full p-2 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500"
                      />
                    </div>
                  </div>
                </div>
                
                {/* Location */}
                <div>
                  <h2 className="text-xl font-semibold mb-4 pb-2 border-b">Location</h2>
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div className="col-span-2">
                      <label htmlFor="location" className="block text-sm font-medium text-gray-700 mb-1">Facility Name *</label>
                      <input
                        type="text"
                        id="location"
                        name="location"
                        value={formData.location}
                        onChange={handleChange}
                        required
                        className="w-full p-2 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500"
                      />
                    </div>
                    
                    <div className="col-span-2">
                      <label htmlFor="address" className="block text-sm font-medium text-gray-700 mb-1">Street Address *</label>
                      <input
                        type="text"
                        id="address"
                        name="address"
                        value={formData.address}
                        onChange={handleChange}
                        required
                        className="w-full p-2 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500"
                      />
                    </div>
                    
                    <div>
                      <label htmlFor="city" className="block text-sm font-medium text-gray-700 mb-1">City *</label>
                      <input
                        type="text"
                        id="city"
                        name="city"
                        value={formData.city}
                        onChange={handleChange}
                        required
                        className="w-full p-2 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500"
                      />
                    </div>
                    
                    <div>
                      <label htmlFor="state" className="block text-sm font-medium text-gray-700 mb-1">State *</label>
                      <input
                        type="text"
                        id="state"
                        name="state"
                        value={formData.state}
                        onChange={handleChange}
                        required
                        className="w-full p-2 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500"
                      />
                    </div>
                    
                    <div>
                      <label htmlFor="zipCode" className="block text-sm font-medium text-gray-700 mb-1">Zip Code *</label>
                      <input
                        type="text"
                        id="zipCode"
                        name="zipCode"
                        value={formData.zipCode}
                        onChange={handleChange}
                        required
                        className="w-full p-2 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500"
                      />
                    </div>
                    
                    <div className="flex items-center">
                      <input
                        type="checkbox"
                        id="remotePossible"
                        name="remotePossible"
                        checked={formData.remotePossible}
                        onChange={(e) => setFormData(prev => ({ ...prev, remotePossible: e.target.checked }))}
                        className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                      />
                      <label htmlFor="remotePossible" className="ml-2 block text-sm text-gray-700">
                        Remote work possible (telehealth)
                      </label>
                    </div>
                  </div>
                </div>
                
                {/* Compensation */}
                <div>
                  <h2 className="text-xl font-semibold mb-4 pb-2 border-b">Compensation</h2>
                  <div>
                    <label htmlFor="hourlyRate" className="block text-sm font-medium text-gray-700 mb-1">Hourly Rate (USD) *</label>
                    <div className="relative rounded-md shadow-sm">
                      <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                        <span className="text-gray-500 sm:text-sm">$</span>
                      </div>
                      <input
                        type="number"
                        id="hourlyRate"
                        name="hourlyRate"
                        value={formData.hourlyRate}
                        onChange={handleChange}
                        required
                        min="1"
                        step="0.01"
                        className="w-full pl-7 p-2 border border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500"
                      />
                    </div>
                  </div>
                </div>
                
                <div className="pt-4 border-t flex justify-end space-x-4">
                  <button
                    type="button"
                    onClick={() => router.push('/employer/jobs')}
                    className="py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    disabled={isLoading}
                    className="py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50"
                  >
                    {isLoading ? 'Posting...' : 'Post Job'}
                  </button>
                </div>
              </div>
            </form>
          </div>
        </main>
      </div>
    </ProtectedRoute>
  );
}