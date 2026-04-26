package com.barber.shop.backend.models;

import com.barber.shop.backend.enums.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import com.barber.shop.backend.utils.ValidationPatterns;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@DynamicUpdate
@SQLDelete(sql = "UPDATE customers SET is_deleted = true, deleted_at = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP, version = version + 1 WHERE id = ? AND version = ?")
@SQLRestriction("is_deleted = false")
@Table(
        name = "customers",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_customers_code", columnNames = "customer_code"),
                @UniqueConstraint(name = "uk_customers_phone", columnNames = "phone")
        },
        indexes = {
                @Index(name = "idx_customers_name", columnList = "last_name, first_name"),
                @Index(name = "idx_customers_deleted", columnList = "is_deleted"),
                @Index(name = "idx_customers_last_visit", columnList = "last_visit_at"),
                @Index(name = "idx_customers_email", columnList = "email")
        }
)
public class Customer extends SoftDeletableEntity {

    @NotBlank
    @Size(max = 50)
    @Column(name = "customer_code", nullable = false, length = 50)
    private String customerCode;

    @NotBlank
    @Size(max = 100)
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Size(max = 100)
    @Column(name = "last_name", length = 100)
    private String lastName;

    @NotBlank
    @Pattern(regexp = ValidationPatterns.PHONE)
    @Size(max = 30)
    @Column(name = "phone", nullable = false, length = 30)
    private String phone;

    @Email
    @Size(max = 150)
    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 20)
    private Gender gender;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "marketing_opt_in", nullable = false)
    private Boolean marketingOptIn = Boolean.FALSE;

    @Column(name = "last_visit_at")
    private LocalDateTime lastVisitAt;

    @JsonIgnore
    @OneToMany(mappedBy = "customer")
    private List<Appointment> appointments = new ArrayList<>();
}
