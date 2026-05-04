package com.barber.shop.backend.service;

import com.barber.shop.backend.dtos.employee.EmployeeCreateDto;
import com.barber.shop.backend.dtos.employee.EmployeeResponseDto;
import com.barber.shop.backend.dtos.employee.EmployeeUpdateDto;

import java.util.List;

public interface EmployeeService {
    EmployeeResponseDto createEmployee(EmployeeCreateDto employeeCreateDto);

    EmployeeResponseDto updateEmployee(Long id,EmployeeUpdateDto employeeUpdateDto);

    List<EmployeeResponseDto> getAllEmployees();

    EmployeeResponseDto getEmployeeById(Long id);

    EmployeeResponseDto getEmployeeCode(String employeeCode);

    void deleteEmployee(Long id);

}
