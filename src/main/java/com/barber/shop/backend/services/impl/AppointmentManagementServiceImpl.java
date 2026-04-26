package com.barber.shop.backend.services.impl;

import com.barber.shop.backend.dtos.AppointmentBookingRequestDto;
import com.barber.shop.backend.dtos.AppointmentDto;
import com.barber.shop.backend.dtos.AppointmentLineRequestDto;
import com.barber.shop.backend.dtos.AppointmentServiceDto;
import com.barber.shop.backend.dtos.PaymentDto;
import com.barber.shop.backend.enums.AppointmentServiceStatus;
import com.barber.shop.backend.enums.AppointmentStatus;
import com.barber.shop.backend.enums.EmploymentStatus;
import com.barber.shop.backend.enums.PaymentMethod;
import com.barber.shop.backend.enums.PaymentStatus;
import com.barber.shop.backend.enums.WorkScheduleStatus;
import com.barber.shop.backend.enums.WorkScheduleType;
import com.barber.shop.backend.exceptions.BusinessValidationException;
import com.barber.shop.backend.exceptions.ResourceNotFoundException;
import com.barber.shop.backend.mappers.AppointmentMapper;
import com.barber.shop.backend.mappers.AppointmentServiceMapper;
import com.barber.shop.backend.mappers.PaymentMapper;
import com.barber.shop.backend.models.Appointment;
import com.barber.shop.backend.models.Employee;
import com.barber.shop.backend.models.EmployeeService;
import com.barber.shop.backend.models.Payment;
import com.barber.shop.backend.models.BarberService;
import com.barber.shop.backend.models.WorkSchedule;
import com.barber.shop.backend.repositories.AppointmentRepository;
import com.barber.shop.backend.repositories.AppointmentServiceRepository;
import com.barber.shop.backend.repositories.CustomerRepository;
import com.barber.shop.backend.repositories.EmployeeRepository;
import com.barber.shop.backend.repositories.EmployeeServiceRepository;
import com.barber.shop.backend.repositories.PaymentRepository;
import com.barber.shop.backend.repositories.ServiceRepository;
import com.barber.shop.backend.repositories.UserRepository;
import com.barber.shop.backend.repositories.WorkScheduleRepository;
import com.barber.shop.backend.services.AppointmentManagementService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Transactional
public class AppointmentManagementServiceImpl implements AppointmentManagementService {

    private static final List<AppointmentStatus> ACTIVE_APPOINTMENT_STATUSES = List.of(
            AppointmentStatus.BOOKED,
            AppointmentStatus.CONFIRMED,
            AppointmentStatus.CHECKED_IN,
            AppointmentStatus.IN_PROGRESS
    );

    private final AppointmentRepository appointmentRepository;
    private final AppointmentServiceRepository appointmentServiceRepository;
    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final ServiceRepository serviceRepository;
    private final EmployeeServiceRepository employeeServiceRepository;
    private final WorkScheduleRepository workScheduleRepository;
    private final AppointmentMapper appointmentMapper;
    private final AppointmentServiceMapper appointmentServiceMapper;
    private final PaymentMapper paymentMapper;

    @Override
    public AppointmentDto createBooking(AppointmentBookingRequestDto request) {
        Appointment appointment = new Appointment();
        appointment.setAppointmentNumber(generateAppointmentNumber());
        Appointment saved = applyBookingRequest(request, appointment, null);
        return appointmentMapper.toDto(saved);
    }

    @Override
    public AppointmentDto updateBooking(Long id, AppointmentBookingRequestDto request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found: " + id));
        Appointment saved = applyBookingRequest(request, appointment, id);
        return appointmentMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentDto getById(Long id) {
        return appointmentMapper.toDto(appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found: " + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDto> getAll() {
        return appointmentRepository.findAll().stream().map(appointmentMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentServiceDto> getServices(Long appointmentId) {
        if (!appointmentRepository.existsById(appointmentId)) {
            throw new ResourceNotFoundException("Appointment not found: " + appointmentId);
        }
        return appointmentServiceRepository.findByAppointmentId(appointmentId).stream()
                .map(appointmentServiceMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentDto> getPayments(Long appointmentId) {
        if (!appointmentRepository.existsById(appointmentId)) {
            throw new ResourceNotFoundException("Appointment not found: " + appointmentId);
        }
        return paymentRepository.findByAppointmentId(appointmentId).stream()
                .map(paymentMapper::toDto)
                .toList();
    }

    @Override
    public void delete(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found: " + id));
        appointmentRepository.delete(appointment);
    }

    private Appointment applyBookingRequest(AppointmentBookingRequestDto request, Appointment appointment, Long existingAppointmentId) {
        validateBookingRequest(request);
        appointment.setCustomer(customerRepository.findById(request.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + request.customerId())));
        appointment.setBookedByUser(userRepository.findById(request.bookedByUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.bookedByUserId())));
        appointment.setAssignedEmployee(request.assignedEmployeeId() == null ? null :
                employeeRepository.findById(request.assignedEmployeeId())
                        .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + request.assignedEmployeeId())));
        if (appointment.getAssignedEmployee() != null && appointment.getAssignedEmployee().getEmploymentStatus() != EmploymentStatus.ACTIVE) {
            throw new BusinessValidationException("Assigned employee is not active");
        }
        appointment.setAppointmentDate(request.appointmentDate());
        appointment.setStartTime(request.startTime());
        appointment.setNotes(request.notes());
        appointment.setStatus(AppointmentStatus.BOOKED);

        BookingComputation computation = buildBookingComputation(request, existingAppointmentId);
        validateAssignedEmployeeConsistency(appointment.getAssignedEmployee(), computation.involvedEmployeeIds());
        appointment.setEndTime(computation.endTime());
        appointment.setTotalAmount(computation.totalAmount());
        appointment.setDiscountAmount(request.discountAmount() != null ? request.discountAmount() : BigDecimal.ZERO);
        appointment.setFinalAmount(computation.totalAmount().subtract(appointment.getDiscountAmount()));
        if (appointment.getFinalAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessValidationException("Final amount cannot be negative");
        }

        Appointment persistedAppointment = appointmentRepository.save(appointment);
        appointmentServiceRepository.deleteByAppointmentId(persistedAppointment.getId());
        paymentRepository.deleteByAppointmentId(persistedAppointment.getId());

        computation.lines().forEach(line -> {
            line.setAppointment(persistedAppointment);
            appointmentServiceRepository.save(line);
        });

        BigDecimal totalPaid = BigDecimal.ZERO;
        if (request.payments() != null) {
            for (var paymentRequest : request.payments()) {
                Payment payment = new Payment();
                payment.setAppointment(persistedAppointment);
                payment.setReceivedByUser(userRepository.findById(paymentRequest.receivedByUserId())
                        .orElseThrow(() -> new ResourceNotFoundException("User not found: " + paymentRequest.receivedByUserId())));
                payment.setPaymentMethod(paymentRequest.paymentMethod() != null ? paymentRequest.paymentMethod() : PaymentMethod.CASH);
                payment.setPaymentStatus(PaymentStatus.PAID);
                payment.setAmount(paymentRequest.amount());
                payment.setTransactionReference(paymentRequest.transactionReference());
                payment.setPaidAt(paymentRequest.paidAt() != null ? paymentRequest.paidAt() : LocalDateTime.now());
                payment.setNotes(paymentRequest.notes());
                totalPaid = totalPaid.add(payment.getAmount());
                paymentRepository.save(payment);
            }
        }

        if (totalPaid.compareTo(persistedAppointment.getFinalAmount()) > 0) {
            throw new BusinessValidationException("Payment total exceeds appointment final amount");
        }
        return persistedAppointment;
    }

    private BookingComputation buildBookingComputation(AppointmentBookingRequestDto request, Long existingAppointmentId) {
        List<AppointmentLineRequestDto> orderedRequests = request.services().stream()
                .sorted(Comparator.comparing(line -> line.sortOrder() != null ? line.sortOrder() : Integer.MAX_VALUE))
                .toList();

        List<com.barber.shop.backend.models.AppointmentService> lines = new ArrayList<>();
        LocalTime currentStart = request.startTime();
        BigDecimal totalAmount = BigDecimal.ZERO;
        Set<Long> involvedEmployeeIds = new HashSet<>();
        int totalDurationMinutes = 0;

        for (AppointmentLineRequestDto lineRequest : orderedRequests) {
            BarberService service = serviceRepository.findById(lineRequest.serviceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Service not found: " + lineRequest.serviceId()));
            if (!Boolean.TRUE.equals(service.getIsActive())) {
                throw new BusinessValidationException("Inactive service cannot be booked: " + service.getName());
            }

            Employee employee = employeeRepository.findById(lineRequest.employeeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + lineRequest.employeeId()));
            if (employee.getEmploymentStatus() != EmploymentStatus.ACTIVE) {
                throw new BusinessValidationException("Employee is not active: " + employee.getEmployeeCode());
            }

            EmployeeService employeeSkill = employeeServiceRepository.findByEmployeeIdAndServiceId(employee.getId(), service.getId())
                    .orElseThrow(() -> new BusinessValidationException("Employee is not assigned to service"));
            if (!Boolean.TRUE.equals(employeeSkill.getIsActive())) {
                throw new BusinessValidationException("Employee service assignment is inactive");
            }

            int duration = employeeSkill.getCustomDurationMinutes() != null
                    ? employeeSkill.getCustomDurationMinutes()
                    : service.getDurationMinutes();
            BigDecimal price = employeeSkill.getCustomPrice() != null
                    ? employeeSkill.getCustomPrice()
                    : service.getBasePrice();

            LocalTime lineEnd = currentStart.plusMinutes(duration);
            if (!lineEnd.isAfter(currentStart)) {
                throw new BusinessValidationException("Computed service duration is invalid");
            }

            validateSchedule(employee, request, currentStart, lineEnd);

            com.barber.shop.backend.models.AppointmentService appointmentLine = new com.barber.shop.backend.models.AppointmentService();
            appointmentLine.setService(service);
            appointmentLine.setEmployee(employee);
            appointmentLine.setServiceNameSnapshot(service.getName());
            appointmentLine.setPriceSnapshot(price);
            appointmentLine.setDurationMinutesSnapshot(duration);
            appointmentLine.setLineTotal(price);
            appointmentLine.setSortOrder(lineRequest.sortOrder() != null ? lineRequest.sortOrder() : lines.size() + 1);
            appointmentLine.setStatus(AppointmentServiceStatus.BOOKED);
            appointmentLine.setNotes(lineRequest.notes());
            lines.add(appointmentLine);

            totalAmount = totalAmount.add(price);
            involvedEmployeeIds.add(employee.getId());
            currentStart = lineEnd;
            totalDurationMinutes += duration;
        }

        LocalTime appointmentEnd = currentStart;
        for (Long employeeId : involvedEmployeeIds) {
            validateOverlap(employeeId, request.appointmentDate(), request.startTime(), appointmentEnd, existingAppointmentId);
        }

        return new BookingComputation(lines, involvedEmployeeIds, totalAmount, totalDurationMinutes, appointmentEnd);
    }

    private void validateBookingRequest(AppointmentBookingRequestDto request) {
        if (request.services() == null || request.services().isEmpty()) {
            throw new BusinessValidationException("Appointment must include at least one service");
        }
        if (request.discountAmount() != null && request.discountAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessValidationException("Discount amount cannot be negative");
        }
    }

    private void validateAssignedEmployeeConsistency(Employee assignedEmployee, Set<Long> involvedEmployeeIds) {
        if (assignedEmployee == null) {
            return;
        }
        if (!involvedEmployeeIds.contains(assignedEmployee.getId())) {
            throw new BusinessValidationException("Assigned employee must be included in booked appointment services");
        }
    }

    private void validateSchedule(Employee employee, AppointmentBookingRequestDto request, LocalTime lineStart, LocalTime lineEnd) {
        List<WorkSchedule> schedules = workScheduleRepository.findByEmployeeIdAndScheduleDate(employee.getId(), request.appointmentDate());
        boolean fits = schedules.stream()
                .filter(schedule -> schedule.getStatus() == WorkScheduleStatus.ACTIVE)
                .filter(schedule -> schedule.getScheduleType() == WorkScheduleType.WORKING)
                .anyMatch(schedule -> fitsSchedule(schedule, lineStart, lineEnd));
        if (!fits) {
            throw new BusinessValidationException("Employee has no valid working schedule for requested time");
        }
    }

    private boolean fitsSchedule(WorkSchedule schedule, LocalTime lineStart, LocalTime lineEnd) {
        boolean insideWorkWindow = !lineStart.isBefore(schedule.getStartTime()) && !lineEnd.isAfter(schedule.getEndTime());
        if (!insideWorkWindow) {
            return false;
        }
        if (schedule.getBreakStartTime() == null || schedule.getBreakEndTime() == null) {
            return true;
        }
        return lineEnd.compareTo(schedule.getBreakStartTime()) <= 0 || lineStart.compareTo(schedule.getBreakEndTime()) >= 0;
    }

    private void validateOverlap(Long employeeId, java.time.LocalDate appointmentDate,
                                 LocalTime lineStart, LocalTime lineEnd, Long existingAppointmentId) {
        long overlaps = appointmentServiceRepository.countEmployeeBookingConflicts(
                employeeId,
                appointmentDate,
                ACTIVE_APPOINTMENT_STATUSES,
                lineStart,
                lineEnd,
                existingAppointmentId
        );
        if (overlaps > 0) {
            throw new BusinessValidationException("Employee already has an overlapping appointment");
        }
    }

    private String generateAppointmentNumber() {
        return "APT-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))
                + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    private record BookingComputation(
            List<com.barber.shop.backend.models.AppointmentService> lines,
            Set<Long> involvedEmployeeIds,
            BigDecimal totalAmount,
            int totalDurationMinutes,
            LocalTime endTime
    ) {
    }
}
