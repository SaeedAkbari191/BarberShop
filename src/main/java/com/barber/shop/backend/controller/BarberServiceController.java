package com.barber.shop.backend.controller;


import com.barber.shop.backend.dtos.barberService.BarberServiceCreateDto;
import com.barber.shop.backend.dtos.barberService.BarberServiceResponseDto;
import com.barber.shop.backend.dtos.barberService.BarberServiceUpdateDto;
import com.barber.shop.backend.service.BarberServiceInf;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/services")
public class BarberServiceController {

    private final BarberServiceInf barberService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<BarberServiceResponseDto> createBarberService(@Valid @RequestBody BarberServiceCreateDto request) {
        return ResponseEntity.ok(barberService.createService(request));
    }

    @GetMapping
    ResponseEntity<List<BarberServiceResponseDto>> getAllServices() {
        return ResponseEntity.ok(barberService.findAllService());
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<BarberServiceResponseDto> updateService(@PathVariable Long id, @Valid @RequestBody BarberServiceUpdateDto request) {
        return ResponseEntity.ok(barberService.updateService(id, request));
    }

    @GetMapping("/active")
    public ResponseEntity<List<BarberServiceResponseDto>> getActiveServices() {
        return ResponseEntity.ok(barberService.findActiveServices());
    }

    @GetMapping("/id/{id}")
    ResponseEntity<BarberServiceResponseDto> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(barberService.findServiceById(id));
    }

    @GetMapping("/code/{code}")
    ResponseEntity<BarberServiceResponseDto> getServiceByCode(@PathVariable String code) {
        return ResponseEntity.ok(barberService.findByServiceCode(code));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<BarberServiceResponseDto>> getByCategory(
            @PathVariable String category
    ) {
        return ResponseEntity.ok(barberService.findByCategory(category));
    }

    @GetMapping("/search")
    public ResponseEntity<List<BarberServiceResponseDto>> searchByName(
            @RequestParam String name
    ) {
        return ResponseEntity.ok(barberService.searchByName(name));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Void> deleteServiceById(@PathVariable Long id) {
        barberService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}
