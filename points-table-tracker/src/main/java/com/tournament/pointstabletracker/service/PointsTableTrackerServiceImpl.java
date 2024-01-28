package com.tournament.pointstabletracker.service;

import com.tournament.pointstabletracker.dto.AddMatchResultRequest;
import com.tournament.pointstabletracker.dto.PointsTableDTO;
import com.tournament.pointstabletracker.entity.app.MatchResult;
import com.tournament.pointstabletracker.entity.app.PointsTable;
import com.tournament.pointstabletracker.exceptions.RecordNotFoundException;
import com.tournament.pointstabletracker.mappers.PointsTableTrackerMappers;
import com.tournament.pointstabletracker.observers.MatchResultSubject;
import com.tournament.pointstabletracker.observers.PointsTableObserver;
import com.tournament.pointstabletracker.observers.TeamStatsObserver;
import com.tournament.pointstabletracker.repository.MatchResultRepository;
import com.tournament.pointstabletracker.repository.PointsTableRepository;
import com.tournament.pointstabletracker.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PointsTableTrackerServiceImpl implements PointsTableTrackerService {

    private final PointsTableTrackerMappers pointsTableTrackerMappers;
    private final MatchResultRepository matchResultRepository;
    private final MatchResultSubject matchResultSubject;
    private final PointsTableRepository pointsTableRepository;
    private final TeamRepository teamRepository;

    private static final Logger logger = (Logger) LoggerFactory.getLogger(PointsTableTrackerServiceImpl.class);

    @Autowired
    public PointsTableTrackerServiceImpl(PointsTableTrackerMappers pointsTableTrackerMappers,
                                         MatchResultRepository matchResultRepository,
                                         MatchResultSubject matchResultSubject,
                                         TeamStatsObserver teamStatsObserver,
                                         PointsTableObserver pointsTableObserver,
                                         PointsTableRepository pointsTableRepository,
                                         TeamRepository teamRepository) {

        this.pointsTableTrackerMappers = pointsTableTrackerMappers;
        this.matchResultRepository = matchResultRepository;
        this.matchResultSubject = matchResultSubject;
        this.pointsTableRepository = pointsTableRepository;
        this.teamRepository = teamRepository;

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
                    return pointsTableTrackerMappers.mapPointsTableToPointsTableDTO(pointsTable, teamName);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void addMatchResult(AddMatchResultRequest addMatchResultRequest) {

        MatchResult matchResult = pointsTableTrackerMappers.mapMatchResultRequestDTOToMatchResult(addMatchResultRequest);

        // update team stats and points table
        matchResultSubject.notifyObserversSequentially(addMatchResultRequest);

        // save match result to DB
        matchResultRepository.save(matchResult);

    }
}
