package com.barber.shop.backend.service.impl;

import com.barber.shop.backend.dtos.role.RoleCreateDto;
import com.barber.shop.backend.dtos.role.RoleResponseDto;
import com.barber.shop.backend.dtos.role.RoleUpdateDto;
import com.barber.shop.backend.enums.RoleCode;
import com.barber.shop.backend.models.Role;
import com.barber.shop.backend.repositories.RoleRepository;
import com.barber.shop.backend.service.RoleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponseDto> getAllRoles() {

        return roleRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponseDto getRoleById(Long roleId) {
        log.info("Fetching role by ID: {}", roleId);

        Role role = findRoleEntityById(roleId);
        return mapToResponseDto(role);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponseDto getRoleByCode(RoleCode code) {

        log.info("Fetching role by code: {}", code);

        Role role = roleRepository.findByCode(code)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Role not found with code: " + code
                        )
                );

        return mapToResponseDto(role);
    }

    @Override
    public RoleResponseDto updateRole(Long roleId, RoleUpdateDto dto) {

        log.info("Updating role with ID: {}", roleId);

        Role role = findRoleEntityById(roleId);

        if (dto.description() != null) {
            role.setDescription(dto.description());
        }

        if (dto.isActive() != null) {
            role.setIsActive(dto.isActive());
        }

        Role updatedRole = roleRepository.save(role);

        log.info("Role updated successfully: {}", updatedRole.getId());

        return mapToResponseDto(updatedRole);
    }

    @Override
    public void deleteRole(Long roleId) {

        log.info("Deleting role with ID: {}", roleId);

        Role role = findRoleEntityById(roleId);

        if (role.getCode() == RoleCode.ADMIN) {
            throw new IllegalStateException("System role ADMIN cannot be deleted.");
        }

        // اگر کاربر وابسته دارد حذف نشود
        if (role.getUsers() != null && !role.getUsers().isEmpty()) {
            throw new IllegalStateException(
                    "Cannot delete role because users are assigned to it."
            );
        }

        roleRepository.delete(role);

        log.info("Role deleted successfully: {}", roleId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponseDto> getActiveRoles() {

        log.info("Fetching active roles");

        return roleRepository.findByIsActiveTrue()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    private Role findRoleEntityById(Long roleId) {

        return roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Role not found with ID: " + roleId
                        )
                );
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
