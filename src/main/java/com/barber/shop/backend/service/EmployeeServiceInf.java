package com.barber.shop.backend.service;

import com.barber.shop.backend.dtos.employeeService.EmployeeServiceCreateDto;
import com.barber.shop.backend.dtos.employeeService.EmployeeServiceResponseDto;
import com.barber.shop.backend.dtos.employeeService.EmployeeServiceUpdateDto;

import java.util.List;

public interface EmployeeServiceInf {

    EmployeeServiceResponseDto create(EmployeeServiceCreateDto request);

    EmployeeServiceResponseDto update(Long id, EmployeeServiceUpdateDto request);

    void delete(Long id);

    EmployeeServiceResponseDto getById(Long id);

    List<EmployeeServiceResponseDto> getAll();

    List<EmployeeServiceResponseDto> getByEmployee(Long employeeId);

    List<EmployeeServiceResponseDto> getByService(Long serviceId);
}
