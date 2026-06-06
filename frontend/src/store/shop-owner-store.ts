import { create } from 'zustand';
import { ShopResponse } from '@/lib/api/shop';

interface ShopOwnerState {
  // My shop
  myShop: ShopResponse | null;
  isShopLoading: boolean;
  
  // Today's stats
  todayStats: {
    totalBookings: number;
    completedBookings: number;
    pendingBookings: number;
    currentToken: number;
    revenue: number;
  };
  
  // Active tab/page
  activeSection: 'dashboard' | 'services' | 'staff' | 'bookings' | 'today' | 'validate' | 'settings';
  
  // Actions
  setMyShop: (shop: ShopResponse | null) => void;
  setShopLoading: (loading: boolean) => void;
  setTodayStats: (stats: Partial<ShopOwnerState['todayStats']>) => void;
  setActiveSection: (section: ShopOwnerState['activeSection']) => void;
  reset: () => void;
}

const initialState = {
  myShop: null,
  isShopLoading: true,
  todayStats: {
    totalBookings: 0,
    completedBookings: 0,
    pendingBookings: 0,
    currentToken: 0,
    revenue: 0,
  },
  activeSection: 'dashboard' as const,
};

export const useShopOwnerStore = create<ShopOwnerState>()((set) => ({
  ...initialState,

  setMyShop: (shop) => set({ myShop: shop, isShopLoading: false }),

  setShopLoading: (loading) => set({ isShopLoading: loading }),

  setTodayStats: (stats) =>
    set((state) => ({
      todayStats: { ...state.todayStats, ...stats },
    })),

  setActiveSection: (section) => set({ activeSection: section }),

  reset: () => set(initialState),
}));

export default useShopOwnerStore;

