package com.tournament.management.dto;

import com.tournament.management.utils.ApplicationConstants;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddMatchResultRequest {

    @NotNull(message = "Match number is mandatory")
    @Min(value = 1, message = "Match number should be greater than or equal to 1")
    private Integer matchNumber;

    @NotNull(message = "Tournament id is mandatory")
    private Long tournamentId;

    private Long winnerTeamId;

    private Long loserTeamId;

    @NotNull(message = "Team one id is mandatory")
    private Long teamOneId;

    @NotNull(message = "Team two id is mandatory")
    private Long teamTwoId;

    @Min(value = 0, message = "Team one score should be greater than or equal to 0")
    private Integer teamOneScore;

    @Min(value = 0, message = "Team two score should be greater than or equal to 0")
    private Integer teamTwoScore;

    @Min(value = 0, message = "Team one wickets should be greater than or equal to 0")
    @Max(value = 10, message = "Team one wickets should be less than or equal to 10")
    private Integer teamOneWickets;

    @Min(value = 0, message = "Team two wickets should be greater than or equal to 0")
    @Max(value = 10, message = "Team two wickets should be less than or equal to 10")
    private Integer teamTwoWickets;

    @Min(value = 0, message = "Team one overs played should be greater than or equal to 0")
    private Double teamOneOversPlayed;

    @Min(value = 0, message = "Team two overs played should be greater than or equal to 0")
    private Double teamTwoOversPlayed;

    @NotNull(message = "Match result status is mandatory")
    private ApplicationConstants.MatchResultStatus matchResultStatus;

}
