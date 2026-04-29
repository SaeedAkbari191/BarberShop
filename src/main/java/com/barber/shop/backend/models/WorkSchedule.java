package com.barber.shop.backend.models;

import com.barber.shop.backend.enums.WorkScheduleStatus;
import com.barber.shop.backend.enums.WorkScheduleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "work_schedules")
public class WorkSchedule extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    private LocalDate scheduleDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private LocalTime breakStartTime;

    private LocalTime breakEndTime;

    @Enumerated(EnumType.STRING)
    private WorkScheduleType scheduleType;

    @Enumerated(EnumType.STRING)
    private WorkScheduleStatus status;

    private String notes;

    private Boolean isOff = false;
}