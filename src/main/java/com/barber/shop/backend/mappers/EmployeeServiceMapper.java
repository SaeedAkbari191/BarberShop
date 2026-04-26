package com.barber.shop.backend.mappers;

import com.barber.shop.backend.dtos.EmployeeServiceDto;
import com.barber.shop.backend.exceptions.ResourceNotFoundException;
import com.barber.shop.backend.models.EmployeeService;
import com.barber.shop.backend.repositories.EmployeeRepository;
import com.barber.shop.backend.repositories.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeServiceMapper {

    private final EmployeeRepository employeeRepository;
    private final ServiceRepository serviceRepository;

    public EmployeeServiceDto toDto(EmployeeService entity) {
        if (entity == null) {
            return null;
        }
        return new EmployeeServiceDto(
                entity.getId(),
                entity.getVersion(),
                entity.getEmployee().getId(),
                entity.getService().getId(),
                entity.getCustomPrice(),
                entity.getCustomDurationMinutes(),
                entity.getSkillLevel(),
                entity.getIsActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public EmployeeService toEntity(EmployeeServiceDto dto) {
        if (dto == null) {
            return null;
        }
        EmployeeService entity = new EmployeeService();
        updateEntityFromDto(dto, entity);
        return entity;
    }

    public void updateEntityFromDto(EmployeeServiceDto dto, EmployeeService entity) {
        entity.setEmployee(employeeRepository.findById(dto.employeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + dto.employeeId())));
        entity.setService(serviceRepository.findById(dto.serviceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service not found: " + dto.serviceId())));
        entity.setCustomPrice(dto.customPrice());
        entity.setCustomDurationMinutes(dto.customDurationMinutes());
        entity.setSkillLevel(dto.skillLevel());
        entity.setIsActive(dto.isActive() != null ? dto.isActive() : Boolean.TRUE);
    }
}
