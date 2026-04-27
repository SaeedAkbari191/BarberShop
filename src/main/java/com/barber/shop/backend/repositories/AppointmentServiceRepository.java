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


}
