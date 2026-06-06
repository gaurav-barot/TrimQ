'use client';

import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';
import { shopApi, getErrorMessage, type ShopSearchRequest, type CreateShopRequest, type CreateServiceRequest, type CreateStaffRequest } from '@/lib/api';

// Query Keys
export const shopKeys = {
  all: ['shops'] as const,
  lists: () => [...shopKeys.all, 'list'] as const,
  list: (filters: ShopSearchRequest) => [...shopKeys.lists(), filters] as const,
  details: () => [...shopKeys.all, 'detail'] as const,
  detail: (id: number) => [...shopKeys.details(), id] as const,
  myShop: () => [...shopKeys.all, 'my'] as const,
  services: (shopId: number) => [...shopKeys.all, 'services', shopId] as const,
  staff: (shopId: number) => [...shopKeys.all, 'staff', shopId] as const,
};

// Search shops
export function useSearchShops(params: ShopSearchRequest) {
  return useQuery({
    queryKey: shopKeys.list(params),
    queryFn: () => shopApi.search(params),
    staleTime: 2 * 60 * 1000, // 2 minutes
  });
}

// Get shop by ID
export function useShop(id: number) {
  return useQuery({
    queryKey: shopKeys.detail(id),
    queryFn: () => shopApi.getById(id),
    staleTime: 5 * 60 * 1000, // 5 minutes
    enabled: !!id,
  });
}

// Get my shop (for shop owner)
export function useMyShop() {
  return useQuery({
    queryKey: shopKeys.myShop(),
    queryFn: () => shopApi.getMyShop(),
    staleTime: 5 * 60 * 1000,
    retry: false,
  });
}

// Create shop
export function useCreateShop() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (data: CreateShopRequest) => shopApi.create(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: shopKeys.myShop() });
      toast.success('Shop created successfully!');
    },
    onError: (error) => {
      toast.error(getErrorMessage(error));
    },
  });
}

// Update shop
export function useUpdateShop() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, data }: { id: number; data: Partial<CreateShopRequest> }) =>
      shopApi.update(id, data),
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: shopKeys.detail(variables.id) });
      queryClient.invalidateQueries({ queryKey: shopKeys.myShop() });
      toast.success('Shop updated successfully!');
    },
    onError: (error) => {
      toast.error(getErrorMessage(error));
    },
  });
}

// Get shop services
export function useShopServices(shopId: number) {
  return useQuery({
    queryKey: shopKeys.services(shopId),
    queryFn: () => shopApi.getServices(shopId),
    staleTime: 5 * 60 * 1000,
    enabled: !!shopId,
  });
}

// Create service
export function useCreateService() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ shopId, data }: { shopId: number; data: CreateServiceRequest }) =>
      shopApi.createService(shopId, data),
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: shopKeys.services(variables.shopId) });
      toast.success('Service added successfully!');
    },
    onError: (error) => {
      toast.error(getErrorMessage(error));
    },
  });
}

// Update service
export function useUpdateService() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, shopId, data }: { id: number; shopId: number; data: Partial<CreateServiceRequest> }) =>
      shopApi.updateService(id, data),
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: shopKeys.services(variables.shopId) });
      toast.success('Service updated successfully!');
    },
    onError: (error) => {
      toast.error(getErrorMessage(error));
    },
  });
}

// Delete service
export function useDeleteService() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, shopId }: { id: number; shopId: number }) => shopApi.deleteService(id),
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: shopKeys.services(variables.shopId) });
      toast.success('Service deleted successfully!');
    },
    onError: (error) => {
      toast.error(getErrorMessage(error));
    },
  });
}

// Get shop staff
export function useShopStaff(shopId: number) {
  return useQuery({
    queryKey: shopKeys.staff(shopId),
    queryFn: () => shopApi.getStaff(shopId),
    staleTime: 5 * 60 * 1000,
    enabled: !!shopId,
  });
}

// Create staff
export function useCreateStaff() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ shopId, data }: { shopId: number; data: CreateStaffRequest }) =>
      shopApi.createStaff(shopId, data),
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: shopKeys.staff(variables.shopId) });
      toast.success('Staff added successfully!');
    },
    onError: (error) => {
      toast.error(getErrorMessage(error));
    },
  });
}

// Update staff
export function useUpdateStaff() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, shopId, data }: { id: number; shopId: number; data: Partial<CreateStaffRequest> }) =>
      shopApi.updateStaff(id, data),
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: shopKeys.staff(variables.shopId) });
      toast.success('Staff updated successfully!');
    },
    onError: (error) => {
      toast.error(getErrorMessage(error));
    },
  });
}

// Delete staff
export function useDeleteStaff() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, shopId }: { id: number; shopId: number }) => shopApi.deleteStaff(id),
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: shopKeys.staff(variables.shopId) });
      toast.success('Staff removed successfully!');
    },
    onError: (error) => {
      toast.error(getErrorMessage(error));
    },
  });
}

