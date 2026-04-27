// package: com.barber.shop.backend.dtos.appointmentservice

package com.barber.shop.backend.dtos.appointmentservice;

import com.barber.shop.backend.enums.AppointmentServiceStatus;
import jakarta.validation.constraints.*;
import java.time.LocalTime;

public record AppointmentServiceUpdateDto(

        @NotNull
        LocalTime startTime,

        @NotNull
        LocalTime endTime,

        @Size(max = 500)
        String notes,

        AppointmentServiceStatus status
) {}