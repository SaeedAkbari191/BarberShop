package com.barber.shop.backend.mappers;

import com.barber.shop.backend.dtos.WorkScheduleDto;
import com.barber.shop.backend.exceptions.ResourceNotFoundException;
import com.barber.shop.backend.models.WorkSchedule;
import com.barber.shop.backend.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkScheduleMapper {

    private final EmployeeRepository employeeRepository;

    public WorkScheduleDto toDto(WorkSchedule entity) {
        if (entity == null) {
            return null;
        }
        return new WorkScheduleDto(
                entity.getId(),
                entity.getVersion(),
                entity.getEmployee().getId(),
                entity.getScheduleDate(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getBreakStartTime(),
                entity.getBreakEndTime(),
                entity.getScheduleType(),
                entity.getStatus(),
                entity.getNotes(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public WorkSchedule toEntity(WorkScheduleDto dto) {
        if (dto == null) {
            return null;
        }
        WorkSchedule entity = new WorkSchedule();
        updateEntityFromDto(dto, entity);
        return entity;
    }

    public void updateEntityFromDto(WorkScheduleDto dto, WorkSchedule entity) {
        entity.setEmployee(employeeRepository.findById(dto.employeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + dto.employeeId())));
        entity.setScheduleDate(dto.scheduleDate());
        entity.setStartTime(dto.startTime());
        entity.setEndTime(dto.endTime());
        entity.setBreakStartTime(dto.breakStartTime());
        entity.setBreakEndTime(dto.breakEndTime());
        entity.setScheduleType(dto.scheduleType());
        entity.setStatus(dto.status());
        entity.setNotes(dto.notes());
    }
}
