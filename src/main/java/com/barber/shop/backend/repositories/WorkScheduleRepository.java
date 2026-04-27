package com.barber.shop.backend.repositories;

import com.barber.shop.backend.models.WorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Long> {


}
