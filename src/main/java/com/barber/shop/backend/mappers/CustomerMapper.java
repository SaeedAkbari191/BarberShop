package com.barber.shop.backend.mappers;

import com.barber.shop.backend.dtos.CustomerDto;
import com.barber.shop.backend.models.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public CustomerDto toDto(Customer entity) {
        if (entity == null) {
            return null;
        }
        return new CustomerDto(
                entity.getId(),
                entity.getVersion(),
                entity.getCustomerCode(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getPhone(),
                entity.getEmail(),
                entity.getDateOfBirth(),
                entity.getGender(),
                entity.getNotes(),
                entity.getMarketingOptIn(),
                entity.getLastVisitAt(),
                entity.getIsDeleted(),
                entity.getDeletedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public Customer toEntity(CustomerDto dto) {
        if (dto == null) {
            return null;
        }
        Customer entity = new Customer();
        updateEntityFromDto(dto, entity);
        return entity;
    }

    public void updateEntityFromDto(CustomerDto dto, Customer entity) {
        entity.setCustomerCode(dto.customerCode());
        entity.setFirstName(dto.firstName());
        entity.setLastName(dto.lastName());
        entity.setPhone(dto.phone());
        entity.setEmail(dto.email());
        entity.setDateOfBirth(dto.dateOfBirth());
        entity.setGender(dto.gender());
        entity.setNotes(dto.notes());
        entity.setMarketingOptIn(dto.marketingOptIn() != null ? dto.marketingOptIn() : Boolean.FALSE);
        entity.setLastVisitAt(dto.lastVisitAt());
    }
}
