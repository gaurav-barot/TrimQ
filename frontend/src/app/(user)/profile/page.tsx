'use client';

import { useState } from 'react';
import { motion } from 'framer-motion';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { 
  ArrowLeft,
  User,
  Mail,
  Phone,
  MapPin,
  Camera,
  LogOut,
  ChevronRight,
  Bell,
  Shield,
  CreditCard,
  Heart,
  HelpCircle,
  FileText,
  Star,
  Settings,
  Edit2
} from 'lucide-react';

// Mock user data
const userData = {
  name: 'Saurabh Kumar',
  email: 'saurabh@example.com',
  phone: '+91 98765 43210',
  avatar: null,
  joinedDate: 'December 2025',
  totalBookings: 12,
  savedAmount: 450
};

const menuItems = [
  { icon: Heart, label: 'Saved Salons', href: '/saved', badge: '3' },
  { icon: CreditCard, label: 'Payment Methods', href: '/payment-methods' },
  { icon: Bell, label: 'Notifications', href: '/notifications', badge: '2' },
  { icon: Shield, label: 'Privacy & Security', href: '/privacy' },
  { icon: Star, label: 'Rate Us', href: '/rate' },
  { icon: HelpCircle, label: 'Help & Support', href: '/support' },
  { icon: FileText, label: 'Terms & Conditions', href: '/terms' },
];

export default function ProfilePage() {
  const router = useRouter();
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState({
    name: userData.name,
    email: userData.email,
    phone: userData.phone
  });

  const handleLogout = () => {
    // TODO: Implement logout
    router.push('/login');
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
            <Link href="/dashboard" className="flex items-center gap-2 text-gray-400 hover:text-white transition-colors">
              <ArrowLeft className="w-5 h-5" />
              <span>Back</span>
            </Link>
            <h1 className="text-lg font-semibold text-white">Profile</h1>
            <button 
              onClick={() => setIsEditing(!isEditing)}
              className="p-2 rounded-lg bg-white/5 hover:bg-white/10 transition-colors"
            >
              <Edit2 className="w-4 h-4 text-gray-400" />
            </button>
          </div>
        </div>
      </header>

      <main className="max-w-2xl mx-auto px-6 py-6">
        {/* Profile Header */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="text-center mb-8"
        >
          <div className="relative inline-block mb-4">
            <div className="w-24 h-24 rounded-full bg-gradient-to-br from-purple-500 to-teal-500 flex items-center justify-center text-4xl font-bold text-white">
              {userData.name.charAt(0)}
            </div>
            <button className="absolute bottom-0 right-0 w-8 h-8 rounded-full bg-purple-500 flex items-center justify-center shadow-lg">
              <Camera className="w-4 h-4 text-white" />
            </button>
          </div>
          <h2 className="text-2xl font-bold text-white">{userData.name}</h2>
          <p className="text-gray-500">Member since {userData.joinedDate}</p>
        </motion.div>

        {/* Stats */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.1 }}
          className="grid grid-cols-2 gap-4 mb-8"
        >
          <div className="p-4 rounded-2xl bg-gradient-to-br from-purple-500/20 to-purple-600/10 border border-purple-500/20 text-center">
            <p className="text-3xl font-bold text-white">{userData.totalBookings}</p>
            <p className="text-sm text-gray-400">Total Bookings</p>
          </div>
          <div className="p-4 rounded-2xl bg-gradient-to-br from-teal-500/20 to-teal-600/10 border border-teal-500/20 text-center">
            <p className="text-3xl font-bold text-white">₹{userData.savedAmount}</p>
            <p className="text-sm text-gray-400">Total Saved</p>
          </div>
        </motion.div>

        {/* Edit Form */}
        {isEditing && (
          <motion.div
            initial={{ opacity: 0, height: 0 }}
            animate={{ opacity: 1, height: 'auto' }}
            className="mb-8 p-5 rounded-2xl bg-white/5 border border-white/10"
          >
            <h3 className="text-lg font-semibold text-white mb-4">Edit Profile</h3>
            <div className="space-y-4">
              <div>
                <label className="block text-sm text-gray-400 mb-1">Full Name</label>
                <div className="relative">
                  <User className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-500" />
                  <input
                    type="text"
                    value={formData.name}
                    onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                    className="w-full pl-11 pr-4 py-3 rounded-xl bg-white/5 border border-white/10 text-white placeholder-gray-500 focus:border-purple-500 outline-none"
                  />
                </div>
              </div>
              <div>
                <label className="block text-sm text-gray-400 mb-1">Email</label>
                <div className="relative">
                  <Mail className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-500" />
                  <input
                    type="email"
                    value={formData.email}
                    onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                    className="w-full pl-11 pr-4 py-3 rounded-xl bg-white/5 border border-white/10 text-white placeholder-gray-500 focus:border-purple-500 outline-none"
                  />
                </div>
              </div>
              <div>
                <label className="block text-sm text-gray-400 mb-1">Phone</label>
                <div className="relative">
                  <Phone className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-500" />
                  <input
                    type="tel"
                    value={formData.phone}
                    onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                    className="w-full pl-11 pr-4 py-3 rounded-xl bg-white/5 border border-white/10 text-white placeholder-gray-500 focus:border-purple-500 outline-none"
                    disabled
                  />
                </div>
                <p className="text-xs text-gray-600 mt-1">Phone number cannot be changed</p>
              </div>
              <button className="w-full py-3 rounded-xl bg-gradient-to-r from-purple-500 to-teal-500 text-white font-semibold hover:opacity-90 transition-opacity">
                Save Changes
              </button>
            </div>
          </motion.div>
        )}

        {/* Contact Info */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.15 }}
          className="mb-6 p-5 rounded-2xl bg-white/5 border border-white/10"
        >
          <div className="space-y-4">
            <div className="flex items-center gap-4">
              <div className="w-10 h-10 rounded-xl bg-purple-500/20 flex items-center justify-center">
                <Mail className="w-5 h-5 text-purple-400" />
              </div>
              <div>
                <p className="text-xs text-gray-500">Email</p>
                <p className="text-white">{userData.email}</p>
              </div>
            </div>
            <div className="flex items-center gap-4">
              <div className="w-10 h-10 rounded-xl bg-teal-500/20 flex items-center justify-center">
                <Phone className="w-5 h-5 text-teal-400" />
              </div>
              <div>
                <p className="text-xs text-gray-500">Phone</p>
                <p className="text-white">{userData.phone}</p>
              </div>
            </div>
          </div>
        </motion.div>

        {/* Menu Items */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.2 }}
          className="space-y-2 mb-8"
        >
          {menuItems.map((item, i) => (
            <Link
              key={i}
              href={item.href}
              className="flex items-center justify-between p-4 rounded-xl bg-white/5 border border-white/10 hover:border-white/20 transition-all group"
            >
              <div className="flex items-center gap-4">
                <div className="w-10 h-10 rounded-xl bg-white/5 flex items-center justify-center group-hover:bg-purple-500/20 transition-colors">
                  <item.icon className="w-5 h-5 text-gray-400 group-hover:text-purple-400 transition-colors" />
                </div>
                <span className="text-gray-300 group-hover:text-white transition-colors">{item.label}</span>
              </div>
              <div className="flex items-center gap-2">
                {item.badge && (
                  <span className="px-2 py-0.5 rounded-full bg-purple-500 text-xs text-white font-medium">
                    {item.badge}
                  </span>
                )}
                <ChevronRight className="w-5 h-5 text-gray-600 group-hover:text-gray-400 transition-colors" />
              </div>
            </Link>
          ))}
        </motion.div>

        {/* Logout Button */}
        <motion.button
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.25 }}
          onClick={handleLogout}
          className="w-full py-4 rounded-xl bg-red-500/10 border border-red-500/20 text-red-400 font-medium flex items-center justify-center gap-2 hover:bg-red-500/20 transition-all"
        >
          <LogOut className="w-5 h-5" />
          Log Out
        </motion.button>

        <p className="text-center text-xs text-gray-600 mt-6">
          TrimQ v1.0.0 • Made with ❤️ in India
        </p>
      </main>
    </div>
  );
}
