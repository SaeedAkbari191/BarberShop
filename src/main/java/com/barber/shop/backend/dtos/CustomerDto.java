package com.barber.shop.backend.dtos;

import com.barber.shop.backend.enums.Gender;
import com.barber.shop.backend.utils.ValidationPatterns;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CustomerDto(
        Long id,
        Long version,
        @NotBlank
        @Size(max = 50)
        String customerCode,
        @NotBlank
        @Size(max = 100)
        String firstName,
        @Size(max = 100)
        String lastName,
        @NotBlank
        @Pattern(regexp = ValidationPatterns.PHONE)
        @Size(max = 30)
        String phone,
        @Email
        @Size(max = 150)
        String email,
        LocalDate dateOfBirth,
        Gender gender,
        String notes,
        Boolean marketingOptIn,
        LocalDateTime lastVisitAt,
        Boolean isDeleted,
        LocalDateTime deletedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
