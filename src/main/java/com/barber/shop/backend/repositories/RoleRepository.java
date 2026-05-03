package com.barber.shop.backend.repositories;

import com.barber.shop.backend.dtos.role.RoleResponseDto;
import com.barber.shop.backend.enums.RoleCode;
import com.barber.shop.backend.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    boolean existsByCode( RoleCode code);

    Role findRoleEntityById(Long roleId);

    Optional<Role> findByCode(RoleCode code);

    List<Role> findByIsActiveTrue();
}
