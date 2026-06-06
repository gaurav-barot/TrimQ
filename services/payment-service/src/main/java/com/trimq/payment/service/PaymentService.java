package com.trimq.payment.service;

import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayException;
import com.razorpay.Refund;
import com.trimq.common.dto.PagedResponse;
import com.trimq.common.enums.PaymentStatus;
import com.trimq.common.exception.BadRequestException;
import com.trimq.common.exception.ResourceNotFoundException;
import com.trimq.payment.config.RazorpayConfig;
import com.trimq.payment.dto.*;
import com.trimq.payment.entity.Transaction;
import com.trimq.payment.mapper.PaymentMapper;
import com.trimq.payment.repository.PaymentRepository;
import com.trimq.payment.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Main payment service for handling all payment operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final TransactionRepository transactionRepository;
    private final PaymentMapper paymentMapper;
    private final RazorpayService razorpayService;
    private final RazorpayConfig razorpayConfig;
    private final PaymentEventPublisher eventPublisher;
    private final BookingServiceClient bookingServiceClient;

    private final AtomicLong receiptCounter = new AtomicLong(System.currentTimeMillis() % 100000);

    /**
     * Create a payment order.
     * 
     * @param request Create order request
     * @param userId User ID from JWT
     * @return Order response with Razorpay details
     */
    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest request, Long userId) {
        log.info("Creating payment order: bookingId={}, amount={}", 
                request.getBookingId(), request.getAmount());

        // Check if payment already exists for this booking
        paymentRepository.findByBookingId(request.getBookingId())
                .ifPresent(existing -> {
                    if (existing.getStatus() == PaymentStatus.SUCCESS) {
                        throw new BadRequestException("Payment already completed for this booking");
                    }
                    // If pending, we can create a new order
                    if (existing.getStatus() == PaymentStatus.PENDING) {
                        log.info("Replacing pending payment for booking: {}", request.getBookingId());
                        existing.setStatus(PaymentStatus.FAILED);
                        existing.setFailureReason("Replaced by new payment order");
                        paymentRepository.save(existing);
                    }
                });

        // Generate receipt
        String receipt = razorpayConfig.getReceiptPrefix() + "-" + receiptCounter.incrementAndGet();

        try {
            // Create Razorpay order
            Order razorpayOrder = razorpayService.createOrder(request, receipt);
            String razorpayOrderId = razorpayOrder.get("id");

            // Create payment record
            com.trimq.payment.entity.Payment payment = com.trimq.payment.entity.Payment.builder()
                    .bookingId(request.getBookingId())
                    .userId(userId)
                    .shopId(request.getShopId())
                    .amount(request.getAmount())
                    .currency(razorpayConfig.getCurrency())
                    .razorpayOrderId(razorpayOrderId)
                    .receipt(receipt)
                    .status(PaymentStatus.PENDING)
                    .customerName(request.getCustomerName())
                    .customerEmail(request.getCustomerEmail())
                    .customerPhone(request.getCustomerPhone())
                    .description(request.getDescription())
                    .build();

            payment = paymentRepository.save(payment);

            // Log transaction
            logTransaction(payment, Transaction.TransactionType.ORDER_CREATED, 
                    "Payment order created", razorpayOrder.toString());

            // Build response
            long amountInPaise = request.getAmount().multiply(BigDecimal.valueOf(100)).longValue();

            return CreateOrderResponse.builder()
                    .paymentId(payment.getId())
                    .razorpayOrderId(razorpayOrderId)
                    .razorpayKeyId(razorpayConfig.getKeyId())
                    .amount(request.getAmount())
                    .amountInPaise(amountInPaise)
                    .currency(razorpayConfig.getCurrency())
                    .receipt(receipt)
                    .customerName(request.getCustomerName())
                    .customerEmail(request.getCustomerEmail())
                    .customerPhone(request.getCustomerPhone())
                    .bookingId(request.getBookingId())
                    .description(request.getDescription())
                    .build();

        } catch (RazorpayException e) {
            log.error("Failed to create Razorpay order", e);
            throw new BadRequestException("Failed to create payment order: " + e.getMessage());
        }
    }

    /**
     * Verify payment after Razorpay checkout.
     * 
     * @param request Verification request
     * @return Payment response
     */
    @Transactional
    public PaymentResponse verifyPayment(VerifyPaymentRequest request) {
        log.info("Verifying payment: orderId={}, paymentId={}", 
                request.getRazorpayOrderId(), request.getRazorpayPaymentId());

        // Find payment by order ID
        com.trimq.payment.entity.Payment payment = paymentRepository
                .findByRazorpayOrderId(request.getRazorpayOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "orderId", 
                        request.getRazorpayOrderId()));

        // Verify signature
        boolean isValid = razorpayService.verifyPaymentSignature(
                request.getRazorpayOrderId(),
                request.getRazorpayPaymentId(),
                request.getRazorpaySignature());

        if (!isValid) {
            log.warn("Invalid payment signature: orderId={}", request.getRazorpayOrderId());
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureReason("Invalid payment signature");
            payment = paymentRepository.save(payment);
            
            logTransaction(payment, Transaction.TransactionType.PAYMENT_FAILED, 
                    "Invalid signature", null);
            
            eventPublisher.publishPaymentFailed(payment);
            
            throw new BadRequestException("Invalid payment signature");
        }

        // Fetch payment details from Razorpay
        try {
            Payment razorpayPayment = razorpayService.fetchPayment(request.getRazorpayPaymentId());
            
            // Update payment with details
            payment.setRazorpayPaymentId(request.getRazorpayPaymentId());
            payment.setRazorpaySignature(request.getRazorpaySignature());
            payment.setPaymentMethod(razorpayPayment.get("method"));
            
            // Set method-specific details
            String method = razorpayPayment.get("method");
            if ("upi".equals(method)) {
                payment.setVpa(razorpayPayment.get("vpa"));
            } else if ("card".equals(method)) {
                JSONObject card = razorpayPayment.get("card");
                if (card != null) {
                    payment.setCardLast4(card.optString("last4"));
                    payment.setCardNetwork(card.optString("network"));
                }
            } else if ("netbanking".equals(method)) {
                payment.setBank(razorpayPayment.get("bank"));
            } else if ("wallet".equals(method)) {
                payment.setWallet(razorpayPayment.get("wallet"));
            }

            // Update status
            String status = razorpayPayment.get("status");
            if ("captured".equals(status) || "authorized".equals(status)) {
                payment.setStatus(PaymentStatus.SUCCESS);
                payment = paymentRepository.save(payment);
                
                logTransaction(payment, Transaction.TransactionType.PAYMENT_SUCCESS, 
                        "Payment successful", razorpayPayment.toString());
                
                // Confirm booking
                bookingServiceClient.confirmBooking(
                        payment.getBookingId(),
                        payment.getRazorpayPaymentId(),
                        payment.getRazorpayOrderId());
                
                // Publish event
                eventPublisher.publishPaymentSuccess(payment);
                
                log.info("Payment successful: paymentId={}", payment.getId());
            } else {
                payment.setStatus(PaymentStatus.FAILED);
                payment.setFailureReason("Payment status: " + status);
                payment = paymentRepository.save(payment);
                
                logTransaction(payment, Transaction.TransactionType.PAYMENT_FAILED, 
                        "Payment status: " + status, razorpayPayment.toString());
                
                eventPublisher.publishPaymentFailed(payment);
            }

        } catch (RazorpayException e) {
            log.error("Failed to fetch payment details", e);
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureReason("Failed to verify: " + e.getMessage());
            payment = paymentRepository.save(payment);
            
            eventPublisher.publishPaymentFailed(payment);
        }

        return paymentMapper.toPaymentResponse(payment);
    }

    /**
     * Process Razorpay webhook.
     * 
     * @param payload Webhook payload
     * @param signature Razorpay signature header
     */
    @Transactional
    public void processWebhook(WebhookPayload payload, String signature, String rawBody) {
        log.info("Processing webhook: event={}", payload.getEvent());

        // Verify signature
        if (!razorpayService.verifyWebhookSignature(rawBody, signature)) {
            log.warn("Invalid webhook signature");
            throw new BadRequestException("Invalid webhook signature");
        }

        String event = payload.getEvent();

        switch (event) {
            case "payment.captured":
            case "payment.authorized":
                handlePaymentSuccess(payload);
                break;
            case "payment.failed":
                handlePaymentFailed(payload);
                break;
            case "refund.created":
            case "refund.processed":
                handleRefundProcessed(payload);
                break;
            default:
                log.debug("Unhandled webhook event: {}", event);
        }
    }

    /**
     * Initiate a refund.
     * 
     * @param request Refund request
     * @return Refund response
     */
    @Transactional
    public RefundResponse initiateRefund(RefundRequest request) {
        log.info("Initiating refund: paymentId={}, amount={}", 
                request.getPaymentId(), request.getAmount());

        com.trimq.payment.entity.Payment payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", request.getPaymentId()));

        if (!payment.isRefundable()) {
            throw new BadRequestException("Payment is not refundable. Status: " + payment.getStatus());
        }

        BigDecimal refundAmount = request.getAmount() != null ? request.getAmount() : payment.getAmount();

        if (refundAmount.compareTo(payment.getAmount()) > 0) {
            throw new BadRequestException("Refund amount cannot exceed payment amount");
        }

        try {
            // Create refund in Razorpay
            Refund refund = razorpayService.createRefund(
                    payment.getRazorpayPaymentId(),
                    refundAmount,
                    request.getReason(),
                    request.getSpeed());

            String refundId = refund.get("id");

            // Update payment
            payment.setRefundId(refundId);
            payment.setRefundAmount(refundAmount);
            payment.setStatus(PaymentStatus.REFUNDED);
            payment = paymentRepository.save(payment);

            // Log transaction
            logTransaction(payment, Transaction.TransactionType.REFUND_SUCCESS, 
                    request.getReason(), refund.toString());

            // Publish event
            eventPublisher.publishRefundProcessed(payment);

            return RefundResponse.builder()
                    .paymentId(payment.getId())
                    .refundId(refundId)
                    .refundAmount(refundAmount)
                    .status("processed")
                    .speed(request.getSpeed())
                    .reason(request.getReason())
                    .createdAt(LocalDateTime.now())
                    .message("Refund initiated successfully")
                    .build();

        } catch (RazorpayException e) {
            log.error("Failed to create refund", e);
            
            logTransaction(payment, Transaction.TransactionType.REFUND_FAILED, 
                    e.getMessage(), null);
            
            throw new BadRequestException("Failed to process refund: " + e.getMessage());
        }
    }

    /**
     * Get payment by ID.
     */
    @Transactional(readOnly = true)
    public PaymentResponse getPayment(Long paymentId) {
        com.trimq.payment.entity.Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", paymentId));
        return paymentMapper.toPaymentResponse(payment);
    }

    /**
     * Get payment by booking ID.
     */
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByBooking(Long bookingId) {
        com.trimq.payment.entity.Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "bookingId", bookingId));
        return paymentMapper.toPaymentResponse(payment);
    }

    /**
     * Get user's payments.
     */
    @Transactional(readOnly = true)
    public PagedResponse<PaymentResponse> getUserPayments(Long userId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<com.trimq.payment.entity.Payment> page = 
                paymentRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);

        List<PaymentResponse> content = page.getContent().stream()
                .map(paymentMapper::toPaymentResponse)
                .toList();

        return PagedResponse.<PaymentResponse>builder()
                .content(content)
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    /**
     * Get shop's transactions.
     */
    @Transactional(readOnly = true)
    public PagedResponse<TransactionResponse> getShopTransactions(Long shopId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Transaction> page = 
                transactionRepository.findByShopIdOrderByCreatedAtDesc(shopId, pageable);

        List<TransactionResponse> content = page.getContent().stream()
                .map(paymentMapper::toTransactionResponse)
                .toList();

        return PagedResponse.<TransactionResponse>builder()
                .content(content)
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    // ==================== Private Methods ====================

    private void handlePaymentSuccess(WebhookPayload payload) {
        Map<String, Object> paymentEntity = payload.getPaymentEntity();
        if (paymentEntity == null) return;

        String orderId = (String) paymentEntity.get("order_id");
        String paymentId = (String) paymentEntity.get("id");

        paymentRepository.findByRazorpayOrderId(orderId).ifPresent(payment -> {
            if (payment.getStatus() != PaymentStatus.SUCCESS) {
                payment.setRazorpayPaymentId(paymentId);
                payment.setStatus(PaymentStatus.SUCCESS);
                payment.setPaymentMethod((String) paymentEntity.get("method"));
                paymentRepository.save(payment);

                logTransaction(payment, Transaction.TransactionType.PAYMENT_SUCCESS, 
                        "Webhook: payment successful", paymentEntity.toString());

                bookingServiceClient.confirmBooking(
                        payment.getBookingId(), paymentId, orderId);

                eventPublisher.publishPaymentSuccess(payment);
                
                log.info("Payment confirmed via webhook: orderId={}", orderId);
            }
        });
    }

    private void handlePaymentFailed(WebhookPayload payload) {
        Map<String, Object> paymentEntity = payload.getPaymentEntity();
        if (paymentEntity == null) return;

        String orderId = (String) paymentEntity.get("order_id");
        @SuppressWarnings("unchecked")
        Map<String, Object> errorInfo = (Map<String, Object>) paymentEntity.get("error");

        paymentRepository.findByRazorpayOrderId(orderId).ifPresent(payment -> {
            if (payment.getStatus() == PaymentStatus.PENDING) {
                payment.setStatus(PaymentStatus.FAILED);
                if (errorInfo != null) {
                    payment.setErrorCode((String) errorInfo.get("code"));
                    payment.setFailureReason((String) errorInfo.get("description"));
                }
                paymentRepository.save(payment);

                logTransaction(payment, Transaction.TransactionType.PAYMENT_FAILED, 
                        "Webhook: payment failed", paymentEntity.toString());

                eventPublisher.publishPaymentFailed(payment);
                
                log.info("Payment failed via webhook: orderId={}", orderId);
            }
        });
    }

    private void handleRefundProcessed(WebhookPayload payload) {
        Map<String, Object> refundEntity = payload.getRefundEntity();
        if (refundEntity == null) return;

        String paymentId = (String) refundEntity.get("payment_id");
        String refundId = (String) refundEntity.get("id");
        Object amountObj = refundEntity.get("amount");
        
        paymentRepository.findByRazorpayPaymentId(paymentId).ifPresent(payment -> {
            payment.setRefundId(refundId);
            if (amountObj != null) {
                long amountInPaise = ((Number) amountObj).longValue();
                payment.setRefundAmount(BigDecimal.valueOf(amountInPaise / 100.0));
            }
            payment.setStatus(PaymentStatus.REFUNDED);
            paymentRepository.save(payment);

            logTransaction(payment, Transaction.TransactionType.REFUND_SUCCESS, 
                    "Webhook: refund processed", refundEntity.toString());

            eventPublisher.publishRefundProcessed(payment);
            
            log.info("Refund processed via webhook: paymentId={}", paymentId);
        });
    }

    private void logTransaction(com.trimq.payment.entity.Payment payment, 
                                Transaction.TransactionType type, 
                                String description, 
                                String rawResponse) {
        Transaction transaction = Transaction.builder()
                .paymentId(payment.getId())
                .shopId(payment.getShopId())
                .type(type)
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .status(payment.getStatus().name())
                .externalRef(payment.getRazorpayPaymentId() != null ? 
                        payment.getRazorpayPaymentId() : payment.getRazorpayOrderId())
                .description(description)
                .rawResponse(rawResponse)
                .build();

        transactionRepository.save(transaction);
    }
}

