import apiClient, { ApiResponse, PagedResponse } from './client';

// Types
export interface CreateOrderRequest {
  bookingId: number;
  amount: number;
}

export interface CreateOrderResponse {
  orderId: string;
  razorpayOrderId: string;
  amount: number;
  currency: string;
  keyId: string;
}

export interface VerifyPaymentRequest {
  razorpayOrderId: string;
  razorpayPaymentId: string;
  razorpaySignature: string;
  bookingId: number;
}

export interface PaymentResponse {
  id: number;
  bookingId: number;
  userId: number;
  shopId: number;
  amount: number;
  currency: string;
  status: 'PENDING' | 'SUCCESS' | 'FAILED' | 'REFUNDED';
  razorpayOrderId: string;
  razorpayPaymentId?: string;
  method?: string;
  createdAt: string;
  updatedAt: string;
}

export interface TransactionResponse {
  id: number;
  paymentId: number;
  bookingId: number;
  userId: number;
  shopId: number;
  amount: number;
  type: 'PAYMENT' | 'REFUND';
  status: 'PENDING' | 'SUCCESS' | 'FAILED';
  referenceId?: string;
  notes?: string;
  createdAt: string;
}

export interface RefundRequest {
  paymentId: number;
  amount?: number; // Optional for partial refund
  reason?: string;
}

export interface RefundResponse {
  id: string;
  amount: number;
  status: string;
  createdAt: string;
}

// Payment API
export const paymentApi = {
  // Create Razorpay order
  createOrder: async (data: CreateOrderRequest): Promise<ApiResponse<CreateOrderResponse>> => {
    const response = await apiClient.post('/payments/create-order', data);
    return response.data;
  },

  // Verify payment after Razorpay callback
  verifyPayment: async (data: VerifyPaymentRequest): Promise<ApiResponse<PaymentResponse>> => {
    const response = await apiClient.post('/payments/verify', data);
    return response.data;
  },

  // Get payment by ID
  getById: async (id: number): Promise<ApiResponse<PaymentResponse>> => {
    const response = await apiClient.get(`/payments/${id}`);
    return response.data;
  },

  // Get payment by booking ID
  getByBookingId: async (bookingId: number): Promise<ApiResponse<PaymentResponse>> => {
    const response = await apiClient.get(`/payments/booking/${bookingId}`);
    return response.data;
  },

  // Get user transactions
  getUserTransactions: async (page = 0, size = 10): Promise<ApiResponse<PagedResponse<TransactionResponse>>> => {
    const response = await apiClient.get('/transactions', { params: { page, size } });
    return response.data;
  },

  // Get shop transactions (for shop owner)
  getShopTransactions: async (shopId: number, page = 0, size = 10): Promise<ApiResponse<PagedResponse<TransactionResponse>>> => {
    const response = await apiClient.get(`/transactions/shop/${shopId}`, { params: { page, size } });
    return response.data;
  },

  // Request refund
  requestRefund: async (data: RefundRequest): Promise<ApiResponse<RefundResponse>> => {
    const response = await apiClient.post('/payments/refund', data);
    return response.data;
  },
};

// Razorpay checkout helper
export interface RazorpayOptions {
  key: string;
  amount: number;
  currency: string;
  name: string;
  description: string;
  order_id: string;
  handler: (response: RazorpayResponse) => void;
  prefill?: {
    name?: string;
    email?: string;
    contact?: string;
  };
  theme?: {
    color?: string;
  };
  modal?: {
    ondismiss?: () => void;
  };
}

export interface RazorpayResponse {
  razorpay_order_id: string;
  razorpay_payment_id: string;
  razorpay_signature: string;
}

declare global {
  interface Window {
    Razorpay: new (options: RazorpayOptions) => {
      open: () => void;
      close: () => void;
    };
  }
}

export const loadRazorpay = (): Promise<boolean> => {
  return new Promise((resolve) => {
    if (typeof window !== 'undefined' && window.Razorpay) {
      resolve(true);
      return;
    }

    const script = document.createElement('script');
    script.src = 'https://checkout.razorpay.com/v1/checkout.js';
    script.onload = () => resolve(true);
    script.onerror = () => resolve(false);
    document.body.appendChild(script);
  });
};

export default paymentApi;

