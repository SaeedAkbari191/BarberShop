// package: com.barber.shop.backend.dtos.payment.PaymentResponseDto
package com.barber.shop.backend.dtos.payment;

import com.barber.shop.backend.enums.PaymentMethod;
import com.barber.shop.backend.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponseDto(

        Long id,

        Long appointmentServiceId,

        Long appointmentId,
        String appointmentNumber,

        Long serviceId,
        String serviceName,

        Long receivedByUserId,
        String receivedByUsername,

        PaymentMethod paymentMethod,
        PaymentStatus paymentStatus,

        BigDecimal amount,

        String transactionReference,

        LocalDateTime paidAt,

        String notes,

        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}