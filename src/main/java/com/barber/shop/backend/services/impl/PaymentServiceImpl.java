package com.barber.shop.backend.services.impl;

import com.barber.shop.backend.dtos.PaymentDto;
import com.barber.shop.backend.enums.AppointmentStatus;
import com.barber.shop.backend.enums.PaymentStatus;
import com.barber.shop.backend.exceptions.BusinessValidationException;
import com.barber.shop.backend.exceptions.ConflictException;
import com.barber.shop.backend.exceptions.ResourceNotFoundException;
import com.barber.shop.backend.mappers.PaymentMapper;
import com.barber.shop.backend.models.Appointment;
import com.barber.shop.backend.models.Payment;
import com.barber.shop.backend.repositories.AppointmentRepository;
import com.barber.shop.backend.repositories.PaymentRepository;
import com.barber.shop.backend.services.PaymentService;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final AppointmentRepository appointmentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public PaymentDto create(PaymentDto dto) {
        validatePaymentConsistency(dto, null);
        Payment saved = paymentRepository.save(paymentMapper.toEntity(dto));
        return paymentMapper.toDto(saved);
    }

    @Override
    public PaymentDto update(Long id, PaymentDto dto) {
        Payment entity = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + id));
        verifyVersion(dto.version(), entity.getVersion(), "Payment");
        validatePaymentConsistency(dto, id);
        paymentMapper.updateEntityFromDto(dto, entity);
        return paymentMapper.toDto(paymentRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentDto getById(Long id) {
        return paymentMapper.toDto(paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentDto> getAll() {
        return paymentRepository.findAll().stream().map(paymentMapper::toDto).toList();
    }

    @Override
    public void delete(Long id) {
        Payment entity = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + id));
        paymentRepository.delete(entity);
    }

    private void validatePaymentConsistency(PaymentDto dto, Long currentPaymentId) {
        Appointment appointment = appointmentRepository.findById(dto.appointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found: " + dto.appointmentId()));
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new BusinessValidationException("Cannot register payment for a cancelled appointment");
        }
        BigDecimal existingPaid = paymentRepository.findByAppointmentId(dto.appointmentId()).stream()
                .filter(payment -> currentPaymentId == null || !payment.getId().equals(currentPaymentId))
                .filter(payment -> payment.getPaymentStatus() != PaymentStatus.VOID)
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal projectedTotal = existingPaid.add(dto.amount());
        if (projectedTotal.compareTo(appointment.getFinalAmount()) > 0) {
            throw new BusinessValidationException("Payment total exceeds appointment final amount");
        }
    }

    private void verifyVersion(Long requestedVersion, Long actualVersion, String resourceName) {
        if (requestedVersion != null && !requestedVersion.equals(actualVersion)) {
            throw new ConflictException(resourceName + " was modified by another transaction");
        }
    }
}
