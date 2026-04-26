package com.barber.shop.backend.dtos;

import com.barber.shop.backend.enums.SkillLevel;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EmployeeServiceDto(
        Long id,
        Long version,
        @NotNull
        Long employeeId,
        @NotNull
        Long serviceId,
        @DecimalMin("0.00")
        BigDecimal customPrice,
        @Min(1)
        Integer customDurationMinutes,
        SkillLevel skillLevel,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
