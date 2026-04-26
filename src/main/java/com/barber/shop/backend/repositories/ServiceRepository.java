package com.barber.shop.backend.repositories;

import com.barber.shop.backend.models.BarberService;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<BarberService, Long> {

    Optional<BarberService> findByServiceCode(String serviceCode);

    Optional<BarberService> findByName(String name);

    boolean existsByName(String name);

    boolean existsByServiceCode(String serviceCode);
}
