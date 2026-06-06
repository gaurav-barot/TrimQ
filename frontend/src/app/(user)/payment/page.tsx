'use client';

import { useState } from 'react';
import { motion } from 'framer-motion';
import { useRouter } from 'next/navigation';
import { 
  ArrowLeft,
  CreditCard,
  Smartphone,
  Wallet,
  Building,
  ChevronRight,
  Shield,
  Lock,
  Check
} from 'lucide-react';

// Mock booking summary
const bookingSummary = {
  shop: 'Royal Cuts Salon',
  date: 'Today, Jan 2',
  time: '11:30 AM',
  services: [
    { name: 'Classic Haircut', price: 300 },
    { name: 'Beard Trim', price: 150 }
  ],
  stylist: 'Rahul',
  subtotal: 450,
  platformFee: 10,
  discount: 0,
  total: 460
};

const paymentMethods = [
  { id: 'upi', name: 'UPI', icon: Smartphone, description: 'GPay, PhonePe, Paytm' },
  { id: 'card', name: 'Credit/Debit Card', icon: CreditCard, description: 'Visa, Mastercard, RuPay' },
  { id: 'wallet', name: 'Wallets', icon: Wallet, description: 'Paytm, Amazon Pay' },
  { id: 'netbanking', name: 'Net Banking', icon: Building, description: 'All major banks' },
];

export default function PaymentPage() {
  const router = useRouter();
  const [selectedMethod, setSelectedMethod] = useState('upi');
  const [upiId, setUpiId] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [couponCode, setCouponCode] = useState('');

  const handlePayment = async () => {
    setIsLoading(true);
    
    // Simulate payment processing
    await new Promise(resolve => setTimeout(resolve, 2000));
    
    // Random success/failure for demo
    const success = Math.random() > 0.2;
    
    if (success) {
      router.push('/booking/success');
    } else {
      router.push('/booking/failed');
    }
  };

  return (
    <div className="min-h-screen bg-[#0a0a0f]">
      {/* Background */}
      <div className="fixed inset-0 -z-10">
        <div className="absolute top-0 right-0 w-96 h-96 bg-purple-500/10 rounded-full blur-[128px]" />
        <div className="absolute bottom-0 left-0 w-96 h-96 bg-teal-500/10 rounded-full blur-[128px]" />
      </div>

      {/* Header */}
      <header className="sticky top-0 z-50 backdrop-blur-xl bg-black/40 border-b border-white/5">
        <div className="max-w-2xl mx-auto px-6 py-4">
          <div className="flex items-center justify-between">
            <button onClick={() => router.back()} className="flex items-center gap-2 text-gray-400 hover:text-white transition-colors">
              <ArrowLeft className="w-5 h-5" />
              <span>Back</span>
            </button>
            <h1 className="text-lg font-semibold text-white">Payment</h1>
            <div className="flex items-center gap-1 text-green-400 text-sm">
              <Lock className="w-4 h-4" />
              <span className="hidden sm:inline">Secure</span>
            </div>
          </div>
        </div>
      </header>

      <main className="max-w-2xl mx-auto px-6 py-6 pb-32">
        {/* Booking Summary */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="p-5 rounded-2xl bg-white/5 border border-white/10 mb-6"
        >
          <h2 className="font-semibold text-white mb-4">Booking Summary</h2>
          
          <div className="space-y-3 mb-4">
            <div className="flex justify-between text-sm">
              <span className="text-gray-400">Shop</span>
              <span className="text-white">{bookingSummary.shop}</span>
            </div>
            <div className="flex justify-between text-sm">
              <span className="text-gray-400">Date & Time</span>
              <span className="text-white">{bookingSummary.date} • {bookingSummary.time}</span>
            </div>
            <div className="flex justify-between text-sm">
              <span className="text-gray-400">Stylist</span>
              <span className="text-white">{bookingSummary.stylist}</span>
            </div>
          </div>

          <div className="border-t border-white/10 pt-3 space-y-2">
            {bookingSummary.services.map((service, i) => (
              <div key={i} className="flex justify-between text-sm">
                <span className="text-gray-400">{service.name}</span>
                <span className="text-white">₹{service.price}</span>
              </div>
            ))}
          </div>

          <div className="border-t border-white/10 mt-3 pt-3 space-y-2">
            <div className="flex justify-between text-sm">
              <span className="text-gray-400">Subtotal</span>
              <span className="text-white">₹{bookingSummary.subtotal}</span>
            </div>
            <div className="flex justify-between text-sm">
              <span className="text-gray-400">Platform Fee</span>
              <span className="text-white">₹{bookingSummary.platformFee}</span>
            </div>
            {bookingSummary.discount > 0 && (
              <div className="flex justify-between text-sm">
                <span className="text-green-400">Discount</span>
                <span className="text-green-400">-₹{bookingSummary.discount}</span>
              </div>
            )}
          </div>

          <div className="border-t border-white/10 mt-3 pt-3">
            <div className="flex justify-between">
              <span className="font-semibold text-white">Total</span>
              <span className="text-xl font-bold text-white">₹{bookingSummary.total}</span>
            </div>
          </div>
        </motion.div>

        {/* Coupon Code */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.1 }}
          className="mb-6"
        >
          <div className="flex gap-3">
            <input
              type="text"
              value={couponCode}
              onChange={(e) => setCouponCode(e.target.value.toUpperCase())}
              placeholder="Enter coupon code"
              className="flex-1 px-4 py-3 rounded-xl bg-white/5 border border-white/10 text-white placeholder-gray-500 focus:border-purple-500 outline-none transition-all"
            />
            <button className="px-5 py-3 rounded-xl bg-white/5 border border-white/10 text-purple-400 font-medium hover:bg-white/10 transition-all">
              Apply
            </button>
          </div>
        </motion.div>

        {/* Payment Methods */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.15 }}
        >
          <h3 className="text-sm font-medium text-gray-400 mb-3">Payment Method</h3>
          <div className="space-y-3">
            {paymentMethods.map(method => (
              <div
                key={method.id}
                onClick={() => setSelectedMethod(method.id)}
                className={`p-4 rounded-xl border cursor-pointer transition-all ${
                  selectedMethod === method.id
                    ? 'bg-purple-500/10 border-purple-500'
                    : 'bg-white/5 border-white/10 hover:border-white/20'
                }`}
              >
                <div className="flex items-center gap-4">
                  <div className={`w-12 h-12 rounded-xl flex items-center justify-center ${
                    selectedMethod === method.id ? 'bg-purple-500' : 'bg-white/10'
                  }`}>
                    <method.icon className="w-6 h-6 text-white" />
                  </div>
                  <div className="flex-1">
                    <p className="font-medium text-white">{method.name}</p>
                    <p className="text-sm text-gray-500">{method.description}</p>
                  </div>
                  <div className={`w-5 h-5 rounded-full flex items-center justify-center ${
                    selectedMethod === method.id ? 'bg-purple-500' : 'bg-white/10'
                  }`}>
                    {selectedMethod === method.id && <Check className="w-3 h-3 text-white" />}
                  </div>
                </div>

                {/* UPI Input */}
                {method.id === 'upi' && selectedMethod === 'upi' && (
                  <div className="mt-4 pt-4 border-t border-white/10">
                    <input
                      type="text"
                      value={upiId}
                      onChange={(e) => setUpiId(e.target.value)}
                      placeholder="Enter UPI ID (e.g., name@upi)"
                      className="w-full px-4 py-3 rounded-xl bg-black/30 border border-white/10 text-white placeholder-gray-500 focus:border-purple-500 outline-none transition-all"
                    />
                  </div>
                )}
              </div>
            ))}
          </div>
        </motion.div>

        {/* Security Badge */}
        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ delay: 0.3 }}
          className="flex items-center justify-center gap-2 mt-8 text-sm text-gray-500"
        >
          <Shield className="w-4 h-4" />
          <span>Secured by Razorpay • 256-bit SSL encryption</span>
        </motion.div>
      </main>

      {/* Bottom Bar */}
      <div className="fixed bottom-0 left-0 right-0 p-4 backdrop-blur-xl bg-black/80 border-t border-white/10">
        <div className="max-w-2xl mx-auto">
          <button
            onClick={handlePayment}
            disabled={isLoading || (selectedMethod === 'upi' && !upiId)}
            className="w-full py-4 rounded-xl bg-gradient-to-r from-purple-500 to-teal-500 text-white font-semibold flex items-center justify-center gap-2 hover:opacity-90 transition-opacity disabled:opacity-50"
          >
            {isLoading ? (
              <>
                <div className="w-5 h-5 border-2 border-white/30 border-t-white rounded-full animate-spin" />
                Processing...
              </>
            ) : (
              <>
                Pay ₹{bookingSummary.total}
                <ChevronRight className="w-5 h-5" />
              </>
            )}
          </button>
        </div>
      </div>
    </div>
  );
}
