package com.barber.shop.backend.service.impl;

import com.barber.shop.backend.dtos.user.AuthResponseDto;
import com.barber.shop.backend.dtos.user.LoginRequestDto;
import com.barber.shop.backend.dtos.user.UserResponseDto;
import com.barber.shop.backend.models.RefreshToken;
import com.barber.shop.backend.models.User;
import com.barber.shop.backend.repositories.UserRepository;
import com.barber.shop.backend.security.AuthUser;
import com.barber.shop.backend.security.CustomUserDetailsService;
import com.barber.shop.backend.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public AuthResponseDto login(LoginRequestDto request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.username(),
                                request.password()
                        )
                );

        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        String accessToken = jwtUtils.generateAccessToken(authUser);
        String refreshToken = jwtUtils.generateRefreshToken(authUser);

        User user = authUser.getUser();


        userRepository.save(user);

        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .user(user)
                .token(refreshToken)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .isRevoked(false)
                .build();

        UserResponseDto userResponse = new UserResponseDto(
                user.getId(),
                user.getRole().getId(),
                user.getRole().getCode().name(),
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
        return AuthResponseDto.builder()
                .user(userResponse)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}

