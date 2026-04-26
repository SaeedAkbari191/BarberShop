package com.barber.shop.backend.services.impl;

import com.barber.shop.backend.dtos.RoleDto;
import com.barber.shop.backend.exceptions.ConflictException;
import com.barber.shop.backend.exceptions.DuplicateResourceException;
import com.barber.shop.backend.exceptions.ResourceNotFoundException;
import com.barber.shop.backend.mappers.RoleMapper;
import com.barber.shop.backend.models.Role;
import com.barber.shop.backend.repositories.RoleRepository;
import com.barber.shop.backend.services.RoleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public RoleDto create(RoleDto dto) {
        if (roleRepository.existsByCode(dto.code())) {
            throw new DuplicateResourceException("Role code already exists: " + dto.code());
        }
        Role saved = roleRepository.save(roleMapper.toEntity(dto));
        return roleMapper.toDto(saved);
    }

    @Override
    public RoleDto update(Long id, RoleDto dto) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + id));
        verifyVersion(dto.version(), role.getVersion(), "Role");
        roleRepository.findByCode(dto.code())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateResourceException("Role code already exists: " + dto.code());
                });
        roleMapper.updateEntityFromDto(dto, role);
        return roleMapper.toDto(roleRepository.save(role));
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDto getById(Long id) {
        return roleMapper.toDto(roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDto> getAll() {
        return roleRepository.findAll().stream().map(roleMapper::toDto).toList();
    }

    @Override
    public void delete(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + id));
        roleRepository.delete(role);
    }

    private void verifyVersion(Long requestedVersion, Long actualVersion, String resourceName) {
        if (requestedVersion != null && !requestedVersion.equals(actualVersion)) {
            throw new ConflictException(resourceName + " was modified by another transaction");
        }
    }
}
