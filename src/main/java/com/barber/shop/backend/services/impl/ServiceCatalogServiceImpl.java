package com.barber.shop.backend.services.impl;

import com.barber.shop.backend.dtos.ServiceDto;
import com.barber.shop.backend.exceptions.ConflictException;
import com.barber.shop.backend.exceptions.DuplicateResourceException;
import com.barber.shop.backend.exceptions.ResourceNotFoundException;
import com.barber.shop.backend.mappers.ServiceMapper;
import com.barber.shop.backend.models.BarberService;
import com.barber.shop.backend.repositories.ServiceRepository;
import com.barber.shop.backend.services.ServiceCatalogService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Transactional
public class ServiceCatalogServiceImpl implements ServiceCatalogService {

    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;

    @Override
    public ServiceDto create(ServiceDto dto) {
        validateUniqueFields(dto, null);
        BarberService saved = serviceRepository.save(serviceMapper.toEntity(dto));
        return serviceMapper.toDto(saved);
    }

    @Override
    public ServiceDto update(Long id, ServiceDto dto) {
        BarberService entity = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found: " + id));
        verifyVersion(dto.version(), entity.getVersion(), "Service");
        validateUniqueFields(dto, id);
        serviceMapper.updateEntityFromDto(dto, entity);
        return serviceMapper.toDto(serviceRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceDto getById(Long id) {
        return serviceMapper.toDto(serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found: " + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceDto> getAll() {
        return serviceRepository.findAll().stream().map(serviceMapper::toDto).toList();
    }

    @Override
    public void delete(Long id) {
        BarberService entity = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found: " + id));
        serviceRepository.delete(entity);
    }

    private void validateUniqueFields(ServiceDto dto, Long currentId) {
        serviceRepository.findByServiceCode(dto.serviceCode())
                .filter(service -> !service.getId().equals(currentId))
                .ifPresent(service -> {
                    throw new DuplicateResourceException("Service code already exists: " + dto.serviceCode());
                });
        serviceRepository.findByName(dto.name())
                .filter(service -> !service.getId().equals(currentId))
                .ifPresent(service -> {
                    throw new DuplicateResourceException("Service name already exists: " + dto.name());
                });
    }

    private void verifyVersion(Long requestedVersion, Long actualVersion, String resourceName) {
        if (requestedVersion != null && !requestedVersion.equals(actualVersion)) {
            throw new ConflictException(resourceName + " was modified by another transaction");
        }
    }
}
