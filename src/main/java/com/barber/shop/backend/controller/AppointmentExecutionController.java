package com.barber.shop.backend.controller;

import com.barber.shop.backend.service.AppointmentExecutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments/execution")
@RequiredArgsConstructor
public class AppointmentExecutionController {

    private final AppointmentExecutionService executionService;

    // ================= CHECK-IN =================

    @PatchMapping("/{appointmentId}/check-in")
    public ResponseEntity<Void> checkIn(@PathVariable Long appointmentId) {
        executionService.checkIn(appointmentId);
        return ResponseEntity.noContent().build();
    }

    // ================= START SERVICE =================

    @PatchMapping("/service/{id}/start")
    public ResponseEntity<Void> startService(@PathVariable Long id) {
        executionService.startService(id);
        return ResponseEntity.noContent().build();
    }

    // ================= COMPLETE SERVICE =================

    @PatchMapping("/service/{id}/complete")
    public ResponseEntity<Void> completeService(@PathVariable Long id) {
        executionService.completeService(id);
        return ResponseEntity.noContent().build();
    }

    // ================= CANCEL SERVICE =================

    @PatchMapping("/service/{id}/cancel")
    public ResponseEntity<Void> cancelService(@PathVariable Long id) {
        executionService.cancelService(id);
        return ResponseEntity.noContent().build();
    }
}