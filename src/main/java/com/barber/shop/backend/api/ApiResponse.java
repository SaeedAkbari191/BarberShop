package com.barber.shop.backend.api;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        Map<String, String> errors,
        LocalDateTime timestamp
) {

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data, null, LocalDateTime.now());
    }

    public static ApiResponse<Void> error(String message, Map<String, String> errors) {
        return new ApiResponse<>(false, message, null, errors, LocalDateTime.now());
    }
}
