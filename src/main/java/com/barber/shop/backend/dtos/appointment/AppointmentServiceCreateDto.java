package com.barber.shop.backend.dtos.appointment;

import jakarta.validation.constraints.NotNull;

public record AppointmentServiceCreateDto(

        @NotNull
        Long serviceId,

        @NotNull
        Long employeeId

) {}