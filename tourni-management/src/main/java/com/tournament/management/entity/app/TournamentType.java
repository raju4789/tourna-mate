package com.tournament.management.entity.app;

import com.tournament.management.entity.common.BaseEntity;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tournament_type_id")
    private Long tournamentTypeId;

    @Column(name = "tournament_type", nullable = false, unique = true)
    private String tournamentType;
}

