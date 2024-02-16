package com.tournament.tournimanager.mappers;



import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.tournament.tournimanager.dto.AddMatchResultRequest;
import com.tournament.tournimanager.dto.PointsTableDTO;
import com.tournament.tournimanager.dto.TeamDTO;
import com.tournament.tournimanager.dto.TournamentDTO;
import com.tournament.tournimanager.entity.app.MatchResult;
import com.tournament.tournimanager.entity.app.PointsTable;
import com.tournament.tournimanager.entity.app.Team;
import com.tournament.tournimanager.entity.app.Tournament;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TournamentManagementMappers {

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

    public TournamentDTO mapTournamentToTournamentDTO(Tournament tournament) {
        return modelMapper.map(tournament, TournamentDTO.class);
    }

    public TeamDTO mapTeamToTeamDTO(Team team) {
        return modelMapper.map(team, TeamDTO.class);
    }

}
