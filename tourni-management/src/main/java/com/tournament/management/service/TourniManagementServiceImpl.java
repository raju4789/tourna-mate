package com.tournament.management.service;

import com.tournament.management.dto.*;
import com.tournament.management.entity.app.MatchResult;
import com.tournament.management.entity.app.PointsTable;
import com.tournament.management.entity.app.Team;
import com.tournament.management.entity.app.Tournament;
import com.tournament.management.exceptions.RecordNotFoundException;
import com.tournament.management.mappers.TournamentManagementMappers;
import com.tournament.management.observers.MatchResultSubject;
import com.tournament.management.observers.PointsTableObserver;
import com.tournament.management.observers.TeamStatsObserver;
import com.tournament.management.repository.MatchResultRepository;
import com.tournament.management.repository.PointsTableRepository;
import com.tournament.management.repository.TeamRepository;
import com.tournament.management.repository.TournamentRepository;
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
    public PointsTableByTournamentResponse getPointsTableByTournamentId(Long tournamentId) throws RecordNotFoundException {

        List<PointsTable> pointsTableList = pointsTableRepository.findByTournamentId(tournamentId)
                .orElseThrow(() -> new RecordNotFoundException("No points table found for tournament id: " + tournamentId));

        List<PointsTableDTO> pointsTableDTO = pointsTableList.stream()
                .map(pointsTable -> {
                    String teamName = teamRepository.findTeamNameByTeamId(pointsTable.getTeamId())
                            .orElseThrow(() -> new RecordNotFoundException("No team found for team id: " + pointsTable.getTeamId()));
                    return tournamentManagementMappers.mapPointsTableToPointsTableDTO(pointsTable, teamName);
                })
                .toList();

        return PointsTableByTournamentResponse.builder()
                .tournamentId(tournamentId)
                .pointsTable(pointsTableDTO)
                .build();
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
