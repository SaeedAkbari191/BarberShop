package com.barber.shop.backend.models;

import com.barber.shop.backend.enums.PaymentMethod;
import com.barber.shop.backend.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@DynamicUpdate
@Table(
        name = "payments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_payments_appointment_service",
                        columnNames = "appointment_service_id"
                )
        },
        indexes = {
                @Index(name = "idx_payments_service", columnList = "appointment_service_id"),
                @Index(name = "idx_payments_method", columnList = "payment_method"),
                @Index(name = "idx_payments_status", columnList = "payment_status"),
                @Index(name = "idx_payments_paid_at", columnList = "paid_at"),
                @Index(name = "idx_payments_reference", columnList = "transaction_reference")
        }
)
public class Payment extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "appointment_service_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_payments_appointment_service"))
    private AppointmentService appointmentService;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "received_by_user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_payments_received_by"))
    private User receivedByUser;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 30)
    private PaymentMethod paymentMethod;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 30)
    private PaymentStatus paymentStatus;

    @NotNull
    @DecimalMin("0.01")
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Size(max = 100)
    @Column(name = "transaction_reference", length = 100)
    private String transactionReference;

    @NotNull
    @Column(name = "paid_at", nullable = false)
    private LocalDateTime paidAt;

    @Size(max = 500)
    @Column(name = "notes", length = 500)
    private String notes;
}
