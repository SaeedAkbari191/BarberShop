package com.barber.shop.backend.repositories;

import com.barber.shop.backend.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

//    boolean existsOverlap(Long employeeId, LocalDateTime start, LocalDateTime end);

    @Query("""
            SELECT COUNT(a) > 0
            FROM AppointmentService a
            WHERE a.employee.id = :employeeId
            AND a.startTime < :end
            AND a.endTime > :start
            """)
    boolean existsOverlap(Long employeeId, LocalDateTime start, LocalDateTime end);


}
