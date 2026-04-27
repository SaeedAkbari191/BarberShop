package com.barber.shop.backend.dtos.role;

import com.barber.shop.backend.enums.RoleCode;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RoleCreateDto(
        @NotNull
        RoleCode code,

        @Size(max = 255)
        String description,

        Boolean isActive
) {
}
