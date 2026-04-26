package com.barber.shop.backend.services.impl;

import com.barber.shop.backend.dtos.EmployeeDto;
import com.barber.shop.backend.exceptions.ConflictException;
import com.barber.shop.backend.exceptions.DuplicateResourceException;
import com.barber.shop.backend.exceptions.ResourceNotFoundException;
import com.barber.shop.backend.mappers.EmployeeMapper;
import com.barber.shop.backend.models.Employee;
import com.barber.shop.backend.repositories.EmployeeRepository;
import com.barber.shop.backend.services.EmployeeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public EmployeeDto create(EmployeeDto dto) {
        validateUniqueFields(dto, null);
        Employee saved = employeeRepository.save(employeeMapper.toEntity(dto));
        return employeeMapper.toDto(saved);
    }

    @Override
    public EmployeeDto update(Long id, EmployeeDto dto) {
        Employee entity = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + id));
        verifyVersion(dto.version(), entity.getVersion(), "Employee");
        validateUniqueFields(dto, id);
        employeeMapper.updateEntityFromDto(dto, entity);
        return employeeMapper.toDto(employeeRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDto getById(Long id) {
        return employeeMapper.toDto(employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDto> getAll() {
        return employeeRepository.findAll().stream().map(employeeMapper::toDto).toList();
    }

    @Override
    public void delete(Long id) {
        Employee entity = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + id));
        employeeRepository.delete(entity);
    }

    private void validateUniqueFields(EmployeeDto dto, Long currentId) {
        employeeRepository.findByEmployeeCode(dto.employeeCode())
                .filter(employee -> !employee.getId().equals(currentId))
                .ifPresent(employee -> {
                    throw new DuplicateResourceException("Employee code already exists: " + dto.employeeCode());
                });
        employeeRepository.findByPhone(dto.phone())
                .filter(employee -> !employee.getId().equals(currentId))
                .ifPresent(employee -> {
                    throw new DuplicateResourceException("Employee phone already exists: " + dto.phone());
                });
        if (dto.userId() != null) {
            employeeRepository.findByUserId(dto.userId())
                    .filter(employee -> !employee.getId().equals(currentId))
                    .ifPresent(employee -> {
                        throw new DuplicateResourceException("User already linked to another employee: " + dto.userId());
                    });
        }
    }

    private void verifyVersion(Long requestedVersion, Long actualVersion, String resourceName) {
        if (requestedVersion != null && !requestedVersion.equals(actualVersion)) {
            throw new ConflictException(resourceName + " was modified by another transaction");
        }
    }
}
