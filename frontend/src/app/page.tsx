import Link from 'next/link';

export default function Home() {
  return (
    <main className="min-h-screen flex flex-col items-center">
      <header className="w-full bg-white shadow-sm py-4">
        <div className="container mx-auto px-4 flex justify-between items-center">
          <h1 className="text-2xl font-bold text-blue-600">HealthGig</h1>
          <div className="space-x-4">
            <Link href="/auth/login" className="btn-secondary">
              Login
            </Link>
            <Link href="/auth/register" className="btn-primary">
              Register
            </Link>
          </div>
        </div>
      </header>

      <section className="bg-gradient-to-r from-blue-500 to-teal-400 w-full py-20 text-white">
        <div className="container mx-auto px-4 flex flex-col items-center text-center">
          <h2 className="text-4xl font-bold mb-6">Healthcare Gigs, On Demand</h2>
          <p className="text-xl max-w-2xl mb-8">
            Connect with healthcare professionals or find shifts at top healthcare facilities. 
            HealthGig makes healthcare staffing simple, reliable, and efficient.
          </p>
          <div className="flex space-x-4">
            <Link href="/auth/register?role=WORKER" className="btn-primary bg-white text-blue-600 hover:bg-gray-100">
              I'm a Healthcare Professional
            </Link>
            <Link href="/auth/register?role=EMPLOYER" className="btn-primary bg-transparent border border-white hover:bg-white hover:text-blue-600">
              I'm a Healthcare Facility
            </Link>
          </div>
        </div>
      </section>

      <section className="py-16 w-full bg-white">
        <div className="container mx-auto px-4">
          <h2 className="text-3xl font-bold text-center mb-12">How It Works</h2>
          
          <div className="grid md:grid-cols-3 gap-8">
            <div className="card text-center">
              <div className="text-5xl font-bold text-blue-500 mb-4">1</div>
              <h3 className="text-xl font-semibold mb-2">Create Your Profile</h3>
              <p className="text-gray-600">
                Sign up as a healthcare professional or facility and complete your profile with your qualifications and requirements.
              </p>
            </div>
            
            <div className="card text-center">
              <div className="text-5xl font-bold text-blue-500 mb-4">2</div>
              <h3 className="text-xl font-semibold mb-2">Find Matches</h3>
              <p className="text-gray-600">
                Browse available shifts or qualified professionals based on your specific needs and location.
              </p>
            </div>
            
            <div className="card text-center">
              <div className="text-5xl font-bold text-blue-500 mb-4">3</div>
              <h3 className="text-xl font-semibold mb-2">Work & Get Paid</h3>
              <p className="text-gray-600">
                Complete shifts, track hours, and receive payments securely through our platform.
              </p>
            </div>
          </div>
        </div>
      </section>

      <section className="py-16 w-full bg-gray-50">
        <div className="container mx-auto px-4 text-center">
          <h2 className="text-3xl font-bold mb-8">Join HealthGig Today</h2>
          <p className="text-xl max-w-2xl mx-auto mb-8">
            Whether you're looking for flexible healthcare work or need to staff your facility, 
            HealthGig provides the platform to connect quickly and efficiently.
          </p>
          <Link href="/auth/register" className="btn-primary text-lg px-6 py-3">
            Get Started
          </Link>
        </div>
      </section>

      <footer className="w-full bg-gray-800 text-white py-8">
        <div className="container mx-auto px-4">
          <div className="flex flex-col md:flex-row justify-between">
            <div className="mb-6 md:mb-0">
              <h3 className="text-xl font-bold mb-4">HealthGig</h3>
              <p className="text-gray-300">Healthcare staffing made simple.</p>
            </div>
            
            <div className="grid grid-cols-2 md:grid-cols-3 gap-8">
              <div>
                <h4 className="text-lg font-semibold mb-3">About</h4>
                <ul className="space-y-2 text-gray-300">
                  <li><Link href="#" className="hover:text-white">Our Story</Link></li>
                  <li><Link href="#" className="hover:text-white">How It Works</Link></li>
                  <li><Link href="#" className="hover:text-white">Testimonials</Link></li>
                </ul>
              </div>
              
              <div>
                <h4 className="text-lg font-semibold mb-3">Resources</h4>
                <ul className="space-y-2 text-gray-300">
                  <li><Link href="#" className="hover:text-white">Blog</Link></li>
                  <li><Link href="#" className="hover:text-white">FAQ</Link></li>
                  <li><Link href="#" className="hover:text-white">Support</Link></li>
                </ul>
              </div>
              
              <div>
                <h4 className="text-lg font-semibold mb-3">Legal</h4>
                <ul className="space-y-2 text-gray-300">
                  <li><Link href="#" className="hover:text-white">Privacy Policy</Link></li>
                  <li><Link href="#" className="hover:text-white">Terms of Service</Link></li>
                </ul>
              </div>
            </div>
          </div>
          
          <div className="mt-8 pt-8 border-t border-gray-700 text-center text-gray-300">
            <p>© {new Date().getFullYear()} HealthGig. All rights reserved.</p>
          </div>
        </div>
      </footer>
    </main>
  );
}