package com.barber.shop.backend.repositories;

import com.barber.shop.backend.models.Customer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {


}
