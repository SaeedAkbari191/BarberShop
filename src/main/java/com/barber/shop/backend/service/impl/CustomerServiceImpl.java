package com.barber.shop.backend.service.impl;

import com.barber.shop.backend.dtos.customer.CustomerCreateDto;
import com.barber.shop.backend.dtos.customer.CustomerResponseDto;
import com.barber.shop.backend.dtos.customer.CustomerUpdateDto;
import com.barber.shop.backend.models.Customer;
import com.barber.shop.backend.repositories.CustomerRepository;
import com.barber.shop.backend.service.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public CustomerResponseDto createCustomer(CustomerCreateDto request) {
        log.info("Creating new Customer: {}", request.firstName() + " " + request.lastName());

        if (customerRepository.existsByCustomerCode(request.customerCode())) {
            throw new DataIntegrityViolationException(
                    "Customer with code already exists: " + request.customerCode()
            );
        }

        Customer customerToSave = Customer.builder()
                .customerCode(request.customerCode())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .phone(request.phone())
                .dateOfBirth(request.dateOfBirth())
                .gender(request.gender())
                .notes(request.notes())
                .marketingOptIn(request.marketingOptIn())
                .build();

        return mapToCustomerResponseDto(customerRepository.save(customerToSave));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponseDto> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::mapToCustomerResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDto getCustomerById(Long customerId) {

        Customer customer = findCustomerEntityById(customerId);

        return mapToCustomerResponseDto(customer);
    }


    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDto findByCustomerCode(String customerCode) {
        Customer customer = customerRepository.findByCustomerCode(customerCode)
                .orElseThrow(() -> new EntityNotFoundException("Customer with CustomerCode " + customerCode + " not found"));
        return mapToCustomerResponseDto(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponseDto> findByPhone(String phoneNumber) {
        return customerRepository.findByPhone(phoneNumber)
                .stream()
                .map(this::mapToCustomerResponseDto)
                .toList();
    }

    @Override
    public CustomerResponseDto updateCustomer(Long customerId, CustomerUpdateDto request) {
        log.info("Updating Customer with ID: {}", customerId);

        Customer customer = findCustomerEntityById(customerId);

        if (request.firstName() != null) {
            customer.setFirstName(request.firstName());
        }
        if (request.lastName() != null) {
            customer.setLastName(request.lastName());
        }
        if (request.email() != null) {
            customer.setEmail(request.email());
        }
        if (request.phone() != null) {
            customer.setPhone(request.phone());
        }
        if (request.dateOfBirth() != null) {
            customer.setDateOfBirth(request.dateOfBirth());
        }
        if (request.gender() != null) {
            customer.setGender(request.gender());
        }
        if (request.notes() != null) {
            customer.setNotes(request.notes());
        }
        if (request.marketingOptIn() != null) {
            customer.setMarketingOptIn(request.marketingOptIn());
        }
        Customer updatedCustomer = customerRepository.save(customer);


        log.info("Customer updated successfully: {}", updatedCustomer.getId());
        return mapToCustomerResponseDto(updatedCustomer);
    }

    @Override
    public void deleteCustomerById(Long customerId) {
        log.info("Deleting customer with ID: {}", customerId);

        Customer customer = findCustomerEntityById(customerId);

        customer.markDeleted();

        customerRepository.save(customer);

        log.info("Customer soft deleted successfully: {}", customerId);
    }


    private Customer findCustomerEntityById(Long customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Customer not found with ID: " + customerId
                        )
                );
        if (Boolean.TRUE.equals(customer.getIsDeleted())) {
            throw new EntityNotFoundException("User deleted");
        }
        return customer;
    }


    private CustomerResponseDto mapToCustomerResponseDto(Customer customer) {
        return new CustomerResponseDto(
                customer.getId(),
                customer.getCustomerCode(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getDateOfBirth(),
                customer.getGender(),
                customer.getNotes(),
                customer.getMarketingOptIn(),
                customer.getCreatedAt()
        );
    }
}
