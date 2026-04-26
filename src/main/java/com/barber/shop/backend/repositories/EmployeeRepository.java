package com.barber.shop.backend.repositories;

import com.barber.shop.backend.models.Employee;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmployeeCode(String employeeCode);

    Optional<Employee> findByPhone(String phone);

    Optional<Employee> findByUserId(Long userId);

    boolean existsByEmployeeCode(String employeeCode);

    boolean existsByPhone(String phone);

    boolean existsByUserId(Long userId);
}
