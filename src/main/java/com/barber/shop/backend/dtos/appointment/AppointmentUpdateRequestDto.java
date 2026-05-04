package com.barber.shop.backend.dtos.appointment;

import com.barber.shop.backend.enums.AppointmentStatus;
import jakarta.validation.constraints.NotNull;

public record AppointmentUpdateRequestDto(

        @NotNull
        AppointmentStatus status,

        String notes,

        String cancellationReason

) {}