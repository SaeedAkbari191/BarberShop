package com.barber.shop.backend.dtos;

import com.barber.shop.backend.enums.AppointmentServiceStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AppointmentServiceDto(
        Long id,
        Long version,
        @NotNull
        Long appointmentId,
        @NotNull
        Long serviceId,
        @NotNull
        Long employeeId,
        @NotBlank
        @Size(max = 150)
        String serviceNameSnapshot,
        @NotNull
        @DecimalMin("0.00")
        BigDecimal priceSnapshot,
        @NotNull
        @Min(1)
        Integer durationMinutesSnapshot,
        @NotNull
        @DecimalMin("0.00")
        BigDecimal lineTotal,
        @NotNull
        @Min(1)
        Integer sortOrder,
        @NotNull
        AppointmentServiceStatus status,
        LocalDateTime startedAt,
        LocalDateTime completedAt,
        @Size(max = 500)
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
