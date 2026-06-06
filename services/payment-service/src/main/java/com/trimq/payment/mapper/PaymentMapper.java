package com.trimq.payment.mapper;

import com.trimq.common.enums.PaymentStatus;
import com.trimq.payment.dto.PaymentResponse;
import com.trimq.payment.dto.TransactionResponse;
import com.trimq.payment.entity.Payment;
import com.trimq.payment.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for Payment entities.
 */
@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "statusDescription", expression = "java(getStatusDescription(payment.getStatus()))")
    @Mapping(target = "refundable", expression = "java(payment.isRefundable())")
    PaymentResponse toPaymentResponse(Payment payment);

    TransactionResponse toTransactionResponse(Transaction transaction);

    default String getStatusDescription(PaymentStatus status) {
        return switch (status) {
            case PENDING -> "Payment pending";
            case SUCCESS -> "Payment successful";
            case FAILED -> "Payment failed";
            case REFUNDED -> "Payment refunded";
            case PARTIALLY_REFUNDED -> "Partially refunded";
        };
    }
}

