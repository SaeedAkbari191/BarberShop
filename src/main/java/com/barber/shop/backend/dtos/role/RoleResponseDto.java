package com.barber.shop.backend.dtos.role;

import com.barber.shop.backend.enums.RoleCode;

import java.time.LocalDateTime;

public record RoleResponseDto(
        Long id,
        Long version,
        RoleCode code,
        String description,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
