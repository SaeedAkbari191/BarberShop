package com.barber.shop.backend.repositories;

import com.barber.shop.backend.enums.RoleCode;
import com.barber.shop.backend.models.Role;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    boolean existsByCode( RoleCode code);
}
