package com.tournament.pointstabletracker.repository;

import com.tournament.pointstabletracker.entity.app.TournamentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentTypeRepository extends JpaRepository<TournamentType, Long> {
}
