package com.tournament.tournimanager.service;

import com.tournament.tournimanager.dto.AddMatchResultRequest;
import com.tournament.tournimanager.dto.PointsTableDTO;
import com.tournament.tournimanager.dto.TeamDTO;
import com.tournament.tournimanager.dto.TournamentDTO;
import com.tournament.tournimanager.entity.app.MatchResult;
import com.tournament.tournimanager.entity.app.PointsTable;
import com.tournament.tournimanager.entity.app.Team;
import com.tournament.tournimanager.entity.app.Tournament;
import com.tournament.tournimanager.exceptions.RecordNotFoundException;
import com.tournament.tournimanager.mappers.TournamentManagementMappers;
import com.tournament.tournimanager.observers.MatchResultSubject;
import com.tournament.tournimanager.observers.PointsTableObserver;
import com.tournament.tournimanager.observers.TeamStatsObserver;
import com.tournament.tournimanager.repository.MatchResultRepository;
import com.tournament.tournimanager.repository.PointsTableRepository;
import com.tournament.tournimanager.repository.TeamRepository;
import com.tournament.tournimanager.repository.TournamentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TourniManagementServiceImpl implements TourniManagementService {

    private final TournamentManagementMappers tournamentManagementMappers;
    private final MatchResultRepository matchResultRepository;
    private final MatchResultSubject matchResultSubject;
    private final PointsTableRepository pointsTableRepository;
    private final TeamRepository teamRepository;

    private final TournamentRepository tournamentRepository;


    @Autowired
    public TourniManagementServiceImpl(TournamentManagementMappers tournamentManagementMappers,
                                       MatchResultRepository matchResultRepository,
                                       MatchResultSubject matchResultSubject,
                                       TeamStatsObserver teamStatsObserver,
                                       PointsTableObserver pointsTableObserver,
                                       PointsTableRepository pointsTableRepository,
                                       TeamRepository teamRepository,
                                       TournamentRepository tournamentRepository) {

        this.tournamentManagementMappers = tournamentManagementMappers;
        this.matchResultRepository = matchResultRepository;
        this.matchResultSubject = matchResultSubject;
        this.pointsTableRepository = pointsTableRepository;
        this.teamRepository = teamRepository;
        this.tournamentRepository = tournamentRepository;

        matchResultSubject.addObserver(teamStatsObserver);
        matchResultSubject.addObserver(pointsTableObserver);

    }

    @Override
    public List<PointsTableDTO> getPointsTableByTournamentId(Long tournamentId) throws RecordNotFoundException {

        List<PointsTable> pointsTableList = pointsTableRepository.findByTournamentId(tournamentId)
                .orElseThrow(() -> new RecordNotFoundException("No points table found for tournament id: " + tournamentId));

        return pointsTableList.stream()
                .map(pointsTable -> {
                    String teamName = teamRepository.findTeamNameByTeamId(pointsTable.getTeamId())
                            .orElseThrow(() -> new RecordNotFoundException("No team found for team id: " + pointsTable.getTeamId()));
                    return tournamentManagementMappers.mapPointsTableToPointsTableDTO(pointsTable, teamName);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void addMatchResult(AddMatchResultRequest addMatchResultRequest) {

        MatchResult matchResult = tournamentManagementMappers.mapMatchResultRequestDTOToMatchResult(addMatchResultRequest);

        // update team stats and points table
        matchResultSubject.notifyObserversSequentially(addMatchResultRequest);

        // save match result to DB
        matchResultRepository.save(matchResult);

    }

    @Override
    public List<TournamentDTO> getAllTournaments() {
        List<Tournament> tournamentList = tournamentRepository.findAll();
        return tournamentList.stream()
                .map(tournamentManagementMappers::mapTournamentToTournamentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TeamDTO> getAllTeams() {
        List<Team> teamList = teamRepository.findAll();
        return teamList.stream()
                .map(tournamentManagementMappers::mapTeamToTeamDTO)
                .collect(Collectors.toList());
    }
}
