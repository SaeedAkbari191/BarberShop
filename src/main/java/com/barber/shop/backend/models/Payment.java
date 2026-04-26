package com.barber.shop.backend.models;

import com.barber.shop.backend.enums.PaymentMethod;
import com.barber.shop.backend.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@DynamicUpdate
@Table(
        name = "payments",
        indexes = {
                @Index(name = "idx_payments_appointment", columnList = "appointment_id"),
                @Index(name = "idx_payments_method", columnList = "payment_method"),
                @Index(name = "idx_payments_status", columnList = "payment_status"),
                @Index(name = "idx_payments_paid_at", columnList = "paid_at"),
                @Index(name = "idx_payments_reference", columnList = "transaction_reference")
        }
)
public class Payment extends BaseEntity {

    @JsonBackReference("appointment-payments")
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "appointment_id", nullable = false,
            foreignKey = @jakarta.persistence.ForeignKey(name = "fk_payments_appointment"))
    private Appointment appointment;

    @JsonIgnore
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "received_by_user_id", nullable = false,
            foreignKey = @jakarta.persistence.ForeignKey(name = "fk_payments_received_by"))
    private User receivedByUser;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 30)
    private PaymentMethod paymentMethod;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 30)
    private PaymentStatus paymentStatus = PaymentStatus.PAID;

    @NotNull
    @DecimalMin(value = "0.01")
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
