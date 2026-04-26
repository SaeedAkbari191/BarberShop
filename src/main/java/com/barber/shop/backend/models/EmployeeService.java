package com.barber.shop.backend.models;

import com.barber.shop.backend.enums.SkillLevel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

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
        name = "employee_services",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_employee_service", columnNames = {"employee_id", "service_id"})
        },
        indexes = {
                @Index(name = "idx_employee_services_employee", columnList = "employee_id"),
                @Index(name = "idx_employee_services_service", columnList = "service_id"),
                @Index(name = "idx_employee_services_active", columnList = "is_active")
        }
)
public class EmployeeService extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false,
            foreignKey = @jakarta.persistence.ForeignKey(name = "fk_employee_services_employee"))
    private Employee employee;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_id", nullable = false,
            foreignKey = @jakarta.persistence.ForeignKey(name = "fk_employee_services_service"))
    private BarberService service;

    @DecimalMin(value = "0.00")
    @Column(name = "custom_price", precision = 10, scale = 2)
    private BigDecimal customPrice;

    @Min(1)
    @Column(name = "custom_duration_minutes")
    private Integer customDurationMinutes;

    @Enumerated(EnumType.STRING)
    @Column(name = "skill_level", length = 30)
    private SkillLevel skillLevel;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = Boolean.TRUE;
}
