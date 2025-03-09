package com.tournament.management.entity.app;

import com.tournament.management.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "team_to_tournament_mapping", uniqueConstraints = @UniqueConstraint(columnNames = {"team_id", "tournament_id"}))
public class TeamToTournamentMapping extends BaseEntity {

    @Id
    @Column(name = "mapping_id")
    private long mappingId;

    @Column(name = "team_id")
    private long teamId;

    @Column(name = "tournament_id")
    private long tournamentId;

}
