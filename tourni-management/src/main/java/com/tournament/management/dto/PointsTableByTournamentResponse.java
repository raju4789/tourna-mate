package com.tournament.management.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PointsTableByTournamentResponse {

    private long tournamentId;

    private List<PointsTableDTO> pointsTable;
}
