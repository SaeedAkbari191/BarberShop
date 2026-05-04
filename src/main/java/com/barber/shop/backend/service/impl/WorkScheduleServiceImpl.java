package com.barber.shop.backend.service.impl;

import com.barber.shop.backend.dtos.workSchedule.WorkScheduleCreateDto;
import com.barber.shop.backend.dtos.workSchedule.WorkScheduleResponseDto;
import com.barber.shop.backend.dtos.workSchedule.WorkScheduleUpdateDto;
import com.barber.shop.backend.enums.WorkScheduleStatus;
import com.barber.shop.backend.enums.WorkScheduleType;
import com.barber.shop.backend.models.Employee;
import com.barber.shop.backend.models.WorkSchedule;
import com.barber.shop.backend.repositories.EmployeeRepository;
import com.barber.shop.backend.repositories.WorkScheduleRepository;
import com.barber.shop.backend.service.WorkScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class WorkScheduleServiceImpl implements WorkScheduleService {

    private final WorkScheduleRepository repository;
    private final EmployeeRepository employeeRepository;

    @Override
    public WorkScheduleResponseDto create(WorkScheduleCreateDto request) {

        Employee employee = employeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        boolean isOff = Boolean.TRUE.equals(request.isOff());

        // ✅ consistency check
        if (isOff && request.scheduleType() == WorkScheduleType.WORKING) {
            throw new RuntimeException("OFF day cannot have WORKING type");
        }

        if (!isOff && request.scheduleType() != WorkScheduleType.WORKING) {
            throw new RuntimeException("Non-OFF must be WORKING type");
        }

        // ✅ OFF DAY STRICT
        if (isOff) {

            if (request.startTime() != null || request.endTime() != null) {
                throw new RuntimeException("OFF day must not have time");
            }

            if (request.breakStartTime() != null || request.breakEndTime() != null) {
                throw new RuntimeException("OFF day must not have break");
            }

            WorkSchedule offDay = WorkSchedule.builder()
                    .employee(employee)
                    .scheduleDate(request.scheduleDate())
                    .isOff(true)
                    .scheduleType(request.scheduleType())
                    .status(WorkScheduleStatus.PLANNED)
                    .notes(request.notes())
                    .build();

            return map(repository.save(offDay));
        }

        // ✅ WORKING VALIDATION
        validateTimeStrict(
                request.startTime(),
                request.endTime(),
                request.breakStartTime(),
                request.breakEndTime()
        );

        // ✅ OVERLAP CHECK
        if (repository.existsOverlapping(
                request.employeeId(),
                request.scheduleDate(),
                request.startTime(),
                request.endTime()
        )) {
            throw new RuntimeException("Schedule overlaps with existing schedule");
        }

        WorkSchedule entity = WorkSchedule.builder()
                .employee(employee)
                .scheduleDate(request.scheduleDate())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .breakStartTime(request.breakStartTime())
                .breakEndTime(request.breakEndTime())
                .scheduleType(WorkScheduleType.WORKING)
                .status(resolveStatus(request.scheduleDate()))
                .isOff(false)
                .notes(request.notes())
                .build();

        return map(repository.save(entity));
    }

    @Override
    public WorkScheduleResponseDto update(Long id, WorkScheduleUpdateDto request) {

        WorkSchedule entity = findByIdOrThrow(id);

        boolean isWorking = request.scheduleType() == WorkScheduleType.WORKING;

        // ✅ OFF / NON-WORKING
        if (!isWorking) {

            if (request.startTime() != null || request.endTime() != null) {
                throw new RuntimeException("Non-working schedule must not have time");
            }

            if (request.breakStartTime() != null || request.breakEndTime() != null) {
                throw new RuntimeException("Non-working schedule must not have break");
            }

            entity.setScheduleType(request.scheduleType());
            entity.setIsOff(true);
            entity.setStartTime(null);
            entity.setEndTime(null);
            entity.setBreakStartTime(null);
            entity.setBreakEndTime(null);
            entity.setStatus(request.status());
            entity.setNotes(request.notes());

            return map(repository.save(entity));
        }

        // ✅ WORKING VALIDATION
        validateTimeStrict(
                request.startTime(),
                request.endTime(),
                request.breakStartTime(),
                request.breakEndTime()
        );

        // ✅ OVERLAP (exclude self)
        if (repository.existsOverlappingExcludeId(
                entity.getId(),
                entity.getEmployee().getId(),
                entity.getScheduleDate(),
                request.startTime(),
                request.endTime()
        )) {
            throw new RuntimeException("Schedule overlaps with existing schedule");
        }

        entity.setStartTime(request.startTime());
        entity.setEndTime(request.endTime());
        entity.setBreakStartTime(request.breakStartTime());
        entity.setBreakEndTime(request.breakEndTime());
        entity.setScheduleType(WorkScheduleType.WORKING);
        entity.setIsOff(false);
        entity.setStatus(request.status());
        entity.setNotes(request.notes());

        return map(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        repository.delete(findByIdOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public WorkScheduleResponseDto getById(Long id) {
        return map(findByIdOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkScheduleResponseDto> getAll() {
        return repository.findAll().stream().map(this::map).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkScheduleResponseDto> getByEmployee(Long employeeId) {
        return repository.findByEmployeeId(employeeId)
                .stream().map(this::map).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkScheduleResponseDto> getByEmployeeAndDate(Long employeeId, LocalDate date) {
        return repository.findByEmployeeIdAndScheduleDate(employeeId, date)
                .stream().map(this::map).toList();
    }

    private WorkSchedule findByIdOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("WorkSchedule not found"));
    }

    private WorkScheduleResponseDto map(WorkSchedule w) {
        return new WorkScheduleResponseDto(
                w.getId(),
                w.getEmployee().getId(),
                w.getEmployee().getFirstName() + " " + w.getEmployee().getLastName(),
                w.getScheduleDate(),
                w.getStartTime(),
                w.getEndTime(),
                w.getBreakStartTime(),
                w.getBreakEndTime(),
                w.getScheduleType(),
                w.getStatus(),
                w.getIsOff(),
                w.getNotes()
        );
    }



    private WorkScheduleStatus resolveStatus(LocalDate date) {
        LocalDate today = LocalDate.now();

        if (date.isAfter(today)) {
            return WorkScheduleStatus.PLANNED;
        }
        if (date.isEqual(today)) {
            return WorkScheduleStatus.ACTIVE;
        }
        return WorkScheduleStatus.COMPLETED;
    }

    private void validateTimeStrict(
            LocalTime start,
            LocalTime end,
            LocalTime breakStart,
            LocalTime breakEnd
    ) {

        if (start == null || end == null) {
            throw new RuntimeException("Start and end time are required");
        }

        if (!start.isBefore(end)) {
            throw new RuntimeException("Start must be before end");
        }

        // ✅ break consistency
        if ((breakStart == null && breakEnd != null) ||
                (breakStart != null && breakEnd == null)) {
            throw new RuntimeException("Break must have both start and end");
        }

        if (breakStart != null) {

            if (!breakStart.isBefore(breakEnd)) {
                throw new RuntimeException("Break start must be before break end");
            }

            if (breakStart.isBefore(start) || breakEnd.isAfter(end)) {
                throw new RuntimeException("Break must be inside working hours");
            }
        }
    }
}