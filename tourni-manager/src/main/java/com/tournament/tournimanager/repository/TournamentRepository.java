package com.tournament.tournimanager.repository;


import com.tournament.tournimanager.entity.app.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {
}
