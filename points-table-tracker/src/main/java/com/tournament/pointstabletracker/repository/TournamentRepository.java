package com.tournament.pointstabletracker.repository;

import com.tournament.pointstabletracker.entity.app.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {
}
