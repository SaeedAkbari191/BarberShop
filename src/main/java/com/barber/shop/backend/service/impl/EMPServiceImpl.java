package com.barber.shop.backend.service.impl;

import com.barber.shop.backend.dtos.employeeService.EmployeeServiceCreateDto;
import com.barber.shop.backend.dtos.employeeService.EmployeeServiceResponseDto;
import com.barber.shop.backend.dtos.employeeService.EmployeeServiceUpdateDto;
import com.barber.shop.backend.models.BarberService;
import com.barber.shop.backend.models.Employee;
import com.barber.shop.backend.models.EmployeeService;
import com.barber.shop.backend.repositories.BarberServiceRepository;
import com.barber.shop.backend.repositories.EmployeeRepository;
import com.barber.shop.backend.repositories.EmployeeServiceRepository;
import com.barber.shop.backend.service.EmployeeServiceInf;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EMPServiceImpl implements EmployeeServiceInf {

    private final EmployeeServiceRepository repository;
    private final EmployeeRepository employeeRepository;
    private final BarberServiceRepository barberServiceRepository;

    @Override
    public EmployeeServiceResponseDto create(EmployeeServiceCreateDto request) {

        log.info("Assigning service {} to employee {}", request.serviceId(), request.employeeId());

        if (repository.existsByEmployeeIdAndServiceId(request.employeeId(), request.serviceId())) {
            throw new DataIntegrityViolationException("This service already assigned to employee");
        }

        Employee employee = employeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        BarberService service = barberServiceRepository.findById(request.serviceId())
                .orElseThrow(() -> new RuntimeException("Service not found"));

        EmployeeService entity = EmployeeService.builder()
                .employee(employee)
                .service(service)
                .customPrice(request.customPrice())
                .customDurationMinutes(request.customDurationMinutes())
                .skillLevel(request.skillLevel())
                .isActive(true)
                .build();

        return map(repository.save(entity));
    }

    @Override
    public EmployeeServiceResponseDto update(Long id, EmployeeServiceUpdateDto request) {

        EmployeeService entity = findByIdOrThrow(id);

        if (request.customPrice() != null) {
            entity.setCustomPrice(request.customPrice());
        }

        if (request.customDurationMinutes() != null) {
            entity.setCustomDurationMinutes(request.customDurationMinutes());
        }

        if (request.skillLevel() != null) {
            entity.setSkillLevel(request.skillLevel());
        }

        if (request.isActive() != null) {
            entity.setIsActive(request.isActive());
        }

        return map(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        EmployeeService entity = findByIdOrThrow(id);
        repository.delete(entity); // hard delete OK for join table
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeServiceResponseDto getById(Long id) {
        return map(findByIdOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeServiceResponseDto> getAll() {
        return repository.findAll().stream().map(this::map).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeServiceResponseDto> getByEmployee(Long employeeId) {
        return repository.findByEmployeeId(employeeId)
                .stream().map(this::map).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeServiceResponseDto> getByService(Long serviceId) {
        return repository.findByServiceId(serviceId)
                .stream().map(this::map).toList();
    }

    private EmployeeService findByIdOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("EmployeeService not found"));
    }

    private EmployeeServiceResponseDto map(EmployeeService e) {
        return new EmployeeServiceResponseDto(
                e.getId(),
                e.getEmployee().getId(),
                e.getService().getId(),
                e.getCustomPrice(),
                e.getCustomDurationMinutes(),
                e.getSkillLevel(),
                e.getIsActive()
        );
    }
}