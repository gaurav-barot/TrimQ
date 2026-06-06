export { default as apiClient, getErrorMessage, type ApiResponse, type PagedResponse, type ApiError } from './client';
export { default as authApi, type RegisterRequest, type LoginRequest, type AuthResponse, type UserResponse } from './auth';
export { default as userApi, type UpdateProfileRequest } from './user';
export { default as shopApi, type ShopResponse, type ShopServiceResponse, type StaffResponse, type ShopSearchRequest } from './shop';
export { default as bookingApi, type BookingResponse, type SlotResponse, type DaySlotResponse, type CreateBookingRequest, type TodayQueueResponse } from './booking';
export { default as paymentApi, type CreateOrderRequest, type CreateOrderResponse, type PaymentResponse, type TransactionResponse, loadRazorpay } from './payment';

