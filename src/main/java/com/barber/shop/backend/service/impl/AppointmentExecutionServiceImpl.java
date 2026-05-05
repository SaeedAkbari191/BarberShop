package com.barber.shop.backend.service.impl;

import com.barber.shop.backend.enums.AppointmentServiceStatus;
import com.barber.shop.backend.enums.AppointmentStatus;
import com.barber.shop.backend.models.Appointment;
import com.barber.shop.backend.models.AppointmentService;
import com.barber.shop.backend.repositories.AppointmentRepository;
import com.barber.shop.backend.repositories.AppointmentServiceRepository;
import com.barber.shop.backend.service.AppointmentExecutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentExecutionServiceImpl implements AppointmentExecutionService {

    private final AppointmentRepository appointmentRepo;
    private final AppointmentServiceRepository appointmentServiceRepo;

    // ================= CHECK-IN =================

    @Override
    public void checkIn(Long appointmentId) {

        Appointment appt = getAppointment(appointmentId);

        if (appt.getStatus() != AppointmentStatus.BOOKED &&
                appt.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new RuntimeException("Invalid state for check-in");
        }

        appt.setStatus(AppointmentStatus.CHECKED_IN);
    }

    // ================= START SERVICE =================

    @Override
    public void startService(Long id) {

        AppointmentService svc = getService(id);

        if (svc.getStatus() != AppointmentServiceStatus.BOOKED) {
            throw new RuntimeException("Service not in BOOKED state");
        }

        // ❗ جلوگیری از اجرای همزمان
        boolean alreadyRunning = svc.getAppointment().getServices().stream()
                .anyMatch(s -> s.getStatus() == AppointmentServiceStatus.IN_PROGRESS);

        if (alreadyRunning) {
            throw new RuntimeException("Another service already in progress");
        }

        svc.setStatus(AppointmentServiceStatus.IN_PROGRESS);
        svc.setUpdatedAt(LocalDateTime.now());

        svc.getAppointment().setStatus(AppointmentStatus.IN_PROGRESS);
    }

    // ================= COMPLETE SERVICE =================

    @Override
    public void completeService(Long id) {

        AppointmentService svc = getService(id);

        if (svc.getStatus() != AppointmentServiceStatus.IN_PROGRESS) {
            throw new RuntimeException("Service not in progress");
        }

        svc.setStatus(AppointmentServiceStatus.COMPLETED);

        Appointment appt = svc.getAppointment();

        boolean allDone = appt.getServices().stream()
                .allMatch(s -> s.getStatus() == AppointmentServiceStatus.COMPLETED);

        if (allDone) {
            appt.setStatus(AppointmentStatus.COMPLETED);
        }
    }

    // ================= CANCEL SERVICE =================

    @Override
    public void cancelService(Long id) {

        AppointmentService svc = getService(id);

        svc.setStatus(AppointmentServiceStatus.CANCELLED);
    }

    // ================= HELPERS =================

    private Appointment getAppointment(Long id) {
        return appointmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    private AppointmentService getService(Long id) {
        return appointmentServiceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
    }
}
