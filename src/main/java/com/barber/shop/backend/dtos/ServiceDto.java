package com.barber.shop.backend.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ServiceDto(
        Long id,
        Long version,
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
        Integer durationMinutes,
        Boolean isActive,
        Boolean isDeleted,
        LocalDateTime deletedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
