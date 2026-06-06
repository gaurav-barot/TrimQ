import apiClient, { ApiResponse } from './client';
import { UserResponse } from './auth';

// Types
export interface UpdateProfileRequest {
  name?: string;
  email?: string;
  profileImage?: string;
}

export interface ChangePasswordRequest {
  currentPassword: string;
  newPassword: string;
}

// User API
export const userApi = {
  // Get current user profile
  getProfile: async (): Promise<ApiResponse<UserResponse>> => {
    const response = await apiClient.get('/users/me');
    return response.data;
  },

  // Update profile
  updateProfile: async (data: UpdateProfileRequest): Promise<ApiResponse<UserResponse>> => {
    const response = await apiClient.put('/users/me', data);
    return response.data;
  },

  // Change password
  changePassword: async (data: ChangePasswordRequest): Promise<ApiResponse<{ message: string }>> => {
    const response = await apiClient.post('/users/me/change-password', data);
    return response.data;
  },

  // Upload profile image
  uploadProfileImage: async (file: File): Promise<ApiResponse<{ imageUrl: string }>> => {
    const formData = new FormData();
    formData.append('file', file);
    
    const response = await apiClient.post('/users/me/image', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    return response.data;
  },
};

export default userApi;

