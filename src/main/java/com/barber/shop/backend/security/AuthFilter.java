package com.barber.shop.backend.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;


    /**
     * مسیرهایی که JWT نیاز ندارند
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getRequestURI();

        return path.equals("/api/auth/login")
                || path.equals("/api/auth/register")
                || path.equals("/api/auth/refresh")
                || path.equals("/api/auth/logout")
                || path.startsWith("/products/");
    }


    /**
     * فیلتر اصلی JWT
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = getTokenFromRequest(request);

        try {

            // اگر توکن وجود نداشت
            if (!StringUtils.hasText(token)) {
                log.debug("No JWT token found in request");
                filterChain.doFilter(request, response);
                return;
            }

            log.info("JWT token detected");

            // اعتبارسنجی Access Token
            if (jwtUtils.validateAccessToken(token)) {

                String username =
                        jwtUtils.extractUsernameFromAccessToken(token);

                log.info("Valid token for user: {}", username);

                // فقط اگر قبلاً authenticate نشده
                if (StringUtils.hasText(username)
                        && SecurityContextHolder.getContext()
                        .getAuthentication() == null) {

                    UserDetails userDetails =
                            customUserDetailsService
                                    .loadUserByUsername(username);

                    if (jwtUtils.isAccessTokenValid(token, userDetails)) {

                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );

                        authentication.setDetails(
                                new WebAuthenticationDetailsSource()
                                        .buildDetails(request)
                        );

                        SecurityContextHolder.getContext()
                                .setAuthentication(authentication);

                        log.info("Security context set for user: {}", username);
                    }
                }
            }

        } catch (ExpiredJwtException ex) {

            log.warn("JWT token expired for user: {}",
                    ex.getClaims().getSubject());

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(
                    """
                    {
                      "error": "TOKEN_EXPIRED",
                      "message": "Access token has expired"
                    }
                    """
            );
            return;

        } catch (Exception ex) {

            log.error("JWT authentication error: {}", ex.getMessage());

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(
                    """
                    {
                      "error": "INVALID_TOKEN",
                      "message": "Authentication failed"
                    }
                    """
            );
            return;
        }

        filterChain.doFilter(request, response);
    }


    /**
     * استخراج JWT از Authorization Header
     */
    private String getTokenFromRequest(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(authHeader)
                && authHeader.startsWith("Bearer ")) {

            return authHeader.substring(7);
        }

        return null;
    }
}