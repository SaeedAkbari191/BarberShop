package com.barber.shop.backend.controller;

import com.barber.shop.backend.dtos.employee.EmployeeCreateDto;
import com.barber.shop.backend.dtos.employee.EmployeeResponseDto;
import com.barber.shop.backend.dtos.employee.EmployeeUpdateDto;
import com.barber.shop.backend.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeResponseDto> createEmployee(
            @RequestBody EmployeeCreateDto request
    ) {
        return ResponseEntity.ok(employeeService.createEmployee(request));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponseDto>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<EmployeeResponseDto> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(employeeService.getEmployeeCode(code));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto> update(
            @PathVariable Long id,
            @RequestBody EmployeeUpdateDto request
    ) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}