package com.tournament.management.repository;

import com.tournament.management.entity.app.TeamStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamStatsRepository extends JpaRepository<TeamStats, Long> {

    Optional<TeamStats> findByTeamIdAndTournamentId(long teamId, long tournamentId);
}
