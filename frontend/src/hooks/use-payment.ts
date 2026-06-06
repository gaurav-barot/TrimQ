'use client';

import { useState } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useRouter } from 'next/navigation';
import { toast } from 'sonner';
import { paymentApi, loadRazorpay, getErrorMessage, type CreateOrderRequest, type VerifyPaymentRequest } from '@/lib/api';
import { useAuthStore, useBookingStore } from '@/store';

// Query Keys
export const paymentKeys = {
  all: ['payments'] as const,
  transactions: (page: number) => [...paymentKeys.all, 'transactions', page] as const,
  shopTransactions: (shopId: number, page: number) => [...paymentKeys.all, 'shopTransactions', shopId, page] as const,
  byBooking: (bookingId: number) => [...paymentKeys.all, 'booking', bookingId] as const,
};

// Create order and initiate payment
export function usePayment() {
  const router = useRouter();
  const queryClient = useQueryClient();
  const { user } = useAuthStore();
  const { reset: resetBooking } = useBookingStore();
  const [isProcessing, setIsProcessing] = useState(false);

  const createOrderMutation = useMutation({
    mutationFn: (data: CreateOrderRequest) => paymentApi.createOrder(data),
  });

  const verifyPaymentMutation = useMutation({
    mutationFn: (data: VerifyPaymentRequest) => paymentApi.verifyPayment(data),
    onSuccess: (response) => {
      queryClient.invalidateQueries({ queryKey: ['bookings'] });
      resetBooking();
      toast.success('Payment successful!');
      router.push(`/pass/${response.data.bookingId}`);
    },
    onError: (error) => {
      toast.error(getErrorMessage(error));
      router.push('/booking/failed');
    },
  });

  const initiatePayment = async (bookingId: number, amount: number) => {
    setIsProcessing(true);

    try {
      // Load Razorpay script
      const loaded = await loadRazorpay();
      if (!loaded) {
        toast.error('Failed to load payment gateway');
        setIsProcessing(false);
        return;
      }

      // Create order
      const orderResponse = await createOrderMutation.mutateAsync({ bookingId, amount });
      const { razorpayOrderId, keyId, currency } = orderResponse.data;

      // Open Razorpay checkout
      const options = {
        key: keyId,
        amount: amount * 100, // Razorpay expects amount in paise
        currency,
        name: 'TrimQ',
        description: 'Salon Booking Payment',
        order_id: razorpayOrderId,
        handler: async (response: {
          razorpay_order_id: string;
          razorpay_payment_id: string;
          razorpay_signature: string;
        }) => {
          // Verify payment
          await verifyPaymentMutation.mutateAsync({
            razorpayOrderId: response.razorpay_order_id,
            razorpayPaymentId: response.razorpay_payment_id,
            razorpaySignature: response.razorpay_signature,
            bookingId,
          });
        },
        prefill: {
          name: user?.name,
          email: user?.email,
          contact: user?.phone,
        },
        theme: {
          color: '#7c3aed',
        },
        modal: {
          ondismiss: () => {
            setIsProcessing(false);
            toast.info('Payment cancelled');
          },
        },
      };

      const razorpay = new window.Razorpay(options);
      razorpay.open();
    } catch (error) {
      toast.error(getErrorMessage(error));
    } finally {
      setIsProcessing(false);
    }
  };

  return {
    initiatePayment,
    isProcessing: isProcessing || createOrderMutation.isPending || verifyPaymentMutation.isPending,
  };
}

// Get payment by booking ID
export function usePaymentByBooking(bookingId: number) {
  return useQuery({
    queryKey: paymentKeys.byBooking(bookingId),
    queryFn: () => paymentApi.getByBookingId(bookingId),
    staleTime: 60 * 1000,
    enabled: !!bookingId,
  });
}

// Get user transactions
export function useUserTransactions(page = 0, size = 10) {
  return useQuery({
    queryKey: paymentKeys.transactions(page),
    queryFn: () => paymentApi.getUserTransactions(page, size),
    staleTime: 60 * 1000,
  });
}

// Get shop transactions
export function useShopTransactions(shopId: number, page = 0, size = 10) {
  return useQuery({
    queryKey: paymentKeys.shopTransactions(shopId, page),
    queryFn: () => paymentApi.getShopTransactions(shopId, page, size),
    staleTime: 60 * 1000,
    enabled: !!shopId,
  });
}

// Request refund
export function useRequestRefund() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: paymentApi.requestRefund,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: paymentKeys.all });
      queryClient.invalidateQueries({ queryKey: ['bookings'] });
      toast.success('Refund request submitted');
    },
    onError: (error) => {
      toast.error(getErrorMessage(error));
    },
  });
}

