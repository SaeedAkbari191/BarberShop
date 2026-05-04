package com.barber.shop.backend.models;

import com.barber.shop.backend.enums.AppointmentServiceStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "appointment_services")
public class AppointmentService extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private BarberService service;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    // ✅ مهم‌ترین بخش سیستم
    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    // ✅ snapshot (برای جلوگیری از تغییر آینده)
    private String serviceNameSnapshot;

    private BigDecimal priceSnapshot;

    private Integer durationMinutesSnapshot;

    private BigDecimal lineTotal;

    private Integer sortOrder;

    @Enumerated(EnumType.STRING)
    private AppointmentServiceStatus status;
}