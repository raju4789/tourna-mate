package com.tournament.management.entity.common;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Base entity with auditing support
 * - JPA Auditing for automatic timestamp and user tracking
 * - Optimistic locking with @Version for concurrent update protection
 * - Soft delete pattern with isActive flag
 */
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(name = "record_created_date", nullable = false, updatable = false)
    private LocalDateTime recordCreatedDate;

    @LastModifiedDate
    @Column(name = "record_updated_date")
    private LocalDateTime recordUpdatedDate;

    @CreatedBy
    @Column(name = "record_created_by", nullable = false, updatable = false)
    private String recordCreatedBy;

    @LastModifiedBy
    @Column(name = "record_updated_by")
    private String recordUpdatedBy;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    /**
     * Version for optimistic locking
     * Prevents lost updates when multiple users modify the same entity
     */
    @Version
    @Column(name = "version")
    private Long version;
}


