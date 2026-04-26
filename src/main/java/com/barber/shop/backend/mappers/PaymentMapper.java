package com.barber.shop.backend.mappers;

import com.barber.shop.backend.dtos.PaymentDto;
import com.barber.shop.backend.exceptions.ResourceNotFoundException;
import com.barber.shop.backend.models.Payment;
import com.barber.shop.backend.repositories.AppointmentRepository;
import com.barber.shop.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentMapper {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    public PaymentDto toDto(Payment entity) {
        if (entity == null) {
            return null;
        }
        return new PaymentDto(
                entity.getId(),
                entity.getVersion(),
                entity.getAppointment().getId(),
                entity.getReceivedByUser().getId(),
                entity.getPaymentMethod(),
                entity.getPaymentStatus(),
                entity.getAmount(),
                entity.getTransactionReference(),
                entity.getPaidAt(),
                entity.getNotes(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public Payment toEntity(PaymentDto dto) {
        if (dto == null) {
            return null;
        }
        Payment entity = new Payment();
        updateEntityFromDto(dto, entity);
        return entity;
    }

    public void updateEntityFromDto(PaymentDto dto, Payment entity) {
        entity.setAppointment(appointmentRepository.findById(dto.appointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found: " + dto.appointmentId())));
        entity.setReceivedByUser(userRepository.findById(dto.receivedByUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + dto.receivedByUserId())));
        entity.setPaymentMethod(dto.paymentMethod());
        entity.setPaymentStatus(dto.paymentStatus());
        entity.setAmount(dto.amount());
        entity.setTransactionReference(dto.transactionReference());
        entity.setPaidAt(dto.paidAt());
        entity.setNotes(dto.notes());
    }
}
