package com.barber.shop.backend.dtos.appointment;

import com.barber.shop.backend.enums.AppointmentServiceStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AppointmentServiceResponseDto(

        Long id,

        Long serviceId,
        String serviceName,

        Long employeeId,
        String employeeName,

        LocalDateTime startTime,
        LocalDateTime endTime,

        Integer durationMinutes,

        BigDecimal price,
        BigDecimal lineTotal,

        AppointmentServiceStatus status

) {}