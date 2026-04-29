package com.barber.shop.backend.service.impl;

import com.barber.shop.backend.dtos.role.RoleCreateDto;
import com.barber.shop.backend.dtos.role.RoleResponseDto;
import com.barber.shop.backend.models.Role;
import com.barber.shop.backend.repositories.RoleRepository;
import com.barber.shop.backend.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public RoleResponseDto createRole(RoleCreateDto role) {
        log.info("Creating new role: {}", role.code());

        if (roleRepository.existsByCode(role.code())) {
            throw new DataIntegrityViolationException(
                    "Role with code already exists: " + role.code()
            );
        }
        Role roleToSave = Role.builder()
                .code(role.code())
                .description(role.description())
                .isActive(role.isActive() != null ? role.isActive() : true)
                .build();

        Role savedRole = roleRepository.save(roleToSave);

        log.info("Role created successfully with ID: {}", savedRole.getId());
        return mapToResponseDto(savedRole);
    }


    //  MAP TO RESPONSE
    private RoleResponseDto mapToResponseDto(Role role) {

        return new RoleResponseDto(
                role.getId(),
                role.getVersion(),
                role.getCode(),
                role.getDescription(),
                role.getIsActive(),
                role.getCreatedAt(),
                role.getUpdatedAt()
        );
    }
}
