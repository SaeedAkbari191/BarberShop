package com.barber.shop.backend.security;

import com.barber.shop.backend.enums.UserStatus;
import com.barber.shop.backend.models.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Builder
@Getter
public class AuthUser implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        // ROLE_ADMIN / ROLE_EMPLOYEE / ROLE_RECEPTIONIST
        return List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().getCode().name())
        );
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // اگر کاربر حذف نشده یا منقضی نشده
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // اگر وضعیت LOCKED نباشد
    @Override
    public boolean isAccountNonLocked() {
        return user.getStatus() != UserStatus.LOCKED;
    }

    // اعتبار رمز عبور
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // فقط ACTIVE مجاز باشد
    @Override
    public boolean isEnabled() {
        return user.getStatus() == UserStatus.ACTIVE;
    }

    // برای دسترسی مستقیم به اطلاعات اصلی کاربر
    public Long getId() {
        return user.getId();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getPhone() {
        return user.getPhone();
    }
}