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
import com.tournament.management.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    private final TeamToTournamentMappingRepository teamToTournamentMappingRepository;


    @Autowired
    public TourniManagementServiceImpl(TournamentManagementMappers tournamentManagementMappers,
                                       MatchResultRepository matchResultRepository,
                                       MatchResultSubject matchResultSubject,
                                       TeamStatsObserver teamStatsObserver,
                                       PointsTableObserver pointsTableObserver,
                                       PointsTableRepository pointsTableRepository,
                                       TeamRepository teamRepository,
                                       TournamentRepository tournamentRepository,
                                       TeamToTournamentMappingRepository teamToTournamentMappingRepository) {

        this.tournamentManagementMappers = tournamentManagementMappers;
        this.matchResultRepository = matchResultRepository;
        this.matchResultSubject = matchResultSubject;
        this.pointsTableRepository = pointsTableRepository;
        this.teamRepository = teamRepository;
        this.tournamentRepository = tournamentRepository;
        this.teamToTournamentMappingRepository = teamToTournamentMappingRepository;

        matchResultSubject.addObserver(teamStatsObserver);
        matchResultSubject.addObserver(pointsTableObserver);

    }


    @Override
    @Cacheable(value="pointsTableByTournamentId", key="#tournamentId")
    public PointsTableByTournamentResponse getPointsTableByTournamentId(Long tournamentId) throws RecordNotFoundException {

        List<PointsTable> pointsTableList = pointsTableRepository.findByTournamentId(tournamentId)
                .orElseThrow(() -> {
                    log.error("No points table found for tournament id: {}", tournamentId);
                    return new RecordNotFoundException("No points table found for tournament id: " + tournamentId);
                });

        List<PointsTableDTO> pointsTableDTO = pointsTableList.stream()
                .map(pointsTable -> {
                    String teamName = teamRepository.findTeamNameByTeamId(pointsTable.getTeamId())
                            .orElseThrow(() -> {
                                log.error("No team found for team id: {}", pointsTable.getTeamId());
                                return new RecordNotFoundException("No team found for team id: " + pointsTable.getTeamId());
                            });
                    return tournamentManagementMappers.mapPointsTableToPointsTableDTO(pointsTable, teamName);
                })
                .toList();

        return PointsTableByTournamentResponse.builder()
                .tournamentId(tournamentId)
                .pointsTable(pointsTableDTO)
                .build();
    }

    @Override
    @CacheEvict(value="pointsTableByTournamentId", key = "#addMatchResultRequest.tournamentId")
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
    @Cacheable(value="teamsByTournamentId", key="#tournamentId")
    public List<TeamDTO> getAllTeamsByTournamentId(Long tournamentId) {

        List<Long> teamIds = teamToTournamentMappingRepository.getTeamIdsByTournamentId(tournamentId)
                .orElseThrow(() -> {
                    log.error("No teams found for tournament id: {}", tournamentId);
                    return new RecordNotFoundException("No teams found for tournament id: " + tournamentId);
                });

        List<Team> teamList = teamRepository.findTeamsByTeamIds(teamIds)
                .orElseThrow(() -> {
                    log.error("No teams found for team ids: {}", teamIds);
                    return new RecordNotFoundException("No teams found for team ids: " + teamIds);
                });

        return teamList.stream()
                .map(tournamentManagementMappers::mapTeamToTeamDTO)
                .collect(Collectors.toList());
    }
}
