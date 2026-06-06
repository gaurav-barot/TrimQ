package com.trimq.payment.controller;

import com.trimq.common.dto.ApiResponse;
import com.trimq.common.dto.PagedResponse;
import com.trimq.payment.dto.*;
import com.trimq.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.trimq.common.constants.AppConstants.*;

/**
 * REST controller for payment operations.
 */
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Payment processing APIs")
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Create a payment order.
     */
    @PostMapping("/create-order")
    @Operation(summary = "Create a Razorpay order")
    public ResponseEntity<ApiResponse<CreateOrderResponse>> createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        
        CreateOrderResponse response = paymentService.createOrder(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Payment order created", response));
    }

    /**
     * Verify payment after Razorpay checkout.
     */
    @PostMapping("/verify")
    @Operation(summary = "Verify payment after Razorpay checkout")
    public ResponseEntity<ApiResponse<PaymentResponse>> verifyPayment(
            @Valid @RequestBody VerifyPaymentRequest request) {
        
        PaymentResponse response = paymentService.verifyPayment(request);
        return ResponseEntity.ok(ApiResponse.success("Payment verified", response));
    }

    /**
     * Initiate a refund.
     */
    @PostMapping("/refund")
    @Operation(summary = "Initiate a refund")
    public ResponseEntity<ApiResponse<RefundResponse>> initiateRefund(
            @Valid @RequestBody RefundRequest request) {
        
        RefundResponse response = paymentService.initiateRefund(request);
        return ResponseEntity.ok(ApiResponse.success("Refund initiated", response));
    }

    /**
     * Get payment by ID.
     */
    @GetMapping("/{paymentId}")
    @Operation(summary = "Get payment by ID")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPayment(
            @PathVariable Long paymentId) {
        
        PaymentResponse response = paymentService.getPayment(paymentId);
        return ResponseEntity.ok(ApiResponse.success("Payment retrieved", response));
    }

    /**
     * Get payment by booking ID.
     */
    @GetMapping("/booking/{bookingId}")
    @Operation(summary = "Get payment by booking ID")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentByBooking(
            @PathVariable Long bookingId) {
        
        PaymentResponse response = paymentService.getPaymentByBooking(bookingId);
        return ResponseEntity.ok(ApiResponse.success("Payment retrieved", response));
    }

    /**
     * Get user's payments.
     */
    @GetMapping("/user")
    @Operation(summary = "Get current user's payments")
    public ResponseEntity<ApiResponse<PagedResponse<PaymentResponse>>> getUserPayments(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        
        PagedResponse<PaymentResponse> response = 
                paymentService.getUserPayments(userId, pageNo, pageSize);
        return ResponseEntity.ok(ApiResponse.success("Payments retrieved", response));
    }

    /**
     * Get shop's transactions.
     */
    @GetMapping("/transactions")
    @Operation(summary = "Get shop's transaction history")
    public ResponseEntity<ApiResponse<PagedResponse<TransactionResponse>>> getShopTransactions(
            @RequestParam Long shopId,
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        
        PagedResponse<TransactionResponse> response = 
                paymentService.getShopTransactions(shopId, pageNo, pageSize);
        return ResponseEntity.ok(ApiResponse.success("Transactions retrieved", response));
    }
}

