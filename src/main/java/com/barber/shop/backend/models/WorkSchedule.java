package com.barber.shop.backend.models;

import com.barber.shop.backend.enums.WorkScheduleStatus;
import com.barber.shop.backend.enums.WorkScheduleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.*;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@DynamicUpdate
@Check(constraints = "end_time > start_time")
@Table(
        name = "work_schedules",
        indexes = {
                @Index(name = "idx_work_schedules_employee_date", columnList = "employee_id, schedule_date"),
                @Index(name = "idx_work_schedules_date", columnList = "schedule_date"),
                @Index(name = "idx_work_schedules_type", columnList = "schedule_type"),
                @Index(name = "idx_work_schedules_status", columnList = "status")
        }
)
public class WorkSchedule extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false,
            foreignKey = @jakarta.persistence.ForeignKey(name = "fk_work_schedules_employee"))
    private Employee employee;

    @NotNull
    @Column(name = "schedule_date", nullable = false)
    private LocalDate scheduleDate;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "break_start_time")
    private LocalTime breakStartTime;

    @Column(name = "break_end_time")
    private LocalTime breakEndTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_type", nullable = false, length = 30)
    private WorkScheduleType scheduleType = WorkScheduleType.WORKING;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private WorkScheduleStatus status = WorkScheduleStatus.ACTIVE;

    @Size(max = 500)
    @Column(name = "notes", length = 500)
    private String notes;
}
