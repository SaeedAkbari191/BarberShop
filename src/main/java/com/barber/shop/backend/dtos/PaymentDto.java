package com.barber.shop.backend.dtos;

import com.barber.shop.backend.enums.PaymentMethod;
import com.barber.shop.backend.enums.PaymentStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentDto(
        Long id,
        Long version,
        @NotNull
        Long appointmentId,
        @NotNull
        Long receivedByUserId,
        @NotNull
        PaymentMethod paymentMethod,
        @NotNull
        PaymentStatus paymentStatus,
        @NotNull
        @DecimalMin("0.01")
        BigDecimal amount,
        @Size(max = 100)
        String transactionReference,
        @NotNull
        LocalDateTime paidAt,
        @Size(max = 500)
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
