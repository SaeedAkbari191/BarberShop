package com.barber.shop.backend.dtos;

import com.barber.shop.backend.enums.RoleCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record RoleDto(
        Long id,
        Long version,
        @NotNull
        RoleCode code,
        @NotBlank
        @Size(max = 100)
        String name,
        @Size(max = 255)
        String description,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
