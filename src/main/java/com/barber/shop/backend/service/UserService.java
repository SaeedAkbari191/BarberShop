package com.barber.shop.backend.service;

import com.barber.shop.backend.dtos.user.UserCreateDto;
import com.barber.shop.backend.dtos.user.UserResponseDto;
import com.barber.shop.backend.dtos.user.UserUpdateDto;

import java.util.List;

public interface UserService {
    public UserResponseDto registerUser(UserCreateDto dto);

    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserById(Long id);

    UserResponseDto updateUser(Long id, UserUpdateDto dto);

    public void deleteUser(Long id);
}
