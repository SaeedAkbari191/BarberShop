package com.barber.shop.backend.dtos.workSchedule;

import com.barber.shop.backend.enums.WorkScheduleType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public record WorkScheduleCreateDto(
        @NotNull
        Long employeeId,

        @NotNull
        LocalDate scheduleDate,

        @NotNull
        LocalTime startTime,

        @NotNull
        LocalTime endTime,

        LocalTime breakStartTime,
        LocalTime breakEndTime,

        @NotNull
        WorkScheduleType scheduleType,

        Boolean isOff,

        @Size(max = 500)
        String notes
) {
}
