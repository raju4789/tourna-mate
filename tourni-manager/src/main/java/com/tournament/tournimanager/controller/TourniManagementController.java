package com.tournament.tournimanager.controller;

import com.tournament.tournimanager.dto.*;
import com.tournament.tournimanager.service.TourniManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/v1/tournimanager")
@Tag(name = "Tourni Manager", description = "Tourni Management APIs")
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
    public ResponseEntity<CommonApiResponse<List<PointsTableDTO>>> getPointsTableByTournamentId(@PathVariable(name = "id") Long tournamentId) {
        List<PointsTableDTO> pointsTableDTOList = tourniManagementService.getPointsTableByTournamentId(tournamentId);
        CommonApiResponse<List<PointsTableDTO>> pointsTable = CommonApiResponse.<List<PointsTableDTO>>builder().data(pointsTableDTOList).build();
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
    @PostMapping("/addMatchResult")
    public ResponseEntity<CommonApiResponse<String>> addMatchResult(@Valid @RequestBody AddMatchResultRequest addMatchResultRequest) {

        tourniManagementService.addMatchResult(addMatchResultRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CommonApiResponse.<String>builder().data("Match result saved successfully").build());
    }

    @Operation(
            description = "Get endpoint to retrieve all tournaments",
            summary = "Get all tournaments",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tournaments retrieved successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            })
    @GetMapping("/tournaments")
    public ResponseEntity<CommonApiResponse<List<TournamentDTO>>> getAllTournaments() {
        List<TournamentDTO> tournamentDTOList = tourniManagementService.getAllTournaments();
        CommonApiResponse<List<TournamentDTO>> tournaments = CommonApiResponse.<List<TournamentDTO>>builder().data(tournamentDTOList).build();
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
    @GetMapping("/teams")
    public ResponseEntity<CommonApiResponse<List<TeamDTO>>> getAllTeams() {
        List<TeamDTO> teamDTOList = tourniManagementService.getAllTeams();
        CommonApiResponse<List<TeamDTO>> teams = CommonApiResponse.<List<TeamDTO>>builder().data(teamDTOList).build();
        return ResponseEntity.ok(teams);
    }

}
