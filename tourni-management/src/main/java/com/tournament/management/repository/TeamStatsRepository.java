package com.tournament.management.repository;

import com.tournament.management.entity.app.TeamStats;
import io.micrometer.observation.annotation.Observed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Observed
public interface TeamStatsRepository extends JpaRepository<TeamStats, Long> {

    Optional<TeamStats> findByTeamIdAndTournamentId(long teamId, long tournamentId);
}
