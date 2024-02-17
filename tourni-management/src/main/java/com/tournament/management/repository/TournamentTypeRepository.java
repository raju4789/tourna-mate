package com.tournament.management.repository;

import com.tournament.management.entity.app.TournamentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentTypeRepository extends JpaRepository<TournamentType, Long> {
}
