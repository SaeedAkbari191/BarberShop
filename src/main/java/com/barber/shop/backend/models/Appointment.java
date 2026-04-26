package com.barber.shop.backend.models;

import com.barber.shop.backend.enums.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import lombok.*;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@DynamicUpdate
@Check(constraints = "end_time > start_time")
@SQLDelete(sql = "UPDATE appointments SET is_deleted = true, deleted_at = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP, version = version + 1 WHERE id = ? AND version = ?")
@SQLRestriction("is_deleted = false")
@Table(
        name = "appointments",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_appointments_number", columnNames = "appointment_number")
        },
        indexes = {
                @Index(name = "idx_appointments_customer", columnList = "customer_id"),
                @Index(name = "idx_appointments_employee_date", columnList = "assigned_employee_id, appointment_date"),
                @Index(name = "idx_appointments_date_time", columnList = "appointment_date, start_time"),
                @Index(name = "idx_appointments_status", columnList = "status"),
                @Index(name = "idx_appointments_booked_by", columnList = "booked_by_user_id"),
                @Index(name = "idx_appointments_deleted", columnList = "is_deleted")
        }
)
public class Appointment extends SoftDeletableEntity {

    @NotBlank
    @Size(max = 50)
    @Column(name = "appointment_number", nullable = false, length = 50)
    private String appointmentNumber;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false,
            foreignKey = @jakarta.persistence.ForeignKey(name = "fk_appointments_customer"))
    private Customer customer;

    @JsonIgnore
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booked_by_user_id", nullable = false,
            foreignKey = @jakarta.persistence.ForeignKey(name = "fk_appointments_booked_by"))
    private User bookedByUser;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_employee_id",
            foreignKey = @jakarta.persistence.ForeignKey(name = "fk_appointments_employee"))
    private Employee assignedEmployee;

    @NotNull
    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private AppointmentStatus status = AppointmentStatus.BOOKED;

    @NotNull
    @DecimalMin(value = "0.00")
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @NotNull
    @DecimalMin(value = "0.00")
    @Column(name = "discount_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @NotNull
    @DecimalMin(value = "0.00")
    @Column(name = "final_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal finalAmount = BigDecimal.ZERO;

    @Size(max = 1000)
    @Column(name = "notes", length = 1000)
    private String notes;

    @Size(max = 500)
    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "checked_in_at")
    private LocalDateTime checkedInAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @JsonManagedReference("appointment-lines")
    @OneToMany(mappedBy = "appointment")
    @OrderBy("sortOrder ASC")
    private List<AppointmentService> appointmentServices = new ArrayList<>();

    @JsonManagedReference("appointment-payments")
    @OneToMany(mappedBy = "appointment")
    @OrderBy("paidAt ASC")
    private List<Payment> payments = new ArrayList<>();
}
