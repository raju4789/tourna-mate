package com.tournament.pointstabletracker.service;

import com.tournament.pointstabletracker.dto.AddMatchResultRequest;
import com.tournament.pointstabletracker.dto.PointsTableDTO;
import com.tournament.pointstabletracker.exceptions.RecordNotFoundException;

import java.util.List;

public interface PointsTableTrackerService {

    List<PointsTableDTO> getPointsTableByTournamentId(Long tournamentId) throws RecordNotFoundException;

    void addMatchResult(AddMatchResultRequest matchResultRequest);
}
