    package com.barber.shop.backend.models;

    import com.barber.shop.backend.enums.CommissionType;
    import com.barber.shop.backend.enums.EmploymentStatus;
    import com.barber.shop.backend.enums.Gender;
    import jakarta.persistence.*;
    import lombok.*;
    import org.hibernate.annotations.DynamicUpdate;
    import org.hibernate.annotations.SQLDelete;
    import org.hibernate.annotations.SQLRestriction;

    import java.math.BigDecimal;
    import java.time.LocalDate;
    import java.util.ArrayList;
    import java.util.List;


    @Entity
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @DynamicUpdate
    @SQLDelete(sql = "UPDATE employees SET is_deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE id=?")
    @SQLRestriction("is_deleted = false")
    @Table(name = "employees")
    public class Employee extends SoftDeletableEntity {

        @OneToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        @Column(name = "employee_code", nullable = false, unique = true)
        private String employeeCode;

        @Column(name = "first_name", nullable = false)
        private String firstName;

        @Column(name = "last_name", nullable = false)
        private String lastName;

        @Enumerated(EnumType.STRING)
        private Gender gender;

        @Column(nullable = false)
        private String phone;

        @Column(name = "hire_date", nullable = false)
        private LocalDate hireDate;

        @Enumerated(EnumType.STRING)
        @Column(name = "employment_status", nullable = false)
        private EmploymentStatus employmentStatus;

        @Enumerated(EnumType.STRING)
        private CommissionType commissionType;

        @Column(name = "commission_value", precision = 10, scale = 2)
        private BigDecimal commissionValue;

        @Column(length = 500)
        private String notes;

        @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<EmployeeService> employeeServices = new ArrayList<>();
    }