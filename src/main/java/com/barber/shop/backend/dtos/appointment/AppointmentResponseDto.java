package com.barber.shop.backend.dtos.appointment;

import com.barber.shop.backend.enums.AppointmentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record AppointmentResponseDto(

        Long id,

        String appointmentNumber,

        Long customerId,
        String customerName,

        Long bookedByUserId,

        LocalDateTime startTime,
        LocalDateTime endTime,

        AppointmentStatus status,

        BigDecimal totalAmount,
        BigDecimal discountAmount,
        BigDecimal finalAmount,

        String notes,

        String cancellationReason,
        LocalDateTime cancelledAt,

        List<AppointmentServiceResponseDto> services

) {}