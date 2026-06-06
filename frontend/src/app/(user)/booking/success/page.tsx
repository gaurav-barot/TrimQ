'use client';

import { useEffect, useState } from 'react';
import { motion } from 'framer-motion';
import Link from 'next/link';
import { 
  CheckCircle, 
  Calendar, 
  Clock, 
  MapPin, 
  QrCode,
  Download,
  Share2,
  Home,
  Sparkles
} from 'lucide-react';
import confetti from 'canvas-confetti';

// Mock booking confirmation data
const bookingConfirmation = {
  passCode: 'TQ-20260102-A7X9',
  tokenNumber: 5,
  shop: {
    name: 'Royal Cuts Salon',
    address: 'MG Road, Koramangala, Bangalore'
  },
  date: 'Today, January 2, 2026',
  time: '11:30 AM',
  services: ['Classic Haircut', 'Beard Trim'],
  stylist: 'Rahul',
  amount: 460
};

export default function BookingSuccessPage() {
  const [showContent, setShowContent] = useState(false);

  useEffect(() => {
    // Trigger confetti
    const duration = 2000;
    const end = Date.now() + duration;

    const frame = () => {
      confetti({
        particleCount: 3,
        angle: 60,
        spread: 55,
        origin: { x: 0 },
        colors: ['#a855f7', '#14b8a6', '#ec4899']
      });
      confetti({
        particleCount: 3,
        angle: 120,
        spread: 55,
        origin: { x: 1 },
        colors: ['#a855f7', '#14b8a6', '#ec4899']
      });

      if (Date.now() < end) {
        requestAnimationFrame(frame);
      }
    };
    frame();

    // Show content after a delay
    setTimeout(() => setShowContent(true), 500);
  }, []);

  return (
    <div className="min-h-screen bg-[#0a0a0f] flex items-center justify-center p-6">
      {/* Background */}
      <div className="fixed inset-0 -z-10">
        <div className="absolute inset-0 bg-gradient-to-br from-purple-900/20 via-transparent to-teal-900/20" />
        <div className="absolute top-1/4 left-1/4 w-96 h-96 bg-purple-500/20 rounded-full blur-[128px]" />
        <div className="absolute bottom-1/4 right-1/4 w-96 h-96 bg-teal-500/20 rounded-full blur-[128px]" />
      </div>

      <motion.div
        initial={{ opacity: 0, scale: 0.9 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ duration: 0.5 }}
        className="w-full max-w-md"
      >
        {/* Success Icon */}
        <motion.div
          initial={{ scale: 0 }}
          animate={{ scale: 1 }}
          transition={{ delay: 0.2, type: 'spring', stiffness: 200 }}
          className="w-24 h-24 rounded-full bg-gradient-to-br from-green-500 to-emerald-600 flex items-center justify-center mx-auto mb-6"
        >
          <CheckCircle className="w-12 h-12 text-white" />
        </motion.div>

        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: showContent ? 1 : 0, y: showContent ? 0 : 20 }}
          transition={{ delay: 0.3 }}
          className="text-center mb-8"
        >
          <h1 className="text-3xl font-bold text-white mb-2">Booking Confirmed!</h1>
          <p className="text-gray-400">Your appointment has been successfully booked</p>
        </motion.div>

        {/* Digital Pass Card */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: showContent ? 1 : 0, y: showContent ? 0 : 20 }}
          transition={{ delay: 0.4 }}
          className="relative overflow-hidden rounded-3xl bg-gradient-to-br from-purple-600 via-purple-700 to-teal-600 p-1 mb-6"
        >
          <div className="absolute inset-0 bg-[url('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNjAiIGhlaWdodD0iNjAiIHZpZXdCb3g9IjAgMCA2MCA2MCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48ZyBmaWxsPSJub25lIiBmaWxsLXJ1bGU9ImV2ZW5vZGQiPjxnIGZpbGw9IiNmZmZmZmYiIGZpbGwtb3BhY2l0eT0iMC4xIj48cGF0aCBkPSJNMzYgMzR2LTRoLTJ2NGgtNHYyaDR2NGgydi00aDR2LTJoLTR6bTAtMzBWMGgtMnY0aC00djJoNHY0aDJWNmg0VjRoLTR6TTYgMzR2LTRINHY0SDB2Mmg0djRoMnYtNGg0di0ySDZ6TTYgNFYwSDR2NEgwdjJoNHY0aDJWNmg0VjRINnoiLz48L2c+PC9nPjwvc3ZnPg==')] opacity-30" />
          
          <div className="relative bg-[#0a0a0f]/90 rounded-[22px] p-6">
            {/* Header */}
            <div className="flex items-center justify-between mb-4">
              <div className="flex items-center gap-2">
                <Sparkles className="w-5 h-5 text-yellow-400" />
                <span className="text-sm font-medium text-white">Digital Pass</span>
              </div>
              <span className="px-3 py-1 rounded-full bg-green-500/20 text-green-400 text-xs font-medium">
                Confirmed
              </span>
            </div>

            {/* Token Number */}
            <div className="text-center mb-4">
              <p className="text-sm text-gray-400 mb-1">Your Token Number</p>
              <p className="text-6xl font-bold text-white">#{bookingConfirmation.tokenNumber}</p>
            </div>

            {/* QR Code */}
            <div className="w-32 h-32 rounded-2xl bg-white p-3 mx-auto mb-4">
              <QrCode className="w-full h-full text-gray-800" />
            </div>

            {/* Pass Code */}
            <div className="text-center mb-4">
              <p className="text-xs text-gray-500">Pass Code</p>
              <p className="text-lg font-mono font-bold text-purple-400">{bookingConfirmation.passCode}</p>
            </div>

            {/* Details */}
            <div className="space-y-3 pt-4 border-t border-white/10">
              <div className="flex items-center gap-3 text-sm">
                <MapPin className="w-4 h-4 text-gray-500" />
                <span className="text-gray-300">{bookingConfirmation.shop.name}</span>
              </div>
              <div className="flex items-center gap-3 text-sm">
                <Calendar className="w-4 h-4 text-gray-500" />
                <span className="text-gray-300">{bookingConfirmation.date}</span>
              </div>
              <div className="flex items-center gap-3 text-sm">
                <Clock className="w-4 h-4 text-gray-500" />
                <span className="text-gray-300">{bookingConfirmation.time}</span>
              </div>
            </div>

            {/* Amount */}
            <div className="mt-4 pt-4 border-t border-white/10 flex items-center justify-between">
              <span className="text-gray-400">Amount Paid</span>
              <span className="text-xl font-bold text-white">₹{bookingConfirmation.amount}</span>
            </div>
          </div>
        </motion.div>

        {/* Actions */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: showContent ? 1 : 0, y: showContent ? 0 : 20 }}
          transition={{ delay: 0.5 }}
          className="space-y-3"
        >
          <div className="flex gap-3">
            <button className="flex-1 py-3 rounded-xl bg-white/5 border border-white/10 text-gray-300 font-medium flex items-center justify-center gap-2 hover:bg-white/10 transition-all">
              <Download className="w-5 h-5" />
              Save Pass
            </button>
            <button className="flex-1 py-3 rounded-xl bg-white/5 border border-white/10 text-gray-300 font-medium flex items-center justify-center gap-2 hover:bg-white/10 transition-all">
              <Share2 className="w-5 h-5" />
              Share
            </button>
          </div>

          <Link
            href={`/pass/${bookingConfirmation.passCode}`}
            className="block w-full py-4 rounded-xl bg-gradient-to-r from-purple-500 to-teal-500 text-white font-semibold text-center hover:opacity-90 transition-opacity"
          >
            View Full Pass
          </Link>

          <Link
            href="/dashboard"
            className="block w-full py-3 rounded-xl bg-white/5 border border-white/10 text-gray-300 font-medium text-center hover:bg-white/10 transition-all flex items-center justify-center gap-2"
          >
            <Home className="w-5 h-5" />
            Back to Dashboard
          </Link>
        </motion.div>
      </motion.div>
    </div>
  );
}
