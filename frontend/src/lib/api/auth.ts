import apiClient, { ApiResponse, ACCESS_TOKEN_KEY, REFRESH_TOKEN_KEY } from './client';
import Cookies from 'js-cookie';

// Types
export interface RegisterRequest {
  name: string;
  email: string;
  phone: string;
  password: string;
  role: 'USER' | 'SHOP_OWNER';
}

export interface LoginRequest {
  emailOrPhone: string;
  password: string;
}

export interface OtpRequest {
  phone: string;
  otp: string;
}

export interface ForgotPasswordRequest {
  phone: string;
}

export interface ResetPasswordRequest {
  phone: string;
  otp: string;
  newPassword: string;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  user: UserResponse;
}

export interface UserResponse {
  id: number;
  name: string;
  email: string;
  phone: string;
  role: 'USER' | 'SHOP_OWNER' | 'ADMIN';
  profileImage?: string;
  createdAt: string;
}

// Auth API
export const authApi = {
  // Register new user
  register: async (data: RegisterRequest): Promise<ApiResponse<{ message: string }>> => {
    const response = await apiClient.post('/auth/register', data);
    return response.data;
  },

  // Login with email/phone and password
  login: async (data: LoginRequest): Promise<ApiResponse<AuthResponse>> => {
    const response = await apiClient.post('/auth/login', data);
    const { accessToken, refreshToken } = response.data.data;
    
    // Store tokens
    Cookies.set(ACCESS_TOKEN_KEY, accessToken, { expires: 1 });
    Cookies.set(REFRESH_TOKEN_KEY, refreshToken, { expires: 7 });
    
    return response.data;
  },

  // Verify OTP
  verifyOtp: async (data: OtpRequest): Promise<ApiResponse<AuthResponse>> => {
    const response = await apiClient.post('/auth/verify-otp', data);
    const { accessToken, refreshToken } = response.data.data;
    
    // Store tokens
    Cookies.set(ACCESS_TOKEN_KEY, accessToken, { expires: 1 });
    Cookies.set(REFRESH_TOKEN_KEY, refreshToken, { expires: 7 });
    
    return response.data;
  },

  // Resend OTP
  resendOtp: async (phone: string): Promise<ApiResponse<{ message: string }>> => {
    const response = await apiClient.post('/auth/resend-otp', { phone });
    return response.data;
  },

  // Forgot password - Send OTP
  forgotPassword: async (data: ForgotPasswordRequest): Promise<ApiResponse<{ message: string }>> => {
    const response = await apiClient.post('/auth/forgot-password', data);
    return response.data;
  },

  // Reset password with OTP
  resetPassword: async (data: ResetPasswordRequest): Promise<ApiResponse<{ message: string }>> => {
    const response = await apiClient.post('/auth/reset-password', data);
    return response.data;
  },

  // Refresh token
  refreshToken: async (refreshToken: string): Promise<ApiResponse<AuthResponse>> => {
    const response = await apiClient.post('/auth/refresh', { refreshToken });
    return response.data;
  },

  // Logout
  logout: () => {
    Cookies.remove(ACCESS_TOKEN_KEY);
    Cookies.remove(REFRESH_TOKEN_KEY);
  },

  // Check if user is authenticated
  isAuthenticated: (): boolean => {
    return !!Cookies.get(ACCESS_TOKEN_KEY);
  },
};

export default authApi;

