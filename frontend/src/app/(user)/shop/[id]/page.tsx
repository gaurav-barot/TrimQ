'use client';

import { useState } from 'react';
import { motion } from 'framer-motion';
import Link from 'next/link';
import { 
  ArrowLeft,
  Star, 
  Clock, 
  MapPin, 
  Phone,
  Calendar,
  ChevronRight,
  Check,
  Users,
  Scissors,
  Share2,
  Heart
} from 'lucide-react';

// Mock shop data
const shop = {
  id: 1,
  name: 'Royal Cuts Salon',
  image: '✂️',
  rating: 4.8,
  reviews: 234,
  address: '123, MG Road, Koramangala, Bangalore - 560034',
  phone: '+91 98765 43210',
  openHours: '10:00 AM - 9:00 PM',
  openNow: true,
  description: 'Premium salon offering the finest grooming experience in Bangalore. Our expert stylists bring years of experience to give you the perfect look.',
  amenities: ['AC', 'WiFi', 'Parking', 'Card Payment'],
  gallery: ['🏠', '💇', '✂️', '💈'],
};

const services = [
  { id: 1, name: 'Classic Haircut', price: 300, duration: 30, popular: true },
  { id: 2, name: 'Premium Haircut', price: 450, duration: 45, popular: true },
  { id: 3, name: 'Beard Trim', price: 150, duration: 20, popular: false },
  { id: 4, name: 'Beard Styling', price: 250, duration: 30, popular: false },
  { id: 5, name: 'Clean Shave', price: 200, duration: 25, popular: false },
  { id: 6, name: 'Hair Color', price: 800, duration: 90, popular: true },
  { id: 7, name: 'Hair Spa', price: 600, duration: 60, popular: false },
  { id: 8, name: 'Head Massage', price: 300, duration: 30, popular: false },
];

const staff = [
  { id: 1, name: 'Rahul', role: 'Senior Stylist', rating: 4.9, image: '👨' },
  { id: 2, name: 'Amit', role: 'Hair Expert', rating: 4.7, image: '🧔' },
  { id: 3, name: 'Vijay', role: 'Barber', rating: 4.6, image: '👤' },
];

const reviews = [
  { id: 1, user: 'Priya S.', rating: 5, date: '2 days ago', comment: 'Excellent service! Rahul is an amazing stylist.' },
  { id: 2, user: 'Kiran M.', rating: 4, date: '1 week ago', comment: 'Good haircut, reasonable prices. Will visit again.' },
  { id: 3, user: 'Arjun K.', rating: 5, date: '2 weeks ago', comment: 'Best salon in Koramangala. Highly recommended!' },
];

export default function ShopDetailPage() {
  const [selectedServices, setSelectedServices] = useState<number[]>([]);
  const [selectedStaff, setSelectedStaff] = useState<number | null>(null);
  const [activeTab, setActiveTab] = useState('services');

  const toggleService = (serviceId: number) => {
    setSelectedServices(prev => 
      prev.includes(serviceId) 
        ? prev.filter(id => id !== serviceId)
        : [...prev, serviceId]
    );
  };

  const totalAmount = services
    .filter(s => selectedServices.includes(s.id))
    .reduce((sum, s) => sum + s.price, 0);

  const totalDuration = services
    .filter(s => selectedServices.includes(s.id))
    .reduce((sum, s) => sum + s.duration, 0);

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
            <Link href="/search" className="flex items-center gap-2 text-gray-400 hover:text-white transition-colors">
              <ArrowLeft className="w-5 h-5" />
              <span className="hidden sm:inline">Back</span>
            </Link>
            <div className="flex items-center gap-3">
              <button className="p-2.5 rounded-xl bg-white/5 hover:bg-white/10 transition-colors">
                <Share2 className="w-5 h-5 text-gray-400" />
              </button>
              <button className="p-2.5 rounded-xl bg-white/5 hover:bg-white/10 transition-colors">
                <Heart className="w-5 h-5 text-gray-400" />
              </button>
            </div>
          </div>
        </div>
      </header>

      <main className="max-w-4xl mx-auto px-6 py-6">
        {/* Shop Header */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="mb-8"
        >
          <div className="flex gap-5">
            <div className="w-24 h-24 rounded-2xl bg-gradient-to-br from-purple-500/20 to-teal-500/20 flex items-center justify-center text-5xl shrink-0">
              {shop.image}
            </div>
            <div className="flex-1">
              <h1 className="text-2xl md:text-3xl font-bold text-white mb-2">{shop.name}</h1>
              <div className="flex flex-wrap items-center gap-3 text-sm">
                <div className="flex items-center gap-1">
                  <Star className="w-4 h-4 text-yellow-400 fill-yellow-400" />
                  <span className="text-white font-medium">{shop.rating}</span>
                  <span className="text-gray-500">({shop.reviews} reviews)</span>
                </div>
                <span className={`px-2 py-0.5 rounded-full text-xs font-medium ${
                  shop.openNow ? 'bg-green-500/20 text-green-400' : 'bg-red-500/20 text-red-400'
                }`}>
                  {shop.openNow ? 'Open Now' : 'Closed'}
                </span>
              </div>
            </div>
          </div>

          {/* Quick Info */}
          <div className="mt-6 grid grid-cols-1 sm:grid-cols-3 gap-3">
            <div className="flex items-center gap-3 p-3 rounded-xl bg-white/5">
              <MapPin className="w-5 h-5 text-purple-400 shrink-0" />
              <span className="text-sm text-gray-400 truncate">{shop.address}</span>
            </div>
            <div className="flex items-center gap-3 p-3 rounded-xl bg-white/5">
              <Clock className="w-5 h-5 text-teal-400 shrink-0" />
              <span className="text-sm text-gray-400">{shop.openHours}</span>
            </div>
            <div className="flex items-center gap-3 p-3 rounded-xl bg-white/5">
              <Phone className="w-5 h-5 text-pink-400 shrink-0" />
              <span className="text-sm text-gray-400">{shop.phone}</span>
            </div>
          </div>

          {/* Amenities */}
          <div className="mt-4 flex flex-wrap gap-2">
            {shop.amenities.map(amenity => (
              <span key={amenity} className="px-3 py-1 rounded-full bg-white/5 text-xs text-gray-400">
                {amenity}
              </span>
            ))}
          </div>
        </motion.div>

        {/* Tabs */}
        <div className="flex gap-1 p-1 rounded-xl bg-white/5 mb-6">
          {['services', 'staff', 'reviews', 'about'].map(tab => (
            <button
              key={tab}
              onClick={() => setActiveTab(tab)}
              className={`flex-1 py-2.5 rounded-lg text-sm font-medium capitalize transition-all ${
                activeTab === tab
                  ? 'bg-gradient-to-r from-purple-500 to-teal-500 text-white'
                  : 'text-gray-400 hover:text-white'
              }`}
            >
              {tab}
            </button>
          ))}
        </div>

        {/* Services Tab */}
        {activeTab === 'services' && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            className="space-y-3"
          >
            {services.map(service => (
              <div
                key={service.id}
                onClick={() => toggleService(service.id)}
                className={`p-4 rounded-xl border cursor-pointer transition-all ${
                  selectedServices.includes(service.id)
                    ? 'bg-purple-500/10 border-purple-500'
                    : 'bg-white/5 border-white/10 hover:border-white/20'
                }`}
              >
                <div className="flex items-center justify-between">
                  <div className="flex-1">
                    <div className="flex items-center gap-2">
                      <h4 className="font-medium text-white">{service.name}</h4>
                      {service.popular && (
                        <span className="px-2 py-0.5 rounded-full bg-yellow-500/20 text-yellow-400 text-xs">
                          Popular
                        </span>
                      )}
                    </div>
                    <div className="flex items-center gap-3 text-sm text-gray-500 mt-1">
                      <span>{service.duration} mins</span>
                    </div>
                  </div>
                  <div className="flex items-center gap-4">
                    <span className="text-lg font-semibold text-white">₹{service.price}</span>
                    <div className={`w-6 h-6 rounded-full flex items-center justify-center transition-all ${
                      selectedServices.includes(service.id)
                        ? 'bg-purple-500'
                        : 'bg-white/10'
                    }`}>
                      {selectedServices.includes(service.id) && (
                        <Check className="w-4 h-4 text-white" />
                      )}
                    </div>
                  </div>
                </div>
              </div>
            ))}
          </motion.div>
        )}

        {/* Staff Tab */}
        {activeTab === 'staff' && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            className="grid grid-cols-1 sm:grid-cols-3 gap-4"
          >
            {staff.map(member => (
              <div
                key={member.id}
                onClick={() => setSelectedStaff(member.id)}
                className={`p-5 rounded-xl border cursor-pointer transition-all text-center ${
                  selectedStaff === member.id
                    ? 'bg-purple-500/10 border-purple-500'
                    : 'bg-white/5 border-white/10 hover:border-white/20'
                }`}
              >
                <div className="w-16 h-16 rounded-full bg-gradient-to-br from-purple-500/20 to-teal-500/20 flex items-center justify-center text-3xl mx-auto mb-3">
                  {member.image}
                </div>
                <h4 className="font-medium text-white">{member.name}</h4>
                <p className="text-sm text-gray-500">{member.role}</p>
                <div className="flex items-center justify-center gap-1 mt-2">
                  <Star className="w-4 h-4 text-yellow-400 fill-yellow-400" />
                  <span className="text-sm text-white">{member.rating}</span>
                </div>
              </div>
            ))}
          </motion.div>
        )}

        {/* Reviews Tab */}
        {activeTab === 'reviews' && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            className="space-y-4"
          >
            {reviews.map(review => (
              <div key={review.id} className="p-4 rounded-xl bg-white/5 border border-white/10">
                <div className="flex items-center justify-between mb-2">
                  <div className="flex items-center gap-2">
                    <div className="w-8 h-8 rounded-full bg-gradient-to-br from-purple-500 to-teal-500 flex items-center justify-center text-sm font-bold text-white">
                      {review.user[0]}
                    </div>
                    <span className="font-medium text-white">{review.user}</span>
                  </div>
                  <span className="text-xs text-gray-500">{review.date}</span>
                </div>
                <div className="flex items-center gap-1 mb-2">
                  {[...Array(5)].map((_, i) => (
                    <Star 
                      key={i}
                      className={`w-4 h-4 ${i < review.rating ? 'text-yellow-400 fill-yellow-400' : 'text-gray-600'}`}
                    />
                  ))}
                </div>
                <p className="text-gray-400 text-sm">{review.comment}</p>
              </div>
            ))}
          </motion.div>
        )}

        {/* About Tab */}
        {activeTab === 'about' && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            className="space-y-6"
          >
            <div>
              <h3 className="text-lg font-semibold text-white mb-3">About</h3>
              <p className="text-gray-400">{shop.description}</p>
            </div>
            <div>
              <h3 className="text-lg font-semibold text-white mb-3">Gallery</h3>
              <div className="grid grid-cols-4 gap-3">
                {shop.gallery.map((img, i) => (
                  <div key={i} className="aspect-square rounded-xl bg-white/5 flex items-center justify-center text-4xl">
                    {img}
                  </div>
                ))}
              </div>
            </div>
          </motion.div>
        )}
      </main>

      {/* Bottom Bar - Book Now */}
      {selectedServices.length > 0 && (
        <motion.div
          initial={{ y: 100 }}
          animate={{ y: 0 }}
          className="fixed bottom-0 left-0 right-0 p-4 backdrop-blur-xl bg-black/80 border-t border-white/10"
        >
          <div className="max-w-4xl mx-auto flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-400">
                {selectedServices.length} service{selectedServices.length > 1 ? 's' : ''} • {totalDuration} mins
              </p>
              <p className="text-2xl font-bold text-white">₹{totalAmount}</p>
            </div>
            <Link
              href={`/book/${shop.id}?services=${selectedServices.join(',')}&staff=${selectedStaff || ''}`}
              className="px-8 py-3.5 rounded-xl bg-gradient-to-r from-purple-500 to-teal-500 text-white font-semibold flex items-center gap-2 hover:opacity-90 transition-opacity"
            >
              <Calendar className="w-5 h-5" />
              Book Now
            </Link>
          </div>
        </motion.div>
      )}
    </div>
  );
}
