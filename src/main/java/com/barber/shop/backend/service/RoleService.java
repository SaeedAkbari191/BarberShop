package com.barber.shop.backend.service;

import com.barber.shop.backend.dtos.role.RoleCreateDto;
import com.barber.shop.backend.dtos.role.RoleResponseDto;
import com.barber.shop.backend.models.Role;

public interface RoleService {

    public RoleResponseDto createRole(RoleCreateDto role);
}
