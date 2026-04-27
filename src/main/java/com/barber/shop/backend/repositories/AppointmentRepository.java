package com.barber.shop.backend.repositories;

import com.barber.shop.backend.enums.AppointmentStatus;
import com.barber.shop.backend.models.Appointment;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Optional<Appointment> findByAppointmentNumber(String appointmentNumber);

    List<Appointment> findByCustomerId(Long customerId);

    List<Appointment> findByAssignedEmployeeIdAndAppointmentDate(Long employeeId, LocalDate appointmentDate);


}
