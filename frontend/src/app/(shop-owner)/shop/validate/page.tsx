'use client';

import { useState } from 'react';
import { motion } from 'framer-motion';
import Link from 'next/link';
import { 
  ArrowLeft,
  QrCode,
  Camera,
  CheckCircle,
  XCircle,
  User,
  Calendar,
  Clock,
  Scissors,
  AlertCircle
} from 'lucide-react';

// Mock validation result
const mockValidation = {
  valid: true,
  booking: {
    passCode: 'TQ-20260102-A7X9',
    tokenNumber: 5,
    customer: {
      name: 'Saurabh Kumar',
      phone: '+91 98765 43210'
    },
    date: 'January 2, 2026',
    time: '11:30 AM',
    services: ['Classic Haircut', 'Beard Trim'],
    staff: 'Rahul',
    amount: 460,
    status: 'CONFIRMED'
  }
};

export default function ValidatePassPage() {
  const [passCode, setPassCode] = useState('');
  const [isScanning, setIsScanning] = useState(false);
  const [validationResult, setValidationResult] = useState<typeof mockValidation | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');

  const handleValidate = async () => {
    if (!passCode.trim()) return;
    
    setIsLoading(true);
    setError('');
    
    await new Promise(resolve => setTimeout(resolve, 1000));
    
    // Mock validation
    if (passCode.toUpperCase().startsWith('TQ-')) {
      setValidationResult(mockValidation);
    } else {
      setError('Invalid pass code. Please check and try again.');
      setValidationResult(null);
    }
    
    setIsLoading(false);
  };

  const handleStartService = async () => {
    setIsLoading(true);
    await new Promise(resolve => setTimeout(resolve, 1000));
    // TODO: Call API to mark as started
    setValidationResult(null);
    setPassCode('');
    setIsLoading(false);
  };

  const handleReset = () => {
    setValidationResult(null);
    setPassCode('');
    setError('');
  };

  return (
    <div className="min-h-screen bg-[#0a0a0f]">
      <div className="fixed inset-0 -z-10">
        <div className="absolute inset-0 bg-gradient-to-br from-purple-900/20 via-transparent to-teal-900/20" />
        <div className="absolute top-1/4 left-1/4 w-96 h-96 bg-purple-500/20 rounded-full blur-[128px]" />
      </div>

      <header className="sticky top-0 z-50 backdrop-blur-xl bg-black/40 border-b border-white/5">
        <div className="max-w-lg mx-auto px-6 py-4">
          <div className="flex items-center justify-between">
            <Link href="/shop/dashboard" className="flex items-center gap-2 text-gray-400 hover:text-white transition-colors">
              <ArrowLeft className="w-5 h-5" />
              <span>Dashboard</span>
            </Link>
            <h1 className="text-lg font-semibold text-white">Validate Pass</h1>
            <div className="w-16"></div>
          </div>
        </div>
      </header>

      <main className="max-w-lg mx-auto px-6 py-8">
        {!validationResult ? (
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
          >
            {/* Scanner Placeholder */}
            <div className="relative aspect-square rounded-3xl bg-white/5 border-2 border-dashed border-white/20 mb-6 overflow-hidden flex items-center justify-center">
              {isScanning ? (
                <div className="text-center">
                  <div className="w-16 h-16 border-4 border-purple-500/30 border-t-purple-500 rounded-full animate-spin mx-auto mb-4" />
                  <p className="text-gray-400">Scanning...</p>
                </div>
              ) : (
                <div className="text-center">
                  <QrCode className="w-20 h-20 text-gray-600 mx-auto mb-4" />
                  <p className="text-gray-400 mb-2">Scan customer's QR code</p>
                  <button
                    onClick={() => setIsScanning(true)}
                    className="px-6 py-2 rounded-xl bg-purple-500 text-white font-medium flex items-center gap-2 mx-auto"
                  >
                    <Camera className="w-5 h-5" />
                    Open Scanner
                  </button>
                </div>
              )}
            </div>

            {/* Divider */}
            <div className="relative my-6">
              <div className="absolute inset-0 flex items-center">
                <div className="w-full border-t border-white/10"></div>
              </div>
              <div className="relative flex justify-center">
                <span className="px-4 bg-[#0a0a0f] text-gray-500 text-sm">or enter manually</span>
              </div>
            </div>

            {/* Manual Entry */}
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-400 mb-2">Pass Code</label>
                <input
                  type="text"
                  value={passCode}
                  onChange={(e) => setPassCode(e.target.value.toUpperCase())}
                  placeholder="e.g., TQ-20260102-A7X9"
                  className="w-full px-4 py-3.5 rounded-xl bg-white/5 border border-white/10 text-white placeholder-gray-500 focus:border-purple-500 outline-none font-mono text-lg tracking-wider text-center"
                />
              </div>

              {error && (
                <div className="p-4 rounded-xl bg-red-500/10 border border-red-500/20 flex items-center gap-3">
                  <AlertCircle className="w-5 h-5 text-red-400 shrink-0" />
                  <p className="text-sm text-red-400">{error}</p>
                </div>
              )}

              <button
                onClick={handleValidate}
                disabled={!passCode.trim() || isLoading}
                className="w-full py-4 rounded-xl bg-gradient-to-r from-purple-500 to-teal-500 text-white font-semibold flex items-center justify-center gap-2 hover:opacity-90 transition-opacity disabled:opacity-50"
              >
                {isLoading ? (
                  <div className="w-5 h-5 border-2 border-white/30 border-t-white rounded-full animate-spin" />
                ) : (
                  <>
                    <CheckCircle className="w-5 h-5" />
                    Validate Pass
                  </>
                )}
              </button>
            </div>
          </motion.div>
        ) : (
          <motion.div
            initial={{ opacity: 0, scale: 0.95 }}
            animate={{ opacity: 1, scale: 1 }}
          >
            {/* Success Banner */}
            <div className="text-center mb-6">
              <div className="w-20 h-20 rounded-full bg-green-500/20 flex items-center justify-center mx-auto mb-4">
                <CheckCircle className="w-10 h-10 text-green-400" />
              </div>
              <h2 className="text-2xl font-bold text-white mb-1">Valid Pass</h2>
              <p className="text-gray-400">Booking verified successfully</p>
            </div>

            {/* Booking Details Card */}
            <div className="p-6 rounded-2xl bg-white/5 border border-white/10 mb-6">
              <div className="flex items-center justify-between mb-4">
                <span className="text-sm text-gray-500">Token Number</span>
                <span className="text-4xl font-bold text-purple-400">#{validationResult.booking.tokenNumber}</span>
              </div>

              <div className="space-y-4">
                <div className="flex items-center gap-3">
                  <User className="w-5 h-5 text-gray-500" />
                  <div>
                    <p className="text-white font-medium">{validationResult.booking.customer.name}</p>
                    <p className="text-sm text-gray-500">{validationResult.booking.customer.phone}</p>
                  </div>
                </div>

                <div className="flex gap-4">
                  <div className="flex items-center gap-2">
                    <Calendar className="w-4 h-4 text-gray-500" />
                    <span className="text-sm text-gray-400">{validationResult.booking.date}</span>
                  </div>
                  <div className="flex items-center gap-2">
                    <Clock className="w-4 h-4 text-gray-500" />
                    <span className="text-sm text-gray-400">{validationResult.booking.time}</span>
                  </div>
                </div>

                <div>
                  <p className="text-xs text-gray-500 mb-2">Services</p>
                  <div className="flex flex-wrap gap-2">
                    {validationResult.booking.services.map((service, i) => (
                      <span key={i} className="px-3 py-1 rounded-full bg-purple-500/10 text-purple-400 text-sm">
                        {service}
                      </span>
                    ))}
                  </div>
                </div>

                <div className="flex items-center justify-between pt-4 border-t border-white/10">
                  <div>
                    <p className="text-xs text-gray-500">Assigned to</p>
                    <p className="text-white">{validationResult.booking.staff}</p>
                  </div>
                  <div className="text-right">
                    <p className="text-xs text-gray-500">Amount</p>
                    <p className="text-xl font-bold text-white">₹{validationResult.booking.amount}</p>
                  </div>
                </div>
              </div>
            </div>

            {/* Actions */}
            <div className="space-y-3">
              <button
                onClick={handleStartService}
                disabled={isLoading}
                className="w-full py-4 rounded-xl bg-gradient-to-r from-purple-500 to-teal-500 text-white font-semibold flex items-center justify-center gap-2 hover:opacity-90 transition-opacity disabled:opacity-50"
              >
                {isLoading ? (
                  <div className="w-5 h-5 border-2 border-white/30 border-t-white rounded-full animate-spin" />
                ) : (
                  <>
                    <Scissors className="w-5 h-5" />
                    Start Service
                  </>
                )}
              </button>

              <button
                onClick={handleReset}
                className="w-full py-3 rounded-xl bg-white/5 border border-white/10 text-gray-300 font-medium hover:bg-white/10 transition-colors"
              >
                Scan Another
              </button>
            </div>
          </motion.div>
        )}
      </main>
    </div>
  );
}
