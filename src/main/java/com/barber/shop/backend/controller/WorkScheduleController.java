package com.barber.shop.backend.controller;

import com.barber.shop.backend.dtos.workSchedule.*;
import com.barber.shop.backend.service.WorkScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/work-schedules")
@RequiredArgsConstructor
public class WorkScheduleController {

    private final WorkScheduleService service;

    @PostMapping
    public ResponseEntity<WorkScheduleResponseDto> create(@RequestBody WorkScheduleCreateDto request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkScheduleResponseDto> update(
            @PathVariable Long id,
            @RequestBody WorkScheduleUpdateDto request
    ) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkScheduleResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<WorkScheduleResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<WorkScheduleResponseDto>> getByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(service.getByEmployee(employeeId));
    }

    @GetMapping("/employee/{employeeId}/date/{date}")
    public ResponseEntity<List<WorkScheduleResponseDto>> getByEmployeeAndDate(
            @PathVariable Long employeeId,
            @PathVariable LocalDate date
    ) {
        return ResponseEntity.ok(service.getByEmployeeAndDate(employeeId, date));
    }
}