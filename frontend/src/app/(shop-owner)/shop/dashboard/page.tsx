'use client';

import { motion } from 'framer-motion';
import Link from 'next/link';
import { 
  Calendar,
  DollarSign,
  Users,
  TrendingUp,
  Clock,
  CheckCircle,
  XCircle,
  AlertCircle,
  ChevronRight,
  QrCode,
  Plus,
  Bell,
  Settings,
  Scissors,
  ArrowUp,
  ArrowDown
} from 'lucide-react';

// Mock data
const todayStats = {
  totalBookings: 12,
  completed: 8,
  pending: 3,
  cancelled: 1,
  revenue: 4850,
  currentToken: 8
};

const liveQueue = [
  { token: 8, name: 'Rahul S.', service: 'Haircut', time: '11:30 AM', status: 'in-progress' },
  { token: 9, name: 'Amit K.', service: 'Beard Trim', time: '12:00 PM', status: 'waiting' },
  { token: 10, name: 'Vijay M.', service: 'Hair Color', time: '12:30 PM', status: 'waiting' },
];

const recentBookings = [
  { id: 1, name: 'Kiran P.', service: 'Haircut', time: '10:00 AM', amount: 300, status: 'completed' },
  { id: 2, name: 'Priya S.', service: 'Hair Spa', time: '10:30 AM', amount: 600, status: 'completed' },
  { id: 3, name: 'Arjun K.', service: 'Shave', time: '11:00 AM', amount: 200, status: 'completed' },
];

const weeklyData = {
  bookings: [8, 12, 10, 15, 11, 18, 12],
  days: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
};

export default function ShopDashboardPage() {
  const shopName = 'Royal Cuts Salon';

  return (
    <div className="min-h-screen bg-[#0a0a0f]">
      {/* Background */}
      <div className="fixed inset-0 -z-10">
        <div className="absolute top-0 right-0 w-96 h-96 bg-purple-500/10 rounded-full blur-[128px]" />
        <div className="absolute bottom-0 left-0 w-96 h-96 bg-teal-500/10 rounded-full blur-[128px]" />
      </div>

      {/* Header */}
      <header className="sticky top-0 z-50 backdrop-blur-xl bg-black/40 border-b border-white/5">
        <div className="max-w-7xl mx-auto px-6 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-purple-500 to-teal-500 flex items-center justify-center">
                <Scissors className="w-5 h-5 text-white" />
              </div>
              <div>
                <h1 className="text-lg font-bold text-white">{shopName}</h1>
                <p className="text-xs text-gray-500">Shop Dashboard</p>
              </div>
            </div>
            <div className="flex items-center gap-3">
              <button className="p-2.5 rounded-xl bg-white/5 hover:bg-white/10 transition-colors relative">
                <Bell className="w-5 h-5 text-gray-400" />
                <span className="absolute top-1 right-1 w-2 h-2 bg-red-500 rounded-full" />
              </button>
              <Link href="/shop/settings" className="p-2.5 rounded-xl bg-white/5 hover:bg-white/10 transition-colors">
                <Settings className="w-5 h-5 text-gray-400" />
              </Link>
            </div>
          </div>
        </div>
      </header>

      <main className="max-w-7xl mx-auto px-6 py-6">
        {/* Quick Actions */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="flex gap-3 mb-8 overflow-x-auto pb-2"
        >
          <Link
            href="/shop/validate"
            className="flex items-center gap-2 px-5 py-3 rounded-xl bg-gradient-to-r from-purple-500 to-teal-500 text-white font-medium shrink-0"
          >
            <QrCode className="w-5 h-5" />
            Validate Pass
          </Link>
          <Link
            href="/shop/today"
            className="flex items-center gap-2 px-5 py-3 rounded-xl bg-white/5 border border-white/10 text-gray-300 font-medium shrink-0 hover:bg-white/10 transition-colors"
          >
            <Calendar className="w-5 h-5" />
            Today's Bookings
          </Link>
          <Link
            href="/shop/services"
            className="flex items-center gap-2 px-5 py-3 rounded-xl bg-white/5 border border-white/10 text-gray-300 font-medium shrink-0 hover:bg-white/10 transition-colors"
          >
            <Plus className="w-5 h-5" />
            Add Service
          </Link>
        </motion.div>

        {/* Stats Grid */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.1 }}
          className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-8"
        >
          <div className="p-5 rounded-2xl bg-gradient-to-br from-purple-500/20 to-purple-600/10 border border-purple-500/20">
            <div className="flex items-center justify-between mb-3">
              <Calendar className="w-6 h-6 text-purple-400" />
              <span className="flex items-center gap-1 text-xs text-green-400">
                <ArrowUp className="w-3 h-3" /> 12%
              </span>
            </div>
            <p className="text-3xl font-bold text-white">{todayStats.totalBookings}</p>
            <p className="text-sm text-gray-500">Today's Bookings</p>
          </div>

          <div className="p-5 rounded-2xl bg-gradient-to-br from-teal-500/20 to-teal-600/10 border border-teal-500/20">
            <div className="flex items-center justify-between mb-3">
              <DollarSign className="w-6 h-6 text-teal-400" />
              <span className="flex items-center gap-1 text-xs text-green-400">
                <ArrowUp className="w-3 h-3" /> 8%
              </span>
            </div>
            <p className="text-3xl font-bold text-white">₹{todayStats.revenue}</p>
            <p className="text-sm text-gray-500">Today's Revenue</p>
          </div>

          <div className="p-5 rounded-2xl bg-gradient-to-br from-green-500/20 to-green-600/10 border border-green-500/20">
            <div className="flex items-center justify-between mb-3">
              <CheckCircle className="w-6 h-6 text-green-400" />
            </div>
            <p className="text-3xl font-bold text-white">{todayStats.completed}</p>
            <p className="text-sm text-gray-500">Completed</p>
          </div>

          <div className="p-5 rounded-2xl bg-gradient-to-br from-yellow-500/20 to-yellow-600/10 border border-yellow-500/20">
            <div className="flex items-center justify-between mb-3">
              <Clock className="w-6 h-6 text-yellow-400" />
            </div>
            <p className="text-3xl font-bold text-white">{todayStats.pending}</p>
            <p className="text-sm text-gray-500">Pending</p>
          </div>
        </motion.div>

        <div className="grid lg:grid-cols-3 gap-6">
          {/* Live Queue */}
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.2 }}
            className="lg:col-span-2"
          >
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-xl font-bold text-white">Live Queue</h2>
              <div className="flex items-center gap-2 px-3 py-1.5 rounded-full bg-green-500/20 text-green-400">
                <div className="w-2 h-2 bg-green-400 rounded-full animate-pulse" />
                <span className="text-sm font-medium">Now Serving: #{todayStats.currentToken}</span>
              </div>
            </div>

            <div className="space-y-3">
              {liveQueue.map((item, i) => (
                <div
                  key={i}
                  className={`p-4 rounded-xl border transition-all ${
                    item.status === 'in-progress'
                      ? 'bg-purple-500/10 border-purple-500'
                      : 'bg-white/5 border-white/10'
                  }`}
                >
                  <div className="flex items-center gap-4">
                    <div className={`w-12 h-12 rounded-xl flex items-center justify-center text-xl font-bold ${
                      item.status === 'in-progress' ? 'bg-purple-500 text-white' : 'bg-white/10 text-gray-400'
                    }`}>
                      #{item.token}
                    </div>
                    <div className="flex-1">
                      <p className="font-medium text-white">{item.name}</p>
                      <p className="text-sm text-gray-500">{item.service} • {item.time}</p>
                    </div>
                    <span className={`px-3 py-1 rounded-full text-xs font-medium ${
                      item.status === 'in-progress'
                        ? 'bg-purple-500/20 text-purple-400'
                        : 'bg-yellow-500/20 text-yellow-400'
                    }`}>
                      {item.status === 'in-progress' ? 'In Progress' : 'Waiting'}
                    </span>
                  </div>
                </div>
              ))}
            </div>

            <Link
              href="/shop/today"
              className="flex items-center justify-center gap-2 mt-4 py-3 rounded-xl bg-white/5 border border-white/10 text-gray-400 hover:text-white transition-colors"
            >
              View All Bookings
              <ChevronRight className="w-4 h-4" />
            </Link>
          </motion.div>

          {/* Weekly Stats */}
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.3 }}
          >
            <h2 className="text-xl font-bold text-white mb-4">This Week</h2>
            <div className="p-5 rounded-2xl bg-white/5 border border-white/10">
              <div className="flex items-end justify-between h-40 mb-4">
                {weeklyData.bookings.map((value, i) => (
                  <div key={i} className="flex flex-col items-center gap-2 flex-1">
                    <div
                      className={`w-full max-w-[30px] rounded-t-lg transition-all ${
                        i === weeklyData.bookings.length - 1
                          ? 'bg-gradient-to-t from-purple-500 to-teal-500'
                          : 'bg-white/10'
                      }`}
                      style={{ height: `${(value / Math.max(...weeklyData.bookings)) * 100}%` }}
                    />
                    <span className="text-xs text-gray-500">{weeklyData.days[i]}</span>
                  </div>
                ))}
              </div>
              <div className="pt-4 border-t border-white/10">
                <div className="flex items-center justify-between">
                  <span className="text-gray-500">Total Bookings</span>
                  <span className="text-xl font-bold text-white">
                    {weeklyData.bookings.reduce((a, b) => a + b, 0)}
                  </span>
                </div>
              </div>
            </div>

            {/* Recent Completions */}
            <h2 className="text-xl font-bold text-white mt-6 mb-4">Recent</h2>
            <div className="space-y-3">
              {recentBookings.map((booking) => (
                <div key={booking.id} className="p-3 rounded-xl bg-white/5 border border-white/10">
                  <div className="flex items-center justify-between">
                    <div>
                      <p className="text-sm font-medium text-white">{booking.name}</p>
                      <p className="text-xs text-gray-500">{booking.service} • {booking.time}</p>
                    </div>
                    <div className="text-right">
                      <p className="text-sm font-medium text-green-400">₹{booking.amount}</p>
                      <CheckCircle className="w-4 h-4 text-green-400 ml-auto" />
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </motion.div>
        </div>
      </main>
    </div>
  );
}
