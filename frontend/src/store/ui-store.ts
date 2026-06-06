import { create } from 'zustand';

interface UIState {
  // Mobile sidebar
  isSidebarOpen: boolean;
  
  // Modal states
  isSearchModalOpen: boolean;
  isLoginModalOpen: boolean;
  
  // Global loading
  isGlobalLoading: boolean;
  loadingMessage: string;
  
  // Theme
  theme: 'light' | 'dark' | 'system';
  
  // Actions
  toggleSidebar: () => void;
  setSidebarOpen: (open: boolean) => void;
  setSearchModalOpen: (open: boolean) => void;
  setLoginModalOpen: (open: boolean) => void;
  setGlobalLoading: (loading: boolean, message?: string) => void;
  setTheme: (theme: UIState['theme']) => void;
}

export const useUIStore = create<UIState>()((set) => ({
  isSidebarOpen: false,
  isSearchModalOpen: false,
  isLoginModalOpen: false,
  isGlobalLoading: false,
  loadingMessage: '',
  theme: 'dark',

  toggleSidebar: () => set((state) => ({ isSidebarOpen: !state.isSidebarOpen })),

  setSidebarOpen: (open) => set({ isSidebarOpen: open }),

  setSearchModalOpen: (open) => set({ isSearchModalOpen: open }),

  setLoginModalOpen: (open) => set({ isLoginModalOpen: open }),

  setGlobalLoading: (loading, message = '') =>
    set({
      isGlobalLoading: loading,
      loadingMessage: message,
    }),

  setTheme: (theme) => set({ theme }),
}));

export default useUIStore;

