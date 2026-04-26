package com.barber.shop.backend.services.impl;

import com.barber.shop.backend.dtos.CustomerDto;
import com.barber.shop.backend.exceptions.ConflictException;
import com.barber.shop.backend.exceptions.DuplicateResourceException;
import com.barber.shop.backend.exceptions.ResourceNotFoundException;
import com.barber.shop.backend.mappers.CustomerMapper;
import com.barber.shop.backend.models.Customer;
import com.barber.shop.backend.repositories.CustomerRepository;
import com.barber.shop.backend.services.CustomerService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerDto create(CustomerDto dto) {
        validateUniqueFields(dto, null);
        Customer saved = customerRepository.save(customerMapper.toEntity(dto));
        return customerMapper.toDto(saved);
    }

    @Override
    public CustomerDto update(Long id, CustomerDto dto) {
        Customer entity = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
        verifyVersion(dto.version(), entity.getVersion(), "Customer");
        validateUniqueFields(dto, id);
        customerMapper.updateEntityFromDto(dto, entity);
        return customerMapper.toDto(customerRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDto getById(Long id) {
        return customerMapper.toDto(customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDto> getAll() {
        return customerRepository.findAll().stream().map(customerMapper::toDto).toList();
    }

    @Override
    public void delete(Long id) {
        Customer entity = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
        customerRepository.delete(entity);
    }

    private void validateUniqueFields(CustomerDto dto, Long currentId) {
        customerRepository.findByCustomerCode(dto.customerCode())
                .filter(customer -> !customer.getId().equals(currentId))
                .ifPresent(customer -> {
                    throw new DuplicateResourceException("Customer code already exists: " + dto.customerCode());
                });
        customerRepository.findByPhone(dto.phone())
                .filter(customer -> !customer.getId().equals(currentId))
                .ifPresent(customer -> {
                    throw new DuplicateResourceException("Customer phone already exists: " + dto.phone());
                });
    }

    private void verifyVersion(Long requestedVersion, Long actualVersion, String resourceName) {
        if (requestedVersion != null && !requestedVersion.equals(actualVersion)) {
            throw new ConflictException(resourceName + " was modified by another transaction");
        }
    }
}
