'use client';

import { useState } from 'react';
import { motion } from 'framer-motion';
import Link from 'next/link';
import { 
  Search, 
  MapPin, 
  Star, 
  Clock, 
  Filter,
  ChevronDown,
  X,
  Sliders,
  Scissors
} from 'lucide-react';

// Mock data
const salons = [
  {
    id: 1,
    name: 'Royal Cuts Salon',
    image: '✂️',
    rating: 4.8,
    reviews: 234,
    address: 'MG Road, Koramangala',
    city: 'Bangalore',
    distance: '0.5 km',
    openNow: true,
    nextSlot: '10:30 AM',
    priceRange: '₹300 - ₹800',
    services: ['Haircut', 'Beard', 'Hair Color', 'Spa'],
    tags: ['Premium', 'AC']
  },
  {
    id: 2,
    name: 'The Barber Shop',
    image: '💈',
    rating: 4.6,
    reviews: 189,
    address: 'HSR Layout',
    city: 'Bangalore',
    distance: '1.2 km',
    openNow: true,
    nextSlot: '11:00 AM',
    priceRange: '₹200 - ₹500',
    services: ['Haircut', 'Shave', 'Massage'],
    tags: ['Budget Friendly']
  },
  {
    id: 3,
    name: 'Style Masters',
    image: '💇',
    rating: 4.9,
    reviews: 412,
    address: 'Indiranagar',
    city: 'Bangalore',
    distance: '2.0 km',
    openNow: true,
    nextSlot: '10:00 AM',
    priceRange: '₹400 - ₹1200',
    services: ['Haircut', 'Styling', 'Hair Color', 'Treatment'],
    tags: ['Luxury', 'Unisex']
  },
  {
    id: 4,
    name: 'Gentlemen\'s Hub',
    image: '🧔',
    rating: 4.5,
    reviews: 156,
    address: 'Whitefield',
    city: 'Bangalore',
    distance: '3.5 km',
    openNow: false,
    nextSlot: 'Tomorrow',
    priceRange: '₹250 - ₹600',
    services: ['Haircut', 'Beard', 'Facial'],
    tags: ['Men Only']
  },
  {
    id: 5,
    name: 'Urban Cuts',
    image: '💇‍♂️',
    rating: 4.7,
    reviews: 298,
    address: 'JP Nagar',
    city: 'Bangalore',
    distance: '4.0 km',
    openNow: true,
    nextSlot: '11:30 AM',
    priceRange: '₹300 - ₹700',
    services: ['Haircut', 'Color', 'Keratin'],
    tags: ['Trending']
  }
];

const serviceFilters = ['All', 'Haircut', 'Beard', 'Hair Color', 'Spa', 'Massage', 'Facial'];
const sortOptions = ['Nearest', 'Highest Rated', 'Price: Low to High', 'Price: High to Low'];

export default function SearchPage() {
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedService, setSelectedService] = useState('All');
  const [sortBy, setSortBy] = useState('Nearest');
  const [showFilters, setShowFilters] = useState(false);
  const [priceRange, setPriceRange] = useState([0, 2000]);
  const [minRating, setMinRating] = useState(0);
  const [openNowOnly, setOpenNowOnly] = useState(false);

  const filteredSalons = salons.filter(salon => {
    if (searchQuery && !salon.name.toLowerCase().includes(searchQuery.toLowerCase())) return false;
    if (selectedService !== 'All' && !salon.services.includes(selectedService)) return false;
    if (openNowOnly && !salon.openNow) return false;
    return true;
  });

  return (
    <div className="min-h-screen bg-[#0a0a0f]">
      {/* Background */}
      <div className="fixed inset-0 -z-10">
        <div className="absolute top-0 right-0 w-96 h-96 bg-purple-500/10 rounded-full blur-[128px]" />
        <div className="absolute bottom-0 left-0 w-96 h-96 bg-teal-500/10 rounded-full blur-[128px]" />
      </div>

      {/* Header */}
      <header className="sticky top-0 z-50 backdrop-blur-xl bg-black/40 border-b border-white/5">
        <div className="max-w-7xl mx-auto px-6 py-4">
          <div className="flex items-center gap-4">
            <Link href="/" className="flex items-center gap-2">
              <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-purple-500 to-teal-500 flex items-center justify-center">
                <Scissors className="w-5 h-5 text-white" />
              </div>
            </Link>

            {/* Search Bar */}
            <div className="flex-1 relative">
              <Search className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-500" />
              <input
                type="text"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                placeholder="Search salons, services..."
                className="w-full pl-12 pr-4 py-3 rounded-xl bg-white/5 border border-white/10 text-white placeholder-gray-500 focus:border-purple-500 outline-none transition-all"
              />
            </div>

            {/* Location */}
            <button className="hidden md:flex items-center gap-2 px-4 py-3 rounded-xl bg-white/5 border border-white/10 text-gray-400 hover:text-white transition-colors">
              <MapPin className="w-4 h-4" />
              <span className="text-sm">Bangalore</span>
              <ChevronDown className="w-4 h-4" />
            </button>

            {/* Filter Button */}
            <button 
              onClick={() => setShowFilters(!showFilters)}
              className={`flex items-center gap-2 px-4 py-3 rounded-xl border transition-all ${
                showFilters 
                  ? 'bg-purple-500/20 border-purple-500 text-purple-400' 
                  : 'bg-white/5 border-white/10 text-gray-400 hover:text-white'
              }`}
            >
              <Sliders className="w-4 h-4" />
              <span className="hidden sm:inline text-sm">Filters</span>
            </button>
          </div>
        </div>
      </header>

      {/* Service Pills */}
      <div className="sticky top-[73px] z-40 backdrop-blur-xl bg-black/40 border-b border-white/5">
        <div className="max-w-7xl mx-auto px-6 py-3">
          <div className="flex gap-2 overflow-x-auto pb-2 scrollbar-hide">
            {serviceFilters.map((service) => (
              <button
                key={service}
                onClick={() => setSelectedService(service)}
                className={`px-4 py-2 rounded-full text-sm font-medium whitespace-nowrap transition-all ${
                  selectedService === service
                    ? 'bg-gradient-to-r from-purple-500 to-teal-500 text-white'
                    : 'bg-white/5 text-gray-400 hover:text-white hover:bg-white/10'
                }`}
              >
                {service}
              </button>
            ))}
          </div>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-6 py-6">
        <div className="flex gap-6">
          {/* Filters Sidebar */}
          {showFilters && (
            <motion.aside
              initial={{ opacity: 0, x: -20 }}
              animate={{ opacity: 1, x: 0 }}
              className="hidden md:block w-72 shrink-0"
            >
              <div className="sticky top-[150px] p-6 rounded-2xl bg-white/5 border border-white/10 space-y-6">
                <div className="flex items-center justify-between">
                  <h3 className="font-semibold text-white">Filters</h3>
                  <button 
                    onClick={() => {
                      setMinRating(0);
                      setOpenNowOnly(false);
                      setPriceRange([0, 2000]);
                    }}
                    className="text-xs text-purple-400 hover:text-purple-300"
                  >
                    Clear All
                  </button>
                </div>

                {/* Sort By */}
                <div>
                  <label className="text-sm text-gray-400 mb-2 block">Sort By</label>
                  <select
                    value={sortBy}
                    onChange={(e) => setSortBy(e.target.value)}
                    className="w-full px-3 py-2 rounded-lg bg-white/5 border border-white/10 text-white text-sm outline-none focus:border-purple-500"
                  >
                    {sortOptions.map(option => (
                      <option key={option} value={option} className="bg-gray-900">{option}</option>
                    ))}
                  </select>
                </div>

                {/* Rating */}
                <div>
                  <label className="text-sm text-gray-400 mb-2 block">Minimum Rating</label>
                  <div className="flex gap-2">
                    {[0, 3, 3.5, 4, 4.5].map((rating) => (
                      <button
                        key={rating}
                        onClick={() => setMinRating(rating)}
                        className={`flex-1 py-2 rounded-lg text-sm font-medium transition-all ${
                          minRating === rating
                            ? 'bg-purple-500 text-white'
                            : 'bg-white/5 text-gray-400 hover:bg-white/10'
                        }`}
                      >
                        {rating === 0 ? 'Any' : `${rating}+`}
                      </button>
                    ))}
                  </div>
                </div>

                {/* Open Now */}
                <div className="flex items-center justify-between">
                  <label className="text-sm text-gray-400">Open Now Only</label>
                  <button
                    onClick={() => setOpenNowOnly(!openNowOnly)}
                    className={`w-12 h-6 rounded-full transition-all ${
                      openNowOnly ? 'bg-purple-500' : 'bg-white/10'
                    }`}
                  >
                    <div className={`w-5 h-5 rounded-full bg-white transition-transform ${
                      openNowOnly ? 'translate-x-6' : 'translate-x-0.5'
                    }`} />
                  </button>
                </div>
              </div>
            </motion.aside>
          )}

          {/* Results */}
          <div className="flex-1">
            {/* Results Header */}
            <div className="flex items-center justify-between mb-6">
              <p className="text-gray-400">
                <span className="text-white font-medium">{filteredSalons.length}</span> salons found
              </p>
              <div className="flex items-center gap-2">
                <span className="text-sm text-gray-500">Sort:</span>
                <select
                  value={sortBy}
                  onChange={(e) => setSortBy(e.target.value)}
                  className="px-3 py-1.5 rounded-lg bg-white/5 border border-white/10 text-white text-sm outline-none focus:border-purple-500"
                >
                  {sortOptions.map(option => (
                    <option key={option} value={option} className="bg-gray-900">{option}</option>
                  ))}
                </select>
              </div>
            </div>

            {/* Salon Cards */}
            <div className="grid gap-4">
              {filteredSalons.map((salon, index) => (
                <motion.div
                  key={salon.id}
                  initial={{ opacity: 0, y: 20 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{ delay: index * 0.1 }}
                >
                  <Link href={`/shop/${salon.id}`}>
                    <div className="group p-5 rounded-2xl bg-white/5 border border-white/10 hover:border-purple-500/50 transition-all cursor-pointer">
                      <div className="flex gap-5">
                        {/* Image */}
                        <div className="w-24 h-24 rounded-xl bg-gradient-to-br from-purple-500/20 to-teal-500/20 flex items-center justify-center text-4xl shrink-0">
                          {salon.image}
                        </div>

                        {/* Info */}
                        <div className="flex-1 min-w-0">
                          <div className="flex items-start justify-between gap-4">
                            <div>
                              <h3 className="text-lg font-semibold text-white group-hover:text-purple-400 transition-colors truncate">
                                {salon.name}
                              </h3>
                              <div className="flex items-center gap-3 text-sm text-gray-400 mt-1">
                                <div className="flex items-center gap-1">
                                  <Star className="w-4 h-4 text-yellow-400 fill-yellow-400" />
                                  <span className="text-white">{salon.rating}</span>
                                  <span>({salon.reviews})</span>
                                </div>
                                <span>•</span>
                                <span>{salon.distance}</span>
                              </div>
                            </div>
                            <div className="text-right shrink-0">
                              <div className="text-sm font-medium text-white">{salon.priceRange}</div>
                              <div className={`text-xs mt-1 ${salon.openNow ? 'text-green-400' : 'text-red-400'}`}>
                                {salon.openNow ? 'Open Now' : 'Closed'}
                              </div>
                            </div>
                          </div>

                          <div className="flex items-center gap-2 text-sm text-gray-500 mt-2">
                            <MapPin className="w-3.5 h-3.5" />
                            <span className="truncate">{salon.address}, {salon.city}</span>
                          </div>

                          <div className="flex items-center justify-between mt-3">
                            <div className="flex flex-wrap gap-1.5">
                              {salon.tags.map(tag => (
                                <span 
                                  key={tag}
                                  className="px-2 py-0.5 rounded-md bg-purple-500/10 text-purple-400 text-xs"
                                >
                                  {tag}
                                </span>
                              ))}
                            </div>
                            {salon.openNow && (
                              <div className="flex items-center gap-1 text-sm">
                                <Clock className="w-3.5 h-3.5 text-teal-400" />
                                <span className="text-teal-400">Next: {salon.nextSlot}</span>
                              </div>
                            )}
                          </div>
                        </div>
                      </div>
                    </div>
                  </Link>
                </motion.div>
              ))}
            </div>

            {/* No Results */}
            {filteredSalons.length === 0 && (
              <div className="text-center py-16">
                <div className="w-20 h-20 rounded-full bg-white/5 flex items-center justify-center mx-auto mb-4">
                  <Search className="w-8 h-8 text-gray-600" />
                </div>
                <h3 className="text-xl font-semibold text-white mb-2">No salons found</h3>
                <p className="text-gray-500">Try adjusting your filters or search query</p>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
