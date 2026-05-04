package com.barber.shop.backend.repositories;

import com.barber.shop.backend.models.EmployeeService;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeServiceRepository extends JpaRepository<EmployeeService, Long> {

    boolean existsByEmployeeIdAndServiceId(Long employeeId, Long serviceId);

    List<EmployeeService> findByEmployeeId(Long employeeId);

    List<EmployeeService> findByServiceId(Long serviceId);
}