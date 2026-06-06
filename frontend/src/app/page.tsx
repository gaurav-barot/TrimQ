'use client';

import { motion } from 'framer-motion';
import Link from 'next/link';
import { 
  Scissors, 
  Clock, 
  MapPin, 
  Star, 
  Calendar, 
  QrCode, 
  Sparkles,
  ArrowRight,
  Check,
  Users,
  TrendingUp,
  Shield
} from 'lucide-react';

// Animation variants
const fadeInUp = {
  initial: { opacity: 0, y: 40 },
  animate: { opacity: 1, y: 0 },
  transition: { duration: 0.6 }
};

const staggerContainer = {
  animate: {
    transition: {
      staggerChildren: 0.1
    }
  }
};

export default function LandingPage() {
  return (
    <main className="min-h-screen bg-[#0a0a0f] text-white overflow-hidden">
      {/* Animated Background */}
      <div className="fixed inset-0 -z-10">
        <div className="absolute inset-0 bg-gradient-to-br from-purple-900/20 via-transparent to-teal-900/20" />
        <div className="absolute top-0 left-1/4 w-96 h-96 bg-purple-500/30 rounded-full blur-[128px] animate-pulse" />
        <div className="absolute bottom-0 right-1/4 w-96 h-96 bg-teal-500/30 rounded-full blur-[128px] animate-pulse" style={{ animationDelay: '1s' }} />
        <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[800px] h-[800px] bg-gradient-conic from-purple-500/10 via-teal-500/10 to-purple-500/10 rounded-full blur-[100px] animate-spin" style={{ animationDuration: '30s' }} />
      </div>

      {/* Navigation */}
      <nav className="fixed top-0 left-0 right-0 z-50 backdrop-blur-xl bg-black/20 border-b border-white/5">
        <div className="max-w-7xl mx-auto px-6 py-4 flex items-center justify-between">
          <motion.div 
            initial={{ opacity: 0, x: -20 }}
            animate={{ opacity: 1, x: 0 }}
            className="flex items-center gap-2"
          >
            <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-purple-500 to-teal-500 flex items-center justify-center">
              <Scissors className="w-5 h-5 text-white" />
            </div>
            <span className="text-2xl font-bold bg-gradient-to-r from-purple-400 to-teal-400 bg-clip-text text-transparent">
              TrimQ
            </span>
          </motion.div>

          <motion.div 
            initial={{ opacity: 0, x: 20 }}
            animate={{ opacity: 1, x: 0 }}
            className="flex items-center gap-6"
          >
            <Link href="/search" className="text-gray-400 hover:text-white transition-colors hidden md:block">
              Find Salons
            </Link>
            <Link href="/shop/register" className="text-gray-400 hover:text-white transition-colors hidden md:block">
              For Business
            </Link>
            <Link 
              href="/login" 
              className="px-5 py-2.5 rounded-xl bg-white/10 hover:bg-white/20 transition-all border border-white/10"
            >
              Login
            </Link>
            <Link 
              href="/register" 
              className="px-5 py-2.5 rounded-xl bg-gradient-to-r from-purple-500 to-teal-500 hover:opacity-90 transition-all font-medium"
            >
              Get Started
            </Link>
          </motion.div>
        </div>
      </nav>

      {/* Hero Section */}
      <section className="min-h-screen flex items-center justify-center px-6 pt-20">
        <div className="max-w-6xl mx-auto text-center">
          <motion.div
            initial={{ opacity: 0, scale: 0.9 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ duration: 0.8 }}
          >
            {/* Badge */}
            <motion.div 
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.2 }}
              className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-white/5 border border-white/10 mb-8"
            >
              <Sparkles className="w-4 h-4 text-yellow-400" />
              <span className="text-sm text-gray-300">Now live across 100+ cities in India</span>
            </motion.div>

            {/* Main Heading */}
            <motion.h1 
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.3 }}
              className="text-5xl md:text-7xl lg:text-8xl font-bold leading-tight mb-6"
            >
              <span className="block">Skip the Wait.</span>
              <span className="block bg-gradient-to-r from-purple-400 via-pink-400 to-teal-400 bg-clip-text text-transparent">
                Own Your Style.
              </span>
            </motion.h1>

            {/* Subtitle */}
            <motion.p 
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.4 }}
              className="text-xl md:text-2xl text-gray-400 max-w-3xl mx-auto mb-12"
            >
              Book your salon appointment in seconds. No more waiting in queues. 
              Get a digital pass and walk in like a VIP.
            </motion.p>

            {/* CTA Buttons */}
            <motion.div 
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.5 }}
              className="flex flex-col sm:flex-row items-center justify-center gap-4"
            >
              <Link 
                href="/search"
                className="group px-8 py-4 rounded-2xl bg-gradient-to-r from-purple-500 to-teal-500 font-semibold text-lg flex items-center gap-3 hover:shadow-2xl hover:shadow-purple-500/25 transition-all"
              >
                <MapPin className="w-5 h-5" />
                Find Salons Near Me
                <ArrowRight className="w-5 h-5 group-hover:translate-x-1 transition-transform" />
              </Link>
              <Link 
                href="/dashboard"
                className="px-8 py-4 rounded-2xl bg-white/5 border border-white/10 hover:bg-white/10 font-semibold text-lg flex items-center gap-3 transition-all"
              >
                <Calendar className="w-5 h-5" />
                View My Bookings
              </Link>
            </motion.div>
          </motion.div>

          {/* Stats */}
          <motion.div 
            initial={{ opacity: 0, y: 40 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.7 }}
            className="grid grid-cols-3 gap-8 mt-20 max-w-2xl mx-auto"
          >
            {[
              { value: '50K+', label: 'Happy Customers' },
              { value: '2000+', label: 'Partner Salons' },
              { value: '100+', label: 'Cities' },
            ].map((stat, index) => (
              <div key={index} className="text-center">
                <div className="text-3xl md:text-4xl font-bold bg-gradient-to-r from-purple-400 to-teal-400 bg-clip-text text-transparent">
                  {stat.value}
                </div>
                <div className="text-sm text-gray-500 mt-1">{stat.label}</div>
              </div>
            ))}
          </motion.div>
        </div>
      </section>

      {/* How It Works Section */}
      <section className="py-32 px-6">
        <div className="max-w-6xl mx-auto">
          <motion.div 
            initial="initial"
            whileInView="animate"
            viewport={{ once: true }}
            variants={staggerContainer}
            className="text-center mb-16"
          >
            <motion.h2 
              variants={fadeInUp}
              className="text-4xl md:text-5xl font-bold mb-4"
            >
              How It Works
            </motion.h2>
            <motion.p 
              variants={fadeInUp}
              className="text-xl text-gray-400"
            >
              Book your perfect grooming session in 3 simple steps
            </motion.p>
          </motion.div>

          <motion.div 
            initial="initial"
            whileInView="animate"
            viewport={{ once: true }}
            variants={staggerContainer}
            className="grid md:grid-cols-3 gap-8"
          >
            {[
              {
                icon: MapPin,
                step: '01',
                title: 'Discover',
                description: 'Find top-rated salons near you with real-time availability and transparent pricing.',
                color: 'from-purple-500 to-purple-600'
              },
              {
                icon: Calendar,
                step: '02',
                title: 'Book & Pay',
                description: 'Select your services, pick a time slot, and pay securely. No surprises.',
                color: 'from-pink-500 to-pink-600'
              },
              {
                icon: QrCode,
                step: '03',
                title: 'Walk In',
                description: 'Show your digital pass, skip the queue, and enjoy your grooming session.',
                color: 'from-teal-500 to-teal-600'
              },
            ].map((item, index) => (
              <motion.div
                key={index}
                variants={fadeInUp}
                className="group relative p-8 rounded-3xl bg-white/5 border border-white/10 hover:border-white/20 transition-all hover:-translate-y-2"
              >
                <div className={`w-14 h-14 rounded-2xl bg-gradient-to-br ${item.color} flex items-center justify-center mb-6`}>
                  <item.icon className="w-7 h-7 text-white" />
                </div>
                <div className="absolute top-6 right-6 text-6xl font-bold text-white/5">
                  {item.step}
                </div>
                <h3 className="text-2xl font-bold mb-3">{item.title}</h3>
                <p className="text-gray-400">{item.description}</p>
              </motion.div>
            ))}
          </motion.div>
        </div>
      </section>

      {/* Features Grid (Bento Style) */}
      <section className="py-32 px-6">
        <div className="max-w-6xl mx-auto">
          <motion.div
            initial="initial"
            whileInView="animate"
            viewport={{ once: true }}
            variants={staggerContainer}
            className="grid md:grid-cols-4 gap-4"
          >
            {/* Large Card */}
            <motion.div 
              variants={fadeInUp}
              className="md:col-span-2 md:row-span-2 p-8 rounded-3xl bg-gradient-to-br from-purple-900/50 to-purple-800/30 border border-purple-500/20 flex flex-col justify-between min-h-[400px]"
            >
              <div>
                <div className="w-12 h-12 rounded-xl bg-purple-500/20 flex items-center justify-center mb-4">
                  <Clock className="w-6 h-6 text-purple-400" />
                </div>
                <h3 className="text-3xl font-bold mb-3">Real-Time Availability</h3>
                <p className="text-gray-400 text-lg">
                  See live slot availability. No more calling to check if your barber is free.
                </p>
              </div>
              <div className="grid grid-cols-3 gap-2 mt-8">
                {['10:00', '10:30', '11:00', '11:30', '12:00', '12:30'].map((time, i) => (
                  <div 
                    key={time}
                    className={`py-3 px-4 rounded-xl text-center text-sm font-medium ${
                      i === 2 ? 'bg-purple-500 text-white' : 'bg-white/5 text-gray-400'
                    }`}
                  >
                    {time}
                  </div>
                ))}
              </div>
            </motion.div>

            {/* Small Cards */}
            <motion.div 
              variants={fadeInUp}
              className="p-6 rounded-3xl bg-gradient-to-br from-teal-900/50 to-teal-800/30 border border-teal-500/20"
            >
              <Star className="w-8 h-8 text-teal-400 mb-4" />
              <h4 className="text-xl font-bold mb-2">Verified Reviews</h4>
              <p className="text-gray-400 text-sm">Real reviews from real customers</p>
            </motion.div>

            <motion.div 
              variants={fadeInUp}
              className="p-6 rounded-3xl bg-gradient-to-br from-pink-900/50 to-pink-800/30 border border-pink-500/20"
            >
              <Shield className="w-8 h-8 text-pink-400 mb-4" />
              <h4 className="text-xl font-bold mb-2">Secure Payments</h4>
              <p className="text-gray-400 text-sm">UPI, Cards, Wallets - All supported</p>
            </motion.div>

            <motion.div 
              variants={fadeInUp}
              className="p-6 rounded-3xl bg-gradient-to-br from-yellow-900/50 to-yellow-800/30 border border-yellow-500/20"
            >
              <QrCode className="w-8 h-8 text-yellow-400 mb-4" />
              <h4 className="text-xl font-bold mb-2">Digital Pass</h4>
              <p className="text-gray-400 text-sm">QR code for hassle-free check-in</p>
            </motion.div>

            <motion.div 
              variants={fadeInUp}
              className="p-6 rounded-3xl bg-gradient-to-br from-blue-900/50 to-blue-800/30 border border-blue-500/20"
            >
              <Users className="w-8 h-8 text-blue-400 mb-4" />
              <h4 className="text-xl font-bold mb-2">Choose Your Stylist</h4>
              <p className="text-gray-400 text-sm">Book with your favorite barber</p>
            </motion.div>
          </motion.div>
        </div>
      </section>

      {/* For Business CTA */}
      <section className="py-32 px-6">
        <div className="max-w-4xl mx-auto">
          <motion.div
            initial={{ opacity: 0, scale: 0.95 }}
            whileInView={{ opacity: 1, scale: 1 }}
            viewport={{ once: true }}
            className="relative p-12 rounded-[32px] bg-gradient-to-r from-purple-600 to-teal-600 overflow-hidden"
          >
            <div className="absolute inset-0 bg-[url('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNjAiIGhlaWdodD0iNjAiIHZpZXdCb3g9IjAgMCA2MCA2MCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48ZyBmaWxsPSJub25lIiBmaWxsLXJ1bGU9ImV2ZW5vZGQiPjxnIGZpbGw9IiNmZmZmZmYiIGZpbGwtb3BhY2l0eT0iMC4xIj48cGF0aCBkPSJNMzYgMzR2LTRoLTJ2NGgtNHYyaDR2NGgydi00aDR2LTJoLTR6bTAtMzBWMGgtMnY0aC00djJoNHY0aDJWNmg0VjRoLTR6TTYgMzR2LTRINHY0SDB2Mmg0djRoMnYtNGg0di0ySDZ6TTYgNFYwSDR2NEgwdjJoNHY0aDJWNmg0VjRINnoiLz48L2c+PC9nPjwvc3ZnPg==')] opacity-20" />
            
            <div className="relative z-10 text-center">
              <div className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-white/20 mb-6">
                <TrendingUp className="w-4 h-4" />
                <span className="text-sm font-medium">Grow your business</span>
              </div>
              <h2 className="text-4xl md:text-5xl font-bold mb-4">
                Own a Salon or Barber Shop?
              </h2>
              <p className="text-xl text-white/80 mb-8 max-w-2xl mx-auto">
                Join TrimQ and reach thousands of customers. Manage bookings, reduce no-shows, and grow your revenue.
              </p>
              <Link 
                href="/shop/register"
                className="inline-flex items-center gap-3 px-8 py-4 rounded-2xl bg-white text-purple-600 font-semibold text-lg hover:bg-gray-100 transition-all"
              >
                Register Your Shop
                <ArrowRight className="w-5 h-5" />
              </Link>
            </div>
          </motion.div>
        </div>
      </section>

      {/* Footer */}
      <footer className="border-t border-white/10 py-16 px-6">
        <div className="max-w-6xl mx-auto">
          <div className="grid md:grid-cols-4 gap-12 mb-12">
            <div>
              <div className="flex items-center gap-2 mb-4">
                <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-purple-500 to-teal-500 flex items-center justify-center">
                  <Scissors className="w-5 h-5 text-white" />
                </div>
                <span className="text-xl font-bold">TrimQ</span>
              </div>
              <p className="text-gray-500 text-sm">
                Skip the Wait. Own Your Style.
              </p>
            </div>

            <div>
              <h4 className="font-semibold mb-4">Product</h4>
              <ul className="space-y-3 text-sm text-gray-500">
                <li><Link href="/search" className="hover:text-white transition-colors">Find Salons</Link></li>
                <li><Link href="/dashboard" className="hover:text-white transition-colors">My Bookings</Link></li>
                <li><Link href="/shop/register" className="hover:text-white transition-colors">For Business</Link></li>
              </ul>
            </div>

            <div>
              <h4 className="font-semibold mb-4">Company</h4>
              <ul className="space-y-3 text-sm text-gray-500">
                <li><Link href="/about" className="hover:text-white transition-colors">About Us</Link></li>
                <li><Link href="/careers" className="hover:text-white transition-colors">Careers</Link></li>
                <li><Link href="/contact" className="hover:text-white transition-colors">Contact</Link></li>
              </ul>
            </div>

            <div>
              <h4 className="font-semibold mb-4">Legal</h4>
              <ul className="space-y-3 text-sm text-gray-500">
                <li><Link href="/privacy" className="hover:text-white transition-colors">Privacy Policy</Link></li>
                <li><Link href="/terms" className="hover:text-white transition-colors">Terms of Service</Link></li>
                <li><Link href="/refund" className="hover:text-white transition-colors">Refund Policy</Link></li>
              </ul>
            </div>
          </div>

          <div className="pt-8 border-t border-white/10 text-center text-sm text-gray-600">
            <p>© {new Date().getFullYear()} TrimQ. All rights reserved. Made with ❤️ in India</p>
          </div>
        </div>
      </footer>
    </main>
  );
}
