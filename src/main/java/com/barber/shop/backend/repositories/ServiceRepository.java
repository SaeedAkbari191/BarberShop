package com.barber.shop.backend.repositories;

import com.barber.shop.backend.models.BarberService;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<BarberService, Long> {


}
