    package com.barber.shop.backend.models;

    import com.barber.shop.backend.enums.CommissionType;
    import com.barber.shop.backend.enums.EmploymentStatus;
    import com.barber.shop.backend.enums.Gender;
    import com.fasterxml.jackson.annotation.JsonBackReference;
    import com.fasterxml.jackson.annotation.JsonIgnore;
    import jakarta.persistence.Column;
    import jakarta.persistence.Entity;
    import jakarta.persistence.EnumType;
    import jakarta.persistence.Enumerated;
    import jakarta.persistence.FetchType;
    import jakarta.persistence.Index;
    import jakarta.persistence.JoinColumn;
    import jakarta.persistence.OneToMany;
    import jakarta.persistence.OneToOne;
    import jakarta.persistence.Table;
    import jakarta.persistence.UniqueConstraint;
    import jakarta.validation.constraints.DecimalMin;
    import jakarta.validation.constraints.NotBlank;
    import jakarta.validation.constraints.NotNull;
    import jakarta.validation.constraints.Pattern;
    import jakarta.validation.constraints.Size;
    import java.math.BigDecimal;
    import java.time.LocalDate;
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
    @SQLDelete(sql = "UPDATE employees SET is_deleted = true, deleted_at = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP, version = version + 1 WHERE id = ? AND version = ?")
    @SQLRestriction("is_deleted = false")
    @Table(
            name = "employees",
            uniqueConstraints = {
                    @UniqueConstraint(name = "uk_employees_user_id", columnNames = "user_id"),
                    @UniqueConstraint(name = "uk_employees_code", columnNames = "employee_code"),
                    @UniqueConstraint(name = "uk_employees_phone", columnNames = "phone")
            },
            indexes = {
                    @Index(name = "idx_employees_status", columnList = "employment_status"),
                    @Index(name = "idx_employees_deleted", columnList = "is_deleted"),
                    @Index(name = "idx_employees_name", columnList = "last_name, first_name")
            }
    )
    public class Employee extends SoftDeletableEntity {

        @JsonBackReference("user-employee")
        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id",
                foreignKey = @jakarta.persistence.ForeignKey(name = "fk_employees_user"))
        private User user;

        @NotBlank
        @Size(max = 50)
        @Column(name = "employee_code", nullable = false, length = 50)
        private String employeeCode;

        @NotBlank
        @Size(max = 100)
        @Column(name = "first_name", nullable = false, length = 100)
        private String firstName;

        @NotBlank
        @Size(max = 100)
        @Column(name = "last_name", nullable = false, length = 100)
        private String lastName;

        @Enumerated(EnumType.STRING)
        @Column(name = "gender", length = 20)
        private Gender gender;

        @NotBlank
        @Pattern(regexp = ValidationPatterns.PHONE)
        @Size(max = 30)
        @Column(name = "phone", nullable = false, length = 30)
        private String phone;

        @NotNull
        @Column(name = "hire_date", nullable = false)
        private LocalDate hireDate;

        @NotNull
        @Enumerated(EnumType.STRING)
        @Column(name = "employment_status", nullable = false, length = 30)
        private EmploymentStatus employmentStatus = EmploymentStatus.ACTIVE;

        @Enumerated(EnumType.STRING)
        @Column(name = "commission_type", length = 30)
        private CommissionType commissionType;

        @DecimalMin(value = "0.00")
        @Column(name = "commission_value", precision = 10, scale = 2)
        private BigDecimal commissionValue;

        @Size(max = 500)
        @Column(name = "notes", length = 500)
        private String notes;

        @JsonIgnore
        @OneToMany(mappedBy = "employee")
        private List<EmployeeService> employeeServices = new ArrayList<>();

        @JsonIgnore
        @OneToMany(mappedBy = "employee")
        private List<WorkSchedule> workSchedules = new ArrayList<>();

        @JsonIgnore
        @OneToMany(mappedBy = "assignedEmployee")
        private List<Appointment> assignedAppointments = new ArrayList<>();

        @JsonIgnore
        @OneToMany(mappedBy = "employee")
        private List<AppointmentService> appointmentServices = new ArrayList<>();


    }
