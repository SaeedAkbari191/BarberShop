package com.barber.shop.backend.models;

import com.barber.shop.backend.enums.WorkScheduleStatus;
import com.barber.shop.backend.enums.WorkScheduleType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
@Table(
        name = "work_schedules",
        indexes = {
                @Index(name = "idx_employee_date", columnList = "employee_id,schedule_date")
        }
)
public class WorkSchedule extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "schedule_date", nullable = false)
    private LocalDate scheduleDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private LocalTime breakStartTime;

    private LocalTime breakEndTime;

    @Enumerated(EnumType.STRING)
    private WorkScheduleType scheduleType;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private WorkScheduleStatus status;

    private String notes;

    private Boolean isOff = false;
}