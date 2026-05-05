package com.barber.shop.backend.service;

public interface AppointmentExecutionService {

    void checkIn(Long appointmentId);

    void startService(Long appointmentServiceId);

    void completeService(Long appointmentServiceId);

    void cancelService(Long appointmentServiceId);
}
