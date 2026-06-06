'use client';

import { motion } from 'framer-motion';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { XCircle, RefreshCw, MessageCircle, Home, AlertTriangle } from 'lucide-react';

export default function BookingFailedPage() {
  const router = useRouter();

  return (
    <div className="min-h-screen bg-[#0a0a0f] flex items-center justify-center p-6">
      {/* Background */}
      <div className="fixed inset-0 -z-10">
        <div className="absolute inset-0 bg-gradient-to-br from-red-900/10 via-transparent to-orange-900/10" />
        <div className="absolute top-1/4 left-1/4 w-96 h-96 bg-red-500/10 rounded-full blur-[128px]" />
      </div>

      <motion.div
        initial={{ opacity: 0, scale: 0.9 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ duration: 0.5 }}
        className="w-full max-w-md text-center"
      >
        {/* Failed Icon */}
        <motion.div
          initial={{ scale: 0 }}
          animate={{ scale: 1 }}
          transition={{ delay: 0.2, type: 'spring', stiffness: 200 }}
          className="w-24 h-24 rounded-full bg-gradient-to-br from-red-500 to-orange-600 flex items-center justify-center mx-auto mb-6"
        >
          <XCircle className="w-12 h-12 text-white" />
        </motion.div>

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.3 }}
          className="mb-8"
        >
          <h1 className="text-3xl font-bold text-white mb-2">Payment Failed</h1>
          <p className="text-gray-400">Your payment could not be processed</p>
        </motion.div>

        {/* Error Details */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.4 }}
          className="p-5 rounded-2xl bg-red-500/10 border border-red-500/20 mb-8"
        >
          <div className="flex items-start gap-3">
            <AlertTriangle className="w-5 h-5 text-red-400 shrink-0 mt-0.5" />
            <div className="text-left">
              <p className="text-sm text-red-400 font-medium mb-1">Transaction Declined</p>
              <p className="text-sm text-gray-400">
                The payment was declined by your bank. Please check your payment details or try a different payment method.
              </p>
            </div>
          </div>
        </motion.div>

        {/* Transaction Info */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.45 }}
          className="p-4 rounded-xl bg-white/5 border border-white/10 mb-8 text-left"
        >
          <div className="flex justify-between text-sm mb-2">
            <span className="text-gray-500">Transaction ID</span>
            <span className="text-gray-300 font-mono">TXN2026010212345</span>
          </div>
          <div className="flex justify-between text-sm mb-2">
            <span className="text-gray-500">Amount</span>
            <span className="text-gray-300">₹460</span>
          </div>
          <div className="flex justify-between text-sm">
            <span className="text-gray-500">Status</span>
            <span className="text-red-400">Failed</span>
          </div>
        </motion.div>

        {/* Actions */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.5 }}
          className="space-y-3"
        >
          <button
            onClick={() => router.push('/payment')}
            className="w-full py-4 rounded-xl bg-gradient-to-r from-purple-500 to-teal-500 text-white font-semibold flex items-center justify-center gap-2 hover:opacity-90 transition-opacity"
          >
            <RefreshCw className="w-5 h-5" />
            Try Again
          </button>

          <button className="w-full py-3 rounded-xl bg-white/5 border border-white/10 text-gray-300 font-medium flex items-center justify-center gap-2 hover:bg-white/10 transition-all">
            <MessageCircle className="w-5 h-5" />
            Contact Support
          </button>

          <Link
            href="/dashboard"
            className="block w-full py-3 rounded-xl text-gray-400 hover:text-white transition-colors flex items-center justify-center gap-2"
          >
            <Home className="w-5 h-5" />
            Back to Dashboard
          </Link>
        </motion.div>

        <p className="mt-8 text-xs text-gray-600">
          If any amount was debited, it will be refunded within 5-7 business days.
        </p>
      </motion.div>
    </div>
  );
}
