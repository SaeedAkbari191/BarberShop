
// 4) AppointmentResponseDto
package com.barber.shop.backend.dtos.appointment;

import com.barber.shop.backend.enums.AppointmentStatus;
import com.barber.shop.backend.dtos.appointmentservice.AppointmentServiceResponseDto;
import com.barber.shop.backend.dtos.payment.PaymentResponseDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record AppointmentResponseDto(

        Long id,
        String appointmentNumber,

        Long customerId,
        String customerFullName,

        Long bookedByUserId,
        String bookedByUsername,

        LocalDate appointmentDate,

        AppointmentStatus status,

        BigDecimal totalAmount,
        BigDecimal discountAmount,
        BigDecimal finalAmount,

        String notes,
        String cancellationReason,

        LocalDateTime cancelledAt,
        LocalDateTime checkedInAt,
        LocalDateTime completedAt,

//        Boolean isDeleted,
//        LocalDateTime deletedAt,

        List<AppointmentServiceResponseDto> services,
        List<PaymentResponseDto> payments,

        LocalDateTime createdAt
//        LocalDateTime updatedAt
) {
}