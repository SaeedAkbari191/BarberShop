package com.barber.shop.backend.service.impl;

import com.barber.shop.backend.dtos.employee.EmployeeCreateDto;
import com.barber.shop.backend.dtos.employee.EmployeeResponseDto;
import com.barber.shop.backend.dtos.employee.EmployeeUpdateDto;
import com.barber.shop.backend.models.Employee;
import com.barber.shop.backend.models.User;
import com.barber.shop.backend.repositories.EmployeeRepository;
import com.barber.shop.backend.repositories.UserRepository;
import com.barber.shop.backend.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    @Override
    public EmployeeResponseDto createEmployee(EmployeeCreateDto request) {
        log.info("Creating new Employee: {}", request.firstName() + " " + request.lastName());

        if (employeeRepository.existsByEmployeeCode(request.employeeCode())) {
            throw new DataIntegrityViolationException(
                    "Employee with code already exists: " + request.employeeCode()
            );
        }
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        Employee employee = Employee.builder()
                .user(user)
                .employeeCode(request.employeeCode())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .gender(request.gender())
                .phone(request.phone())
                .hireDate(request.hireDate())
                .employmentStatus(request.employmentStatus())
                .commissionType(request.commissionType())
                .commissionValue(request.commissionValue())
                .notes(request.notes())
                .build();

        return mapToEmployeeResponseDto(employeeRepository.save(employee));
    }

    @Override
    public EmployeeResponseDto updateEmployee(Long id, EmployeeUpdateDto request) {

        log.info("Updating Employee with ID: {}", id);

        Employee employee = findEmployeeById(id);

        if (request.firstName() != null) {
            employee.setFirstName(request.firstName());
        }
        if (request.lastName() != null) {
            employee.setLastName(request.lastName());
        }
        if (request.gender() != null) {
            employee.setGender(request.gender());
        }
        if (request.phone() != null) {
            employee.setPhone(request.phone());
        }
        if (request.employmentStatus() != null) {
            employee.setEmploymentStatus(request.employmentStatus());
        }
        if (request.commissionType() != null) {
            employee.setCommissionType(request.commissionType());
        }
        if (request.commissionValue() != null) {
            employee.setCommissionValue(request.commissionValue());
        }
        if (request.notes() != null) {
            employee.setNotes(request.notes());
        }

        Employee updated = employeeRepository.save(employee);

        log.info("Employee updated successfully: {}", updated.getId());

        return mapToEmployeeResponseDto(updated);
    }


    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(this::mapToEmployeeResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDto getEmployeeById(Long id) {
        return mapToEmployeeResponseDto(findEmployeeById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDto getEmployeeCode(String employeeCode) {

        Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found with code: " + employeeCode)
                );

        return mapToEmployeeResponseDto(employee);
    }


    @Override
    public void deleteEmployee(Long id) {

        Employee employee = findEmployeeById(id);

        employee.markDeleted();
        employeeRepository.save(employee);

        log.info("Employee soft deleted successfully: {}", id);
    }

    private Employee findEmployeeById(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found with ID: " + id)
                );

        if (Boolean.TRUE.equals(employee.getIsDeleted())) {
            throw new RuntimeException("Employee already deleted");
        }

        return employee;
    }

    private EmployeeResponseDto mapToEmployeeResponseDto(Employee employee) {
        return new EmployeeResponseDto(
                employee.getId(),
                employee.getEmployeeCode(),
                employee.getUser().getId(),
                employee.getUser().getUsername(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getGender(),
                employee.getPhone(),
                employee.getHireDate(),
                employee.getEmploymentStatus(),
                employee.getCommissionType(),
                employee.getCommissionValue(),
                employee.getNotes(),
                employee.getCreatedAt(),
                employee.getUpdatedAt()

        );
    }
}
