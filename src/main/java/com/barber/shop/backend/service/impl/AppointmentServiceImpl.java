package com.barber.shop.backend.service.impl;

import com.barber.shop.backend.dtos.appointment.*;
import com.barber.shop.backend.enums.AppointmentServiceStatus;
import com.barber.shop.backend.enums.AppointmentStatus;
import com.barber.shop.backend.models.*;
import com.barber.shop.backend.repositories.*;
import com.barber.shop.backend.service.AppointmentServiceInf;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentServiceImpl implements AppointmentServiceInf {

    private final AppointmentRepository appointmentRepo;
    private final CustomerRepository customerRepo;
    private final UserRepository userRepo;
    private final BarberServiceRepository serviceRepo;
    private final EmployeeRepository employeeRepo;
    private final EmployeeServiceRepository employeeServiceRepo;
    private final WorkScheduleRepository workScheduleRepo;

    // ================= CREATE =================

    @Override
    public AppointmentResponseDto create(AppointmentCreateRequestDto request) {

        validateRequest(request);

        Customer customer = customerRepo.findById(request.customerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        User user = userRepo.findById(request.bookedByUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDateTime currentStart = request.startTime();

        List<AppointmentService> serviceEntities = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        int order = 0;

        for (AppointmentServiceItemDto item : request.services()) {

            BarberService service = serviceRepo.findById(item.serviceId())
                    .orElseThrow(() -> new RuntimeException("Service not found"));

            if (!service.getIsActive()) {
                throw new RuntimeException("Service is not active");
            }

            Employee employee = employeeRepo.findById(item.employeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));

            // ✔ بررسی مهارت
            EmployeeService empService = employeeServiceRepo
                    .findByEmployeeIdAndServiceIdAndIsActiveTrue(employee.getId(), service.getId())
                    .orElseThrow(() -> new RuntimeException("Employee not skilled for this service"));

            int duration = empService.getCustomDurationMinutes() != null
                    ? empService.getCustomDurationMinutes()
                    : service.getDurationMinutes();

            BigDecimal price = empService.getCustomPrice() != null
                    ? empService.getCustomPrice()
                    : service.getBasePrice();

            LocalDateTime end = currentStart.plusMinutes(duration);

            // ✔ بررسی داخل شیفت کاری
            validateWithinWorkSchedule(employee.getId(), currentStart, end);

            // ✔ بررسی تداخل
            validateNoOverlap(employee.getId(), currentStart, end);

            AppointmentService entity = AppointmentService.builder()
                    .service(service)
                    .employee(employee)
                    .startTime(currentStart)
                    .endTime(end)
                    .durationMinutesSnapshot(duration)
                    .priceSnapshot(price)
                    .lineTotal(price)
                    .serviceNameSnapshot(service.getName())
                    .status(AppointmentServiceStatus.BOOKED)
                    .sortOrder(order++)
                    .build();

            serviceEntities.add(entity);
            totalAmount = totalAmount.add(price);

            currentStart = end; // 🔗 chaining
        }

        Appointment appointment = Appointment.builder()
                .appointmentNumber(generateNumber())
                .customer(customer)
                .bookedByUser(user)
                .startTime(request.startTime())
                .endTime(currentStart)
                .status(AppointmentStatus.BOOKED)
                .totalAmount(totalAmount)
                .discountAmount(BigDecimal.ZERO)
                .finalAmount(totalAmount)
                .notes(request.notes())
                .build();

        serviceEntities.forEach(s -> s.setAppointment(appointment));
        appointment.setServices(serviceEntities);

        return map(appointmentRepo.save(appointment));
    }

    // ================= UPDATE =================

    @Override
    public AppointmentResponseDto update(Long id, AppointmentUpdateRequestDto request) {

        Appointment appt = findOrThrow(id);

        appt.setStatus(request.status());
        appt.setNotes(request.notes());

        if (request.status() == AppointmentStatus.CANCELLED) {
            appt.setCancelledAt(LocalDateTime.now());
            appt.setCancellationReason(request.cancellationReason());
        }

        return map(appointmentRepo.save(appt));
    }

    // ================= CANCEL =================

    @Override
    public void cancel(Long id, String reason) {

        Appointment appt = findOrThrow(id);

        appt.setStatus(AppointmentStatus.CANCELLED);
        appt.setCancellationReason(reason);
        appt.setCancelledAt(LocalDateTime.now());
    }

    // ================= READ =================

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponseDto getById(Long id) {
        return map(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponseDto> getAll() {
        return appointmentRepo.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    // ================= VALIDATIONS =================

    private void validateRequest(AppointmentCreateRequestDto request) {

        if (request.startTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Cannot book in the past");
        }

        if (request.services() == null || request.services().isEmpty()) {
            throw new RuntimeException("At least one service required");
        }
    }

    private void validateWithinWorkSchedule(Long employeeId,
                                            LocalDateTime start,
                                            LocalDateTime end) {

        boolean valid = workScheduleRepo.existsValidSchedule(
                employeeId,
                start.toLocalDate(),
                start.toLocalTime(),
                end.toLocalTime()
        );

        if (!valid) {
            throw new RuntimeException("Outside work schedule");
        }
    }

    private void validateNoOverlap(Long empId,
                                   LocalDateTime start,
                                   LocalDateTime end) {

        if (appointmentRepo.existsOverlap(empId, start, end)) {
            throw new RuntimeException("Time conflict for employee");
        }
    }

    // ================= HELPERS =================

    private Appointment findOrThrow(Long id) {
        return appointmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    private String generateNumber() {
        return "APT-" + UUID.randomUUID().toString().substring(0, 8);
    }

    // ================= MAPPER =================

    private AppointmentResponseDto map(Appointment a) {

        List<AppointmentServiceViewDto> services = a.getServices().stream()
                .sorted(Comparator.comparing(AppointmentService::getSortOrder))
                .map(s -> new AppointmentServiceViewDto(
                        s.getId(),
                        s.getService().getId(),
                        s.getServiceNameSnapshot(),
                        s.getEmployee().getId(),
                        s.getEmployee().getFirstName() + " " + s.getEmployee().getLastName(),
                        s.getStartTime(),
                        s.getEndTime(),
                        s.getDurationMinutesSnapshot(),
                        s.getPriceSnapshot(),
                        s.getLineTotal(),
                        s.getStatus()
                )).toList();

        return new AppointmentResponseDto(
                a.getId(),
                a.getAppointmentNumber(),
                a.getCustomer().getId(),
                a.getCustomer().getFirstName() + " " + a.getCustomer().getLastName(),
                a.getBookedByUser().getId(),
                a.getStartTime(),
                a.getEndTime(),
                a.getStatus(),
                a.getTotalAmount(),
                a.getDiscountAmount(),
                a.getFinalAmount(),
                a.getNotes(),
                a.getCancellationReason(),
                a.getCancelledAt(),
                services
        );
    }
}