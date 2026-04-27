package com.barber.shop.backend.dtos.user;

import com.barber.shop.backend.enums.UserStatus;

import java.time.LocalDateTime;

public record UserResponseDto(
        Long id,
        Long roleId,
        String roleName,
        String username,
        String email,
        String phone,
        UserStatus status,
        LocalDateTime lastLoginAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
