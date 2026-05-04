package com.barber.shop.backend.service;

import com.barber.shop.backend.dtos.workSchedule.*;

import java.time.LocalDate;
import java.util.List;

public interface WorkScheduleService {

    WorkScheduleResponseDto create(WorkScheduleCreateDto request);

    WorkScheduleResponseDto update(Long id, WorkScheduleUpdateDto request);

    void delete(Long id);

    WorkScheduleResponseDto getById(Long id);

    List<WorkScheduleResponseDto> getAll();

    List<WorkScheduleResponseDto> getByEmployee(Long employeeId);

    List<WorkScheduleResponseDto> getByEmployeeAndDate(Long employeeId, LocalDate date);
}