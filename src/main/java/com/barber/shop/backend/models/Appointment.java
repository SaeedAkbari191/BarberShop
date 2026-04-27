package com.barber.shop.backend.models;

import com.barber.shop.backend.enums.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(
        name = "appointments",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_appointments_number", columnNames = "appointment_number")
        },
        indexes = {
                @Index(name = "idx_appointments_customer", columnList = "customer_id"),
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


    @NotNull
    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;


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
