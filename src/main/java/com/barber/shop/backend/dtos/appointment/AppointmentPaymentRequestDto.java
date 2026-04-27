// package: com.barber.shop.backend.dtos.appointment.AppointmentPaymentRequestDto
package com.barber.shop.backend.dtos.appointment;

import com.barber.shop.backend.enums.PaymentMethod;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AppointmentPaymentRequestDto(

        @NotNull
        Long receivedByUserId,

        @NotNull
        PaymentMethod paymentMethod,

        @NotNull
        @DecimalMin("0.01")
        BigDecimal amount,

        @Size(max = 100)
        String transactionReference,

        LocalDateTime paidAt,

        @Size(max = 500)
        String notes
) {
}