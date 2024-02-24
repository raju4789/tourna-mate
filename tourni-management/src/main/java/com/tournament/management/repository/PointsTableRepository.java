package com.tournament.management.repository;

import com.tournament.management.entity.app.PointsTable;
import io.micrometer.observation.annotation.Observed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Observed
public interface PointsTableRepository extends JpaRepository<PointsTable, Long> {

    Optional<PointsTable> findByTeamIdAndTournamentId(long teamOneId, long tournamentId);

    Optional<List<PointsTable>> findByTournamentId(long tournamentId);
}
