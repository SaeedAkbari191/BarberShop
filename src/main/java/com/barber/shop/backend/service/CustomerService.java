package com.barber.shop.backend.service;

import com.barber.shop.backend.dtos.customer.CustomerCreateDto;
import com.barber.shop.backend.dtos.customer.CustomerResponseDto;
import com.barber.shop.backend.dtos.customer.CustomerUpdateDto;

import java.util.List;

public interface CustomerService {
    CustomerResponseDto createCustomer(CustomerCreateDto customer);

    List<CustomerResponseDto> getAllCustomers();

    CustomerResponseDto getCustomerById(Long customerId);

    CustomerResponseDto updateCustomer(Long customerId, CustomerUpdateDto customer);

    void deleteCustomerById(Long customerId);

    List<CustomerResponseDto> findByPhone(String phoneNumber);

    CustomerResponseDto findByCustomerCode(String email);
}
