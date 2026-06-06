import apiClient, { ApiResponse, PagedResponse } from './client';

// Types
export interface ShopResponse {
  id: number;
  name: string;
  description: string;
  address: string;
  city: string;
  area: string;
  pincode: string;
  phone: string;
  email?: string;
  latitude?: number;
  longitude?: number;
  rating: number;
  totalRatings: number;
  images: string[];
  coverImage?: string;
  status: 'ACTIVE' | 'INACTIVE' | 'PENDING';
  ownerId: number;
  ownerName: string;
  workingHours: WorkingHoursResponse[];
  services: ShopServiceResponse[];
  staff: StaffResponse[];
  createdAt: string;
}

export interface ShopServiceResponse {
  id: number;
  name: string;
  description?: string;
  price: number;
  duration: number; // in minutes
  category: string;
  isActive: boolean;
}

export interface StaffResponse {
  id: number;
  name: string;
  phone?: string;
  email?: string;
  image?: string;
  specializations: string[];
  isActive: boolean;
  rating: number;
}

export interface WorkingHoursResponse {
  dayOfWeek: number; // 1-7 (Monday-Sunday)
  openTime: string; // HH:mm
  closeTime: string; // HH:mm
  breakStartTime?: string;
  breakEndTime?: string;
  isOpen: boolean;
}

export interface ShopSearchRequest {
  city?: string;
  area?: string;
  query?: string;
  minRating?: number;
  maxPrice?: number;
  services?: string[];
  sortBy?: 'rating' | 'price' | 'distance' | 'popularity';
  sortOrder?: 'asc' | 'desc';
  page?: number;
  size?: number;
}

export interface CreateShopRequest {
  name: string;
  description: string;
  address: string;
  city: string;
  area: string;
  pincode: string;
  phone: string;
  email?: string;
  latitude?: number;
  longitude?: number;
  images?: string[];
  coverImage?: string;
}

export interface CreateServiceRequest {
  name: string;
  description?: string;
  price: number;
  duration: number;
  category: string;
}

export interface CreateStaffRequest {
  name: string;
  phone?: string;
  email?: string;
  image?: string;
  specializations?: string[];
}

// Shop API
export const shopApi = {
  // Search shops
  search: async (params: ShopSearchRequest): Promise<ApiResponse<PagedResponse<ShopResponse>>> => {
    const response = await apiClient.get('/shops/search', { params });
    return response.data;
  },

  // Get shop by ID
  getById: async (id: number): Promise<ApiResponse<ShopResponse>> => {
    const response = await apiClient.get(`/shops/${id}`);
    return response.data;
  },

  // Get my shop (for shop owner)
  getMyShop: async (): Promise<ApiResponse<ShopResponse>> => {
    const response = await apiClient.get('/shops/my');
    return response.data;
  },

  // Create shop
  create: async (data: CreateShopRequest): Promise<ApiResponse<ShopResponse>> => {
    const response = await apiClient.post('/shops', data);
    return response.data;
  },

  // Update shop
  update: async (id: number, data: Partial<CreateShopRequest>): Promise<ApiResponse<ShopResponse>> => {
    const response = await apiClient.put(`/shops/${id}`, data);
    return response.data;
  },

  // Delete shop
  delete: async (id: number): Promise<ApiResponse<{ message: string }>> => {
    const response = await apiClient.delete(`/shops/${id}`);
    return response.data;
  },

  // Get shop services
  getServices: async (shopId: number): Promise<ApiResponse<ShopServiceResponse[]>> => {
    const response = await apiClient.get(`/services/${shopId}`);
    return response.data;
  },

  // Create service
  createService: async (shopId: number, data: CreateServiceRequest): Promise<ApiResponse<ShopServiceResponse>> => {
    const response = await apiClient.post('/services', { ...data, shopId });
    return response.data;
  },

  // Update service
  updateService: async (id: number, data: Partial<CreateServiceRequest>): Promise<ApiResponse<ShopServiceResponse>> => {
    const response = await apiClient.put(`/services/${id}`, data);
    return response.data;
  },

  // Delete service
  deleteService: async (id: number): Promise<ApiResponse<{ message: string }>> => {
    const response = await apiClient.delete(`/services/${id}`);
    return response.data;
  },

  // Get shop staff
  getStaff: async (shopId: number): Promise<ApiResponse<StaffResponse[]>> => {
    const response = await apiClient.get(`/staff/${shopId}`);
    return response.data;
  },

  // Create staff
  createStaff: async (shopId: number, data: CreateStaffRequest): Promise<ApiResponse<StaffResponse>> => {
    const response = await apiClient.post('/staff', { ...data, shopId });
    return response.data;
  },

  // Update staff
  updateStaff: async (id: number, data: Partial<CreateStaffRequest>): Promise<ApiResponse<StaffResponse>> => {
    const response = await apiClient.put(`/staff/${id}`, data);
    return response.data;
  },

  // Delete staff
  deleteStaff: async (id: number): Promise<ApiResponse<{ message: string }>> => {
    const response = await apiClient.delete(`/staff/${id}`);
    return response.data;
  },

  // Update working hours
  updateWorkingHours: async (shopId: number, data: WorkingHoursResponse[]): Promise<ApiResponse<WorkingHoursResponse[]>> => {
    const response = await apiClient.put(`/shops/${shopId}/working-hours`, data);
    return response.data;
  },
};

export default shopApi;

