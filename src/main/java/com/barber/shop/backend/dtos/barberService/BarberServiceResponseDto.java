package com.barber.shop.backend.dtos.barberService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BarberServiceResponseDto(
        Long id,
        Long version,
        String serviceCode,
        String name,
        String category,
        String description,
        BigDecimal basePrice,
        Integer durationMinutes,
        Boolean isActive,
//        Boolean isDeleted,
//        LocalDateTime deletedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
