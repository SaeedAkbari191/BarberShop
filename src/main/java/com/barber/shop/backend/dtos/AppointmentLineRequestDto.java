package com.barber.shop.backend.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AppointmentLineRequestDto(
        @NotNull
        Long serviceId,
        @NotNull
        Long employeeId,
        @Min(1)
        Integer sortOrder,
        @Size(max = 500)
        String notes
) {
}
