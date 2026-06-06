package com.trimq.payment.service;

import com.trimq.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Client for communicating with Booking Service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceClient {

    private final RestTemplate restTemplate;

    @Value("${booking-service.url}")
    private String bookingServiceUrl;

    /**
     * Confirm a booking after successful payment.
     * 
     * @param bookingId Booking ID
     * @param paymentId Razorpay payment ID
     * @param paymentOrderId Razorpay order ID
     * @return true if confirmed
     */
    public boolean confirmBooking(Long bookingId, String paymentId, String paymentOrderId) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(bookingServiceUrl)
                    .path("/bookings/{bookingId}/confirm")
                    .queryParam("paymentId", paymentId)
                    .queryParam("paymentOrderId", paymentOrderId)
                    .buildAndExpand(bookingId)
                    .toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            log.info("Confirming booking: bookingId={}, paymentId={}", bookingId, paymentId);
            
            ResponseEntity<ApiResponse> response = restTemplate.postForEntity(
                    url, entity, ApiResponse.class);

            boolean success = response.getStatusCode().is2xxSuccessful();
            log.info("Booking confirmation result: bookingId={}, success={}", bookingId, success);
            
            return success;
        } catch (Exception e) {
            log.error("Failed to confirm booking: bookingId={}", bookingId, e);
            return false;
        }
    }

    /**
     * Cancel a booking (after refund).
     * 
     * @param bookingId Booking ID
     * @param reason Cancellation reason
     * @return true if cancelled
     */
    public boolean cancelBooking(Long bookingId, String reason) {
        try {
            String url = bookingServiceUrl + "/bookings/" + bookingId;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-User-Id", "0"); // System user
            headers.set("X-User-Roles", "ADMIN");

            String body = "{\"reason\": \"" + reason + "\"}";
            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            log.info("Cancelling booking after refund: bookingId={}", bookingId);
            
            restTemplate.delete(url, entity);
            return true;
        } catch (Exception e) {
            log.error("Failed to cancel booking: bookingId={}", bookingId, e);
            return false;
        }
    }
}

