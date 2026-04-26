package com.barber.shop.backend.services.impl;

import com.barber.shop.backend.dtos.UserDto;
import com.barber.shop.backend.exceptions.ConflictException;
import com.barber.shop.backend.exceptions.DuplicateResourceException;
import com.barber.shop.backend.exceptions.ResourceNotFoundException;
import com.barber.shop.backend.mappers.UserMapper;
import com.barber.shop.backend.models.User;
import com.barber.shop.backend.repositories.UserRepository;
import com.barber.shop.backend.services.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto create(UserDto dto) {
        validateUniqueFields(dto, null);
        User saved = userRepository.save(userMapper.toEntity(dto));
        return userMapper.toDto(saved);
    }

    @Override
    public UserDto update(Long id, UserDto dto) {
        User entity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        verifyVersion(dto.version(), entity.getVersion(), "User");
        validateUniqueFields(dto, id);
        userMapper.updateEntityFromDto(dto, entity);
        return userMapper.toDto(userRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getById(Long id) {
        return userMapper.toDto(userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(userMapper::toDto).toList();
    }

    @Override
    public void delete(Long id) {
        User entity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        userRepository.delete(entity);
    }

    private void validateUniqueFields(UserDto dto, Long currentId) {
        userRepository.findByUsername(dto.username())
                .filter(user -> !user.getId().equals(currentId))
                .ifPresent(user -> {
                    throw new DuplicateResourceException("Username already exists: " + dto.username());
                });
        userRepository.findByEmail(dto.email())
                .filter(user -> !user.getId().equals(currentId))
                .ifPresent(user -> {
                    throw new DuplicateResourceException("Email already exists: " + dto.email());
                });
        if (dto.phone() != null && !dto.phone().isBlank()) {
            userRepository.findByPhone(dto.phone())
                    .filter(user -> !user.getId().equals(currentId))
                    .ifPresent(user -> {
                        throw new DuplicateResourceException("Phone already exists: " + dto.phone());
                    });
        }
    }

    private void verifyVersion(Long requestedVersion, Long actualVersion, String resourceName) {
        if (requestedVersion != null && !requestedVersion.equals(actualVersion)) {
            throw new ConflictException(resourceName + " was modified by another transaction");
        }
    }
}
