package com.barber.shop.backend.dtos.employeeService;

import com.barber.shop.backend.enums.SkillLevel;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record EmployeeServiceCreateDto(
        @NotNull
        Long employeeId,

        @NotNull
        Long serviceId,

        @DecimalMin("0.00")
        BigDecimal customPrice,

        @Min(1)
        Integer customDurationMinutes,

        SkillLevel skillLevel
) {
}
