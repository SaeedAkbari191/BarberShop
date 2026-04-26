package com.barber.shop.backend.mappers;

import com.barber.shop.backend.dtos.ServiceDto;
import com.barber.shop.backend.models.BarberService;
import org.springframework.stereotype.Component;

@Component
public class ServiceMapper {

    public ServiceDto toDto(BarberService entity) {
        if (entity == null) {
            return null;
        }
        return new ServiceDto(
                entity.getId(),
                entity.getVersion(),
                entity.getServiceCode(),
                entity.getName(),
                entity.getCategory(),
                entity.getDescription(),
                entity.getBasePrice(),
                entity.getDurationMinutes(),
                entity.getIsActive(),
                entity.getIsDeleted(),
                entity.getDeletedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public BarberService toEntity(ServiceDto dto) {
        if (dto == null) {
            return null;
        }
        BarberService entity = new BarberService();
        updateEntityFromDto(dto, entity);
        return entity;
    }

    public void updateEntityFromDto(ServiceDto dto, BarberService entity) {
        entity.setServiceCode(dto.serviceCode());
        entity.setName(dto.name());
        entity.setCategory(dto.category());
        entity.setDescription(dto.description());
        entity.setBasePrice(dto.basePrice());
        entity.setDurationMinutes(dto.durationMinutes());
        entity.setIsActive(dto.isActive() != null ? dto.isActive() : Boolean.TRUE);
    }
}
