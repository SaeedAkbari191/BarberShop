package com.barber.shop.backend.dtos.workSchedule;

import com.barber.shop.backend.enums.WorkScheduleStatus;
import com.barber.shop.backend.enums.WorkScheduleType;

import java.time.LocalDate;
import java.time.LocalTime;

public record WorkScheduleResponseDto(
        Long id,
        Long employeeId,
        String employeeName,

        LocalDate scheduleDate,
        LocalTime startTime,
        LocalTime endTime,
        LocalTime breakStartTime,
        LocalTime breakEndTime,

        WorkScheduleType scheduleType,
        WorkScheduleStatus status,
        Boolean isOff,
        String notes
) {
}
