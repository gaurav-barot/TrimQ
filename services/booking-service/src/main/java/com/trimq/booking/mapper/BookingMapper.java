package com.trimq.booking.mapper;

import com.trimq.booking.dto.BookingResponse;
import com.trimq.booking.dto.BookingSummaryResponse;
import com.trimq.booking.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for Booking entity.
 */
@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "statusDescription", expression = "java(getStatusDescription(booking.getStatus()))")
    @Mapping(target = "cancellable", expression = "java(booking.isCancellable())")
    @Mapping(target = "upcoming", expression = "java(booking.isUpcoming())")
    BookingResponse toBookingResponse(Booking booking);

    @Mapping(target = "cancellable", expression = "java(booking.isCancellable())")
    @Mapping(target = "upcoming", expression = "java(booking.isUpcoming())")
    BookingSummaryResponse toBookingSummaryResponse(Booking booking);

    default String getStatusDescription(com.trimq.common.enums.BookingStatus status) {
        return switch (status) {
            case PENDING -> "Awaiting payment confirmation";
            case CONFIRMED -> "Booking confirmed. See you soon!";
            case IN_PROGRESS -> "Service in progress";
            case COMPLETED -> "Service completed. Thank you!";
            case CANCELLED -> "Booking cancelled";
            case NO_SHOW -> "Customer did not show up";
            case REFUNDED -> "Payment refunded";
        };
    }
}

