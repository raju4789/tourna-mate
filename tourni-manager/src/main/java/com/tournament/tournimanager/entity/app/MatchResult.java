package com.tournament.tournimanager.entity.app;

import com.tournament.tournimanager.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "match_result")
public class MatchResult extends BaseEntity {

    @Id
    @Column(name = "match_id", nullable = false)
    private long matchId;

    @Column(name = "match_number", nullable = false)
    private long matchNumber;

    @Column(name = "tournament_id", nullable = false)
    private long tournamentId;

    @Column(name = "winner_team_id", nullable = false)
    private long winnerTeamId;

    @Column(name = "loser_team_id", nullable = false)
    private long loserTeamId;

    @Column(name = "team_one_id", nullable = false)
    private long teamOneId;

    @Column(name = "team_two_id", nullable = false)
    private long teamTwoId;

    @Column(name = "team_one_score", nullable = false)
    private int teamOneScore;

    @Column(name = "team_two_score", nullable = false)
    private int teamTwoScore;

    @Column(name = "team_one_wickets", nullable = false)
    private int teamOneWickets;

    @Column(name = "team_two_wickets", nullable = false)
    private int teamTwoWickets;

    @Column(name = "team_one_overs_played", nullable = false)
    private double teamOneOversPlayed;

    @Column(name = "team_two_overs_played", nullable = false)
    private double teamTwoOversPlayed;

    @Column(name = "match_result_status", nullable = false)
    private String matchResultStatus;

}

