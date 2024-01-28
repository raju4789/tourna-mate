package com.tournament.pointstabletracker.entity.app;

import com.tournament.pointstabletracker.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tournament_type")
public class TournamentType extends BaseEntity {

    @Id
    @Column(name = "tournament_type_id")
    private Long tournamentTypeId;

    @Column(name = "tournament_type", nullable = false, unique = true)
    private String tournamentType;
}

