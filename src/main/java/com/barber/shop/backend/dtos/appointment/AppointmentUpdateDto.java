// package: com.barber.shop.backend.dtos.appointment.AppointmentUpdateDto
package com.barber.shop.backend.dtos.appointment;

import com.barber.shop.backend.enums.AppointmentStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AppointmentUpdateDto(

//        @NotNull
//        Long customerId,

        @NotNull
        LocalDate appointmentDate,

//        AppointmentStatus status,
//
//        @NotNull
//        @DecimalMin(value = "0.00")
//        BigDecimal totalAmount,
//
//        @NotNull
//        @DecimalMin(value = "0.00")
//        BigDecimal discountAmount,
//
//        @NotNull
//        @DecimalMin(value = "0.00")
//        BigDecimal finalAmount,

        @Size(max = 1000)
        String notes

//        @Size(max = 500)
//        String cancellationReason,
//
//        LocalDateTime cancelledAt,
//
//        LocalDateTime checkedInAt,
//
//        LocalDateTime completedAt
) {
}