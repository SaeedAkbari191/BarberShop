// package: com.barber.shop.backend.dtos.payment.PaymentUpdateDto
package com.barber.shop.backend.dtos.payment;

import com.barber.shop.backend.enums.PaymentMethod;
import com.barber.shop.backend.enums.PaymentStatus;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentUpdateDto(

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
        String notes
) {}