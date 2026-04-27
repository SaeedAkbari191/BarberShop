package com.barber.shop.backend.models;

import com.barber.shop.backend.enums.AppointmentServiceStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Check;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@DynamicUpdate
@Check(constraints = "end_time > start_time")
@Table(
        name = "appointment_services",
        indexes = {
                @Index(name = "idx_appointment_services_appointment", columnList = "appointment_id"),
                @Index(name = "idx_appointment_services_employee", columnList = "employee_id"),
                @Index(name = "idx_appointment_services_service", columnList = "service_id"),
                @Index(name = "idx_appointment_services_status", columnList = "status")
        }
)
public class AppointmentService extends BaseEntity {

    @JsonBackReference("appointment-lines")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "appointment_id", nullable = false,
            foreignKey = @jakarta.persistence.ForeignKey(name = "fk_appointment_services_appointment"))
    private Appointment appointment;

    @JsonIgnore
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_id", nullable = false,
            foreignKey = @jakarta.persistence.ForeignKey(name = "fk_appointment_services_service"))
    private BarberService service;

    @JsonIgnore
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false,
            foreignKey = @jakarta.persistence.ForeignKey(name = "fk_appointment_services_employee"))
    private Employee employee;

    @NotBlank
    @Size(max = 150)
    @Column(name = "service_name_snapshot", nullable = false, length = 150)
    private String serviceNameSnapshot;

    @NotNull
    @DecimalMin(value = "0.00")
    @Column(name = "price_snapshot", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceSnapshot;

    @NotNull
    @Min(1)
    @Column(name = "duration_minutes_snapshot", nullable = false)
    private Integer durationMinutesSnapshot;

    @NotNull
    @DecimalMin(value = "0.00")
    @Column(name = "line_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal lineTotal;

    @NotNull
    @Min(1)
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 1;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private AppointmentServiceStatus status = AppointmentServiceStatus.BOOKED;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Size(max = 500)
    @Column(name = "notes", length = 500)
    private String notes;
}
