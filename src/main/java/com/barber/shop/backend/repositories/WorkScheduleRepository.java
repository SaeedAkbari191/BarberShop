package com.barber.shop.backend.repositories;

import com.barber.shop.backend.models.WorkSchedule;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Long> {

    List<WorkSchedule> findByEmployeeIdAndScheduleDate(Long employeeId, LocalDate scheduleDate);

    List<WorkSchedule> findByEmployeeId(Long employeeId);
}
