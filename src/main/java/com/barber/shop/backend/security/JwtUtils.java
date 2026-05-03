package com.barber.shop.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class JwtUtils {

    /**
     * Access Token:
     * کوتاه مدت برای امنیت بیشتر
     * مثال: 15 دقیقه
     */

    private final long accessTokenExpirationMs = 1000 * 60 * 7;

    /**
     * Refresh Token:
     * بلندمدت‌تر
     * مثال: 7 روز
     */

    private final long refreshTokenExpirationMs = 1000L * 60 * 60 * 24 * 7;

    /**
     * Secret جدا برای Access Token
     */
    @Value("${jwt.access.secret}")
    private String accessTokenSecretString;

    /**
     * Secret جدا برای Refresh Token
     */
    @Value("${jwt.refresh.secret}")
    private String refreshTokenSecretString;

    private SecretKey accessTokenSecret;
    private SecretKey refreshTokenSecret;


    /**
     * تبدیل Secret String به SecretKey
     */
    @PostConstruct
    public void init() {

        this.accessTokenSecret = Keys.hmacShaKeyFor(
                accessTokenSecretString.getBytes(StandardCharsets.UTF_8)
        );

        this.refreshTokenSecret = Keys.hmacShaKeyFor(
                refreshTokenSecretString.getBytes(StandardCharsets.UTF_8)
        );
    }


    /**
     * تولید Access Token
     */
    public String generateAccessToken(UserDetails user) {

        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(
                        new Date(System.currentTimeMillis() + accessTokenExpirationMs)
                )
                .signWith(accessTokenSecret, SignatureAlgorithm.HS256)
                .compact();
    }


    /**
     * تولید Refresh Token
     */
    public String generateRefreshToken(UserDetails user) {

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + refreshTokenExpirationMs)
                )
                .signWith(refreshTokenSecret, SignatureAlgorithm.HS256)
                .compact();
    }


    /**
     * استخراج username از Access Token
     */
    public String extractUsernameFromAccessToken(String token) {
        return parseAccessToken(token).getBody().getSubject();
    }


    /**
     * استخراج username از Refresh Token
     */
    public String extractUsernameFromRefreshToken(String token) {
        return parseRefreshToken(token).getBody().getSubject();
    }


    /**
     * استخراج roles
     */
    public List<String> extractRolesFromAccessToken(String token) {
        Claims claims = parseAccessToken(token).getBody();
        return claims.get("roles", List.class);
    }


    /**
     * اعتبارسنجی Access Token
     */
    public boolean validateAccessToken(String token) throws ExpiredJwtException {

        try {
            parseAccessToken(token);
            return true;

        } catch (ExpiredJwtException ex) {
            log.warn("Access token expired");
            throw ex;

        } catch (JwtException ex) {
            log.error("Invalid access token: {}", ex.getMessage());
            return false;
        }
    }


    /**
     * اعتبارسنجی Refresh Token
     */
    public boolean validateRefreshToken(String token) {

        try {
            parseRefreshToken(token);
            return true;

        } catch (JwtException ex) {
            log.error("Invalid refresh token: {}", ex.getMessage());
            return false;
        }
    }


    /**
     * بررسی اعتبار Access Token برای کاربر
     */
    public boolean isAccessTokenValid(String token, UserDetails userDetails) {

        final String username = extractUsernameFromAccessToken(token);

        return username.equals(userDetails.getUsername())
                && validateAccessToken(token);
    }


    /**
     * بررسی اعتبار Refresh Token
     */
    public boolean isRefreshTokenValid(String token, UserDetails userDetails) {

        final String username = extractUsernameFromRefreshToken(token);

        return username.equals(userDetails.getUsername())
                && validateRefreshToken(token);
    }


    /**
     * پارس Access Token
     * سازگار با نسخه‌های جدید JJWT
     */
    private io.jsonwebtoken.Jws<Claims> parseAccessToken(String token) {

        return Jwts.parser()
                .verifyWith(accessTokenSecret)
                .build()
                .parseSignedClaims(token);
    }


    /**
     * پارس Refresh Token
     * سازگار با نسخه‌های جدید JJWT
     */
    private io.jsonwebtoken.Jws<Claims> parseRefreshToken(String token) {

        return Jwts.parser()
                .verifyWith(refreshTokenSecret)
                .build()
                .parseSignedClaims(token);
    }
}