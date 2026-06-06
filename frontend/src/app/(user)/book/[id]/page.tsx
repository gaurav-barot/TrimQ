'use client';

import { useState } from 'react';
import { motion } from 'framer-motion';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { 
  ArrowLeft,
  Calendar,
  Clock,
  ChevronLeft,
  ChevronRight,
  Check,
  User,
  Scissors
} from 'lucide-react';

// Generate dates for next 7 days
const generateDates = () => {
  const dates = [];
  const today = new Date();
  for (let i = 0; i < 7; i++) {
    const date = new Date(today);
    date.setDate(today.getDate() + i);
    dates.push({
      date: date,
      day: date.toLocaleDateString('en-US', { weekday: 'short' }),
      dayNum: date.getDate(),
      month: date.toLocaleDateString('en-US', { month: 'short' }),
      isToday: i === 0
    });
  }
  return dates;
};

// Generate time slots
const generateSlots = () => {
  const slots = [];
  for (let hour = 10; hour < 21; hour++) {
    for (let min = 0; min < 60; min += 30) {
      const time = `${hour.toString().padStart(2, '0')}:${min.toString().padStart(2, '0')}`;
      const displayTime = new Date(2024, 0, 1, hour, min).toLocaleTimeString('en-US', { 
        hour: 'numeric', 
        minute: '2-digit',
        hour12: true 
      });
      slots.push({
        time,
        displayTime,
        available: Math.random() > 0.3, // Random availability for demo
        tokenNumber: Math.floor(Math.random() * 10) + 1
      });
    }
  }
  return slots;
};

const dates = generateDates();
const allSlots = generateSlots();

// Mock booking data
const bookingData = {
  shop: {
    id: 1,
    name: 'Royal Cuts Salon',
    address: 'MG Road, Koramangala'
  },
  services: [
    { id: 1, name: 'Classic Haircut', price: 300, duration: 30 },
    { id: 3, name: 'Beard Trim', price: 150, duration: 20 }
  ],
  staff: { id: 1, name: 'Rahul', image: '👨' }
};

export default function BookingPage() {
  const router = useRouter();
  const [selectedDate, setSelectedDate] = useState(dates[0]);
  const [selectedSlot, setSelectedSlot] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  const totalAmount = bookingData.services.reduce((sum, s) => sum + s.price, 0);
  const totalDuration = bookingData.services.reduce((sum, s) => sum + s.duration, 0);

  const handleBooking = async () => {
    if (!selectedSlot) return;
    
    setIsLoading(true);
    await new Promise(resolve => setTimeout(resolve, 1500));
    
    // Navigate to payment
    router.push('/payment');
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
        <div className="max-w-4xl mx-auto px-6 py-4">
          <div className="flex items-center justify-between">
            <button onClick={() => router.back()} className="flex items-center gap-2 text-gray-400 hover:text-white transition-colors">
              <ArrowLeft className="w-5 h-5" />
              <span>Back</span>
            </button>
            <h1 className="text-lg font-semibold text-white">Select Time Slot</h1>
            <div className="w-16"></div>
          </div>
        </div>
      </header>

      <main className="max-w-4xl mx-auto px-6 py-6 pb-32">
        {/* Shop Info */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="p-4 rounded-xl bg-white/5 border border-white/10 mb-6"
        >
          <div className="flex items-center gap-4">
            <div className="w-14 h-14 rounded-xl bg-gradient-to-br from-purple-500/20 to-teal-500/20 flex items-center justify-center">
              <Scissors className="w-6 h-6 text-purple-400" />
            </div>
            <div className="flex-1">
              <h2 className="font-semibold text-white">{bookingData.shop.name}</h2>
              <p className="text-sm text-gray-500">{bookingData.shop.address}</p>
            </div>
          </div>
        </motion.div>

        {/* Selected Services */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.1 }}
          className="mb-6"
        >
          <h3 className="text-sm font-medium text-gray-400 mb-3">Selected Services</h3>
          <div className="space-y-2">
            {bookingData.services.map(service => (
              <div key={service.id} className="flex items-center justify-between p-3 rounded-xl bg-white/5">
                <div>
                  <p className="text-white">{service.name}</p>
                  <p className="text-xs text-gray-500">{service.duration} mins</p>
                </div>
                <p className="font-medium text-white">₹{service.price}</p>
              </div>
            ))}
          </div>
        </motion.div>

        {/* Staff */}
        {bookingData.staff && (
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.15 }}
            className="mb-6"
          >
            <h3 className="text-sm font-medium text-gray-400 mb-3">Stylist</h3>
            <div className="flex items-center gap-3 p-3 rounded-xl bg-white/5">
              <div className="w-10 h-10 rounded-full bg-gradient-to-br from-purple-500/20 to-teal-500/20 flex items-center justify-center text-xl">
                {bookingData.staff.image}
              </div>
              <span className="text-white">{bookingData.staff.name}</span>
            </div>
          </motion.div>
        )}

        {/* Date Selection */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.2 }}
          className="mb-6"
        >
          <h3 className="text-sm font-medium text-gray-400 mb-3 flex items-center gap-2">
            <Calendar className="w-4 h-4" />
            Select Date
          </h3>
          <div className="flex gap-2 overflow-x-auto pb-2 scrollbar-hide">
            {dates.map((date, i) => (
              <button
                key={i}
                onClick={() => setSelectedDate(date)}
                className={`flex flex-col items-center p-3 rounded-xl min-w-[70px] transition-all ${
                  selectedDate === date
                    ? 'bg-gradient-to-br from-purple-500 to-teal-500 text-white'
                    : 'bg-white/5 text-gray-400 hover:bg-white/10'
                }`}
              >
                <span className="text-xs">{date.day}</span>
                <span className="text-xl font-bold">{date.dayNum}</span>
                <span className="text-xs">{date.month}</span>
                {date.isToday && (
                  <span className="text-xs mt-1 px-2 py-0.5 rounded-full bg-white/20">Today</span>
                )}
              </button>
            ))}
          </div>
        </motion.div>

        {/* Time Slots */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.25 }}
        >
          <h3 className="text-sm font-medium text-gray-400 mb-3 flex items-center gap-2">
            <Clock className="w-4 h-4" />
            Select Time ({totalDuration} mins required)
          </h3>
          
          {/* Morning */}
          <div className="mb-4">
            <p className="text-xs text-gray-500 mb-2">Morning</p>
            <div className="grid grid-cols-4 sm:grid-cols-6 gap-2">
              {allSlots.filter(s => parseInt(s.time) < 12).map(slot => (
                <button
                  key={slot.time}
                  onClick={() => slot.available && setSelectedSlot(slot.time)}
                  disabled={!slot.available}
                  className={`py-2.5 px-3 rounded-xl text-sm font-medium transition-all ${
                    selectedSlot === slot.time
                      ? 'bg-gradient-to-r from-purple-500 to-teal-500 text-white'
                      : slot.available
                      ? 'bg-white/5 text-gray-300 hover:bg-white/10'
                      : 'bg-white/5 text-gray-600 cursor-not-allowed line-through'
                  }`}
                >
                  {slot.displayTime}
                </button>
              ))}
            </div>
          </div>

          {/* Afternoon */}
          <div className="mb-4">
            <p className="text-xs text-gray-500 mb-2">Afternoon</p>
            <div className="grid grid-cols-4 sm:grid-cols-6 gap-2">
              {allSlots.filter(s => parseInt(s.time) >= 12 && parseInt(s.time) < 17).map(slot => (
                <button
                  key={slot.time}
                  onClick={() => slot.available && setSelectedSlot(slot.time)}
                  disabled={!slot.available}
                  className={`py-2.5 px-3 rounded-xl text-sm font-medium transition-all ${
                    selectedSlot === slot.time
                      ? 'bg-gradient-to-r from-purple-500 to-teal-500 text-white'
                      : slot.available
                      ? 'bg-white/5 text-gray-300 hover:bg-white/10'
                      : 'bg-white/5 text-gray-600 cursor-not-allowed line-through'
                  }`}
                >
                  {slot.displayTime}
                </button>
              ))}
            </div>
          </div>

          {/* Evening */}
          <div>
            <p className="text-xs text-gray-500 mb-2">Evening</p>
            <div className="grid grid-cols-4 sm:grid-cols-6 gap-2">
              {allSlots.filter(s => parseInt(s.time) >= 17).map(slot => (
                <button
                  key={slot.time}
                  onClick={() => slot.available && setSelectedSlot(slot.time)}
                  disabled={!slot.available}
                  className={`py-2.5 px-3 rounded-xl text-sm font-medium transition-all ${
                    selectedSlot === slot.time
                      ? 'bg-gradient-to-r from-purple-500 to-teal-500 text-white'
                      : slot.available
                      ? 'bg-white/5 text-gray-300 hover:bg-white/10'
                      : 'bg-white/5 text-gray-600 cursor-not-allowed line-through'
                  }`}
                >
                  {slot.displayTime}
                </button>
              ))}
            </div>
          </div>
        </motion.div>
      </main>

      {/* Bottom Bar */}
      <div className="fixed bottom-0 left-0 right-0 p-4 backdrop-blur-xl bg-black/80 border-t border-white/10">
        <div className="max-w-4xl mx-auto">
          <div className="flex items-center justify-between mb-3">
            <div>
              <p className="text-sm text-gray-400">Total Amount</p>
              <p className="text-2xl font-bold text-white">₹{totalAmount}</p>
            </div>
            {selectedSlot && (
              <div className="text-right">
                <p className="text-sm text-gray-400">Selected Slot</p>
                <p className="text-lg font-semibold text-purple-400">
                  {selectedDate.day}, {selectedDate.dayNum} {selectedDate.month} • {allSlots.find(s => s.time === selectedSlot)?.displayTime}
                </p>
              </div>
            )}
          </div>
          <button
            onClick={handleBooking}
            disabled={!selectedSlot || isLoading}
            className="w-full py-4 rounded-xl bg-gradient-to-r from-purple-500 to-teal-500 text-white font-semibold flex items-center justify-center gap-2 hover:opacity-90 transition-opacity disabled:opacity-50"
          >
            {isLoading ? (
              <div className="w-5 h-5 border-2 border-white/30 border-t-white rounded-full animate-spin" />
            ) : (
              <>
                Proceed to Payment
                <ChevronRight className="w-5 h-5" />
              </>
            )}
          </button>
        </div>
      </div>
    </div>
  );
}
