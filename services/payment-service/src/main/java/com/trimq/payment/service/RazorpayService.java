package com.trimq.payment.service;

import com.razorpay.*;
import com.trimq.payment.config.RazorpayConfig;
import com.trimq.payment.dto.CreateOrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;

/**
 * Service for Razorpay operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RazorpayService {

    private final RazorpayClient razorpayClient;
    private final RazorpayConfig razorpayConfig;

    private static final String HMAC_SHA256 = "HmacSHA256";

    /**
     * Create a Razorpay order.
     * 
     * @param request Order request
     * @param receipt Internal receipt number
     * @return Razorpay Order object
     */
    public Order createOrder(CreateOrderRequest request, String receipt) throws RazorpayException {
        // Convert to paise (Razorpay expects amount in smallest currency unit)
        long amountInPaise = request.getAmount().multiply(BigDecimal.valueOf(100)).longValue();

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", razorpayConfig.getCurrency());
        orderRequest.put("receipt", receipt);
        orderRequest.put("payment_capture", 1); // Auto capture

        // Add notes
        JSONObject notes = new JSONObject();
        notes.put("booking_id", request.getBookingId().toString());
        notes.put("shop_id", request.getShopId().toString());
        if (request.getDescription() != null) {
            notes.put("description", request.getDescription());
        }
        orderRequest.put("notes", notes);

        log.info("Creating Razorpay order: amount={}, receipt={}", amountInPaise, receipt);
        
        Order order = razorpayClient.orders.create(orderRequest);
        log.info("Razorpay order created: {}", order.get("id"));
        
        return order;
    }

    /**
     * Verify payment signature.
     * 
     * @param orderId Razorpay order ID
     * @param paymentId Razorpay payment ID
     * @param signature Signature to verify
     * @return true if valid
     */
    public boolean verifyPaymentSignature(String orderId, String paymentId, String signature) {
        try {
            String data = orderId + "|" + paymentId;
            String generatedSignature = generateHmacSha256(data, razorpayConfig.getKeySecret());
            
            boolean valid = generatedSignature.equals(signature);
            log.info("Payment signature verification: orderId={}, valid={}", orderId, valid);
            
            return valid;
        } catch (Exception e) {
            log.error("Failed to verify payment signature", e);
            return false;
        }
    }

    /**
     * Verify webhook signature.
     * 
     * @param body Request body
     * @param signature X-Razorpay-Signature header
     * @return true if valid
     */
    public boolean verifyWebhookSignature(String body, String signature) {
        try {
            String generatedSignature = generateHmacSha256(body, razorpayConfig.getWebhookSecret());
            
            boolean valid = generatedSignature.equals(signature);
            log.debug("Webhook signature verification: valid={}", valid);
            
            return valid;
        } catch (Exception e) {
            log.error("Failed to verify webhook signature", e);
            return false;
        }
    }

    /**
     * Fetch payment details from Razorpay.
     * 
     * @param paymentId Razorpay payment ID
     * @return Payment object
     */
    public Payment fetchPayment(String paymentId) throws RazorpayException {
        log.info("Fetching payment details: {}", paymentId);
        return razorpayClient.payments.fetch(paymentId);
    }

    /**
     * Create a refund.
     * 
     * @param paymentId Razorpay payment ID
     * @param amount Amount to refund (in rupees)
     * @param reason Refund reason
     * @param speed "normal" or "optimum"
     * @return Refund object
     */
    public Refund createRefund(String paymentId, BigDecimal amount, String reason, String speed) 
            throws RazorpayException {
        
        JSONObject refundRequest = new JSONObject();
        
        if (amount != null) {
            long amountInPaise = amount.multiply(BigDecimal.valueOf(100)).longValue();
            refundRequest.put("amount", amountInPaise);
        }
        
        if (reason != null) {
            JSONObject notes = new JSONObject();
            notes.put("reason", reason);
            refundRequest.put("notes", notes);
        }
        
        if (speed != null) {
            refundRequest.put("speed", speed);
        }

        log.info("Creating refund: paymentId={}, amount={}", paymentId, amount);
        
        Refund refund = razorpayClient.payments.refund(paymentId, refundRequest);
        log.info("Refund created: {}", refund.get("id"));
        
        return refund;
    }

    /**
     * Fetch refund details.
     * 
     * @param paymentId Razorpay payment ID
     * @param refundId Razorpay refund ID
     * @return Refund object
     */
    public Refund fetchRefund(String paymentId, String refundId) throws RazorpayException {
        return razorpayClient.payments.fetchRefund(paymentId, refundId);
    }

    /**
     * Generate HMAC-SHA256 signature.
     */
    private String generateHmacSha256(String data, String secret) throws Exception {
        Mac mac = Mac.getInstance(HMAC_SHA256);
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), HMAC_SHA256);
        mac.init(secretKey);
        byte[] hash = mac.doFinal(data.getBytes());
        return Hex.encodeHexString(hash);
    }
}

