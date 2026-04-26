package com.barber.shop.backend.dtos;

import com.barber.shop.backend.enums.WorkScheduleStatus;
import com.barber.shop.backend.enums.WorkScheduleType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record WorkScheduleDto(
        Long id,
        Long version,
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
        @NotNull
        WorkScheduleStatus status,
        @Size(max = 500)
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
