package com.barber.shop.backend.controller;

import com.barber.shop.backend.dtos.employeeService.EmployeeServiceCreateDto;
import com.barber.shop.backend.dtos.employeeService.EmployeeServiceResponseDto;
import com.barber.shop.backend.dtos.employeeService.EmployeeServiceUpdateDto;
import com.barber.shop.backend.service.EmployeeServiceInf;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee-services")
@RequiredArgsConstructor
public class EmployeeServiceController {

    private final EmployeeServiceInf service;

    @PostMapping
    public ResponseEntity<EmployeeServiceResponseDto> create(@RequestBody EmployeeServiceCreateDto request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeServiceResponseDto> update(
            @PathVariable Long id,
            @RequestBody EmployeeServiceUpdateDto request
    ) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeServiceResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeServiceResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<EmployeeServiceResponseDto>> getByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(service.getByEmployee(employeeId));
    }

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<EmployeeServiceResponseDto>> getByService(@PathVariable Long serviceId) {
        return ResponseEntity.ok(service.getByService(serviceId));
    }
}
