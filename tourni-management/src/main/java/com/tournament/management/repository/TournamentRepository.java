package com.tournament.management.repository;


import com.tournament.management.entity.app.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {
}
