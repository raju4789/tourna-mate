package com.tournament.management.repository;


import com.tournament.management.entity.app.Tournament;
import io.micrometer.observation.annotation.Observed;
import org.springframework.data.jpa.repository.JpaRepository;

@Observed
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
}
