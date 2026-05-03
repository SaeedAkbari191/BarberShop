package com.barber.shop.backend.repositories;

import com.barber.shop.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("""
    SELECT u FROM User u
    JOIN FETCH u.role
    WHERE u.username = :username
""")
    Optional<User> findByUsernameWithRole(String username);

    Optional<User> findActiveUserById(Long id);
}
