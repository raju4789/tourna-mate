package com.tournament.pointstabletracker.entity.app;

import com.tournament.pointstabletracker.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tournament", uniqueConstraints = @UniqueConstraint(columnNames = {"tournament_name", "tournament_year"}))
public class Tournament extends BaseEntity {

    @Id
    @Column(name = "tournament_id")
    private Long tournamentId;

    @Column(name = "tournament_type_id", nullable = false)
    private long tournamentTypeId;

    @Column(name = "tournament_name", nullable = false, unique = true)
    private String tournamentName;

    @Column(name = "tournament_description")
    private String tournamentDescription;

    @Column(name = "tournament_year", nullable = false)
    private int tournamentYear;

    @Column(name = "maximum_overs_per_match", nullable = false)
    private int maximumOversPerMatch;

}
