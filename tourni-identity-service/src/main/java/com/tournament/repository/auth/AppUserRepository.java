package com.tournament.repository.auth;

import com.tournament.entity.AppUser;
import io.micrometer.observation.annotation.Observed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Observed
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, String> {
}
