package com.barber.shop.backend.mappers;

import com.barber.shop.backend.dtos.AppointmentDto;
import com.barber.shop.backend.exceptions.ResourceNotFoundException;
import com.barber.shop.backend.models.Appointment;
import com.barber.shop.backend.repositories.CustomerRepository;
import com.barber.shop.backend.repositories.EmployeeRepository;
import com.barber.shop.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentMapper {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    public AppointmentDto toDto(Appointment entity) {
        if (entity == null) {
            return null;
        }
        return new AppointmentDto(
                entity.getId(),
                entity.getVersion(),
                entity.getAppointmentNumber(),
                entity.getCustomer().getId(),
                entity.getBookedByUser().getId(),
                entity.getAssignedEmployee() != null ? entity.getAssignedEmployee().getId() : null,
                entity.getAppointmentDate(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getStatus(),
                entity.getTotalAmount(),
                entity.getDiscountAmount(),
                entity.getFinalAmount(),
                entity.getNotes(),
                entity.getCancellationReason(),
                entity.getCancelledAt(),
                entity.getCheckedInAt(),
                entity.getCompletedAt(),
                entity.getIsDeleted(),
                entity.getDeletedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public Appointment toEntity(AppointmentDto dto) {
        if (dto == null) {
            return null;
        }
        Appointment entity = new Appointment();
        updateEntityFromDto(dto, entity);
        return entity;
    }

    public void updateEntityFromDto(AppointmentDto dto, Appointment entity) {
        entity.setAppointmentNumber(dto.appointmentNumber());
        entity.setCustomer(customerRepository.findById(dto.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + dto.customerId())));
        entity.setBookedByUser(userRepository.findById(dto.bookedByUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + dto.bookedByUserId())));
        entity.setAssignedEmployee(dto.assignedEmployeeId() == null ? null :
                employeeRepository.findById(dto.assignedEmployeeId())
                        .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + dto.assignedEmployeeId())));
        entity.setAppointmentDate(dto.appointmentDate());
        entity.setStartTime(dto.startTime());
        entity.setEndTime(dto.endTime());
        entity.setStatus(dto.status());
        entity.setTotalAmount(dto.totalAmount());
        entity.setDiscountAmount(dto.discountAmount());
        entity.setFinalAmount(dto.finalAmount());
        entity.setNotes(dto.notes());
        entity.setCancellationReason(dto.cancellationReason());
        entity.setCancelledAt(dto.cancelledAt());
        entity.setCheckedInAt(dto.checkedInAt());
        entity.setCompletedAt(dto.completedAt());
    }
}
