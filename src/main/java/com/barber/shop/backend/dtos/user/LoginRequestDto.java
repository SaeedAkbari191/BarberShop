package com.barber.shop.backend.dtos.user;

public record LoginRequestDto (
        String username,
        String password
){
}
