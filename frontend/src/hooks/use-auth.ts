'use client';

import { useMutation, useQuery } from '@tanstack/react-query';
import { useRouter } from 'next/navigation';
import { toast } from 'sonner';
import { authApi, userApi, getErrorMessage, type LoginRequest, type RegisterRequest, type OtpRequest } from '@/lib/api';
import { useAuthStore } from '@/store';

export function useLogin() {
  const router = useRouter();
  const { login } = useAuthStore();

  return useMutation({
    mutationFn: (data: LoginRequest) => authApi.login(data),
    onSuccess: (response) => {
      login(response.data.user);
      toast.success('Welcome back!');
      
      // Redirect based on role
      if (response.data.user.role === 'SHOP_OWNER') {
        router.push('/shop/dashboard');
      } else {
        router.push('/dashboard');
      }
    },
    onError: (error) => {
      toast.error(getErrorMessage(error));
    },
  });
}

export function useRegister() {
  const router = useRouter();

  return useMutation({
    mutationFn: (data: RegisterRequest) => authApi.register(data),
    onSuccess: (response, variables) => {
      toast.success(response.message || 'OTP sent to your phone!');
      router.push(`/verify-otp?phone=${encodeURIComponent(variables.phone)}`);
    },
    onError: (error) => {
      toast.error(getErrorMessage(error));
    },
  });
}

export function useVerifyOtp() {
  const router = useRouter();
  const { login } = useAuthStore();

  return useMutation({
    mutationFn: (data: OtpRequest) => authApi.verifyOtp(data),
    onSuccess: (response) => {
      login(response.data.user);
      toast.success('Phone verified successfully!');
      
      if (response.data.user.role === 'SHOP_OWNER') {
        router.push('/shop/register');
      } else {
        router.push('/dashboard');
      }
    },
    onError: (error) => {
      toast.error(getErrorMessage(error));
    },
  });
}

export function useResendOtp() {
  return useMutation({
    mutationFn: (phone: string) => authApi.resendOtp(phone),
    onSuccess: () => {
      toast.success('OTP resent successfully!');
    },
    onError: (error) => {
      toast.error(getErrorMessage(error));
    },
  });
}

export function useForgotPassword() {
  const router = useRouter();

  return useMutation({
    mutationFn: (phone: string) => authApi.forgotPassword({ phone }),
    onSuccess: (_, phone) => {
      toast.success('OTP sent for password reset!');
      router.push(`/reset-password?phone=${encodeURIComponent(phone)}`);
    },
    onError: (error) => {
      toast.error(getErrorMessage(error));
    },
  });
}

export function useResetPassword() {
  const router = useRouter();

  return useMutation({
    mutationFn: (data: { phone: string; otp: string; newPassword: string }) =>
      authApi.resetPassword(data),
    onSuccess: () => {
      toast.success('Password reset successfully!');
      router.push('/login');
    },
    onError: (error) => {
      toast.error(getErrorMessage(error));
    },
  });
}

export function useLogout() {
  const router = useRouter();
  const { logout } = useAuthStore();

  return () => {
    authApi.logout();
    logout();
    toast.success('Logged out successfully');
    router.push('/');
  };
}

export function useCurrentUser() {
  const { setUser, setLoading } = useAuthStore();

  return useQuery({
    queryKey: ['currentUser'],
    queryFn: async () => {
      const response = await userApi.getProfile();
      setUser(response.data);
      return response.data;
    },
    staleTime: 5 * 60 * 1000, // 5 minutes
    retry: false,
    enabled: authApi.isAuthenticated(),
  });
}

export function useUpdateProfile() {
  const { updateProfile } = useAuthStore();

  return useMutation({
    mutationFn: userApi.updateProfile,
    onSuccess: (response) => {
      updateProfile(response.data);
      toast.success('Profile updated successfully!');
    },
    onError: (error) => {
      toast.error(getErrorMessage(error));
    },
  });
}

