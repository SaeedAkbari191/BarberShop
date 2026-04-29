package com.barber.shop.backend.service;

import com.barber.shop.backend.dtos.user.UserCreateDto;
import com.barber.shop.backend.dtos.user.UserResponseDto;

public interface UserService {
    public UserResponseDto registerUser(UserCreateDto dto);
}
