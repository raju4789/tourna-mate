package com.tournament.tournimanager.repository;

import com.tournament.tournimanager.entity.app.TournamentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentTypeRepository extends JpaRepository<TournamentType, Long> {
}
