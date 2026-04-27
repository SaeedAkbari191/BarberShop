// package: com.barber.shop.backend.dtos.appointment.AppointmentBookingRequestDto
package com.barber.shop.backend.dtos.appointment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record AppointmentBookingRequestDto(

        @NotNull
        Long customerId,

        @NotNull
        Long bookedByUserId,

        @NotNull
        LocalDate appointmentDate,

        @DecimalMin("0.00")
        BigDecimal discountAmount,

        @Size(max = 1000)
        String notes,

        @Valid
        @NotEmpty
        List<AppointmentLineRequestDto> services,

        @Valid
        List<AppointmentPaymentRequestDto> payments
) {
}