package com.tournament.tournimanager.service;

import com.tournament.tournimanager.dto.AddMatchResultRequest;
import com.tournament.tournimanager.dto.PointsTableDTO;
import com.tournament.tournimanager.dto.TeamDTO;
import com.tournament.tournimanager.dto.TournamentDTO;
import com.tournament.tournimanager.exceptions.RecordNotFoundException;

import java.util.List;

public interface TourniManagementService {

    List<PointsTableDTO> getPointsTableByTournamentId(Long tournamentId) throws RecordNotFoundException;

    void addMatchResult(AddMatchResultRequest matchResultRequest);

    List<TournamentDTO> getAllTournaments();

    List<TeamDTO> getAllTeams();
}
