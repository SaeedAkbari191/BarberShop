package com.barber.shop.backend.services.impl;

import com.barber.shop.backend.dtos.WorkScheduleDto;
import com.barber.shop.backend.enums.WorkScheduleType;
import com.barber.shop.backend.exceptions.BusinessValidationException;
import com.barber.shop.backend.exceptions.ConflictException;
import com.barber.shop.backend.exceptions.ResourceNotFoundException;
import com.barber.shop.backend.mappers.WorkScheduleMapper;
import com.barber.shop.backend.models.WorkSchedule;
import com.barber.shop.backend.repositories.WorkScheduleRepository;
import com.barber.shop.backend.services.WorkScheduleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkScheduleServiceImpl implements WorkScheduleService {

    private final WorkScheduleRepository workScheduleRepository;
    private final WorkScheduleMapper workScheduleMapper;

    @Override
    public WorkScheduleDto create(WorkScheduleDto dto) {
        validateSchedule(dto);
        WorkSchedule saved = workScheduleRepository.save(workScheduleMapper.toEntity(dto));
        return workScheduleMapper.toDto(saved);
    }

    @Override
    public WorkScheduleDto update(Long id, WorkScheduleDto dto) {
        WorkSchedule entity = workScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work schedule not found: " + id));
        verifyVersion(dto.version(), entity.getVersion(), "Work schedule");
        validateSchedule(dto);
        workScheduleMapper.updateEntityFromDto(dto, entity);
        return workScheduleMapper.toDto(workScheduleRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public WorkScheduleDto getById(Long id) {
        return workScheduleMapper.toDto(workScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work schedule not found: " + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkScheduleDto> getAll() {
        return workScheduleRepository.findAll().stream().map(workScheduleMapper::toDto).toList();
    }

    @Override
    public void delete(Long id) {
        WorkSchedule entity = workScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work schedule not found: " + id));
        workScheduleRepository.delete(entity);
    }

    private void validateSchedule(WorkScheduleDto dto) {
        if (!dto.endTime().isAfter(dto.startTime())) {
            throw new BusinessValidationException("Schedule end time must be after start time");
        }
        if (dto.breakStartTime() != null && dto.breakEndTime() != null && !dto.breakEndTime().isAfter(dto.breakStartTime())) {
            throw new BusinessValidationException("Break end time must be after break start time");
        }
        if (dto.scheduleType() == WorkScheduleType.WORKING && dto.breakStartTime() != null && dto.breakEndTime() != null) {
            if (dto.breakStartTime().isBefore(dto.startTime()) || dto.breakEndTime().isAfter(dto.endTime())) {
                throw new BusinessValidationException("Break time must be within working hours");
            }
        }
    }

    private void verifyVersion(Long requestedVersion, Long actualVersion, String resourceName) {
        if (requestedVersion != null && !requestedVersion.equals(actualVersion)) {
            throw new ConflictException(resourceName + " was modified by another transaction");
        }
    }
}
