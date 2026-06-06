'use client';

import { useState } from 'react';
import { motion } from 'framer-motion';
import Link from 'next/link';
import { 
  ArrowLeft,
  Calendar,
  Clock,
  CheckCircle,
  XCircle,
  AlertCircle,
  User,
  Phone,
  ChevronDown,
  Search,
  Filter
} from 'lucide-react';

// Mock bookings
const bookings = [
  { id: 1, token: 1, name: 'Kiran P.', phone: '+91 98765 43210', service: 'Haircut', time: '10:00 AM', amount: 300, status: 'completed', staff: 'Rahul' },
  { id: 2, token: 2, name: 'Priya S.', phone: '+91 98765 43211', service: 'Hair Spa', time: '10:30 AM', amount: 600, status: 'completed', staff: 'Amit' },
  { id: 3, token: 3, name: 'Arjun K.', phone: '+91 98765 43212', service: 'Shave', time: '11:00 AM', amount: 200, status: 'completed', staff: 'Vijay' },
  { id: 4, token: 4, name: 'Rahul S.', phone: '+91 98765 43213', service: 'Haircut + Beard', time: '11:30 AM', amount: 450, status: 'in-progress', staff: 'Rahul' },
  { id: 5, token: 5, name: 'Amit K.', phone: '+91 98765 43214', service: 'Beard Trim', time: '12:00 PM', amount: 150, status: 'confirmed', staff: 'Amit' },
  { id: 6, token: 6, name: 'Vijay M.', phone: '+91 98765 43215', service: 'Hair Color', time: '12:30 PM', amount: 800, status: 'confirmed', staff: 'Rahul' },
  { id: 7, token: 7, name: 'Suresh T.', phone: '+91 98765 43216', service: 'Haircut', time: '1:00 PM', amount: 300, status: 'confirmed', staff: 'Vijay' },
  { id: 8, token: 8, name: 'Ramesh V.', phone: '+91 98765 43217', service: 'Massage', time: '1:30 PM', amount: 500, status: 'cancelled', staff: 'Amit' },
];

const statusConfig = {
  'completed': { color: 'green', icon: CheckCircle, label: 'Completed' },
  'in-progress': { color: 'purple', icon: AlertCircle, label: 'In Progress' },
  'confirmed': { color: 'blue', icon: Clock, label: 'Confirmed' },
  'cancelled': { color: 'red', icon: XCircle, label: 'Cancelled' }
};

export default function TodayBookingsPage() {
  const [filter, setFilter] = useState('all');
  const [searchQuery, setSearchQuery] = useState('');

  const filteredBookings = bookings.filter(booking => {
    if (filter !== 'all' && booking.status !== filter) return false;
    if (searchQuery && !booking.name.toLowerCase().includes(searchQuery.toLowerCase())) return false;
    return true;
  });

  const stats = {
    total: bookings.length,
    completed: bookings.filter(b => b.status === 'completed').length,
    pending: bookings.filter(b => b.status === 'confirmed' || b.status === 'in-progress').length,
    cancelled: bookings.filter(b => b.status === 'cancelled').length
  };

  return (
    <div className="min-h-screen bg-[#0a0a0f]">
      <div className="fixed inset-0 -z-10">
        <div className="absolute top-0 right-0 w-96 h-96 bg-purple-500/10 rounded-full blur-[128px]" />
      </div>

      <header className="sticky top-0 z-50 backdrop-blur-xl bg-black/40 border-b border-white/5">
        <div className="max-w-4xl mx-auto px-6 py-4">
          <div className="flex items-center justify-between">
            <Link href="/shop/dashboard" className="flex items-center gap-2 text-gray-400 hover:text-white transition-colors">
              <ArrowLeft className="w-5 h-5" />
              <span>Dashboard</span>
            </Link>
            <h1 className="text-lg font-semibold text-white">Today's Bookings</h1>
            <div className="flex items-center gap-2">
              <Calendar className="w-5 h-5 text-purple-400" />
              <span className="text-sm text-gray-400">{new Date().toLocaleDateString('en-IN', { day: 'numeric', month: 'short' })}</span>
            </div>
          </div>
        </div>
      </header>

      <main className="max-w-4xl mx-auto px-6 py-6">
        {/* Stats */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="grid grid-cols-4 gap-3 mb-6"
        >
          <div className="p-4 rounded-xl bg-white/5 border border-white/10 text-center">
            <p className="text-2xl font-bold text-white">{stats.total}</p>
            <p className="text-xs text-gray-500">Total</p>
          </div>
          <div className="p-4 rounded-xl bg-green-500/10 border border-green-500/20 text-center">
            <p className="text-2xl font-bold text-green-400">{stats.completed}</p>
            <p className="text-xs text-gray-500">Completed</p>
          </div>
          <div className="p-4 rounded-xl bg-blue-500/10 border border-blue-500/20 text-center">
            <p className="text-2xl font-bold text-blue-400">{stats.pending}</p>
            <p className="text-xs text-gray-500">Pending</p>
          </div>
          <div className="p-4 rounded-xl bg-red-500/10 border border-red-500/20 text-center">
            <p className="text-2xl font-bold text-red-400">{stats.cancelled}</p>
            <p className="text-xs text-gray-500">Cancelled</p>
          </div>
        </motion.div>

        {/* Search & Filter */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.1 }}
          className="flex gap-3 mb-6"
        >
          <div className="flex-1 relative">
            <Search className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-500" />
            <input
              type="text"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              placeholder="Search by customer name..."
              className="w-full pl-12 pr-4 py-3 rounded-xl bg-white/5 border border-white/10 text-white placeholder-gray-500 focus:border-purple-500 outline-none"
            />
          </div>
          <select
            value={filter}
            onChange={(e) => setFilter(e.target.value)}
            className="px-4 py-3 rounded-xl bg-white/5 border border-white/10 text-white outline-none focus:border-purple-500"
          >
            <option value="all" className="bg-gray-900">All Status</option>
            <option value="completed" className="bg-gray-900">Completed</option>
            <option value="in-progress" className="bg-gray-900">In Progress</option>
            <option value="confirmed" className="bg-gray-900">Confirmed</option>
            <option value="cancelled" className="bg-gray-900">Cancelled</option>
          </select>
        </motion.div>

        {/* Bookings List */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.2 }}
          className="space-y-3"
        >
          {filteredBookings.map((booking) => {
            const status = statusConfig[booking.status as keyof typeof statusConfig];
            const StatusIcon = status.icon;
            
            return (
              <div
                key={booking.id}
                className={`p-4 rounded-xl border transition-all ${
                  booking.status === 'in-progress'
                    ? 'bg-purple-500/10 border-purple-500'
                    : 'bg-white/5 border-white/10'
                }`}
              >
                <div className="flex items-center gap-4">
                  <div className={`w-12 h-12 rounded-xl flex items-center justify-center text-lg font-bold ${
                    booking.status === 'in-progress' ? 'bg-purple-500 text-white' : 'bg-white/10 text-gray-400'
                  }`}>
                    #{booking.token}
                  </div>
                  
                  <div className="flex-1 min-w-0">
                    <div className="flex items-center gap-2 mb-1">
                      <p className="font-medium text-white truncate">{booking.name}</p>
                      <span className={`px-2 py-0.5 rounded-full text-xs font-medium bg-${status.color}-500/20 text-${status.color}-400`}>
                        {status.label}
                      </span>
                    </div>
                    <div className="flex items-center gap-4 text-sm text-gray-500">
                      <span>{booking.service}</span>
                      <span>•</span>
                      <span>{booking.time}</span>
                      <span>•</span>
                      <span>{booking.staff}</span>
                    </div>
                  </div>

                  <div className="text-right">
                    <p className="font-medium text-white">₹{booking.amount}</p>
                    <a href={`tel:${booking.phone}`} className="text-sm text-purple-400 hover:underline flex items-center gap-1">
                      <Phone className="w-3 h-3" />
                      Call
                    </a>
                  </div>
                </div>

                {booking.status === 'confirmed' && (
                  <div className="flex gap-2 mt-3 pt-3 border-t border-white/10">
                    <button className="flex-1 py-2 rounded-lg bg-purple-500 text-white text-sm font-medium hover:bg-purple-600 transition-colors">
                      Start Service
                    </button>
                    <button className="flex-1 py-2 rounded-lg bg-red-500/10 text-red-400 text-sm font-medium hover:bg-red-500/20 transition-colors">
                      Cancel
                    </button>
                  </div>
                )}

                {booking.status === 'in-progress' && (
                  <div className="mt-3 pt-3 border-t border-white/10">
                    <button className="w-full py-2 rounded-lg bg-green-500 text-white text-sm font-medium hover:bg-green-600 transition-colors">
                      Mark as Completed
                    </button>
                  </div>
                )}
              </div>
            );
          })}

          {filteredBookings.length === 0 && (
            <div className="text-center py-12">
              <Calendar className="w-12 h-12 text-gray-600 mx-auto mb-3" />
              <p className="text-gray-400">No bookings found</p>
            </div>
          )}
        </motion.div>
      </main>
    </div>
  );
}
