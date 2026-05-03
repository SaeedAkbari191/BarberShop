package com.barber.shop.backend.service.impl;

import com.barber.shop.backend.dtos.barberService.BarberServiceCreateDto;
import com.barber.shop.backend.dtos.barberService.BarberServiceResponseDto;
import com.barber.shop.backend.dtos.barberService.BarberServiceUpdateDto;
import com.barber.shop.backend.models.BarberService;
import com.barber.shop.backend.repositories.BarberServiceRepository;
import com.barber.shop.backend.service.BarberServiceInf;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class BarberServiceImpl implements BarberServiceInf {
    private final BarberServiceRepository barberServiceRepository;

    @Override
    public BarberServiceResponseDto createService(BarberServiceCreateDto request) {
        log.info("Creating new Service: {}", request.name());

        if (barberServiceRepository.existsByServiceCode(request.serviceCode())) {
            throw new DataIntegrityViolationException(
                    "Service with code already exists: " + request.serviceCode()
            );
        }
        BarberService serviceToSave = BarberService.builder()
                .serviceCode(request.serviceCode())
                .name(request.name())
                .category(request.category())
                .description(request.description())
                .basePrice(request.basePrice())
                .durationMinutes(request.durationMinutes())
                .isActive(request.isActive() != null ? request.isActive() : true)
                .build();

        return mapToBarberServiceResponseDto(barberServiceRepository.save(serviceToSave));
    }

    @Override
    public BarberServiceResponseDto updateService(Long id, BarberServiceUpdateDto request) {
        BarberService barberService = findBarberServiceEntityById(id);

        if (!barberService.getVersion().equals(request.version())) {
            throw new OptimisticLockException(
                    "Service was modified by another transaction."
            );
        }
        if (request.name() != null) {
            barberService.setName(request.name());
        }
        if (request.category() != null) {
            barberService.setCategory(request.category());
        }

        if (request.description() != null) {
            barberService.setDescription(request.description());
        }

        if (request.basePrice() != null) {
            barberService.setBasePrice(request.basePrice());
        }
        if (request.durationMinutes() != null) {
            barberService.setDurationMinutes(request.durationMinutes());
        }
        if (request.isActive() != null) {
            barberService.setIsActive(request.isActive());
        }
        BarberService updatedBarberService = barberServiceRepository.save(barberService);

        log.info("Barber Service updated successfully: {}", updatedBarberService.getId());
        return mapToBarberServiceResponseDto(updatedBarberService);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BarberServiceResponseDto> findAllService() {
        return barberServiceRepository.findAll()
                .stream()
                .map(this::mapToBarberServiceResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BarberServiceResponseDto findServiceById(Long id) {
        return mapToBarberServiceResponseDto(findBarberServiceEntityById(id));
    }


    @Override
    @Transactional(readOnly = true)
    public BarberServiceResponseDto findByServiceCode(String serviceCode) {
        BarberService barberService = barberServiceRepository.findByServiceCode(serviceCode)
                .orElseThrow(() -> new EntityNotFoundException("Barber service with code " + serviceCode + " not found"));
        return mapToBarberServiceResponseDto(barberService);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BarberServiceResponseDto> findActiveServices() {

        return barberServiceRepository.findByIsActiveTrue()
                .stream()
                .map(this::mapToBarberServiceResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BarberServiceResponseDto> findByCategory(String category) {

        return barberServiceRepository.findByCategoryIgnoreCase(category)
                .stream()
                .map(this::mapToBarberServiceResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BarberServiceResponseDto> searchByName(String name) {

        return barberServiceRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::mapToBarberServiceResponseDto)
                .toList();
    }


    @Override
    public void deleteService(Long id) {
        log.info("Deleting barber service with ID: {}", id);

        BarberService service = findBarberServiceEntityById(id);

        service.markDeleted();

        barberServiceRepository.save(service);

        log.info("Barber service soft deleted successfully: {}", id);
    }

    private BarberService findBarberServiceEntityById(Long id) {

        BarberService service = barberServiceRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "BarberService not found with ID: " + id
                        )
                );
        if (Boolean.TRUE.equals(service.getIsDeleted())) {
            throw new EntityNotFoundException("Service deleted");
        }
        return service;
    }

    private BarberServiceResponseDto mapToBarberServiceResponseDto(BarberService barberService) {
        return new BarberServiceResponseDto(
                barberService.getId(),
                barberService.getVersion(),
                barberService.getServiceCode(),
                barberService.getName(),
                barberService.getCategory(),
                barberService.getDescription(),
                barberService.getBasePrice(),
                barberService.getDurationMinutes(),
                barberService.getIsActive(),
                barberService.getCreatedAt(),
                barberService.getUpdatedAt()
        );
    }
}
