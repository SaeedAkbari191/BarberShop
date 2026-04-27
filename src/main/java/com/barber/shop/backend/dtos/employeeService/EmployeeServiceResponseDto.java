package com.barber.shop.backend.dtos.employeeService;

import com.barber.shop.backend.enums.SkillLevel;

import java.math.BigDecimal;

public record EmployeeServiceResponseDto(
        Long id,
        Long employeeId,
        Long serviceId,
        BigDecimal customPrice,
        Integer customDurationMinutes,
        SkillLevel skillLevel,
        Boolean isActive
) {
}
