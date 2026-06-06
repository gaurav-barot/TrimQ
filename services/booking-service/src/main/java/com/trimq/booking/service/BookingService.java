package com.trimq.booking.service;

import com.trimq.booking.config.BookingConfig;
import com.trimq.booking.dto.*;
import com.trimq.booking.entity.Booking;
import com.trimq.booking.mapper.BookingMapper;
import com.trimq.booking.repository.BookingRepository;
import com.trimq.common.dto.PagedResponse;
import com.trimq.common.enums.BookingStatus;
import com.trimq.common.exception.BadRequestException;
import com.trimq.common.exception.ConflictException;
import com.trimq.common.exception.ForbiddenException;
import com.trimq.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Service for managing bookings.
 * 
 * Handles booking creation, updates, cancellation, and pass validation.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final SlotService slotService;
    private final PassCodeGenerator passCodeGenerator;
    private final QrCodeService qrCodeService;
    private final BookingEventPublisher eventPublisher;
    private final BookingConfig bookingConfig;

    /**
     * Create a new booking.
     * 
     * @param request Booking request
     * @param userId User ID (from JWT)
     * @param customerName Customer name (from user service)
     * @param customerPhone Customer phone
     * @param customerEmail Customer email
     * @return Created booking response
     */
    @Transactional
    public BookingResponse createBooking(CreateBookingRequest request, Long userId,
                                          String customerName, String customerPhone, 
                                          String customerEmail) {
        log.info("Creating booking: userId={}, shopId={}, date={}, time={}", 
                userId, request.getShopId(), request.getBookingDate(), request.getStartTime());

        // Validate booking time
        validateBookingTime(request.getBookingDate(), request.getStartTime());

        // Check for conflicts
        if (hasConflict(request)) {
            throw new ConflictException("Selected time slot is not available");
        }

        // TODO: Fetch service details from Shop Service
        // For now, using mock data
        String serviceName = "Haircut"; // Fetch from shop-service
        String shopName = "TrimQ Shop"; // Fetch from shop-service
        int durationMinutes = 30;
        BigDecimal amount = new BigDecimal("300.00");

        // Calculate end time
        LocalTime endTime = request.getStartTime().plusMinutes(durationMinutes);

        // Generate pass code
        String passCode = passCodeGenerator.generate(request.getBookingDate());
        
        // Ensure uniqueness (retry if collision)
        int attempts = 0;
        while (bookingRepository.findByPassCode(passCode).isPresent() && attempts < 5) {
            passCode = passCodeGenerator.generate(request.getBookingDate());
            attempts++;
        }

        // Get next token number for the day
        int tokenNumber = bookingRepository.getMaxTokenNumberForShopAndDate(
                request.getShopId(), request.getBookingDate()) + 1;

        // Create booking
        Booking booking = Booking.builder()
                .userId(userId)
                .shopId(request.getShopId())
                .serviceId(request.getServiceId())
                .serviceName(serviceName)
                .staffId(request.getStaffId())
                .staffName(null) // TODO: Fetch from shop-service
                .bookingDate(request.getBookingDate())
                .startTime(request.getStartTime())
                .endTime(endTime)
                .durationMinutes(durationMinutes)
                .amount(amount)
                .status(BookingStatus.PENDING)
                .passCode(passCode)
                .customerName(customerName)
                .customerPhone(customerPhone)
                .customerEmail(customerEmail)
                .customerNotes(request.getCustomerNotes())
                .shopName(shopName)
                .tokenNumber(tokenNumber)
                .build();

        // Generate QR code
        String qrCode = qrCodeService.generateQrCodeBase64(passCode);
        booking.setQrCodeBase64(qrCode);

        // Save booking
        booking = bookingRepository.save(booking);

        // Book the slot
        slotService.bookSlot(request.getShopId(), request.getBookingDate(), 
                request.getStartTime(), endTime, booking.getId(), request.getStaffId());

        // Publish event
        eventPublisher.publishBookingCreated(booking);

        log.info("Booking created: id={}, passCode={}", booking.getId(), passCode);
        return bookingMapper.toBookingResponse(booking);
    }

    /**
     * Confirm booking after payment.
     * 
     * @param bookingId Booking ID
     * @param paymentId Payment ID
     * @param paymentOrderId Payment order ID
     * @return Updated booking
     */
    @Transactional
    public BookingResponse confirmBooking(Long bookingId, String paymentId, String paymentOrderId) {
        Booking booking = findBookingById(bookingId);

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BadRequestException("Booking cannot be confirmed. Current status: " + booking.getStatus());
        }

        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setPaymentId(paymentId);
        booking.setPaymentOrderId(paymentOrderId);
        
        booking = bookingRepository.save(booking);

        // Publish event
        eventPublisher.publishBookingConfirmed(booking);

        log.info("Booking confirmed: id={}, paymentId={}", bookingId, paymentId);
        return bookingMapper.toBookingResponse(booking);
    }

    /**
     * Get booking by ID.
     * 
     * @param bookingId Booking ID
     * @param userId User ID (for authorization)
     * @return Booking response
     */
    @Transactional(readOnly = true)
    public BookingResponse getBooking(Long bookingId, Long userId, boolean isShopOwner) {
        Booking booking = findBookingById(bookingId);
        
        // Authorization check
        if (!isShopOwner && !booking.getUserId().equals(userId)) {
            throw new ForbiddenException("You don't have access to this booking");
        }

        return bookingMapper.toBookingResponse(booking);
    }

    /**
     * Get user's bookings.
     * 
     * @param userId User ID
     * @param pageNo Page number
     * @param pageSize Page size
     * @return Paged booking list
     */
    @Transactional(readOnly = true)
    public PagedResponse<BookingSummaryResponse> getUserBookings(Long userId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Booking> page = bookingRepository.findByUserIdOrderByBookingDateDescStartTimeDesc(userId, pageable);
        
        List<BookingSummaryResponse> content = page.getContent().stream()
                .map(bookingMapper::toBookingSummaryResponse)
                .toList();

        return PagedResponse.<BookingSummaryResponse>builder()
                .content(content)
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    /**
     * Get user's upcoming bookings.
     * 
     * @param userId User ID
     * @return List of upcoming bookings
     */
    @Transactional(readOnly = true)
    public List<BookingSummaryResponse> getUserUpcomingBookings(Long userId) {
        List<Booking> bookings = bookingRepository.findUpcomingBookingsByUser(
                userId, LocalDate.now(), LocalTime.now());
        
        return bookings.stream()
                .map(bookingMapper::toBookingSummaryResponse)
                .toList();
    }

    /**
     * Get shop's bookings.
     * 
     * @param shopId Shop ID
     * @param pageNo Page number
     * @param pageSize Page size
     * @return Paged booking list
     */
    @Transactional(readOnly = true)
    public PagedResponse<BookingSummaryResponse> getShopBookings(Long shopId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Booking> page = bookingRepository.findByShopIdOrderByBookingDateDescStartTimeDesc(shopId, pageable);
        
        List<BookingSummaryResponse> content = page.getContent().stream()
                .map(bookingMapper::toBookingSummaryResponse)
                .toList();

        return PagedResponse.<BookingSummaryResponse>builder()
                .content(content)
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    /**
     * Get shop's today queue.
     * 
     * @param shopId Shop ID
     * @return Today's queue response
     */
    @Transactional(readOnly = true)
    public TodayQueueResponse getShopTodayQueue(Long shopId) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        List<Booking> allBookings = bookingRepository.findByShopIdAndBookingDateOrderByStartTimeAsc(shopId, today);
        
        List<BookingSummaryResponse> queue = allBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.CONFIRMED || b.getStatus() == BookingStatus.IN_PROGRESS)
                .map(bookingMapper::toBookingSummaryResponse)
                .toList();

        int completed = (int) allBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.COMPLETED)
                .count();
        
        int cancelled = (int) allBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.CANCELLED)
                .count();

        int upcoming = (int) queue.stream()
                .filter(BookingSummaryResponse::isUpcoming)
                .count();

        BookingSummaryResponse current = bookingRepository.findCurrentBookingForShop(shopId, today)
                .map(bookingMapper::toBookingSummaryResponse)
                .orElse(null);

        BookingSummaryResponse next = bookingRepository.findNextBookingForShop(shopId, today, now)
                .map(bookingMapper::toBookingSummaryResponse)
                .orElse(null);

        return TodayQueueResponse.builder()
                .shopId(shopId)
                .date(today)
                .totalBookings(allBookings.size())
                .completedBookings(completed)
                .upcomingBookings(upcoming)
                .cancelledBookings(cancelled)
                .currentBooking(current)
                .nextBooking(next)
                .queue(queue)
                .build();
    }

    /**
     * Cancel a booking.
     * 
     * @param bookingId Booking ID
     * @param userId User ID
     * @param reason Cancellation reason
     * @return Updated booking
     */
    @Transactional
    public BookingResponse cancelBooking(Long bookingId, Long userId, String reason, boolean isShopOwner) {
        Booking booking = findBookingById(bookingId);

        // Authorization check
        if (!isShopOwner && !booking.getUserId().equals(userId)) {
            throw new ForbiddenException("You don't have access to cancel this booking");
        }

        if (!booking.isCancellable()) {
            throw new BadRequestException("Booking cannot be cancelled. Current status: " + booking.getStatus());
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancellationReason(reason);
        booking = bookingRepository.save(booking);

        // Release the slot
        slotService.releaseBookedSlot(bookingId);

        // Publish event
        eventPublisher.publishBookingCancelled(booking);

        log.info("Booking cancelled: id={}, reason={}", bookingId, reason);
        return bookingMapper.toBookingResponse(booking);
    }

    /**
     * Update booking status (shop owner action).
     * 
     * @param bookingId Booking ID
     * @param status New status
     * @return Updated booking
     */
    @Transactional
    public BookingResponse updateBookingStatus(Long bookingId, BookingStatus status) {
        Booking booking = findBookingById(bookingId);
        
        BookingStatus oldStatus = booking.getStatus();
        booking.setStatus(status);
        booking = bookingRepository.save(booking);

        // If completed, publish event
        if (status == BookingStatus.COMPLETED) {
            eventPublisher.publishBookingCompleted(booking);
        }

        log.info("Booking status updated: id={}, {} -> {}", bookingId, oldStatus, status);
        return bookingMapper.toBookingResponse(booking);
    }

    /**
     * Validate a booking pass.
     * 
     * @param passCode Pass code to validate
     * @param shopId Shop ID (for authorization)
     * @return Validation response
     */
    @Transactional(readOnly = true)
    public ValidatePassResponse validatePass(String passCode, Long shopId) {
        return bookingRepository.findByPassCode(passCode)
                .map(booking -> {
                    // Check if pass is for this shop
                    if (!booking.getShopId().equals(shopId)) {
                        return ValidatePassResponse.builder()
                                .valid(false)
                                .message("This pass is for a different shop")
                                .build();
                    }

                    // Check if already used
                    boolean alreadyUsed = booking.getStatus() == BookingStatus.COMPLETED ||
                                         booking.getStatus() == BookingStatus.IN_PROGRESS;

                    // Check if cancelled
                    if (booking.getStatus() == BookingStatus.CANCELLED) {
                        return ValidatePassResponse.builder()
                                .valid(false)
                                .message("This booking has been cancelled")
                                .bookingId(booking.getId())
                                .customerName(booking.getCustomerName())
                                .status(booking.getStatus())
                                .build();
                    }

                    // Check if for today
                    if (!booking.getBookingDate().equals(LocalDate.now())) {
                        return ValidatePassResponse.builder()
                                .valid(false)
                                .message("This pass is for " + booking.getBookingDate() + ", not today")
                                .bookingId(booking.getId())
                                .customerName(booking.getCustomerName())
                                .bookingDate(booking.getBookingDate())
                                .startTime(booking.getStartTime())
                                .status(booking.getStatus())
                                .build();
                    }

                    return ValidatePassResponse.builder()
                            .valid(true)
                            .message(alreadyUsed ? "Pass already validated" : "Valid pass")
                            .bookingId(booking.getId())
                            .customerName(booking.getCustomerName())
                            .customerPhone(booking.getCustomerPhone())
                            .serviceName(booking.getServiceName())
                            .staffName(booking.getStaffName())
                            .bookingDate(booking.getBookingDate())
                            .startTime(booking.getStartTime())
                            .endTime(booking.getEndTime())
                            .tokenNumber(booking.getTokenNumber())
                            .amount(booking.getAmount())
                            .status(booking.getStatus())
                            .alreadyUsed(alreadyUsed)
                            .build();
                })
                .orElse(ValidatePassResponse.builder()
                        .valid(false)
                        .message("Invalid pass code")
                        .build());
    }

    /**
     * Start a booking (mark as in progress).
     * 
     * @param bookingId Booking ID
     * @param shopId Shop ID (for authorization)
     * @return Updated booking
     */
    @Transactional
    public BookingResponse startBooking(Long bookingId, Long shopId) {
        Booking booking = findBookingById(bookingId);

        if (!booking.getShopId().equals(shopId)) {
            throw new ForbiddenException("You don't have access to this booking");
        }

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new BadRequestException("Only confirmed bookings can be started");
        }

        booking.setStatus(BookingStatus.IN_PROGRESS);
        booking = bookingRepository.save(booking);

        log.info("Booking started: id={}", bookingId);
        return bookingMapper.toBookingResponse(booking);
    }

    /**
     * Complete a booking.
     * 
     * @param bookingId Booking ID
     * @param shopId Shop ID (for authorization)
     * @return Updated booking
     */
    @Transactional
    public BookingResponse completeBooking(Long bookingId, Long shopId) {
        Booking booking = findBookingById(bookingId);

        if (!booking.getShopId().equals(shopId)) {
            throw new ForbiddenException("You don't have access to this booking");
        }

        if (booking.getStatus() != BookingStatus.IN_PROGRESS && 
            booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new BadRequestException("Booking cannot be completed from current status: " + booking.getStatus());
        }

        booking.setStatus(BookingStatus.COMPLETED);
        booking = bookingRepository.save(booking);

        // Publish event
        eventPublisher.publishBookingCompleted(booking);

        log.info("Booking completed: id={}", bookingId);
        return bookingMapper.toBookingResponse(booking);
    }

    private Booking findBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", bookingId));
    }

    private void validateBookingTime(LocalDate date, LocalTime time) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        if (date.isBefore(today)) {
            throw new BadRequestException("Cannot book for past dates");
        }

        if (date.equals(today)) {
            LocalTime minTime = now.plusHours(bookingConfig.getMinAdvanceHours());
            if (time.isBefore(minTime)) {
                throw new BadRequestException("Booking must be at least " + 
                        bookingConfig.getMinAdvanceHours() + " hour(s) in advance");
            }
        }

        LocalDate maxDate = today.plusDays(bookingConfig.getMaxAdvanceDays());
        if (date.isAfter(maxDate)) {
            throw new BadRequestException("Cannot book more than " + 
                    bookingConfig.getMaxAdvanceDays() + " days in advance");
        }
    }

    private boolean hasConflict(CreateBookingRequest request) {
        // First check Redis lock
        if (slotLockService.isLocked(request.getShopId(), request.getBookingDate(), 
                request.getStartTime())) {
            return true;
        }

        // Check slot availability
        return !slotService.isSlotAvailable(request.getShopId(), request.getBookingDate(), 
                request.getStartTime());
    }

    // Autowired for use in hasConflict check
    private final SlotLockService slotLockService;
}

