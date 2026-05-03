package com.barber.shop.backend.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
@SQLDelete(sql = "UPDATE refresh_tokens SET is_deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE id=?")
@SQLRestriction("is_deleted = false")
@Table(
        name = "refresh_tokens",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_refresh_tokens_token_hash",
                        columnNames = "token_hash"
                )
        },
        indexes = {
                @Index(
                        name = "idx_refresh_tokens_user",
                        columnList = "user_id"
                ),
                @Index(
                        name = "idx_refresh_tokens_expiry",
                        columnList = "expiry_date"
                ),
                @Index(
                        name = "idx_refresh_tokens_hash",
                        columnList = "token_hash"
                ),
                @Index(
                        name = "idx_refresh_tokens_revoked",
                        columnList = "is_revoked"
                )
        }
)
public class RefreshToken extends SoftDeletableEntity {

    /**
     * چون BaseEntity خودش id دارد:
     * Long id + GenerationType.IDENTITY
     *
     * بنابراین sessionId جدا به عنوان شناسه سشن ذخیره می‌شود
     * نه Primary Key
     */

    @Column(
            name = "session_id",
            nullable = false,
            unique = true,
            length = 36
    )
    private String sessionId;

    /**
     * هر کاربر می‌تواند چند Refresh Token داشته باشد
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_refresh_tokens_user")
    )
    private User user;

    /**
     * هش توکن فعلی
     */
    @Column(
            name = "token_hash",
            nullable = false,
            unique = true,
            length = 512
    )
    private String tokenHash;

    /**
     * هش قبلی برای Token Rotation
     */
    @Column(
            name = "previous_token_hash",
            length = 512
    )
    private String previousTokenHash;

    /**
     * تاریخ انقضا
     */
    @Column(
            name = "expiry_date",
            nullable = false
    )
    private LocalDateTime expiryDate;

    /**
     * آیا توکن revoke شده؟
     */
    @Column(
            name = "is_revoked",
            nullable = false
    )
    private Boolean isRevoked = false;

    /**
     * آی‌پی کاربر
     */
    @Column(
            name = "ip_address",
            length = 100
    )
    private String ipAddress;

    /**
     * مرورگر / کلاینت
     */
    @Column(
            name = "user_agent",
            length = 500
    )
    private String userAgent;

    /**
     * دستگاه
     */
    @Column(
            name = "device_info",
            length = 255
    )
    private String deviceInfo;

    /**
     * هنگام ایجاد خودکار sessionId
     */
//    @PrePersist
//    public void generateSessionId() {
//        if (this.sessionId == null || this.sessionId.isBlank()) {
//            this.sessionId = java.util.UUID.randomUUID().toString();
//        }
//    }

    /**
     * بررسی اعتبار
     */
    public boolean isExpired() {
        return expiryDate != null && expiryDate.isBefore(LocalDateTime.now());
    }

    public boolean isValid() {
        return !Boolean.TRUE.equals(isRevoked)
                && !Boolean.TRUE.equals(getIsDeleted())
                && !isExpired();
    }

    public void revoke() {
        this.isRevoked = true;
    }
}