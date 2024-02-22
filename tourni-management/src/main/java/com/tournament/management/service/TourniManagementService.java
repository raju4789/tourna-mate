package com.tournament.management.service;

import com.tournament.management.dto.*;
import com.tournament.management.exceptions.RecordNotFoundException;

import java.util.List;

public interface TourniManagementService {

    PointsTableByTournamentResponse getPointsTableByTournamentId(Long tournamentId) throws RecordNotFoundException;

    void addMatchResult(AddMatchResultRequest matchResultRequest);

    List<TournamentDTO> getAllTournaments();

    List<TeamDTO> getAllTeamsByTournamentId(Long tournamentId);
}
