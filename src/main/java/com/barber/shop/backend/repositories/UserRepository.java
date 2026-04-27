package com.barber.shop.backend.repositories;

import com.barber.shop.backend.models.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


}
