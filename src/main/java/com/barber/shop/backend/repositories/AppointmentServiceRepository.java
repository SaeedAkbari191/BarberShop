package com.barber.shop.backend.repositories;

import com.barber.shop.backend.models.AppointmentService;
import com.barber.shop.backend.enums.AppointmentStatus;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppointmentServiceRepository extends JpaRepository<AppointmentService, Long> {

    List<AppointmentService> findByAppointmentId(Long appointmentId);

    List<AppointmentService> findByEmployeeId(Long employeeId);

    void deleteByAppointmentId(Long appointmentId);

    @Query("""
            select count(aps) from AppointmentService aps
            join aps.appointment a
            where aps.employee.id = :employeeId
              and a.appointmentDate = :appointmentDate
              and a.status in :statuses
              and a.startTime < :requestedEnd
              and a.endTime > :requestedStart
              and (:appointmentId is null or a.id <> :appointmentId)
            """)
    long countEmployeeBookingConflicts(
            @Param("employeeId") Long employeeId,
            @Param("appointmentDate") LocalDate appointmentDate,
            @Param("statuses") Collection<AppointmentStatus> statuses,
            @Param("requestedStart") LocalTime requestedStart,
            @Param("requestedEnd") LocalTime requestedEnd,
            @Param("appointmentId") Long appointmentId
    );
}
