package com.tournament.management.repository;

import com.tournament.management.entity.app.TeamToTournamentMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamToTournamentMappingRepository extends JpaRepository<TeamToTournamentMapping, Long> {

    @Query("SELECT t.teamId FROM TeamToTournamentMapping t WHERE t.tournamentId = :tournamentId AND t.isActive = true")
    Optional<List<Long>> getTeamIdsByTournamentId(@Param("tournamentId") Long tournamentId);
}
