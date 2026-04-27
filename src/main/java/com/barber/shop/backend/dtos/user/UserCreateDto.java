package com.barber.shop.backend.dtos.user;

import com.barber.shop.backend.utils.ValidationPatterns;
import jakarta.validation.constraints.*;

public record UserCreateDto(

        @NotNull
        Long roleId,

        @NotBlank
        @Size(max = 100)
        String username,

        @NotBlank
        @Email
        @Size(max = 150)
        String email,

        @NotBlank
        @Size(min = 6, max = 100)
        String password,

        @Pattern(regexp = ValidationPatterns.PHONE)
        @Size(max = 30)
        String phone
) {
}