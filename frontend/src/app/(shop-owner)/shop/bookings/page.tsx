'use client';

import { useState } from 'react';
import { motion } from 'framer-motion';
import Link from 'next/link';
import { 
  ArrowLeft,
  Calendar,
  Search,
  Filter,
  ChevronLeft,
  ChevronRight,
  CheckCircle,
  XCircle,
  Clock,
  Download
} from 'lucide-react';

// Mock all bookings
const allBookings = [
  { id: 1, date: '2026-01-02', customer: 'Saurabh K.', service: 'Haircut', time: '10:00 AM', amount: 300, status: 'completed', staff: 'Rahul' },
  { id: 2, date: '2026-01-02', customer: 'Priya S.', service: 'Hair Spa', time: '10:30 AM', amount: 600, status: 'completed', staff: 'Amit' },
  { id: 3, date: '2026-01-02', customer: 'Arjun K.', service: 'Shave', time: '11:00 AM', amount: 200, status: 'completed', staff: 'Vijay' },
  { id: 4, date: '2026-01-02', customer: 'Rahul S.', service: 'Haircut', time: '11:30 AM', amount: 300, status: 'confirmed', staff: 'Rahul' },
  { id: 5, date: '2026-01-01', customer: 'Amit K.', service: 'Beard', time: '12:00 PM', amount: 150, status: 'completed', staff: 'Amit' },
  { id: 6, date: '2026-01-01', customer: 'Vijay M.', service: 'Color', time: '2:00 PM', amount: 800, status: 'completed', staff: 'Rahul' },
  { id: 7, date: '2026-01-01', customer: 'Kiran P.', service: 'Haircut', time: '3:00 PM', amount: 300, status: 'cancelled', staff: 'Vijay' },
  { id: 8, date: '2025-12-31', customer: 'Suresh T.', service: 'Massage', time: '11:00 AM', amount: 500, status: 'completed', staff: 'Amit' },
  { id: 9, date: '2025-12-31', customer: 'Ramesh V.', service: 'Haircut', time: '1:00 PM', amount: 300, status: 'completed', staff: 'Rahul' },
  { id: 10, date: '2025-12-30', customer: 'Ganesh R.', service: 'Spa', time: '10:00 AM', amount: 600, status: 'completed', staff: 'Vijay' },
];

const statusColors = {
  completed: 'text-green-400 bg-green-500/20',
  confirmed: 'text-blue-400 bg-blue-500/20',
  cancelled: 'text-red-400 bg-red-500/20',
  'no-show': 'text-yellow-400 bg-yellow-500/20'
};

export default function AllBookingsPage() {
  const [searchQuery, setSearchQuery] = useState('');
  const [statusFilter, setStatusFilter] = useState('all');
  const [dateFilter, setDateFilter] = useState('');

  const filteredBookings = allBookings.filter(booking => {
    if (searchQuery && !booking.customer.toLowerCase().includes(searchQuery.toLowerCase())) return false;
    if (statusFilter !== 'all' && booking.status !== statusFilter) return false;
    if (dateFilter && booking.date !== dateFilter) return false;
    return true;
  });

  const stats = {
    total: allBookings.length,
    completed: allBookings.filter(b => b.status === 'completed').length,
    cancelled: allBookings.filter(b => b.status === 'cancelled').length,
    revenue: allBookings.filter(b => b.status === 'completed').reduce((sum, b) => sum + b.amount, 0)
  };

  return (
    <div className="min-h-screen bg-[#0a0a0f]">
      <div className="fixed inset-0 -z-10">
        <div className="absolute top-0 right-0 w-96 h-96 bg-purple-500/10 rounded-full blur-[128px]" />
      </div>

      <header className="sticky top-0 z-50 backdrop-blur-xl bg-black/40 border-b border-white/5">
        <div className="max-w-5xl mx-auto px-6 py-4">
          <div className="flex items-center justify-between">
            <Link href="/shop/dashboard" className="flex items-center gap-2 text-gray-400 hover:text-white transition-colors">
              <ArrowLeft className="w-5 h-5" />
              <span>Dashboard</span>
            </Link>
            <h1 className="text-lg font-semibold text-white">All Bookings</h1>
            <button className="flex items-center gap-2 px-4 py-2 rounded-xl bg-white/5 border border-white/10 text-gray-400 hover:text-white transition-colors">
              <Download className="w-4 h-4" />
              Export
            </button>
          </div>
        </div>
      </header>

      <main className="max-w-5xl mx-auto px-6 py-6">
        {/* Stats */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="grid grid-cols-2 md:grid-cols-4 gap-3 mb-6"
        >
          <div className="p-4 rounded-xl bg-white/5 border border-white/10">
            <p className="text-2xl font-bold text-white">{stats.total}</p>
            <p className="text-sm text-gray-500">Total Bookings</p>
          </div>
          <div className="p-4 rounded-xl bg-green-500/10 border border-green-500/20">
            <p className="text-2xl font-bold text-green-400">{stats.completed}</p>
            <p className="text-sm text-gray-500">Completed</p>
          </div>
          <div className="p-4 rounded-xl bg-red-500/10 border border-red-500/20">
            <p className="text-2xl font-bold text-red-400">{stats.cancelled}</p>
            <p className="text-sm text-gray-500">Cancelled</p>
          </div>
          <div className="p-4 rounded-xl bg-teal-500/10 border border-teal-500/20">
            <p className="text-2xl font-bold text-teal-400">₹{stats.revenue}</p>
            <p className="text-sm text-gray-500">Revenue</p>
          </div>
        </motion.div>

        {/* Filters */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.1 }}
          className="flex flex-wrap gap-3 mb-6"
        >
          <div className="flex-1 min-w-[200px] relative">
            <Search className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-500" />
            <input
              type="text"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              placeholder="Search customer..."
              className="w-full pl-12 pr-4 py-3 rounded-xl bg-white/5 border border-white/10 text-white placeholder-gray-500 focus:border-purple-500 outline-none"
            />
          </div>
          <input
            type="date"
            value={dateFilter}
            onChange={(e) => setDateFilter(e.target.value)}
            className="px-4 py-3 rounded-xl bg-white/5 border border-white/10 text-white focus:border-purple-500 outline-none"
          />
          <select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            className="px-4 py-3 rounded-xl bg-white/5 border border-white/10 text-white focus:border-purple-500 outline-none"
          >
            <option value="all" className="bg-gray-900">All Status</option>
            <option value="completed" className="bg-gray-900">Completed</option>
            <option value="confirmed" className="bg-gray-900">Confirmed</option>
            <option value="cancelled" className="bg-gray-900">Cancelled</option>
          </select>
        </motion.div>

        {/* Bookings Table */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.2 }}
          className="rounded-2xl bg-white/5 border border-white/10 overflow-hidden"
        >
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead>
                <tr className="border-b border-white/10">
                  <th className="text-left text-sm font-medium text-gray-500 px-4 py-3">Date</th>
                  <th className="text-left text-sm font-medium text-gray-500 px-4 py-3">Customer</th>
                  <th className="text-left text-sm font-medium text-gray-500 px-4 py-3">Service</th>
                  <th className="text-left text-sm font-medium text-gray-500 px-4 py-3">Time</th>
                  <th className="text-left text-sm font-medium text-gray-500 px-4 py-3">Staff</th>
                  <th className="text-left text-sm font-medium text-gray-500 px-4 py-3">Amount</th>
                  <th className="text-left text-sm font-medium text-gray-500 px-4 py-3">Status</th>
                </tr>
              </thead>
              <tbody>
                {filteredBookings.map((booking) => (
                  <tr key={booking.id} className="border-b border-white/5 hover:bg-white/[0.02]">
                    <td className="px-4 py-4 text-sm text-gray-400">
                      {new Date(booking.date).toLocaleDateString('en-IN', { day: 'numeric', month: 'short' })}
                    </td>
                    <td className="px-4 py-4 text-sm text-white font-medium">{booking.customer}</td>
                    <td className="px-4 py-4 text-sm text-gray-400">{booking.service}</td>
                    <td className="px-4 py-4 text-sm text-gray-400">{booking.time}</td>
                    <td className="px-4 py-4 text-sm text-gray-400">{booking.staff}</td>
                    <td className="px-4 py-4 text-sm text-white font-medium">₹{booking.amount}</td>
                    <td className="px-4 py-4">
                      <span className={`px-2 py-1 rounded-full text-xs font-medium ${statusColors[booking.status as keyof typeof statusColors]}`}>
                        {booking.status}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          {filteredBookings.length === 0 && (
            <div className="text-center py-12">
              <Calendar className="w-12 h-12 text-gray-600 mx-auto mb-3" />
              <p className="text-gray-400">No bookings found</p>
            </div>
          )}
        </motion.div>

        {/* Pagination */}
        <div className="flex items-center justify-between mt-6">
          <p className="text-sm text-gray-500">Showing {filteredBookings.length} of {allBookings.length} bookings</p>
          <div className="flex gap-2">
            <button className="p-2 rounded-lg bg-white/5 text-gray-400 hover:bg-white/10 transition-colors">
              <ChevronLeft className="w-5 h-5" />
            </button>
            <button className="px-3 py-2 rounded-lg bg-purple-500 text-white text-sm font-medium">1</button>
            <button className="px-3 py-2 rounded-lg bg-white/5 text-gray-400 text-sm hover:bg-white/10 transition-colors">2</button>
            <button className="p-2 rounded-lg bg-white/5 text-gray-400 hover:bg-white/10 transition-colors">
              <ChevronRight className="w-5 h-5" />
            </button>
          </div>
        </div>
      </main>
    </div>
  );
}
