package com.tournament.pointstabletracker.entity.app;

import com.tournament.pointstabletracker.entity.common.BaseEntity;
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
@Table(name = "team_stats", uniqueConstraints = @UniqueConstraint(columnNames = {"team_id", "tournament_id"}))
public class TeamStats extends BaseEntity {

    @Id
    @Column(name = "team_stats_id")
    private long teamStatsId;

    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @Column(name = "tournament_id", nullable = false)
    private Long tournamentId;

    @Column(name = "total_runs_scored", nullable = false)
    private int totalRunsScored;

    @Column(name = "total_overs_played", nullable = false)
    private double totalTeamOversPlayed;

    @Column(name = "total_runs_conceded", nullable = false)
    private int totalRunsConceded;

    @Column(name = "total_overs_bowled", nullable = false)
    private double totalOversBowled;

}
