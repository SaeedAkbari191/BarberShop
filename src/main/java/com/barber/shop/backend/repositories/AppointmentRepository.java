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

    long countByAssignedEmployeeIdAndAppointmentDateAndStatusInAndStartTimeLessThanAndEndTimeGreaterThan(
            Long employeeId,
            LocalDate appointmentDate,
            Collection<AppointmentStatus> statuses,
            LocalTime newEnd,
            LocalTime newStart
    );

    @Query("""
            select count(a) from Appointment a
            where a.assignedEmployee.id = :employeeId
              and a.appointmentDate = :appointmentDate
              and a.status in :statuses
              and a.startTime < :newEnd
              and a.endTime > :newStart
              and (:appointmentId is null or a.id <> :appointmentId)
            """)
    long countOverlappingAppointments(
            @Param("employeeId") Long employeeId,
            @Param("appointmentDate") LocalDate appointmentDate,
            @Param("statuses") Collection<AppointmentStatus> statuses,
            @Param("newStart") LocalTime newStart,
            @Param("newEnd") LocalTime newEnd,
            @Param("appointmentId") Long appointmentId
    );
}
