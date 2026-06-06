'use client';

import { motion } from 'framer-motion';
import Link from 'next/link';
import { 
  ArrowLeft,
  QrCode,
  Calendar,
  Clock,
  MapPin,
  User,
  Phone,
  Navigation,
  Share2,
  Download,
  Sparkles,
  CheckCircle
} from 'lucide-react';

// Mock pass data
const passData = {
  passCode: 'TQ-20260102-A7X9',
  tokenNumber: 5,
  status: 'CONFIRMED',
  shop: {
    name: 'Royal Cuts Salon',
    address: '123, MG Road, Koramangala, Bangalore - 560034',
    phone: '+91 98765 43210',
    lat: 12.9352,
    lng: 77.6245
  },
  booking: {
    date: 'January 2, 2026',
    day: 'Thursday',
    time: '11:30 AM',
    endTime: '12:20 PM'
  },
  services: [
    { name: 'Classic Haircut', duration: 30, price: 300 },
    { name: 'Beard Trim', duration: 20, price: 150 }
  ],
  stylist: 'Rahul',
  payment: {
    subtotal: 450,
    fee: 10,
    total: 460,
    method: 'UPI',
    transactionId: 'TXN2026010254321'
  }
};

export default function DigitalPassPage() {
  const totalDuration = passData.services.reduce((sum, s) => sum + s.duration, 0);

  return (
    <div className="min-h-screen bg-[#0a0a0f]">
      {/* Background */}
      <div className="fixed inset-0 -z-10">
        <div className="absolute inset-0 bg-gradient-to-br from-purple-900/20 via-transparent to-teal-900/20" />
        <div className="absolute top-0 right-0 w-96 h-96 bg-purple-500/20 rounded-full blur-[128px]" />
        <div className="absolute bottom-0 left-0 w-96 h-96 bg-teal-500/20 rounded-full blur-[128px]" />
      </div>

      {/* Header */}
      <header className="sticky top-0 z-50 backdrop-blur-xl bg-black/40 border-b border-white/5">
        <div className="max-w-lg mx-auto px-6 py-4">
          <div className="flex items-center justify-between">
            <Link href="/dashboard" className="flex items-center gap-2 text-gray-400 hover:text-white transition-colors">
              <ArrowLeft className="w-5 h-5" />
              <span>Back</span>
            </Link>
            <h1 className="text-lg font-semibold text-white">Digital Pass</h1>
            <div className="flex items-center gap-2">
              <button className="p-2 rounded-lg bg-white/5 hover:bg-white/10 transition-colors">
                <Share2 className="w-4 h-4 text-gray-400" />
              </button>
              <button className="p-2 rounded-lg bg-white/5 hover:bg-white/10 transition-colors">
                <Download className="w-4 h-4 text-gray-400" />
              </button>
            </div>
          </div>
        </div>
      </header>

      <main className="max-w-lg mx-auto px-6 py-6">
        {/* Pass Card */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="relative overflow-hidden rounded-3xl bg-gradient-to-br from-purple-600 via-purple-700 to-teal-600 p-1"
        >
          {/* Pattern Overlay */}
          <div className="absolute inset-0 bg-[url('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNjAiIGhlaWdodD0iNjAiIHZpZXdCb3g9IjAgMCA2MCA2MCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48ZyBmaWxsPSJub25lIiBmaWxsLXJ1bGU9ImV2ZW5vZGQiPjxnIGZpbGw9IiNmZmZmZmYiIGZpbGwtb3BhY2l0eT0iMC4xIj48cGF0aCBkPSJNMzYgMzR2LTRoLTJ2NGgtNHYyaDR2NGgydi00aDR2LTJoLTR6bTAtMzBWMGgtMnY0aC00djJoNHY0aDJWNmg0VjRoLTR6TTYgMzR2LTRINHY0SDB2Mmg0djRoMnYtNGg0di0ySDZ6TTYgNFYwSDR2NEgwdjJoNHY0aDJWNmg0VjRINnoiLz48L2c+PC9nPjwvc3ZnPg==')] opacity-30" />
          
          <div className="relative bg-[#0a0a0f]/95 rounded-[22px] overflow-hidden">
            {/* Header */}
            <div className="px-6 py-4 border-b border-white/10">
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-2">
                  <Sparkles className="w-5 h-5 text-yellow-400" />
                  <span className="text-sm font-medium text-white">TrimQ Pass</span>
                </div>
                <span className="flex items-center gap-1.5 px-3 py-1 rounded-full bg-green-500/20 text-green-400 text-xs font-medium">
                  <CheckCircle className="w-3.5 h-3.5" />
                  {passData.status}
                </span>
              </div>
            </div>

            {/* Token & QR */}
            <div className="px-6 py-8 text-center border-b border-white/10 border-dashed">
              <p className="text-sm text-gray-400 mb-2">Your Token Number</p>
              <p className="text-7xl font-bold text-white mb-6">#{passData.tokenNumber}</p>
              
              <div className="w-40 h-40 rounded-2xl bg-white p-4 mx-auto mb-4">
                <QrCode className="w-full h-full text-gray-800" />
              </div>
              
              <p className="text-xs text-gray-500">Show this QR code at the salon</p>
              <p className="text-lg font-mono font-bold text-purple-400 mt-2">{passData.passCode}</p>
            </div>

            {/* Booking Details */}
            <div className="px-6 py-5 space-y-4">
              {/* Shop */}
              <div className="flex items-start gap-4">
                <div className="w-10 h-10 rounded-xl bg-purple-500/20 flex items-center justify-center shrink-0">
                  <MapPin className="w-5 h-5 text-purple-400" />
                </div>
                <div className="flex-1">
                  <p className="font-medium text-white">{passData.shop.name}</p>
                  <p className="text-sm text-gray-500 mt-0.5">{passData.shop.address}</p>
                </div>
                <a 
                  href={`https://maps.google.com/?q=${passData.shop.lat},${passData.shop.lng}`}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="p-2 rounded-lg bg-white/5 hover:bg-white/10 transition-colors"
                >
                  <Navigation className="w-4 h-4 text-teal-400" />
                </a>
              </div>

              {/* Date & Time */}
              <div className="flex gap-4">
                <div className="flex-1 flex items-center gap-3 p-3 rounded-xl bg-white/5">
                  <Calendar className="w-5 h-5 text-purple-400" />
                  <div>
                    <p className="text-xs text-gray-500">Date</p>
                    <p className="text-sm text-white">{passData.booking.date}</p>
                  </div>
                </div>
                <div className="flex-1 flex items-center gap-3 p-3 rounded-xl bg-white/5">
                  <Clock className="w-5 h-5 text-teal-400" />
                  <div>
                    <p className="text-xs text-gray-500">Time</p>
                    <p className="text-sm text-white">{passData.booking.time}</p>
                  </div>
                </div>
              </div>

              {/* Stylist */}
              <div className="flex items-center gap-3 p-3 rounded-xl bg-white/5">
                <User className="w-5 h-5 text-pink-400" />
                <div>
                  <p className="text-xs text-gray-500">Stylist</p>
                  <p className="text-sm text-white">{passData.stylist}</p>
                </div>
              </div>

              {/* Services */}
              <div>
                <p className="text-xs text-gray-500 mb-2">Services ({totalDuration} mins)</p>
                <div className="space-y-2">
                  {passData.services.map((service, i) => (
                    <div key={i} className="flex items-center justify-between p-3 rounded-xl bg-white/5">
                      <div>
                        <p className="text-sm text-white">{service.name}</p>
                        <p className="text-xs text-gray-500">{service.duration} mins</p>
                      </div>
                      <p className="text-sm font-medium text-white">₹{service.price}</p>
                    </div>
                  ))}
                </div>
              </div>

              {/* Payment */}
              <div className="pt-4 border-t border-white/10">
                <div className="flex justify-between text-sm mb-2">
                  <span className="text-gray-500">Subtotal</span>
                  <span className="text-gray-300">₹{passData.payment.subtotal}</span>
                </div>
                <div className="flex justify-between text-sm mb-2">
                  <span className="text-gray-500">Platform Fee</span>
                  <span className="text-gray-300">₹{passData.payment.fee}</span>
                </div>
                <div className="flex justify-between pt-2 border-t border-white/10">
                  <span className="font-medium text-white">Total Paid</span>
                  <span className="text-xl font-bold text-white">₹{passData.payment.total}</span>
                </div>
                <p className="text-xs text-gray-600 mt-2">
                  Paid via {passData.payment.method} • {passData.payment.transactionId}
                </p>
              </div>
            </div>
          </div>
        </motion.div>

        {/* Actions */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.2 }}
          className="mt-6 space-y-3"
        >
          <a 
            href={`tel:${passData.shop.phone}`}
            className="w-full py-3 rounded-xl bg-white/5 border border-white/10 text-gray-300 font-medium flex items-center justify-center gap-2 hover:bg-white/10 transition-all"
          >
            <Phone className="w-5 h-5" />
            Call Salon
          </a>

          <a
            href={`https://maps.google.com/?q=${passData.shop.lat},${passData.shop.lng}`}
            target="_blank"
            rel="noopener noreferrer"
            className="w-full py-3 rounded-xl bg-gradient-to-r from-purple-500 to-teal-500 text-white font-semibold flex items-center justify-center gap-2 hover:opacity-90 transition-opacity"
          >
            <Navigation className="w-5 h-5" />
            Get Directions
          </a>
        </motion.div>

        {/* Note */}
        <p className="text-center text-xs text-gray-600 mt-6">
          Please arrive 5 minutes before your scheduled time
        </p>
      </main>
    </div>
  );
}
