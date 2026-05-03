package com.barber.shop.backend.service;

import com.barber.shop.backend.dtos.barberService.BarberServiceCreateDto;
import com.barber.shop.backend.dtos.barberService.BarberServiceResponseDto;
import com.barber.shop.backend.dtos.barberService.BarberServiceUpdateDto;

import java.util.List;

public interface BarberServiceInf {

    BarberServiceResponseDto createService(BarberServiceCreateDto request);

    BarberServiceResponseDto updateService(Long id, BarberServiceUpdateDto request);

    List<BarberServiceResponseDto> findAllService();

    BarberServiceResponseDto findServiceById(Long id);

    BarberServiceResponseDto findByServiceCode(String serviceCode);

    List<BarberServiceResponseDto> findActiveServices();

    List<BarberServiceResponseDto> findByCategory(String category);

    List<BarberServiceResponseDto> searchByName(String name);

    void deleteService(Long id);
}
