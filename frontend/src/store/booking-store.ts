import { create } from 'zustand';
import { ShopResponse, ShopServiceResponse, StaffResponse } from '@/lib/api/shop';
import { SlotResponse } from '@/lib/api/booking';

interface BookingState {
  // Selected shop
  selectedShop: ShopResponse | null;
  
  // Booking details
  selectedServices: ShopServiceResponse[];
  selectedStaff: StaffResponse | null;
  selectedDate: string | null; // YYYY-MM-DD
  selectedSlot: SlotResponse | null;
  
  // Slot lock
  slotLockToken: string | null;
  slotLockExpiry: Date | null;
  
  // Notes
  notes: string;
  
  // Calculated totals
  totalAmount: number;
  totalDuration: number;
  
  // Current step (for multi-step form)
  currentStep: number;
  
  // Actions
  setShop: (shop: ShopResponse | null) => void;
  addService: (service: ShopServiceResponse) => void;
  removeService: (serviceId: number) => void;
  clearServices: () => void;
  setStaff: (staff: StaffResponse | null) => void;
  setDate: (date: string | null) => void;
  setSlot: (slot: SlotResponse | null) => void;
  setSlotLock: (token: string | null, expiry: Date | null) => void;
  setNotes: (notes: string) => void;
  setStep: (step: number) => void;
  nextStep: () => void;
  prevStep: () => void;
  reset: () => void;
}

const initialState = {
  selectedShop: null,
  selectedServices: [],
  selectedStaff: null,
  selectedDate: null,
  selectedSlot: null,
  slotLockToken: null,
  slotLockExpiry: null,
  notes: '',
  totalAmount: 0,
  totalDuration: 0,
  currentStep: 1,
};

export const useBookingStore = create<BookingState>()((set, get) => ({
  ...initialState,

  setShop: (shop) => set({ selectedShop: shop }),

  addService: (service) => {
    const current = get().selectedServices;
    const exists = current.find((s) => s.id === service.id);
    if (!exists) {
      const newServices = [...current, service];
      set({
        selectedServices: newServices,
        totalAmount: newServices.reduce((sum, s) => sum + s.price, 0),
        totalDuration: newServices.reduce((sum, s) => sum + s.duration, 0),
      });
    }
  },

  removeService: (serviceId) => {
    const newServices = get().selectedServices.filter((s) => s.id !== serviceId);
    set({
      selectedServices: newServices,
      totalAmount: newServices.reduce((sum, s) => sum + s.price, 0),
      totalDuration: newServices.reduce((sum, s) => sum + s.duration, 0),
    });
  },

  clearServices: () =>
    set({
      selectedServices: [],
      totalAmount: 0,
      totalDuration: 0,
    }),

  setStaff: (staff) => set({ selectedStaff: staff }),

  setDate: (date) =>
    set({
      selectedDate: date,
      selectedSlot: null, // Clear slot when date changes
      slotLockToken: null,
      slotLockExpiry: null,
    }),

  setSlot: (slot) => set({ selectedSlot: slot }),

  setSlotLock: (token, expiry) =>
    set({
      slotLockToken: token,
      slotLockExpiry: expiry,
    }),

  setNotes: (notes) => set({ notes }),

  setStep: (step) => set({ currentStep: step }),

  nextStep: () => set((state) => ({ currentStep: state.currentStep + 1 })),

  prevStep: () => set((state) => ({ currentStep: Math.max(1, state.currentStep - 1) })),

  reset: () => set(initialState),
}));

export default useBookingStore;

