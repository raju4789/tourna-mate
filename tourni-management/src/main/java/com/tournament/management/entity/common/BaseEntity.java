package com.tournament.management.entity.common;


import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {

    @Column(name = "record_created_date", nullable = false)
    private LocalDateTime recordCreatedDate;

    @Column(name = "record_updated_date")
    private LocalDateTime recordUpdatedDate;

    @Column(name = "record_created_by", nullable = false)
    private String recordCreatedBy;

    @Column(name = "record_updated_by")
    private String recordUpdatedBy;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;
}


