package com.barber.shop.backend.mappers;

import com.barber.shop.backend.dtos.EmployeeDto;
import com.barber.shop.backend.exceptions.ResourceNotFoundException;
import com.barber.shop.backend.models.Employee;
import com.barber.shop.backend.models.User;
import com.barber.shop.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeMapper {

    private final UserRepository userRepository;

    public EmployeeDto toDto(Employee entity) {
        if (entity == null) {
            return null;
        }
        return new EmployeeDto(
                entity.getId(),
                entity.getVersion(),
                entity.getUser() != null ? entity.getUser().getId() : null,
                entity.getEmployeeCode(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getGender(),
                entity.getPhone(),
                entity.getHireDate(),
                entity.getEmploymentStatus(),
                entity.getCommissionType(),
                entity.getCommissionValue(),
                entity.getNotes(),
                entity.getIsDeleted(),
                entity.getDeletedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public Employee toEntity(EmployeeDto dto) {
        if (dto == null) {
            return null;
        }
        Employee entity = new Employee();
        updateEntityFromDto(dto, entity);
        return entity;
    }

    public void updateEntityFromDto(EmployeeDto dto, Employee entity) {
        User user = null;
        if (dto.userId() != null) {
            user = userRepository.findById(dto.userId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found: " + dto.userId()));
        }
        entity.setUser(user);
        entity.setEmployeeCode(dto.employeeCode());
        entity.setFirstName(dto.firstName());
        entity.setLastName(dto.lastName());
        entity.setGender(dto.gender());
        entity.setPhone(dto.phone());
        entity.setHireDate(dto.hireDate());
        entity.setEmploymentStatus(dto.employmentStatus());
        entity.setCommissionType(dto.commissionType());
        entity.setCommissionValue(dto.commissionValue());
        entity.setNotes(dto.notes());
    }
}
