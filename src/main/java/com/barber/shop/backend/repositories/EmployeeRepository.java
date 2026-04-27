package com.barber.shop.backend.repositories;

import com.barber.shop.backend.models.Employee;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
