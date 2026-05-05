package com.barber.shop.backend.service;

import com.barber.shop.backend.dtos.appointment.*;

import java.util.List;

public interface AppointmentServiceInf {

    AppointmentResponseDto create(AppointmentCreateRequestDto request);

    AppointmentResponseDto update(Long id, AppointmentUpdateRequestDto request);

    AppointmentResponseDto getById(Long id);

    List<AppointmentResponseDto> getAll();

    void cancel(Long id, String reason);
}