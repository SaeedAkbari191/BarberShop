package com.barber.shop.backend.dtos;

import com.barber.shop.backend.enums.UserStatus;
import com.barber.shop.backend.utils.ValidationPatterns;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record UserDto(
        Long id,
        Long version,
        @NotNull
        Long roleId,
        @NotBlank
        @Size(max = 100)
        String username,
        @NotBlank
        @Email
        @Size(max = 150)
        String email,
        @Pattern(regexp = ValidationPatterns.PHONE)
        @Size(max = 30)
        String phone,
        @NotNull
        UserStatus status,
        @Size(max = 255)
        String passwordHash,
        LocalDateTime lastLoginAt,
        Boolean isDeleted,
        LocalDateTime deletedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
