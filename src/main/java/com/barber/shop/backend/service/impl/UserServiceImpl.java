package com.barber.shop.backend.service.impl;

import com.barber.shop.backend.dtos.user.UserCreateDto;
import com.barber.shop.backend.dtos.user.UserResponseDto;
import com.barber.shop.backend.dtos.user.UserUpdateDto;
import com.barber.shop.backend.enums.UserStatus;
import com.barber.shop.backend.models.Role;
import com.barber.shop.backend.models.User;
import com.barber.shop.backend.repositories.RoleRepository;
import com.barber.shop.backend.repositories.UserRepository;
import com.barber.shop.backend.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto registerUser(UserCreateDto dto) {
        log.info("Registering new user: {}", dto.username());

        validateUniqueUser(dto.username(), dto.email());
        Role role = findRoleById(dto.roleId());

        User user = User.builder()
                .role(role)
                .username(dto.username())
                .email(dto.email())
                .passwordHash(passwordEncoder.encode(dto.password()))
                .phone(dto.phone())
                .status(UserStatus.ACTIVE)
                .build();

        User savedUser = userRepository.save(user);


        return mapToResponseDto(savedUser);

    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .filter(user -> !Boolean.TRUE.equals(user.getIsDeleted()))
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    public UserResponseDto getUserById(Long id) {

        User user = findActiveUserById(id);
        return mapToResponseDto(user);
    }

    @Override
    public UserResponseDto updateUser(Long id, UserUpdateDto dto) {
        User user = findActiveUserById(id);

        if (dto.roleId() != null) {
            Role role = roleRepository.findById(dto.roleId())
                    .orElseThrow(() -> new EntityNotFoundException("Role not found"));
            user.setRole(role);
        }
        if (dto.email() != null) {
            user.setEmail(dto.email());
        }

        if (dto.phone() != null) {
            user.setPhone(dto.phone());
        }

        if (dto.status() != null) {
            user.setStatus(dto.status());
        }
        return mapToResponseDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {

        User user = findActiveUserById(id);

//        user.setIsDeleted(true);
//        user.setDeletedAt(LocalDateTime.now());
        user.markDeleted();

        userRepository.save(user);
    }


    private User findActiveUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found")
                );

        if (Boolean.TRUE.equals(user.getIsDeleted())) {
            throw new EntityNotFoundException("User deleted");
        }

        return user;
    }

    /**
     * یافتن Role
     */
    private Role findRoleById(Long roleId) {

        return roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Role not found with ID: " + roleId
                        )
                );
    }

    /**
     * اعتبارسنجی یکتا بودن username/email
     */
    private void validateUniqueUser(String username, String email) {

        if (userRepository.existsByUsername(username)) {
            throw new DataIntegrityViolationException(
                    "Username already exists: " + username
            );
        }

        if (userRepository.existsByEmail(email)) {
            throw new DataIntegrityViolationException(
                    "Email already exists: " + email
            );
        }
    }

    /**
     * تبدیل Entity به DTO
     */
    private UserResponseDto mapToResponseDto(User user) {

        return new UserResponseDto(
                user.getId(),
                user.getRole().getId(),
                user.getRole().getCode().name(),
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
