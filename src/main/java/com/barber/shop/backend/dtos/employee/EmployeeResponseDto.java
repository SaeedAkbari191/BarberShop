package com.barber.shop.backend.dtos.employee;

import com.barber.shop.backend.enums.CommissionType;
import com.barber.shop.backend.enums.EmploymentStatus;
import com.barber.shop.backend.enums.Gender;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record EmployeeResponseDto(
        Long id,
        String employeeCode,
        Long userId,
        String username,

        String firstName,
        String lastName,
        Gender gender,
        String phone,
        LocalDate hireDate,
        EmploymentStatus employmentStatus,

        CommissionType commissionType,
        BigDecimal commissionValue,
        String notes,

        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
