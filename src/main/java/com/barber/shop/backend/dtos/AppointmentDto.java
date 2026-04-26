package com.barber.shop.backend.dtos;

import com.barber.shop.backend.enums.AppointmentStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record AppointmentDto(
        Long id,
        Long version,
        @NotBlank
        @Size(max = 50)
        String appointmentNumber,
        @NotNull
        Long customerId,
        @NotNull
        Long bookedByUserId,
        Long assignedEmployeeId,
        @NotNull
        LocalDate appointmentDate,
        @NotNull
        LocalTime startTime,
        @NotNull
        LocalTime endTime,
        @NotNull
        AppointmentStatus status,
        @NotNull
        @DecimalMin("0.00")
        BigDecimal totalAmount,
        @NotNull
        @DecimalMin("0.00")
        BigDecimal discountAmount,
        @NotNull
        @DecimalMin("0.00")
        BigDecimal finalAmount,
        @Size(max = 1000)
        String notes,
        @Size(max = 500)
        String cancellationReason,
        LocalDateTime cancelledAt,
        LocalDateTime checkedInAt,
        LocalDateTime completedAt,
        Boolean isDeleted,
        LocalDateTime deletedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
