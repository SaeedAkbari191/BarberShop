package com.barber.shop.backend.dtos.appointment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

public record AppointmentCreateRequestDto(

        @NotNull
        Long customerId,

        @NotNull
        Long bookedByUserId,

        @NotNull
        LocalDateTime startTime,

        @NotNull
        @Size(min = 1)
        List<AppointmentServiceCreateDto> services,

        String notes
) {}