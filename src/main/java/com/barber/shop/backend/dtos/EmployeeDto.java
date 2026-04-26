package com.barber.shop.backend.dtos;

import com.barber.shop.backend.enums.CommissionType;
import com.barber.shop.backend.enums.EmploymentStatus;
import com.barber.shop.backend.enums.Gender;
import com.barber.shop.backend.utils.ValidationPatterns;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record EmployeeDto(
        Long id,
        Long version,
        Long userId,
        @NotBlank
        @Size(max = 50)
        String employeeCode,
        @NotBlank
        @Size(max = 100)
        String firstName,
        @NotBlank
        @Size(max = 100)
        String lastName,
        Gender gender,
        @NotBlank
        @Pattern(regexp = ValidationPatterns.PHONE)
        @Size(max = 30)
        String phone,
        @NotNull
        LocalDate hireDate,
        @NotNull
        EmploymentStatus employmentStatus,
        CommissionType commissionType,
        @DecimalMin("0.00")
        BigDecimal commissionValue,
        @Size(max = 500)
        String notes,
        Boolean isDeleted,
        LocalDateTime deletedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
