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

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
@SQLDelete(sql = "UPDATE customers SET is_deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE id=?")
@SQLRestriction("is_deleted = false")
@Table(name = "customers")
public class Customer extends SoftDeletableEntity {

    @Column(name = "customer_code", nullable = false, unique = true)
    private String customerCode;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    private String lastName;

    @Column(nullable = false)
    private String phone;

    private String email;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "marketing_opt_in", nullable = false)
    private Boolean marketingOptIn = false;

}