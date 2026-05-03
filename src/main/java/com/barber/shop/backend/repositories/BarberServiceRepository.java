package com.barber.shop.backend.repositories;

import com.barber.shop.backend.models.BarberService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BarberServiceRepository extends JpaRepository<BarberService, Long> {


    boolean existsByServiceCode(String serviceCode);

    boolean existsByName(String name);

    Optional<BarberService> findByServiceCode(String serviceCode);

    List<BarberService> findByIsActiveTrue();

    List<BarberService> findByCategoryIgnoreCase(String category);


    List<BarberService> findByNameContainingIgnoreCase(String name);
}
