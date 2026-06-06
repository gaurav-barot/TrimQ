'use client';

import { motion } from 'framer-motion';
import Link from 'next/link';
import { 
  ArrowLeft,
  TrendingUp,
  TrendingDown,
  DollarSign,
  Calendar,
  Download,
  ArrowUpRight,
  ArrowDownRight,
  CreditCard,
  Wallet
} from 'lucide-react';

// Mock earnings data
const earningsData = {
  today: { amount: 4850, bookings: 12, change: 15 },
  week: { amount: 28500, bookings: 68, change: 8 },
  month: { amount: 125000, bookings: 312, change: 12 },
  pending: 8500
};

const recentTransactions = [
  { id: 1, customer: 'Saurabh K.', service: 'Haircut', amount: 300, time: '10:00 AM', method: 'UPI' },
  { id: 2, customer: 'Priya S.', service: 'Hair Spa', amount: 600, time: '10:30 AM', method: 'Card' },
  { id: 3, customer: 'Arjun K.', service: 'Shave', amount: 200, time: '11:00 AM', method: 'Cash' },
  { id: 4, customer: 'Rahul S.', service: 'Haircut + Beard', amount: 450, time: '11:30 AM', method: 'UPI' },
  { id: 5, customer: 'Amit K.', service: 'Color', amount: 800, time: '12:00 PM', method: 'Card' },
];

const dailyEarnings = [
  { day: 'Mon', amount: 3800 },
  { day: 'Tue', amount: 4200 },
  { day: 'Wed', amount: 3500 },
  { day: 'Thu', amount: 4800 },
  { day: 'Fri', amount: 5200 },
  { day: 'Sat', amount: 6500 },
  { day: 'Sun', amount: 4850 },
];

const maxDailyAmount = Math.max(...dailyEarnings.map(d => d.amount));

export default function EarningsPage() {
  return (
    <div className="min-h-screen bg-[#0a0a0f]">
      <div className="fixed inset-0 -z-10">
        <div className="absolute top-0 right-0 w-96 h-96 bg-purple-500/10 rounded-full blur-[128px]" />
        <div className="absolute bottom-0 left-0 w-96 h-96 bg-teal-500/10 rounded-full blur-[128px]" />
      </div>

      <header className="sticky top-0 z-50 backdrop-blur-xl bg-black/40 border-b border-white/5">
        <div className="max-w-5xl mx-auto px-6 py-4">
          <div className="flex items-center justify-between">
            <Link href="/shop/dashboard" className="flex items-center gap-2 text-gray-400 hover:text-white transition-colors">
              <ArrowLeft className="w-5 h-5" />
              <span>Dashboard</span>
            </Link>
            <h1 className="text-lg font-semibold text-white">Earnings</h1>
            <button className="flex items-center gap-2 px-4 py-2 rounded-xl bg-white/5 border border-white/10 text-gray-400 hover:text-white transition-colors">
              <Download className="w-4 h-4" />
              Report
            </button>
          </div>
        </div>
      </header>

      <main className="max-w-5xl mx-auto px-6 py-6">
        {/* Earnings Cards */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="grid md:grid-cols-4 gap-4 mb-8"
        >
          <div className="p-5 rounded-2xl bg-gradient-to-br from-teal-500/20 to-teal-600/10 border border-teal-500/20">
            <div className="flex items-center justify-between mb-3">
              <DollarSign className="w-6 h-6 text-teal-400" />
              <span className="flex items-center gap-1 text-xs text-green-400">
                <ArrowUpRight className="w-3 h-3" />
                {earningsData.today.change}%
              </span>
            </div>
            <p className="text-3xl font-bold text-white">₹{earningsData.today.amount}</p>
            <p className="text-sm text-gray-500">Today's Earnings</p>
            <p className="text-xs text-gray-600 mt-1">{earningsData.today.bookings} bookings</p>
          </div>

          <div className="p-5 rounded-2xl bg-gradient-to-br from-purple-500/20 to-purple-600/10 border border-purple-500/20">
            <div className="flex items-center justify-between mb-3">
              <Calendar className="w-6 h-6 text-purple-400" />
              <span className="flex items-center gap-1 text-xs text-green-400">
                <ArrowUpRight className="w-3 h-3" />
                {earningsData.week.change}%
              </span>
            </div>
            <p className="text-3xl font-bold text-white">₹{earningsData.week.amount.toLocaleString()}</p>
            <p className="text-sm text-gray-500">This Week</p>
            <p className="text-xs text-gray-600 mt-1">{earningsData.week.bookings} bookings</p>
          </div>

          <div className="p-5 rounded-2xl bg-gradient-to-br from-pink-500/20 to-pink-600/10 border border-pink-500/20">
            <div className="flex items-center justify-between mb-3">
              <TrendingUp className="w-6 h-6 text-pink-400" />
              <span className="flex items-center gap-1 text-xs text-green-400">
                <ArrowUpRight className="w-3 h-3" />
                {earningsData.month.change}%
              </span>
            </div>
            <p className="text-3xl font-bold text-white">₹{(earningsData.month.amount / 1000).toFixed(0)}K</p>
            <p className="text-sm text-gray-500">This Month</p>
            <p className="text-xs text-gray-600 mt-1">{earningsData.month.bookings} bookings</p>
          </div>

          <div className="p-5 rounded-2xl bg-gradient-to-br from-yellow-500/20 to-yellow-600/10 border border-yellow-500/20">
            <div className="flex items-center justify-between mb-3">
              <Wallet className="w-6 h-6 text-yellow-400" />
            </div>
            <p className="text-3xl font-bold text-white">₹{earningsData.pending.toLocaleString()}</p>
            <p className="text-sm text-gray-500">Pending Payout</p>
            <p className="text-xs text-gray-600 mt-1">Next payout: Jan 5</p>
          </div>
        </motion.div>

        <div className="grid lg:grid-cols-3 gap-6">
          {/* Weekly Chart */}
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.1 }}
            className="lg:col-span-2 p-6 rounded-2xl bg-white/5 border border-white/10"
          >
            <h2 className="text-lg font-semibold text-white mb-6">This Week's Performance</h2>
            
            <div className="flex items-end justify-between h-48 mb-4">
              {dailyEarnings.map((day, i) => (
                <div key={i} className="flex flex-col items-center gap-2 flex-1">
                  <span className="text-xs text-gray-400">₹{day.amount}</span>
                  <div
                    className={`w-full max-w-[40px] rounded-t-lg transition-all ${
                      i === dailyEarnings.length - 1
                        ? 'bg-gradient-to-t from-purple-500 to-teal-500'
                        : 'bg-white/10'
                    }`}
                    style={{ height: `${(day.amount / maxDailyAmount) * 100}%` }}
                  />
                  <span className="text-xs text-gray-500">{day.day}</span>
                </div>
              ))}
            </div>

            <div className="flex items-center justify-between pt-4 border-t border-white/10">
              <div>
                <p className="text-sm text-gray-500">Weekly Total</p>
                <p className="text-2xl font-bold text-white">₹{dailyEarnings.reduce((a, b) => a + b.amount, 0).toLocaleString()}</p>
              </div>
              <div className="text-right">
                <p className="text-sm text-gray-500">Average per day</p>
                <p className="text-xl font-semibold text-purple-400">
                  ₹{Math.round(dailyEarnings.reduce((a, b) => a + b.amount, 0) / 7).toLocaleString()}
                </p>
              </div>
            </div>
          </motion.div>

          {/* Payment Methods */}
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.15 }}
            className="p-6 rounded-2xl bg-white/5 border border-white/10"
          >
            <h2 className="text-lg font-semibold text-white mb-4">Payment Methods</h2>
            
            <div className="space-y-4">
              <div>
                <div className="flex items-center justify-between mb-2">
                  <span className="text-sm text-gray-400">UPI</span>
                  <span className="text-sm text-white">60%</span>
                </div>
                <div className="h-2 bg-white/10 rounded-full overflow-hidden">
                  <div className="h-full w-[60%] bg-gradient-to-r from-purple-500 to-teal-500 rounded-full" />
                </div>
              </div>
              
              <div>
                <div className="flex items-center justify-between mb-2">
                  <span className="text-sm text-gray-400">Card</span>
                  <span className="text-sm text-white">25%</span>
                </div>
                <div className="h-2 bg-white/10 rounded-full overflow-hidden">
                  <div className="h-full w-[25%] bg-purple-500 rounded-full" />
                </div>
              </div>
              
              <div>
                <div className="flex items-center justify-between mb-2">
                  <span className="text-sm text-gray-400">Cash</span>
                  <span className="text-sm text-white">15%</span>
                </div>
                <div className="h-2 bg-white/10 rounded-full overflow-hidden">
                  <div className="h-full w-[15%] bg-teal-500 rounded-full" />
                </div>
              </div>
            </div>
          </motion.div>
        </div>

        {/* Recent Transactions */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.2 }}
          className="mt-6"
        >
          <h2 className="text-lg font-semibold text-white mb-4">Today's Transactions</h2>
          
          <div className="space-y-3">
            {recentTransactions.map((txn) => (
              <div key={txn.id} className="flex items-center justify-between p-4 rounded-xl bg-white/5 border border-white/10">
                <div className="flex items-center gap-4">
                  <div className="w-10 h-10 rounded-xl bg-green-500/20 flex items-center justify-center">
                    <ArrowUpRight className="w-5 h-5 text-green-400" />
                  </div>
                  <div>
                    <p className="font-medium text-white">{txn.customer}</p>
                    <p className="text-sm text-gray-500">{txn.service} • {txn.time}</p>
                  </div>
                </div>
                <div className="text-right">
                  <p className="font-semibold text-green-400">+₹{txn.amount}</p>
                  <p className="text-xs text-gray-500">{txn.method}</p>
                </div>
              </div>
            ))}
          </div>
        </motion.div>
      </main>
    </div>
  );
}
