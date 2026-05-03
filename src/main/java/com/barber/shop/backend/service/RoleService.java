package com.barber.shop.backend.service;

import com.barber.shop.backend.dtos.role.RoleCreateDto;
import com.barber.shop.backend.dtos.role.RoleResponseDto;
import com.barber.shop.backend.dtos.role.RoleUpdateDto;
import com.barber.shop.backend.enums.RoleCode;

import java.util.List;

public interface RoleService {

    public RoleResponseDto createRole(RoleCreateDto role);

    List<RoleResponseDto> getAllRoles();

    RoleResponseDto getRoleById(Long id);

    RoleResponseDto getRoleByCode(RoleCode code);

    public RoleResponseDto updateRole(Long roleId, RoleUpdateDto dto);

    List<RoleResponseDto> getActiveRoles();

    public void deleteRole(Long roleId);
}
