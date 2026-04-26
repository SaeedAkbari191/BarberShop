package com.barber.shop.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DynamicUpdate
@SQLDelete(sql = "UPDATE services SET is_deleted = true, deleted_at = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP, version = version + 1 WHERE id = ? AND version = ?")
@SQLRestriction("is_deleted = false")
@Table(
        name = "services",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_services_code", columnNames = "service_code"),
                @UniqueConstraint(name = "uk_services_name", columnNames = "name")
        },
        indexes = {
                @Index(name = "idx_services_active", columnList = "is_active"),
                @Index(name = "idx_services_deleted", columnList = "is_deleted"),
                @Index(name = "idx_services_category", columnList = "category")
        }
)
public class BarberService extends SoftDeletableEntity {

    @NotBlank
    @Size(max = 50)
    @Column(name = "service_code", nullable = false, length = 50)
    private String serviceCode;

    @NotBlank
    @Size(max = 150)
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Size(max = 100)
    @Column(name = "category", length = 100)
    private String category;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @NotNull
    @DecimalMin(value = "0.00")
    @Column(name = "base_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice;

    @NotNull
    @Positive
    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = Boolean.TRUE;

    @JsonIgnore
    @OneToMany(mappedBy = "service")
    private List<EmployeeService> employeeServices = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "service")
    private List<AppointmentService> appointmentServices = new ArrayList<>();
}
