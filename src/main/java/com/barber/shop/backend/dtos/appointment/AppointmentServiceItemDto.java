package com.barber.shop.backend.dtos.appointment;

import jakarta.validation.constraints.NotNull;

public record AppointmentServiceItemDto(

        @NotNull
        Long serviceId,

        @NotNull
        Long employeeId

) {}