package com.barber.shop.backend.service.impl;

import com.barber.shop.backend.dtos.user.AuthResponseDto;
import com.barber.shop.backend.dtos.user.LoginRequestDto;
import com.barber.shop.backend.dtos.user.UserResponseDto;
import com.barber.shop.backend.models.RefreshToken;
import com.barber.shop.backend.models.User;
import com.barber.shop.backend.repositories.RefreshTokenRepository;
import com.barber.shop.backend.repositories.UserRepository;
import com.barber.shop.backend.security.AuthUser;
import com.barber.shop.backend.security.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthResponseDto login(LoginRequestDto request, HttpServletRequest httpRequest) {

        log.info("Attempting login for username: {}", request.username());

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.username(),
                                request.password()
                        )
                );

        AuthUser authUser = (AuthUser) authentication.getPrincipal();

        String accessToken = jwtUtils.generateAccessToken(authUser);
        String refreshTokenValue = jwtUtils.generateRefreshToken(authUser);

        User user = authUser.getUser();

        /*
         * Update last login timestamp
         */
        userRepository.save(user);

        /*
         * Revoke old active tokens from same device if needed
         */
        String ipAddress = extractIpAddress(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");
        String deviceInfo = parseDeviceInfo(userAgent);

        /*
         * Save hashed refresh token
         */
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .sessionId(UUID.randomUUID().toString())
                .user(user)
                .tokenHash(hashToken(refreshTokenValue))
                .previousTokenHash(null)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .isRevoked(false)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .deviceInfo(deviceInfo)
                .build();

        refreshTokenRepository.save(refreshTokenEntity);

        log.info("Login successful for user: {}", user.getUsername());

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
                .refreshToken(refreshTokenValue)
                .build();
    }

    /**
     * Secure SHA-256 hashing for refresh token storage
     */
    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to hash refresh token", e);
        }
    }

    /**
     * Normalize IP
     */
    private String extractIpAddress(HttpServletRequest request) {

        String forwarded = request.getHeader("X-Forwarded-For");

        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }

        String ip = request.getRemoteAddr();

        if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
            return "127.0.0.1";
        }

        return ip;
    }

    /**
     * Basic device detection
     */
    private String parseDeviceInfo(String userAgent) {

        if (userAgent == null) return "Unknown";

        if (userAgent.contains("Mobile")) return "Mobile";
        if (userAgent.contains("Tablet")) return "Tablet";
        if (userAgent.contains("Windows")) return "Windows PC";
        if (userAgent.contains("Macintosh")) return "Mac";
        if (userAgent.contains("Linux")) return "Linux";

        return "Desktop";
    }
}