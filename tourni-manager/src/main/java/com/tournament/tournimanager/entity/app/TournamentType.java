package com.tournament.tournimanager.entity.app;

import com.tournament.tournimanager.entity.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

