package com.barber.shop.backend.dtos.user;

import lombok.Builder;


@Builder
public record AuthResponseDto(
        UserResponseDto user,
        String accessToken,
        String refreshToken
) {
}
