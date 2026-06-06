'use client';

import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useRouter } from 'next/navigation';
import { toast } from 'sonner';
import { bookingApi, getErrorMessage, type CreateBookingRequest, type ValidatePassRequest } from '@/lib/api';
import { useBookingStore } from '@/store';

// Query Keys
export const bookingKeys = {
  all: ['bookings'] as const,
  lists: () => [...bookingKeys.all, 'list'] as const,
  userBookings: (page: number) => [...bookingKeys.lists(), 'user', page] as const,
  shopBookings: (shopId: number, date?: string) => [...bookingKeys.lists(), 'shop', shopId, date] as const,
  details: () => [...bookingKeys.all, 'detail'] as const,
  detail: (id: number) => [...bookingKeys.details(), id] as const,
  slots: (shopId: number, date: string, staffId?: number) => [...bookingKeys.all, 'slots', shopId, date, staffId] as const,
  slotsRange: (shopId: number, startDate: string, endDate: string) => [...bookingKeys.all, 'slotsRange', shopId, startDate, endDate] as const,
  todayQueue: (shopId: number) => [...bookingKeys.all, 'today', shopId] as const,
};

// Get available slots
export function useSlots(shopId: number, date: string, staffId?: number) {
  return useQuery({
    queryKey: bookingKeys.slots(shopId, date, staffId),
    queryFn: () => bookingApi.getSlots(shopId, date, staffId),
    staleTime: 30 * 1000, // 30 seconds - slots can change quickly
    enabled: !!shopId && !!date,
    refetchInterval: 60 * 1000, // Refetch every minute
  });
}

// Get slots for date range
export function useSlotsRange(shopId: number, startDate: string, endDate: string, staffId?: number) {
  return useQuery({
    queryKey: bookingKeys.slotsRange(shopId, startDate, endDate),
    queryFn: () => bookingApi.getSlotsRange(shopId, startDate, endDate, staffId),
    staleTime: 30 * 1000,
    enabled: !!shopId && !!startDate && !!endDate,
  });
}

// Lock slot
export function useLockSlot() {
  const { setSlotLock } = useBookingStore();

  return useMutation({
    mutationFn: (slotId: number) => bookingApi.lockSlot(slotId),
    onSuccess: (response) => {
      setSlotLock(response.data.lockToken, new Date(response.data.expiresAt));
      toast.success('Slot reserved for 5 minutes');
    },
    onError: (error) => {
      toast.error(getErrorMessage(error));
    },
  });
}

// Release slot
export function useReleaseSlot() {
  const { setSlotLock } = useBookingStore();

  return useMutation({
    mutationFn: ({ slotId, lockToken }: { slotId: number; lockToken: string }) =>
      bookingApi.releaseSlot(slotId, lockToken),
    onSuccess: () => {
      setSlotLock(null, null);
    },
  });
}

// Create booking
export function useCreateBooking() {
  const router = useRouter();
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: CreateBookingRequest) => bookingApi.create(data),
    onSuccess: (response) => {
      queryClient.invalidateQueries({ queryKey: bookingKeys.lists() });
      router.push(`/payment?bookingId=${response.data.id}`);
    },
    onError: (error) => {
      toast.error(getErrorMessage(error));
    },
  });
}

// Get booking by ID
export function useBooking(id: number) {
  return useQuery({
    queryKey: bookingKeys.detail(id),
    queryFn: () => bookingApi.getById(id),
    staleTime: 60 * 1000,
    enabled: !!id,
  });
}

// Get user bookings
export function useUserBookings(page = 0, size = 10) {
  return useQuery({
    queryKey: bookingKeys.userBookings(page),
    queryFn: () => bookingApi.getUserBookings(page, size),
    staleTime: 60 * 1000,
  });
}

// Get shop bookings (for shop owner)
export function useShopBookings(shopId: number, date?: string, page = 0, size = 10) {
  return useQuery({
    queryKey: bookingKeys.shopBookings(shopId, date),
    queryFn: () => bookingApi.getShopBookings(shopId, date, page, size),
    staleTime: 30 * 1000,
    enabled: !!shopId,
  });
}

// Get today's queue
export function useTodayQueue(shopId: number) {
  return useQuery({
    queryKey: bookingKeys.todayQueue(shopId),
    queryFn: () => bookingApi.getTodayQueue(shopId),
    staleTime: 15 * 1000, // 15 seconds
    enabled: !!shopId,
    refetchInterval: 30 * 1000, // Refresh every 30 seconds
  });
}

// Cancel booking
export function useCancelBooking() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, reason }: { id: number; reason?: string }) =>
      bookingApi.cancel(id, reason),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: bookingKeys.lists() });
      toast.success('Booking cancelled successfully');
    },
    onError: (error) => {
      toast.error(getErrorMessage(error));
    },
  });
}

// Update booking status
export function useUpdateBookingStatus() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, status }: { id: number; status: string }) =>
      bookingApi.updateStatus(id, status),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: bookingKeys.lists() });
      toast.success('Booking status updated');
    },
    onError: (error) => {
      toast.error(getErrorMessage(error));
    },
  });
}

// Validate pass
export function useValidatePass() {
  return useMutation({
    mutationFn: (data: ValidatePassRequest) => bookingApi.validatePass(data),
    onSuccess: (response) => {
      if (response.data.valid) {
        toast.success('Pass validated successfully!');
      } else {
        toast.error(response.data.message);
      }
    },
    onError: (error) => {
      toast.error(getErrorMessage(error));
    },
  });
}

// Mark as completed
export function useMarkCompleted() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (id: number) => bookingApi.markCompleted(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: bookingKeys.lists() });
      toast.success('Booking marked as completed');
    },
    onError: (error) => {
      toast.error(getErrorMessage(error));
    },
  });
}

// Mark as no-show
export function useMarkNoShow() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (id: number) => bookingApi.markNoShow(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: bookingKeys.lists() });
      toast.success('Booking marked as no-show');
    },
    onError: (error) => {
      toast.error(getErrorMessage(error));
    },
  });
}

