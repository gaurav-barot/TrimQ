package com.trimq.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trimq.common.dto.ApiResponse;
import com.trimq.payment.dto.WebhookPayload;
import com.trimq.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for Razorpay webhooks.
 * 
 * This endpoint is public (no JWT required) but verified using Razorpay signature.
 */
@RestController
@RequestMapping("/payments/webhook")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Webhooks", description = "Razorpay webhook handler")
public class WebhookController {

    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;

    /**
     * Handle Razorpay webhook events.
     * 
     * Note: This endpoint receives raw JSON and Razorpay signature in header.
     * We need raw body for signature verification.
     */
    @PostMapping
    @Operation(summary = "Razorpay webhook handler")
    public ResponseEntity<ApiResponse<String>> handleWebhook(
            @RequestBody String rawBody,
            @RequestHeader(value = "X-Razorpay-Signature", required = false) String signature) {
        
        try {
            WebhookPayload payload = objectMapper.readValue(rawBody, WebhookPayload.class);
            log.info("Received webhook: event={}", payload.getEvent());
            
            if (signature == null || signature.isEmpty()) {
                log.warn("Webhook received without signature");
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Missing signature"));
            }
            
            paymentService.processWebhook(payload, signature, rawBody);
            return ResponseEntity.ok(ApiResponse.success("Webhook processed", "OK"));
            
        } catch (Exception e) {
            log.error("Webhook processing failed", e);
            // Return 200 to prevent Razorpay from retrying too many times
            return ResponseEntity.ok(ApiResponse.error("Webhook processing failed: " + e.getMessage()));
        }
    }
}
