'use client';

import { useState } from 'react';
import { motion } from 'framer-motion';
import Link from 'next/link';
import { 
  ArrowLeft,
  Plus,
  Edit2,
  Trash2,
  Clock,
  DollarSign,
  Star,
  Search,
  X
} from 'lucide-react';

// Mock services
const initialServices = [
  { id: 1, name: 'Classic Haircut', price: 300, duration: 30, category: 'Haircut', popular: true },
  { id: 2, name: 'Premium Haircut', price: 450, duration: 45, category: 'Haircut', popular: true },
  { id: 3, name: 'Kids Haircut', price: 200, duration: 25, category: 'Haircut', popular: false },
  { id: 4, name: 'Beard Trim', price: 150, duration: 20, category: 'Beard', popular: true },
  { id: 5, name: 'Beard Styling', price: 250, duration: 30, category: 'Beard', popular: false },
  { id: 6, name: 'Clean Shave', price: 200, duration: 25, category: 'Beard', popular: false },
  { id: 7, name: 'Hair Color', price: 800, duration: 90, category: 'Color', popular: true },
  { id: 8, name: 'Hair Spa', price: 600, duration: 60, category: 'Treatment', popular: false },
  { id: 9, name: 'Head Massage', price: 300, duration: 30, category: 'Treatment', popular: false },
];

const categories = ['All', 'Haircut', 'Beard', 'Color', 'Treatment'];

export default function ServicesPage() {
  const [services, setServices] = useState(initialServices);
  const [selectedCategory, setSelectedCategory] = useState('All');
  const [searchQuery, setSearchQuery] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [editingService, setEditingService] = useState<typeof initialServices[0] | null>(null);
  const [formData, setFormData] = useState({
    name: '',
    price: '',
    duration: '',
    category: 'Haircut',
    popular: false
  });

  const filteredServices = services.filter(service => {
    if (selectedCategory !== 'All' && service.category !== selectedCategory) return false;
    if (searchQuery && !service.name.toLowerCase().includes(searchQuery.toLowerCase())) return false;
    return true;
  });

  const handleAddService = () => {
    setEditingService(null);
    setFormData({ name: '', price: '', duration: '', category: 'Haircut', popular: false });
    setShowModal(true);
  };

  const handleEditService = (service: typeof initialServices[0]) => {
    setEditingService(service);
    setFormData({
      name: service.name,
      price: service.price.toString(),
      duration: service.duration.toString(),
      category: service.category,
      popular: service.popular
    });
    setShowModal(true);
  };

  const handleDeleteService = (id: number) => {
    setServices(services.filter(s => s.id !== id));
  };

  const handleSave = () => {
    if (editingService) {
      setServices(services.map(s => 
        s.id === editingService.id 
          ? { ...s, ...formData, price: parseInt(formData.price), duration: parseInt(formData.duration) }
          : s
      ));
    } else {
      setServices([...services, {
        id: Date.now(),
        name: formData.name,
        price: parseInt(formData.price),
        duration: parseInt(formData.duration),
        category: formData.category,
        popular: formData.popular
      }]);
    }
    setShowModal(false);
  };

  return (
    <div className="min-h-screen bg-[#0a0a0f]">
      <div className="fixed inset-0 -z-10">
        <div className="absolute top-0 right-0 w-96 h-96 bg-purple-500/10 rounded-full blur-[128px]" />
      </div>

      <header className="sticky top-0 z-50 backdrop-blur-xl bg-black/40 border-b border-white/5">
        <div className="max-w-4xl mx-auto px-6 py-4">
          <div className="flex items-center justify-between">
            <Link href="/shop/dashboard" className="flex items-center gap-2 text-gray-400 hover:text-white transition-colors">
              <ArrowLeft className="w-5 h-5" />
              <span>Dashboard</span>
            </Link>
            <h1 className="text-lg font-semibold text-white">Services</h1>
            <button
              onClick={handleAddService}
              className="flex items-center gap-2 px-4 py-2 rounded-xl bg-purple-500 text-white font-medium hover:bg-purple-600 transition-colors"
            >
              <Plus className="w-4 h-4" />
              Add
            </button>
          </div>
        </div>
      </header>

      <main className="max-w-4xl mx-auto px-6 py-6">
        {/* Search & Filter */}
        <div className="flex gap-3 mb-6">
          <div className="flex-1 relative">
            <Search className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-500" />
            <input
              type="text"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              placeholder="Search services..."
              className="w-full pl-12 pr-4 py-3 rounded-xl bg-white/5 border border-white/10 text-white placeholder-gray-500 focus:border-purple-500 outline-none"
            />
          </div>
        </div>

        {/* Category Tabs */}
        <div className="flex gap-2 mb-6 overflow-x-auto pb-2">
          {categories.map(cat => (
            <button
              key={cat}
              onClick={() => setSelectedCategory(cat)}
              className={`px-4 py-2 rounded-full text-sm font-medium whitespace-nowrap transition-all ${
                selectedCategory === cat
                  ? 'bg-purple-500 text-white'
                  : 'bg-white/5 text-gray-400 hover:bg-white/10'
              }`}
            >
              {cat}
            </button>
          ))}
        </div>

        {/* Services Grid */}
        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          className="grid gap-3"
        >
          {filteredServices.map((service) => (
            <div
              key={service.id}
              className="p-4 rounded-xl bg-white/5 border border-white/10 hover:border-white/20 transition-all"
            >
              <div className="flex items-center justify-between">
                <div className="flex-1">
                  <div className="flex items-center gap-2 mb-1">
                    <h3 className="font-medium text-white">{service.name}</h3>
                    {service.popular && (
                      <Star className="w-4 h-4 text-yellow-400 fill-yellow-400" />
                    )}
                  </div>
                  <div className="flex items-center gap-4 text-sm text-gray-500">
                    <span className="flex items-center gap-1">
                      <Clock className="w-3.5 h-3.5" />
                      {service.duration} mins
                    </span>
                    <span className="px-2 py-0.5 rounded bg-white/5 text-xs">
                      {service.category}
                    </span>
                  </div>
                </div>
                <div className="flex items-center gap-4">
                  <span className="text-lg font-bold text-white">₹{service.price}</span>
                  <div className="flex gap-2">
                    <button
                      onClick={() => handleEditService(service)}
                      className="p-2 rounded-lg bg-white/5 hover:bg-white/10 transition-colors"
                    >
                      <Edit2 className="w-4 h-4 text-gray-400" />
                    </button>
                    <button
                      onClick={() => handleDeleteService(service.id)}
                      className="p-2 rounded-lg bg-red-500/10 hover:bg-red-500/20 transition-colors"
                    >
                      <Trash2 className="w-4 h-4 text-red-400" />
                    </button>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </motion.div>

        {filteredServices.length === 0 && (
          <div className="text-center py-12">
            <DollarSign className="w-12 h-12 text-gray-600 mx-auto mb-3" />
            <p className="text-gray-400">No services found</p>
          </div>
        )}
      </main>

      {/* Add/Edit Modal */}
      {showModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-6 bg-black/80">
          <motion.div
            initial={{ opacity: 0, scale: 0.95 }}
            animate={{ opacity: 1, scale: 1 }}
            className="w-full max-w-md p-6 rounded-2xl bg-[#0a0a0f] border border-white/10"
          >
            <div className="flex items-center justify-between mb-6">
              <h2 className="text-xl font-bold text-white">
                {editingService ? 'Edit Service' : 'Add Service'}
              </h2>
              <button onClick={() => setShowModal(false)} className="p-2 rounded-lg hover:bg-white/10">
                <X className="w-5 h-5 text-gray-400" />
              </button>
            </div>

            <div className="space-y-4">
              <div>
                <label className="block text-sm text-gray-400 mb-1">Service Name</label>
                <input
                  type="text"
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  placeholder="e.g., Classic Haircut"
                  className="w-full px-4 py-3 rounded-xl bg-white/5 border border-white/10 text-white placeholder-gray-500 focus:border-purple-500 outline-none"
                />
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm text-gray-400 mb-1">Price (₹)</label>
                  <input
                    type="number"
                    value={formData.price}
                    onChange={(e) => setFormData({ ...formData, price: e.target.value })}
                    placeholder="300"
                    className="w-full px-4 py-3 rounded-xl bg-white/5 border border-white/10 text-white placeholder-gray-500 focus:border-purple-500 outline-none"
                  />
                </div>
                <div>
                  <label className="block text-sm text-gray-400 mb-1">Duration (mins)</label>
                  <input
                    type="number"
                    value={formData.duration}
                    onChange={(e) => setFormData({ ...formData, duration: e.target.value })}
                    placeholder="30"
                    className="w-full px-4 py-3 rounded-xl bg-white/5 border border-white/10 text-white placeholder-gray-500 focus:border-purple-500 outline-none"
                  />
                </div>
              </div>

              <div>
                <label className="block text-sm text-gray-400 mb-1">Category</label>
                <select
                  value={formData.category}
                  onChange={(e) => setFormData({ ...formData, category: e.target.value })}
                  className="w-full px-4 py-3 rounded-xl bg-white/5 border border-white/10 text-white focus:border-purple-500 outline-none"
                >
                  {categories.filter(c => c !== 'All').map(cat => (
                    <option key={cat} value={cat} className="bg-gray-900">{cat}</option>
                  ))}
                </select>
              </div>

              <div className="flex items-center gap-3">
                <input
                  type="checkbox"
                  id="popular"
                  checked={formData.popular}
                  onChange={(e) => setFormData({ ...formData, popular: e.target.checked })}
                  className="w-4 h-4 rounded border-white/20 bg-white/5 text-purple-500 focus:ring-purple-500"
                />
                <label htmlFor="popular" className="text-sm text-gray-400">Mark as popular service</label>
              </div>

              <button
                onClick={handleSave}
                className="w-full py-4 rounded-xl bg-gradient-to-r from-purple-500 to-teal-500 text-white font-semibold hover:opacity-90 transition-opacity"
              >
                {editingService ? 'Save Changes' : 'Add Service'}
              </button>
            </div>
          </motion.div>
        </div>
      )}
    </div>
  );
}
