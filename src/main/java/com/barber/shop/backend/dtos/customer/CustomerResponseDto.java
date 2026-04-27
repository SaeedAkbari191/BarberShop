package com.barber.shop.backend.dtos.customer;

import com.barber.shop.backend.enums.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CustomerResponseDto(
        Long id,
        String customerCode,
        String firstName,
        String lastName,
        String phone,
        String email,
        LocalDate dateOfBirth,
        Gender gender,
        String notes,
        Boolean marketingOptIn,
        LocalDateTime lastVisitAt,
        LocalDateTime createdAt
) {
}
