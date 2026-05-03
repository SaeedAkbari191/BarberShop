package com.barber.shop.backend.controller;


import com.barber.shop.backend.dtos.customer.CustomerCreateDto;
import com.barber.shop.backend.dtos.customer.CustomerResponseDto;
import com.barber.shop.backend.dtos.customer.CustomerUpdateDto;
import com.barber.shop.backend.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerResponseDto> createCustomer(@RequestBody CustomerCreateDto customerRequest) {
        return ResponseEntity.ok(customerService.createCustomer(customerRequest));
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<CustomerResponseDto> getCustomerByCode(@PathVariable String code) {
        return ResponseEntity.ok(customerService.findByCustomerCode(code));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> updateCustomer(@PathVariable Long id, @RequestBody CustomerUpdateDto request) {
        return ResponseEntity.ok(customerService.updateCustomer(id, request));
    }

    @GetMapping("/phone")
    public ResponseEntity<List<CustomerResponseDto>> getCustomerByPhone(@RequestParam("phoneNumber") String phoneNumber) {
        return ResponseEntity.ok(customerService.findByPhone(phoneNumber));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomerById(id);
        return ResponseEntity.noContent().build();
    }


}
