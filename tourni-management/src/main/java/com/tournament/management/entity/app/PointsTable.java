package com.tournament.management.entity.app;

import com.tournament.management.entity.common.BaseEntity;
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
@Table(name = "points_table")
public class PointsTable extends BaseEntity {

    @Id
    @Column(name = "points_table_id", nullable = false)
    private long pointsTableId;

    @Column(name = "tournament_id", nullable = false)
    private long tournamentId;

    @Column(name = "team_id", nullable = false)
    private long teamId;

    @Column(name = "played", nullable = false)
    private int played;

    @Column(name = "won", nullable = false)
    private int won;

    @Column(name = "lost", nullable = false)
    private int lost;

    @Column(name = "tied", nullable = false)
    private int tied;

    @Column(name = "no_result", nullable = false)
    private int noResult;

    @Column(name = "points", nullable = false)
    private int points;

    @Column(name = "net_match_rate", nullable = false)
    private double netMatchRate;

}
