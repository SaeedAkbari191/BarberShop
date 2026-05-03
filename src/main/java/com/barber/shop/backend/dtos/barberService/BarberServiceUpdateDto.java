package com.barber.shop.backend.dtos.barberService;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record BarberServiceUpdateDto(


        Long version,


        @Size(max = 150)
        String name,

        @Size(max = 100)
        String category,

        @Size(max = 500)
        String description,


        @DecimalMin("0.00")
        BigDecimal basePrice,


        @Positive
        Integer durationMinutes,

        Boolean isActive
) {
}
