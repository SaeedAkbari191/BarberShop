package com.barber.shop.backend.dtos.user;

import com.barber.shop.backend.enums.UserStatus;
import com.barber.shop.backend.utils.ValidationPatterns;
import jakarta.validation.constraints.*;

public record UserUpdateDto(
        Long roleId,

        @NotBlank
        @Email
        @Size(max = 150)
        String email,

        @Pattern(regexp = ValidationPatterns.PHONE)
        @Size(max = 30)
        String phone,

        @NotNull
        UserStatus status
) {
}
