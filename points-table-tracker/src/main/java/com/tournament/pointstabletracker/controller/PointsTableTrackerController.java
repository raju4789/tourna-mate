package com.tournament.pointstabletracker.controller;

import com.tournament.pointstabletracker.dto.AddMatchResultRequest;
import com.tournament.pointstabletracker.dto.CommonApiResponse;
import com.tournament.pointstabletracker.dto.PointsTableDTO;
import com.tournament.pointstabletracker.service.PointsTableTrackerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/v1/pointsTable/")
@Tag(name = "Points table tracker")
public class PointsTableTrackerController {

    private final PointsTableTrackerService pointsTableTrackerService;

    @Operation(
            description = "Get endpoint to retrieve points table by tournament id",
            summary = "Get points table by tournament id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Points table retrieved successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "500", description = "Internal server error"),
            })
    @GetMapping("/tournament/{id}")
    public ResponseEntity<CommonApiResponse<List<PointsTableDTO>>> getPointsTableByTournamentId(@PathVariable(name = "id") Long tournamentId) {
        List<PointsTableDTO> pointsTableDTOList = pointsTableTrackerService.getPointsTableByTournamentId(tournamentId);
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
    @PostMapping("addMatchResult")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonApiResponse<String>> addMatchResult(@Valid @RequestBody AddMatchResultRequest addMatchResultRequest) {

        pointsTableTrackerService.addMatchResult(addMatchResultRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CommonApiResponse.<String>builder().data("Match result saved successfully").build());
    }

}
