// package: com.barber.shop.backend.dtos.appointmentservice.AppointmentServiceResponseDto
package com.barber.shop.backend.dtos.appointmentservice;

import com.barber.shop.backend.enums.AppointmentServiceStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record AppointmentServiceResponseDto(

        Long id,

        Long appointmentId,

        String appointmentNumber,

        Long serviceId,

        String serviceName,

        Long employeeId,

        String employeeName,

        String serviceNameSnapshot,

        BigDecimal priceSnapshot,

        Integer durationMinutesSnapshot,

        BigDecimal lineTotal,

        Integer sortOrder,

        AppointmentServiceStatus status,

        LocalDateTime startedAt,

        LocalDateTime completedAt,

        LocalTime startTime,

        LocalTime endTime,

        String notes,

        LocalDateTime createdAt

//        LocalDateTime updatedAt
) {
}