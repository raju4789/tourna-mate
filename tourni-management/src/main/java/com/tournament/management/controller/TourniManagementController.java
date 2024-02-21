package com.tournament.management.controller;

import com.tournament.management.dto.*;
import com.tournament.management.service.TourniManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/manage")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Tourni Management", description = "Tourni Management APIs")
public class TourniManagementController {

    private final TourniManagementService tourniManagementService;

    @Operation(
            description = "Get endpoint to retrieve points table by tournament id",
            summary = "Get points table by tournament id",

            responses = {
                    @ApiResponse(responseCode = "200", description = "Points table retrieved successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            })
    @GetMapping("/pointstable/tournament/{id}")
    public ResponseEntity<CommonApiResponse<PointsTableByTournamentResponse>> getPointsTableByTournamentId(@PathVariable(name = "id") Long tournamentId) {
        PointsTableByTournamentResponse pointsTableByTournamentResponse = tourniManagementService.getPointsTableByTournamentId(tournamentId);
        CommonApiResponse<PointsTableByTournamentResponse> pointsTable = new CommonApiResponse<>(pointsTableByTournamentResponse);
        return ResponseEntity.ok(pointsTable);
    }

    @Operation(
            description = "Post endpoint to add match result",
            summary = "Save match result, update team stats and points table",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Match result saved successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized / Invalid token")
            })
    @PostMapping("addMatchResult")
    public ResponseEntity<CommonApiResponse<String>> addMatchResult(@Valid @RequestBody AddMatchResultRequest addMatchResultRequest) {

        tourniManagementService.addMatchResult(addMatchResultRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CommonApiResponse<>("Match result saved successfully"));
    }

    @Operation(
            description = "Get endpoint to retrieve all tournaments",
            summary = "Get all tournaments",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tournaments retrieved successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            })
    @GetMapping("tournaments")
    public ResponseEntity<CommonApiResponse<List<TournamentDTO>>> getAllTournaments() {
        List<TournamentDTO> tournamentDTOList = tourniManagementService.getAllTournaments();
        CommonApiResponse<List<TournamentDTO>> tournaments = new CommonApiResponse<>(tournamentDTOList);
        return ResponseEntity.ok(tournaments);
    }

    @Operation(
            description = "Get endpoint to retrieve all teams",
            summary = "Get all teams",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Teams retrieved successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            })
    @GetMapping("/teams?tournamentId={id}")
    public ResponseEntity<CommonApiResponse<List<TeamDTO>>> getAllTeamsByTournamentId(@RequestParam(name = "id") int tournamentId){
        List<TeamDTO> teamDTOList = tourniManagementService.getAllTeamsByTournamentId(tournamentId);
        CommonApiResponse<List<TeamDTO>> teams = new CommonApiResponse<>(teamDTOList);
        return ResponseEntity.ok(teams);
    }
}
