package com.barber.shop.backend.repositories;

import com.barber.shop.backend.models.WorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Long> {

    List<WorkSchedule> findByEmployeeId(Long employeeId);

    List<WorkSchedule> findByEmployeeIdAndScheduleDate(Long employeeId, LocalDate date);

    @Query("""
                SELECT COUNT(ws) > 0 FROM WorkSchedule ws
                WHERE ws.employee.id = :employeeId
                AND ws.scheduleDate = :date
                AND ws.isOff = false
                AND (
                    (:start < ws.endTime AND :end > ws.startTime)
                )
            """)
    boolean existsOverlapping(
            Long employeeId,
            LocalDate date,
            LocalTime start,
            LocalTime end
    );

    @Query("""
            SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END
            FROM WorkSchedule w
            WHERE w.employee.id = :employeeId
            AND w.scheduleDate = :date
            AND w.id <> :id
            AND w.isOff = false
            AND (
                (:start < w.endTime AND :end > w.startTime)
            )
            """)
    boolean existsOverlappingExcludeId(
            Long id,
            Long employeeId,
            LocalDate date,
            LocalTime start,
            LocalTime end
    );

    @Query("""
                SELECT CASE WHEN COUNT(ws) > 0 THEN true ELSE false END
                FROM WorkSchedule ws
                WHERE ws.employee.id = :employeeId
                AND ws.scheduleDate = :date
                AND ws.startTime <= :start
                AND ws.endTime >= :end
            """)
    boolean existsValidSchedule(
            @Param("employeeId") Long employeeId,
            @Param("date") LocalDate date,
            @Param("start") LocalTime start,
            @Param("end") LocalTime end
    );
}