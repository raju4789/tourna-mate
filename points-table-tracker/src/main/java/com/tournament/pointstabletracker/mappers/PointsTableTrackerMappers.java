package com.tournament.pointstabletracker.mappers;

import com.tournament.pointstabletracker.dto.AddMatchResultRequest;
import com.tournament.pointstabletracker.dto.PointsTableDTO;

import java.time.LocalDateTime;
import java.util.UUID;

import com.tournament.pointstabletracker.entity.app.MatchResult;
import com.tournament.pointstabletracker.entity.app.PointsTable;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointsTableTrackerMappers {

    private final ModelMapper modelMapper;

    public MatchResult mapMatchResultRequestDTOToMatchResult(AddMatchResultRequest addMatchResultRequest) {
        MatchResult matchResult = modelMapper.map(addMatchResultRequest, MatchResult.class);
        matchResult.setRecordCreatedBy(UUID.randomUUID().toString());
        matchResult.setRecordCreatedDate(LocalDateTime.now());
        matchResult.setActive(true);
        return matchResult;
    }

    public PointsTableDTO mapPointsTableToPointsTableDTO(PointsTable pointsTable, String teamName) {
        PointsTableDTO pointsTableDTO = modelMapper.map(pointsTable, PointsTableDTO.class);
        pointsTableDTO.setTeamName(teamName);
        return pointsTableDTO;
    }

}
