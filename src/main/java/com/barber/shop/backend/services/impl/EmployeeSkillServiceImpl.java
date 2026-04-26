package com.barber.shop.backend.services.impl;

import com.barber.shop.backend.dtos.EmployeeServiceDto;
import com.barber.shop.backend.exceptions.ConflictException;
import com.barber.shop.backend.exceptions.DuplicateResourceException;
import com.barber.shop.backend.exceptions.ResourceNotFoundException;
import com.barber.shop.backend.mappers.EmployeeServiceMapper;
import com.barber.shop.backend.models.EmployeeService;
import com.barber.shop.backend.repositories.EmployeeServiceRepository;
import com.barber.shop.backend.services.EmployeeSkillService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeSkillServiceImpl implements EmployeeSkillService {

    private final EmployeeServiceRepository employeeServiceRepository;
    private final EmployeeServiceMapper employeeServiceMapper;

    @Override
    public EmployeeServiceDto create(EmployeeServiceDto dto) {
        if (employeeServiceRepository.existsByEmployeeIdAndServiceId(dto.employeeId(), dto.serviceId())) {
            throw new DuplicateResourceException("Employee already assigned to service");
        }
        EmployeeService saved = employeeServiceRepository.save(employeeServiceMapper.toEntity(dto));
        return employeeServiceMapper.toDto(saved);
    }

    @Override
    public EmployeeServiceDto update(Long id, EmployeeServiceDto dto) {
        EmployeeService entity = employeeServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee service mapping not found: " + id));
        verifyVersion(dto.version(), entity.getVersion(), "Employee service mapping");
        employeeServiceRepository.findByEmployeeIdAndServiceId(dto.employeeId(), dto.serviceId())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateResourceException("Employee already assigned to service");
                });
        employeeServiceMapper.updateEntityFromDto(dto, entity);
        return employeeServiceMapper.toDto(employeeServiceRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeServiceDto getById(Long id) {
        return employeeServiceMapper.toDto(employeeServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee service mapping not found: " + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeServiceDto> getAll() {
        return employeeServiceRepository.findAll().stream().map(employeeServiceMapper::toDto).toList();
    }

    @Override
    public void delete(Long id) {
        EmployeeService entity = employeeServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee service mapping not found: " + id));
        employeeServiceRepository.delete(entity);
    }

    private void verifyVersion(Long requestedVersion, Long actualVersion, String resourceName) {
        if (requestedVersion != null && !requestedVersion.equals(actualVersion)) {
            throw new ConflictException(resourceName + " was modified by another transaction");
        }
    }
}
