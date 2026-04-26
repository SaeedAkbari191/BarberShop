package com.barber.shop.backend.repositories;

import com.barber.shop.backend.models.Payment;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByAppointmentId(Long appointmentId);

    List<Payment> findByPaidAtBetween(LocalDateTime start, LocalDateTime end);

    void deleteByAppointmentId(Long appointmentId);

    @Query("select coalesce(sum(p.amount), 0) from Payment p where p.appointment.id = :appointmentId and p.paymentStatus <> com.barber.shop.backend.enums.PaymentStatus.VOID")
    BigDecimal sumPaidAmountByAppointmentId(@Param("appointmentId") Long appointmentId);
}
