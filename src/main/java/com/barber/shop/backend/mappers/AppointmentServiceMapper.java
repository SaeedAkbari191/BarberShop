package com.barber.shop.backend.mappers;

import com.barber.shop.backend.dtos.AppointmentServiceDto;
import com.barber.shop.backend.exceptions.ResourceNotFoundException;
import com.barber.shop.backend.models.AppointmentService;
import com.barber.shop.backend.repositories.AppointmentRepository;
import com.barber.shop.backend.repositories.EmployeeRepository;
import com.barber.shop.backend.repositories.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentServiceMapper {

    private final AppointmentRepository appointmentRepository;
    private final ServiceRepository serviceRepository;
    private final EmployeeRepository employeeRepository;

    public AppointmentServiceDto toDto(AppointmentService entity) {
        if (entity == null) {
            return null;
        }
        return new AppointmentServiceDto(
                entity.getId(),
                entity.getVersion(),
                entity.getAppointment().getId(),
                entity.getService().getId(),
                entity.getEmployee().getId(),
                entity.getServiceNameSnapshot(),
                entity.getPriceSnapshot(),
                entity.getDurationMinutesSnapshot(),
                entity.getLineTotal(),
                entity.getSortOrder(),
                entity.getStatus(),
                entity.getStartedAt(),
                entity.getCompletedAt(),
                entity.getNotes(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public AppointmentService toEntity(AppointmentServiceDto dto) {
        if (dto == null) {
            return null;
        }
        AppointmentService entity = new AppointmentService();
        updateEntityFromDto(dto, entity);
        return entity;
    }

    public void updateEntityFromDto(AppointmentServiceDto dto, AppointmentService entity) {
        entity.setAppointment(appointmentRepository.findById(dto.appointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found: " + dto.appointmentId())));
        entity.setService(serviceRepository.findById(dto.serviceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service not found: " + dto.serviceId())));
        entity.setEmployee(employeeRepository.findById(dto.employeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + dto.employeeId())));
        entity.setServiceNameSnapshot(dto.serviceNameSnapshot());
        entity.setPriceSnapshot(dto.priceSnapshot());
        entity.setDurationMinutesSnapshot(dto.durationMinutesSnapshot());
        entity.setLineTotal(dto.lineTotal());
        entity.setSortOrder(dto.sortOrder());
        entity.setStatus(dto.status());
        entity.setStartedAt(dto.startedAt());
        entity.setCompletedAt(dto.completedAt());
        entity.setNotes(dto.notes());
    }
}
