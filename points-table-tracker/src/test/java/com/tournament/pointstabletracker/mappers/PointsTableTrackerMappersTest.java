package com.tournament.pointstabletracker.mappers;

import com.tournament.pointstabletracker.dto.AddMatchResultRequest;
import com.tournament.pointstabletracker.dto.PointsTableDTO;
import com.tournament.pointstabletracker.entity.app.MatchResult;
import com.tournament.pointstabletracker.entity.app.PointsTable;
import com.tournament.pointstabletracker.utils.ApplicationConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for PointsTableTrackerMappers")
class PointsTableTrackerMappersTest {

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PointsTableTrackerMappers mapper;

    @Test
    @DisplayName("Map MatchResultRequestDTO to MatchResult")
    void mapMatchResultRequestDTOToMatchResult() {
        AddMatchResultRequest addMatchResultRequest = new AddMatchResultRequest();
        addMatchResultRequest.setTournamentId(101L);
        addMatchResultRequest.setTeamOneId(1L);
        addMatchResultRequest.setTeamTwoId(2L);
        addMatchResultRequest.setTeamOneScore(100);
        addMatchResultRequest.setTeamTwoScore(200);
        addMatchResultRequest.setTeamOneWickets(5);
        addMatchResultRequest.setTeamTwoWickets(10);
        addMatchResultRequest.setTeamOneOversPlayed(20.0);
        addMatchResultRequest.setTeamTwoOversPlayed(17.5);
        addMatchResultRequest.setWinnerTeamId(2L);
        addMatchResultRequest.setLoserTeamId(1L);
        addMatchResultRequest.setMatchResultStatus(ApplicationConstants.MatchResultStatus.COMPLETED);
        addMatchResultRequest.setMatchNumber(1);

        MatchResult expectedMatchResult = new MatchResult();
        expectedMatchResult.setTournamentId(101L);
        expectedMatchResult.setTeamOneId(1L);
        expectedMatchResult.setTeamTwoId(2L);
        expectedMatchResult.setTeamOneScore(100);
        expectedMatchResult.setTeamTwoScore(200);
        expectedMatchResult.setTeamOneWickets(5);
        expectedMatchResult.setTeamTwoWickets(10);
        expectedMatchResult.setTeamOneOversPlayed(20.0);
        expectedMatchResult.setTeamTwoOversPlayed(17.5);
        expectedMatchResult.setWinnerTeamId(2L);
        expectedMatchResult.setLoserTeamId(1L);
        expectedMatchResult.setMatchResultStatus("COMPLETED");
        expectedMatchResult.setMatchNumber(1);

        when(modelMapper.map(any(), Mockito.eq(MatchResult.class))).thenReturn(expectedMatchResult);

        // Act
        MatchResult result = mapper.mapMatchResultRequestDTOToMatchResult(addMatchResultRequest);

        // Assert
        assertEquals(expectedMatchResult.getRecordCreatedBy(), result.getRecordCreatedBy());
        assertEquals(expectedMatchResult.getRecordUpdatedBy(), result.getRecordUpdatedBy());
        assertEquals(expectedMatchResult.getRecordCreatedDate(), result.getRecordCreatedDate());
        assertEquals(expectedMatchResult.getRecordUpdatedDate(), result.getRecordUpdatedDate());
        assertEquals(expectedMatchResult.isActive(), result.isActive());
        assertEquals(expectedMatchResult.getTournamentId(), result.getTournamentId());
        assertEquals(expectedMatchResult.getTeamOneId(), result.getTeamOneId());
        assertEquals(expectedMatchResult.getTeamTwoId(), result.getTeamTwoId());
        assertEquals(expectedMatchResult.getTeamOneScore(), result.getTeamOneScore());
        assertEquals(expectedMatchResult.getTeamTwoScore(), result.getTeamTwoScore());
        assertEquals(expectedMatchResult.getTeamOneWickets(), result.getTeamOneWickets());
        assertEquals(expectedMatchResult.getTeamTwoWickets(), result.getTeamTwoWickets());
        assertEquals(expectedMatchResult.getTeamOneOversPlayed(), result.getTeamOneOversPlayed());
        assertEquals(expectedMatchResult.getTeamTwoOversPlayed(), result.getTeamTwoOversPlayed());
        assertEquals(expectedMatchResult.getWinnerTeamId(), result.getWinnerTeamId());
        assertEquals(expectedMatchResult.getLoserTeamId(), result.getLoserTeamId());
        assertEquals(expectedMatchResult.getMatchResultStatus(), result.getMatchResultStatus());
        assertEquals(expectedMatchResult.getMatchNumber(), result.getMatchNumber());
    }

    @Test
    @DisplayName("Map PointsTable to PointsTableDTO")
    void mapPointsTableToPointsTableDTO() {
        // Arrange
        PointsTable pointsTable = new PointsTable();
        pointsTable.setTournamentId(101L);
        pointsTable.setTeamId(1L);
        pointsTable.setPlayed(1);
        pointsTable.setWon(1);
        pointsTable.setLost(0);
        pointsTable.setNoResult(0);
        pointsTable.setPoints(2);
        pointsTable.setNetMatchRate(0.5);

        String teamName = "Sample Team";

        PointsTableDTO expectedPointsTableDTO = new PointsTableDTO();
        expectedPointsTableDTO.setPlayed(1);
        expectedPointsTableDTO.setWon(1);
        expectedPointsTableDTO.setLost(0);
        expectedPointsTableDTO.setNoResult(0);
        expectedPointsTableDTO.setPoints(2);
        expectedPointsTableDTO.setNetMatchRate(0.5);

        // Set properties of expectedPointsTableDTO based on your expectations

        when(modelMapper.map(any(), Mockito.eq(PointsTableDTO.class))).thenReturn(expectedPointsTableDTO);

        // Act
        PointsTableDTO result = mapper.mapPointsTableToPointsTableDTO(pointsTable, teamName);

        // Assert
        assertEquals(expectedPointsTableDTO.getTeamName(), result.getTeamName());
        assertEquals(expectedPointsTableDTO.getPlayed(), result.getPlayed());
        assertEquals(expectedPointsTableDTO.getWon(), result.getWon());
        assertEquals(expectedPointsTableDTO.getLost(), result.getLost());
        assertEquals(expectedPointsTableDTO.getNoResult(), result.getNoResult());
        assertEquals(expectedPointsTableDTO.getPoints(), result.getPoints());
        assertEquals(expectedPointsTableDTO.getNetMatchRate(), result.getNetMatchRate());

    }
}
