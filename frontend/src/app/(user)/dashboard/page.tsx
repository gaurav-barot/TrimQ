'use client';

import { motion } from 'framer-motion';
import Link from 'next/link';
import { 
  Calendar, 
  Clock, 
  MapPin, 
  QrCode, 
  ChevronRight,
  Scissors,
  Star,
  TrendingUp,
  Bell,
  Search,
  User,
  Plus,
  CheckCircle,
  XCircle,
  AlertCircle
} from 'lucide-react';

// Mock data for demo
const upcomingBooking = {
  id: 1,
  shopName: 'Royal Cuts Salon',
  shopAddress: 'MG Road, Bangalore',
  date: 'Today',
  time: '11:30 AM',
  services: ['Haircut', 'Beard Trim'],
  tokenNumber: 5,
  passCode: 'TQ-20260102-A7X9',
  status: 'CONFIRMED',
  totalAmount: 450,
  staffName: 'Rahul'
};

const recentBookings = [
  {
    id: 2,
    shopName: 'Style Studio',
    date: 'Dec 28, 2025',
    services: ['Hair Color', 'Haircut'],
    status: 'COMPLETED',
    amount: 1200
  },
  {
    id: 3,
    shopName: 'Gentlemen\'s Hub',
    date: 'Dec 20, 2025',
    services: ['Haircut'],
    status: 'COMPLETED',
    amount: 300
  },
  {
    id: 4,
    shopName: 'Urban Barbers',
    date: 'Dec 15, 2025',
    services: ['Beard Styling'],
    status: 'CANCELLED',
    amount: 200
  }
];

const recommendedShops = [
  {
    id: 1,
    name: 'Premium Cuts',
    rating: 4.8,
    reviews: 234,
    image: '✂️',
    distance: '0.5 km',
    price: '₹300+'
  },
  {
    id: 2,
    name: 'The Barber Shop',
    rating: 4.6,
    reviews: 189,
    image: '💈',
    distance: '1.2 km',
    price: '₹250+'
  },
  {
    id: 3,
    name: 'Style Masters',
    rating: 4.9,
    reviews: 412,
    image: '💇',
    distance: '2.0 km',
    price: '₹400+'
  }
];

const fadeInUp = {
  initial: { opacity: 0, y: 20 },
  animate: { opacity: 1, y: 0 },
  transition: { duration: 0.4 }
};

const staggerContainer = {
  animate: {
    transition: {
      staggerChildren: 0.1
    }
  }
};

export default function UserDashboardPage() {
  const userName = 'Saurabh';

  return (
    <div className="min-h-screen bg-[#0a0a0f]">
      {/* Background Effects */}
      <div className="fixed inset-0 -z-10">
        <div className="absolute top-0 right-0 w-96 h-96 bg-purple-500/10 rounded-full blur-[128px]" />
        <div className="absolute bottom-0 left-0 w-96 h-96 bg-teal-500/10 rounded-full blur-[128px]" />
      </div>

      {/* Header */}
      <header className="sticky top-0 z-50 backdrop-blur-xl bg-black/40 border-b border-white/5">
        <div className="max-w-7xl mx-auto px-6 py-4">
          <div className="flex items-center justify-between">
            <Link href="/" className="flex items-center gap-2">
              <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-purple-500 to-teal-500 flex items-center justify-center">
                <Scissors className="w-5 h-5 text-white" />
              </div>
              <span className="text-xl font-bold bg-gradient-to-r from-purple-400 to-teal-400 bg-clip-text text-transparent">
                TrimQ
              </span>
            </Link>

            <div className="flex items-center gap-4">
              <button className="p-2.5 rounded-xl bg-white/5 hover:bg-white/10 transition-colors relative">
                <Bell className="w-5 h-5 text-gray-400" />
                <span className="absolute top-1 right-1 w-2 h-2 bg-purple-500 rounded-full" />
              </button>
              <Link href="/search" className="p-2.5 rounded-xl bg-white/5 hover:bg-white/10 transition-colors">
                <Search className="w-5 h-5 text-gray-400" />
              </Link>
              <Link href="/profile" className="flex items-center gap-3 px-3 py-2 rounded-xl bg-white/5 hover:bg-white/10 transition-colors">
                <div className="w-8 h-8 rounded-lg bg-gradient-to-br from-purple-500 to-teal-500 flex items-center justify-center">
                  <User className="w-4 h-4 text-white" />
                </div>
                <span className="text-sm font-medium hidden sm:block">{userName}</span>
              </Link>
            </div>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto px-6 py-8">
        <motion.div
          initial="initial"
          animate="animate"
          variants={staggerContainer}
          className="space-y-8"
        >
          {/* Welcome Section */}
          <motion.div variants={fadeInUp} className="flex items-center justify-between">
            <div>
              <h1 className="text-3xl font-bold text-white mb-1">
                Welcome back, {userName}! 👋
              </h1>
              <p className="text-gray-500">Ready for your next grooming session?</p>
            </div>
            <Link 
              href="/search"
              className="hidden md:flex items-center gap-2 px-5 py-3 rounded-xl bg-gradient-to-r from-purple-500 to-teal-500 text-white font-medium hover:opacity-90 transition-opacity"
            >
              <Plus className="w-5 h-5" />
              Book New
            </Link>
          </motion.div>

          {/* Stats Cards */}
          <motion.div variants={fadeInUp} className="grid grid-cols-2 md:grid-cols-4 gap-4">
            {[
              { label: 'Total Bookings', value: '12', icon: Calendar, color: 'purple' },
              { label: 'This Month', value: '3', icon: TrendingUp, color: 'teal' },
              { label: 'Completed', value: '10', icon: CheckCircle, color: 'green' },
              { label: 'Saved', value: '₹450', icon: Star, color: 'yellow' },
            ].map((stat, index) => (
              <div 
                key={index}
                className="p-5 rounded-2xl bg-white/5 border border-white/10 hover:border-white/20 transition-colors"
              >
                <div className={`w-10 h-10 rounded-xl bg-${stat.color}-500/20 flex items-center justify-center mb-3`}>
                  <stat.icon className={`w-5 h-5 text-${stat.color}-400`} />
                </div>
                <div className="text-2xl font-bold text-white">{stat.value}</div>
                <div className="text-sm text-gray-500">{stat.label}</div>
              </div>
            ))}
          </motion.div>

          {/* Upcoming Booking Card */}
          {upcomingBooking && (
            <motion.div variants={fadeInUp}>
              <div className="flex items-center justify-between mb-4">
                <h2 className="text-xl font-bold text-white">Upcoming Booking</h2>
                <Link href={`/pass/${upcomingBooking.id}`} className="text-sm text-purple-400 hover:text-purple-300 flex items-center gap-1">
                  View Pass <ChevronRight className="w-4 h-4" />
                </Link>
              </div>

              <div className="relative overflow-hidden rounded-3xl bg-gradient-to-br from-purple-600 via-purple-700 to-teal-600 p-1">
                <div className="absolute inset-0 bg-[url('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNjAiIGhlaWdodD0iNjAiIHZpZXdCb3g9IjAgMCA2MCA2MCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48ZyBmaWxsPSJub25lIiBmaWxsLXJ1bGU9ImV2ZW5vZGQiPjxnIGZpbGw9IiNmZmZmZmYiIGZpbGwtb3BhY2l0eT0iMC4xIj48cGF0aCBkPSJNMzYgMzR2LTRoLTJ2NGgtNHYyaDR2NGgydi00aDR2LTJoLTR6bTAtMzBWMGgtMnY0aC00djJoNHY0aDJWNmg0VjRoLTR6TTYgMzR2LTRINHY0SDB2Mmg0djRoMnYtNGg0di0ySDZ6TTYgNFYwSDR2NEgwdjJoNHY0aDJWNmg0VjRINnoiLz48L2c+PC9nPjwvc3ZnPg==')] opacity-30" />
                
                <div className="relative bg-[#0a0a0f]/90 rounded-[22px] p-6">
                  <div className="flex flex-col md:flex-row md:items-center justify-between gap-6">
                    {/* Left: Booking Info */}
                    <div className="flex-1">
                      <div className="flex items-center gap-2 mb-3">
                        <span className="px-3 py-1 rounded-full bg-green-500/20 text-green-400 text-xs font-medium">
                          Confirmed
                        </span>
                        <span className="px-3 py-1 rounded-full bg-purple-500/20 text-purple-400 text-xs font-medium">
                          Token #{upcomingBooking.tokenNumber}
                        </span>
                      </div>

                      <h3 className="text-2xl font-bold text-white mb-2">
                        {upcomingBooking.shopName}
                      </h3>

                      <div className="flex flex-wrap gap-4 text-sm text-gray-400 mb-4">
                        <div className="flex items-center gap-2">
                          <MapPin className="w-4 h-4" />
                          {upcomingBooking.shopAddress}
                        </div>
                        <div className="flex items-center gap-2">
                          <User className="w-4 h-4" />
                          {upcomingBooking.staffName}
                        </div>
                      </div>

                      <div className="flex items-center gap-6">
                        <div className="flex items-center gap-2">
                          <div className="w-10 h-10 rounded-xl bg-white/10 flex items-center justify-center">
                            <Calendar className="w-5 h-5 text-purple-400" />
                          </div>
                          <div>
                            <div className="text-xs text-gray-500">Date</div>
                            <div className="text-sm font-medium text-white">{upcomingBooking.date}</div>
                          </div>
                        </div>
                        <div className="flex items-center gap-2">
                          <div className="w-10 h-10 rounded-xl bg-white/10 flex items-center justify-center">
                            <Clock className="w-5 h-5 text-teal-400" />
                          </div>
                          <div>
                            <div className="text-xs text-gray-500">Time</div>
                            <div className="text-sm font-medium text-white">{upcomingBooking.time}</div>
                          </div>
                        </div>
                      </div>

                      <div className="mt-4 flex flex-wrap gap-2">
                        {upcomingBooking.services.map((service, i) => (
                          <span key={i} className="px-3 py-1.5 rounded-lg bg-white/5 text-sm text-gray-300">
                            {service}
                          </span>
                        ))}
                      </div>
                    </div>

                    {/* Right: QR Code */}
                    <div className="flex flex-col items-center gap-3">
                      <div className="w-32 h-32 rounded-2xl bg-white p-3 flex items-center justify-center">
                        <QrCode className="w-full h-full text-gray-800" />
                      </div>
                      <div className="text-center">
                        <div className="text-xs text-gray-500">Pass Code</div>
                        <div className="text-sm font-mono font-bold text-white">{upcomingBooking.passCode}</div>
                      </div>
                      <div className="text-xl font-bold text-white">₹{upcomingBooking.totalAmount}</div>
                    </div>
                  </div>
                </div>
              </div>
            </motion.div>
          )}

          {/* Two Column Layout */}
          <div className="grid md:grid-cols-2 gap-8">
            {/* Recent Bookings */}
            <motion.div variants={fadeInUp}>
              <div className="flex items-center justify-between mb-4">
                <h2 className="text-xl font-bold text-white">Recent Bookings</h2>
                <Link href="/bookings" className="text-sm text-purple-400 hover:text-purple-300 flex items-center gap-1">
                  View All <ChevronRight className="w-4 h-4" />
                </Link>
              </div>

              <div className="space-y-3">
                {recentBookings.map((booking) => (
                  <div 
                    key={booking.id}
                    className="p-4 rounded-2xl bg-white/5 border border-white/10 hover:border-white/20 transition-all cursor-pointer group"
                  >
                    <div className="flex items-center justify-between">
                      <div className="flex-1">
                        <div className="flex items-center gap-2 mb-1">
                          <h4 className="font-medium text-white">{booking.shopName}</h4>
                          {booking.status === 'COMPLETED' && (
                            <CheckCircle className="w-4 h-4 text-green-400" />
                          )}
                          {booking.status === 'CANCELLED' && (
                            <XCircle className="w-4 h-4 text-red-400" />
                          )}
                        </div>
                        <div className="text-sm text-gray-500">{booking.date}</div>
                        <div className="flex flex-wrap gap-1 mt-2">
                          {booking.services.map((service, i) => (
                            <span key={i} className="text-xs text-gray-400">{service}{i < booking.services.length - 1 ? ',' : ''}</span>
                          ))}
                        </div>
                      </div>
                      <div className="text-right">
                        <div className="font-medium text-white">₹{booking.amount}</div>
                        <div className={`text-xs ${booking.status === 'COMPLETED' ? 'text-green-400' : 'text-red-400'}`}>
                          {booking.status}
                        </div>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </motion.div>

            {/* Recommended Shops */}
            <motion.div variants={fadeInUp}>
              <div className="flex items-center justify-between mb-4">
                <h2 className="text-xl font-bold text-white">Recommended For You</h2>
                <Link href="/search" className="text-sm text-purple-400 hover:text-purple-300 flex items-center gap-1">
                  Explore <ChevronRight className="w-4 h-4" />
                </Link>
              </div>

              <div className="space-y-3">
                {recommendedShops.map((shop) => (
                  <Link 
                    key={shop.id}
                    href={`/shop/${shop.id}`}
                    className="block p-4 rounded-2xl bg-white/5 border border-white/10 hover:border-purple-500/50 transition-all group"
                  >
                    <div className="flex items-center gap-4">
                      <div className="w-14 h-14 rounded-xl bg-gradient-to-br from-purple-500/20 to-teal-500/20 flex items-center justify-center text-2xl">
                        {shop.image}
                      </div>
                      <div className="flex-1">
                        <h4 className="font-medium text-white group-hover:text-purple-400 transition-colors">
                          {shop.name}
                        </h4>
                        <div className="flex items-center gap-3 text-sm text-gray-500">
                          <div className="flex items-center gap-1">
                            <Star className="w-3.5 h-3.5 text-yellow-400 fill-yellow-400" />
                            {shop.rating} ({shop.reviews})
                          </div>
                          <span>•</span>
                          <span>{shop.distance}</span>
                        </div>
                      </div>
                      <div className="text-right">
                        <div className="text-sm font-medium text-white">{shop.price}</div>
                        <ChevronRight className="w-5 h-5 text-gray-600 group-hover:text-purple-400 group-hover:translate-x-1 transition-all ml-auto" />
                      </div>
                    </div>
                  </Link>
                ))}
              </div>
            </motion.div>
          </div>

          {/* Quick Actions */}
          <motion.div variants={fadeInUp} className="md:hidden">
            <Link 
              href="/search"
              className="flex items-center justify-center gap-2 w-full px-5 py-4 rounded-xl bg-gradient-to-r from-purple-500 to-teal-500 text-white font-medium"
            >
              <Plus className="w-5 h-5" />
              Book New Appointment
            </Link>
          </motion.div>
        </motion.div>
      </main>
    </div>
  );
}
