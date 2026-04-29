package com.barber.shop.backend.controller;

import com.barber.shop.backend.dtos.user.AuthResponseDto;
import com.barber.shop.backend.dtos.user.LoginRequestDto;
import com.barber.shop.backend.dtos.user.UserCreateDto;
import com.barber.shop.backend.dtos.user.UserResponseDto;
import com.barber.shop.backend.service.UserService;
import com.barber.shop.backend.service.impl.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> Register(@RequestBody UserCreateDto dto) {
        return ResponseEntity.ok(userService.registerUser(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }
}
