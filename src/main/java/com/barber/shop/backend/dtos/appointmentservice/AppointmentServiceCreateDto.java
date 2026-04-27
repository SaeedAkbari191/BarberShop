//// package: com.barber.shop.backend.dtos.appointmentservice.AppointmentServiceCreateDto
//package com.barber.shop.backend.dtos.appointmentservice;
//
//import jakarta.validation.constraints.*;
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//
//public record AppointmentServiceCreateDto(
//
//        @NotNull
//        Long appointmentId,
//
//        @NotNull
//        Long serviceId,
//
//        @NotNull
//        Long employeeId,
//
//        @NotBlank
//        @Size(max = 150)
//        String serviceNameSnapshot,
//
//        @NotNull
//        @DecimalMin("0.00")
//        BigDecimal priceSnapshot,
//
//        @NotNull
//        @Min(1)
//        Integer durationMinutesSnapshot,
//
//        @NotNull
//        @DecimalMin("0.00")
//        BigDecimal lineTotal,
//
//        @NotNull
//        @Min(1)
//        Integer sortOrder,
//
//        @NotNull
//        LocalTime startTime,
//
//        @NotNull
//        LocalTime endTime,
//
//        String notes
//) {
//}