package com.tournament.management.repository;

import com.tournament.management.entity.app.TournamentType;
import io.micrometer.observation.annotation.Observed;
import org.springframework.data.jpa.repository.JpaRepository;

@Observed
public interface TournamentTypeRepository extends JpaRepository<TournamentType, Long> {
}
