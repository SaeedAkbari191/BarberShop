package com.barber.shop.backend.mappers;

import com.barber.shop.backend.dtos.UserDto;
import com.barber.shop.backend.exceptions.ResourceNotFoundException;
import com.barber.shop.backend.models.User;
import com.barber.shop.backend.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final RoleRepository roleRepository;

    public UserDto toDto(User entity) {
        if (entity == null) {
            return null;
        }
        return new UserDto(
                entity.getId(),
                entity.getVersion(),
                entity.getRole().getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getStatus(),
                null,
                entity.getLastLoginAt(),
                entity.getIsDeleted(),
                entity.getDeletedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public User toEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }
        User entity = new User();
        updateEntityFromDto(dto, entity);
        return entity;
    }

    public void updateEntityFromDto(UserDto dto, User entity) {
        entity.setRole(roleRepository.findById(dto.roleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + dto.roleId())));
        entity.setUsername(dto.username());
        entity.setEmail(dto.email());
        entity.setPhone(dto.phone());
        entity.setStatus(dto.status());
        if (dto.passwordHash() != null && !dto.passwordHash().isBlank()) {
            entity.setPasswordHash(dto.passwordHash());
        }
    }
}
