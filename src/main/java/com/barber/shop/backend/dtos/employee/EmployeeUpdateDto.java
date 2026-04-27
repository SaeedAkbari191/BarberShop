package com.barber.shop.backend.dtos.employee;

import com.barber.shop.backend.enums.CommissionType;
import com.barber.shop.backend.enums.EmploymentStatus;
import com.barber.shop.backend.enums.Gender;
import com.barber.shop.backend.utils.ValidationPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record EmployeeUpdateDto(

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
        EmploymentStatus employmentStatus,

        CommissionType commissionType,
        BigDecimal commissionValue,

        @Size(max = 500)
        String notes
) {
}
