import apiClient, { ApiResponse, PagedResponse } from './client';

// Types
export interface SlotResponse {
  id: number;
  startTime: string; // HH:mm
  endTime: string; // HH:mm
  status: 'AVAILABLE' | 'BOOKED' | 'LOCKED' | 'BLOCKED';
  staffId?: number;
  staffName?: string;
}

export interface DaySlotResponse {
  date: string; // YYYY-MM-DD
  dayOfWeek: string;
  isOpen: boolean;
  slots: SlotResponse[];
}

export interface BookingResponse {
  id: number;
  userId: number;
  userName: string;
  userPhone: string;
  shopId: number;
  shopName: string;
  shopAddress: string;
  staffId?: number;
  staffName?: string;
  services: BookingServiceResponse[];
  bookingDate: string; // YYYY-MM-DD
  startTime: string; // HH:mm
  endTime: string; // HH:mm
  totalAmount: number;
  totalDuration: number; // minutes
  status: 'PENDING' | 'CONFIRMED' | 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED' | 'NO_SHOW';
  passCode: string;
  qrCodeBase64?: string;
  tokenNumber: number;
  paymentStatus: 'PENDING' | 'SUCCESS' | 'FAILED' | 'REFUNDED';
  notes?: string;
  cancellationReason?: string;
  createdAt: string;
}

export interface BookingServiceResponse {
  serviceId: number;
  serviceName: string;
  price: number;
  duration: number;
}

export interface CreateBookingRequest {
  shopId: number;
  staffId?: number;
  serviceIds: number[];
  bookingDate: string; // YYYY-MM-DD
  startTime: string; // HH:mm
  notes?: string;
}

export interface ValidatePassRequest {
  passCode: string;
  shopId: number;
}

export interface ValidatePassResponse {
  valid: boolean;
  booking?: BookingResponse;
  message: string;
}

export interface TodayQueueResponse {
  totalBookings: number;
  completedBookings: number;
  pendingBookings: number;
  currentToken: number;
  bookings: BookingResponse[];
}

// Booking API
export const bookingApi = {
  // Get available slots for a shop
  getSlots: async (shopId: number, date: string, staffId?: number): Promise<ApiResponse<DaySlotResponse>> => {
    const params: Record<string, string | number> = { shopId, date };
    if (staffId) params.staffId = staffId;
    
    const response = await apiClient.get('/slots', { params });
    return response.data;
  },

  // Get slots for multiple days
  getSlotsRange: async (shopId: number, startDate: string, endDate: string, staffId?: number): Promise<ApiResponse<DaySlotResponse[]>> => {
    const params: Record<string, string | number> = { shopId, startDate, endDate };
    if (staffId) params.staffId = staffId;
    
    const response = await apiClient.get('/slots/range', { params });
    return response.data;
  },

  // Lock a slot temporarily
  lockSlot: async (slotId: number): Promise<ApiResponse<{ lockToken: string; expiresAt: string }>> => {
    const response = await apiClient.post(`/slots/${slotId}/lock`);
    return response.data;
  },

  // Release a locked slot
  releaseSlot: async (slotId: number, lockToken: string): Promise<ApiResponse<{ message: string }>> => {
    const response = await apiClient.post(`/slots/${slotId}/release`, { lockToken });
    return response.data;
  },

  // Create booking
  create: async (data: CreateBookingRequest): Promise<ApiResponse<BookingResponse>> => {
    const response = await apiClient.post('/bookings', data);
    return response.data;
  },

  // Get booking by ID
  getById: async (id: number): Promise<ApiResponse<BookingResponse>> => {
    const response = await apiClient.get(`/bookings/${id}`);
    return response.data;
  },

  // Get user bookings
  getUserBookings: async (page = 0, size = 10): Promise<ApiResponse<PagedResponse<BookingResponse>>> => {
    const response = await apiClient.get('/bookings/user', { params: { page, size } });
    return response.data;
  },

  // Get shop bookings (for shop owner)
  getShopBookings: async (shopId: number, date?: string, page = 0, size = 10): Promise<ApiResponse<PagedResponse<BookingResponse>>> => {
    const params: Record<string, string | number> = { page, size };
    if (date) params.date = date;
    
    const response = await apiClient.get(`/bookings/shop/${shopId}`, { params });
    return response.data;
  },

  // Get today's queue (for shop owner)
  getTodayQueue: async (shopId: number): Promise<ApiResponse<TodayQueueResponse>> => {
    const response = await apiClient.get(`/bookings/shop/${shopId}/today`);
    return response.data;
  },

  // Update booking status
  updateStatus: async (id: number, status: string): Promise<ApiResponse<BookingResponse>> => {
    const response = await apiClient.put(`/bookings/${id}/status`, { status });
    return response.data;
  },

  // Cancel booking
  cancel: async (id: number, reason?: string): Promise<ApiResponse<BookingResponse>> => {
    const response = await apiClient.delete(`/bookings/${id}`, { data: { reason } });
    return response.data;
  },

  // Validate pass (for shop owner)
  validatePass: async (data: ValidatePassRequest): Promise<ApiResponse<ValidatePassResponse>> => {
    const response = await apiClient.post('/bookings/validate-pass', data);
    return response.data;
  },

  // Mark as no-show
  markNoShow: async (id: number): Promise<ApiResponse<BookingResponse>> => {
    const response = await apiClient.put(`/bookings/${id}/no-show`);
    return response.data;
  },

  // Mark as completed
  markCompleted: async (id: number): Promise<ApiResponse<BookingResponse>> => {
    const response = await apiClient.put(`/bookings/${id}/complete`);
    return response.data;
  },
};

export default bookingApi;

