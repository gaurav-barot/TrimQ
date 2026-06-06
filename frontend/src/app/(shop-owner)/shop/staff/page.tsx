'use client';

import { useState } from 'react';
import { motion } from 'framer-motion';
import Link from 'next/link';
import { 
  ArrowLeft,
  Plus,
  Edit2,
  Trash2,
  Star,
  Phone,
  Mail,
  User,
  X,
  CheckCircle
} from 'lucide-react';

// Mock staff
const initialStaff = [
  { id: 1, name: 'Rahul Sharma', role: 'Senior Stylist', phone: '+91 98765 43210', email: 'rahul@shop.com', rating: 4.9, bookings: 156, active: true },
  { id: 2, name: 'Amit Kumar', role: 'Hair Expert', phone: '+91 98765 43211', email: 'amit@shop.com', rating: 4.7, bookings: 124, active: true },
  { id: 3, name: 'Vijay Singh', role: 'Barber', phone: '+91 98765 43212', email: 'vijay@shop.com', rating: 4.6, bookings: 98, active: true },
  { id: 4, name: 'Suresh Patel', role: 'Trainee', phone: '+91 98765 43213', email: 'suresh@shop.com', rating: 4.3, bookings: 45, active: false },
];

const roles = ['Senior Stylist', 'Hair Expert', 'Barber', 'Colorist', 'Spa Therapist', 'Trainee'];

export default function StaffPage() {
  const [staff, setStaff] = useState(initialStaff);
  const [showModal, setShowModal] = useState(false);
  const [editingStaff, setEditingStaff] = useState<typeof initialStaff[0] | null>(null);
  const [formData, setFormData] = useState({
    name: '',
    role: 'Barber',
    phone: '',
    email: '',
    active: true
  });

  const handleAdd = () => {
    setEditingStaff(null);
    setFormData({ name: '', role: 'Barber', phone: '', email: '', active: true });
    setShowModal(true);
  };

  const handleEdit = (member: typeof initialStaff[0]) => {
    setEditingStaff(member);
    setFormData({
      name: member.name,
      role: member.role,
      phone: member.phone,
      email: member.email,
      active: member.active
    });
    setShowModal(true);
  };

  const handleDelete = (id: number) => {
    setStaff(staff.filter(s => s.id !== id));
  };

  const handleSave = () => {
    if (editingStaff) {
      setStaff(staff.map(s => 
        s.id === editingStaff.id 
          ? { ...s, ...formData }
          : s
      ));
    } else {
      setStaff([...staff, {
        id: Date.now(),
        ...formData,
        rating: 0,
        bookings: 0
      }]);
    }
    setShowModal(false);
  };

  const toggleActive = (id: number) => {
    setStaff(staff.map(s => 
      s.id === id ? { ...s, active: !s.active } : s
    ));
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
            <h1 className="text-lg font-semibold text-white">Staff Management</h1>
            <button
              onClick={handleAdd}
              className="flex items-center gap-2 px-4 py-2 rounded-xl bg-purple-500 text-white font-medium hover:bg-purple-600 transition-colors"
            >
              <Plus className="w-4 h-4" />
              Add
            </button>
          </div>
        </div>
      </header>

      <main className="max-w-4xl mx-auto px-6 py-6">
        {/* Stats */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="grid grid-cols-2 gap-4 mb-6"
        >
          <div className="p-4 rounded-xl bg-green-500/10 border border-green-500/20 text-center">
            <p className="text-2xl font-bold text-green-400">{staff.filter(s => s.active).length}</p>
            <p className="text-sm text-gray-500">Active Staff</p>
          </div>
          <div className="p-4 rounded-xl bg-gray-500/10 border border-gray-500/20 text-center">
            <p className="text-2xl font-bold text-gray-400">{staff.filter(s => !s.active).length}</p>
            <p className="text-sm text-gray-500">Inactive</p>
          </div>
        </motion.div>

        {/* Staff List */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.1 }}
          className="space-y-3"
        >
          {staff.map((member) => (
            <div
              key={member.id}
              className={`p-5 rounded-xl border transition-all ${
                member.active 
                  ? 'bg-white/5 border-white/10' 
                  : 'bg-white/[0.02] border-white/5 opacity-60'
              }`}
            >
              <div className="flex items-start gap-4">
                <div className="w-14 h-14 rounded-xl bg-gradient-to-br from-purple-500/20 to-teal-500/20 flex items-center justify-center text-2xl">
                  👤
                </div>
                
                <div className="flex-1 min-w-0">
                  <div className="flex items-center gap-2 mb-1">
                    <h3 className="font-semibold text-white">{member.name}</h3>
                    {member.active && (
                      <span className="flex items-center gap-1 px-2 py-0.5 rounded-full bg-green-500/20 text-green-400 text-xs">
                        <CheckCircle className="w-3 h-3" />
                        Active
                      </span>
                    )}
                  </div>
                  <p className="text-sm text-gray-500 mb-2">{member.role}</p>
                  <div className="flex items-center gap-4 text-sm">
                    <div className="flex items-center gap-1 text-yellow-400">
                      <Star className="w-4 h-4 fill-yellow-400" />
                      <span>{member.rating || 'New'}</span>
                    </div>
                    <span className="text-gray-500">{member.bookings} bookings</span>
                  </div>
                </div>

                <div className="flex flex-col gap-2">
                  <div className="flex gap-2">
                    <button
                      onClick={() => handleEdit(member)}
                      className="p-2 rounded-lg bg-white/5 hover:bg-white/10 transition-colors"
                    >
                      <Edit2 className="w-4 h-4 text-gray-400" />
                    </button>
                    <button
                      onClick={() => handleDelete(member.id)}
                      className="p-2 rounded-lg bg-red-500/10 hover:bg-red-500/20 transition-colors"
                    >
                      <Trash2 className="w-4 h-4 text-red-400" />
                    </button>
                  </div>
                  <button
                    onClick={() => toggleActive(member.id)}
                    className={`px-3 py-1 rounded-lg text-xs font-medium transition-colors ${
                      member.active
                        ? 'bg-gray-500/20 text-gray-400 hover:bg-gray-500/30'
                        : 'bg-green-500/20 text-green-400 hover:bg-green-500/30'
                    }`}
                  >
                    {member.active ? 'Deactivate' : 'Activate'}
                  </button>
                </div>
              </div>

              <div className="flex gap-4 mt-3 pt-3 border-t border-white/5">
                <a href={`tel:${member.phone}`} className="flex items-center gap-2 text-sm text-gray-400 hover:text-purple-400">
                  <Phone className="w-4 h-4" />
                  {member.phone}
                </a>
                <a href={`mailto:${member.email}`} className="flex items-center gap-2 text-sm text-gray-400 hover:text-purple-400">
                  <Mail className="w-4 h-4" />
                  {member.email}
                </a>
              </div>
            </div>
          ))}
        </motion.div>
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
                {editingStaff ? 'Edit Staff' : 'Add Staff'}
              </h2>
              <button onClick={() => setShowModal(false)} className="p-2 rounded-lg hover:bg-white/10">
                <X className="w-5 h-5 text-gray-400" />
              </button>
            </div>

            <div className="space-y-4">
              <div>
                <label className="block text-sm text-gray-400 mb-1">Full Name</label>
                <input
                  type="text"
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  placeholder="Enter name"
                  className="w-full px-4 py-3 rounded-xl bg-white/5 border border-white/10 text-white placeholder-gray-500 focus:border-purple-500 outline-none"
                />
              </div>

              <div>
                <label className="block text-sm text-gray-400 mb-1">Role</label>
                <select
                  value={formData.role}
                  onChange={(e) => setFormData({ ...formData, role: e.target.value })}
                  className="w-full px-4 py-3 rounded-xl bg-white/5 border border-white/10 text-white focus:border-purple-500 outline-none"
                >
                  {roles.map(role => (
                    <option key={role} value={role} className="bg-gray-900">{role}</option>
                  ))}
                </select>
              </div>

              <div>
                <label className="block text-sm text-gray-400 mb-1">Phone</label>
                <input
                  type="tel"
                  value={formData.phone}
                  onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                  placeholder="+91 98765 43210"
                  className="w-full px-4 py-3 rounded-xl bg-white/5 border border-white/10 text-white placeholder-gray-500 focus:border-purple-500 outline-none"
                />
              </div>

              <div>
                <label className="block text-sm text-gray-400 mb-1">Email</label>
                <input
                  type="email"
                  value={formData.email}
                  onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                  placeholder="staff@shop.com"
                  className="w-full px-4 py-3 rounded-xl bg-white/5 border border-white/10 text-white placeholder-gray-500 focus:border-purple-500 outline-none"
                />
              </div>

              <button
                onClick={handleSave}
                className="w-full py-4 rounded-xl bg-gradient-to-r from-purple-500 to-teal-500 text-white font-semibold hover:opacity-90 transition-opacity"
              >
                {editingStaff ? 'Save Changes' : 'Add Staff Member'}
              </button>
            </div>
          </motion.div>
        </div>
      )}
    </div>
  );
}
