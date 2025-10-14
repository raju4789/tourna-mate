package com.tournament.entity;


import com.tournament.utils.ApplicationConstants.AppUserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Application User entity with automatic audit tracking
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "app_user")
@EntityListeners(AuditingEntityListener.class)
public class AppUser implements UserDetails {

    @Id
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    /**
     * User roles - supports multiple roles per user
     * Uses separate table for many-to-many relationship
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "username")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    @Builder.Default
    private Set<AppUserRole> roles = new HashSet<>();
    
    /**
     * Token version for role change handling
     * Incremented when roles change - invalidates old tokens
     */
    @Column(name = "token_version", nullable = false)
    @Builder.Default
    private Integer tokenVersion = 0;

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
     */
    @Version
    @Column(name = "version")
    private Long version;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toSet());
    }
    
    /**
     * Helper method to add a role
     */
    public void addRole(AppUserRole role) {
        this.roles.add(role);
        incrementTokenVersion();
    }
    
    /**
     * Helper method to remove a role
     */
    public void removeRole(AppUserRole role) {
        this.roles.remove(role);
        incrementTokenVersion();
    }
    
    /**
     * Helper method to check if user has a specific role
     */
    public boolean hasRole(AppUserRole role) {
        return roles.contains(role);
    }
    
    /**
     * Increment token version - invalidates existing tokens
     * Call this when roles change or password changes
     */
    public void incrementTokenVersion() {
        this.tokenVersion++;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
