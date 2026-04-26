package com.barber.shop.backend.services;

import com.barber.shop.backend.dtos.AppointmentBookingRequestDto;
import com.barber.shop.backend.dtos.AppointmentDto;
import com.barber.shop.backend.dtos.AppointmentServiceDto;
import com.barber.shop.backend.dtos.PaymentDto;
import java.util.List;

public interface AppointmentManagementService {

    AppointmentDto createBooking(AppointmentBookingRequestDto request);

    AppointmentDto updateBooking(Long id, AppointmentBookingRequestDto request);

    AppointmentDto getById(Long id);

    List<AppointmentDto> getAll();

    List<AppointmentServiceDto> getServices(Long appointmentId);

    List<PaymentDto> getPayments(Long appointmentId);

    void delete(Long id);
}
