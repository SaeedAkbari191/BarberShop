//// package: com.barber.shop.backend.dtos.appointment.AppointmentCreateDto
//package com.barber.shop.backend.dtos.appointment;
//
//import com.barber.shop.backend.enums.AppointmentStatus;
//import jakarta.validation.constraints.DecimalMin;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Size;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//
//public record AppointmentCreateDto(
//
//        @NotNull
//        Long customerId,
//
//        @NotNull
//        Long bookedByUserId,
//
//        @NotNull
//        LocalDate appointmentDate,
//
//        AppointmentStatus status,
//
//        @NotNull
//        @DecimalMin(value = "0.00")
//        BigDecimal totalAmount,
//
//        @NotNull
//        @DecimalMin(value = "0.00")
//        BigDecimal discountAmount,
//
//        @NotNull
//        @DecimalMin(value = "0.00")
//        BigDecimal finalAmount,
//
//        @Size(max = 1000)
//        String notes
//) {
//}