package com.barber.shop.backend.controller;

import com.barber.shop.backend.dtos.appointment.*;
import com.barber.shop.backend.service.AppointmentServiceInf;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentServiceInf appointmentService;

    // ================= CREATE =================

    @PostMapping
    public ResponseEntity<AppointmentResponseDto> create(
            @RequestBody AppointmentCreateRequestDto request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(appointmentService.create(request));
    }

    // ================= UPDATE =================

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponseDto> update(
            @PathVariable Long id,
            @RequestBody AppointmentUpdateRequestDto request
    ) {
        return ResponseEntity.ok(appointmentService.update(id, request));
    }

    // ================= CANCEL =================

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(
            @PathVariable Long id,
            @RequestParam(required = false) String reason
    ) {
        appointmentService.cancel(id, reason);
        return ResponseEntity.noContent().build();
    }

    // ================= GET BY ID =================

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getById(id));
    }

    // ================= GET ALL =================

    @GetMapping
    public ResponseEntity<List<AppointmentResponseDto>> getAll() {
        return ResponseEntity.ok(appointmentService.getAll());
    }
}