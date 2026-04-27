
// اصلاحات استاندارد DTOهای Appointment بر اساس مدل نهایی شما

// 1) AppointmentLineRequestDto
package com.barber.shop.backend.dtos.appointment;

import jakarta.validation.constraints.*;
import java.time.LocalTime;

public record AppointmentLineRequestDto(

        @NotNull
        Long serviceId,

        @NotNull
        Long employeeId,

        @Min(1)
        Integer sortOrder,

        @NotNull
        LocalTime startTime,

        @NotNull
        LocalTime endTime,

        @Size(max = 500)
        String notes
) {
}