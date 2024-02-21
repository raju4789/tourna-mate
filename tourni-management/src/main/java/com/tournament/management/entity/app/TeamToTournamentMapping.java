package com.tournament.management.entity.app;

import com.tournament.management.entity.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "team_to_tournament_mapping")
public class TeamToTournamentMapping extends BaseEntity {

    @Id
    @Column(name = "teamId")
    private long teamId;

    @Column(name = "tournamentId")
    private long tournamentId;

}
