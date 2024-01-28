package com.tournament.pointstabletracker.repository;

import com.tournament.pointstabletracker.entity.app.PointsTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PointsTableRepository extends JpaRepository<PointsTable, Long> {

    Optional<PointsTable> findByTeamIdAndTournamentId(long teamOneId, long tournamentId);

    Optional<List<PointsTable>> findByTournamentId(long tournamentId);
}
