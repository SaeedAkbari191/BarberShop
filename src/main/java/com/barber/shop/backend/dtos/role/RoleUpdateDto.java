package com.barber.shop.backend.dtos.role;

import jakarta.validation.constraints.Size;

public record RoleUpdateDto(

        @Size(max = 255)
        String description,

        Boolean isActive
) {
}
