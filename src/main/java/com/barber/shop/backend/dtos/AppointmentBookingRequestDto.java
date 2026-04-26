package com.barber.shop.backend.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record AppointmentBookingRequestDto(
        @NotNull
        Long customerId,
        @NotNull
        Long bookedByUserId,
        Long assignedEmployeeId,
        @NotNull
        LocalDate appointmentDate,
        @NotNull
        LocalTime startTime,
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
