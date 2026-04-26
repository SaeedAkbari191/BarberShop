package com.barber.shop.backend.mappers;

import com.barber.shop.backend.dtos.RoleDto;
import com.barber.shop.backend.models.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public RoleDto toDto(Role entity) {
        if (entity == null) {
            return null;
        }
        return new RoleDto(
                entity.getId(),
                entity.getVersion(),
                entity.getCode(),
                entity.getName(),
                entity.getDescription(),
                entity.getIsActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public Role toEntity(RoleDto dto) {
        if (dto == null) {
            return null;
        }
        Role entity = new Role();
        updateEntityFromDto(dto, entity);
        return entity;
    }

    public void updateEntityFromDto(RoleDto dto, Role entity) {
        entity.setCode(dto.code());
        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setIsActive(dto.isActive() != null ? dto.isActive() : Boolean.TRUE);
    }
}
