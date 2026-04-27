package com.barber.shop.backend.dtos.barberService;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record BarberServiceCreateDto(
        @NotBlank
        @Size(max = 50)
        String serviceCode,

        @NotBlank
        @Size(max = 150)
        String name,

        @Size(max = 100)
        String category,

        @Size(max = 500)
        String description,

        @NotNull
        @DecimalMin("0.00")
        BigDecimal basePrice,

        @NotNull
        @Positive
        Integer durationMinutes
) {
}
