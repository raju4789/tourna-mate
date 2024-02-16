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
@Table(name = "team")
public class Team extends BaseEntity {

    @Id
    @Column(name="team_id")
    private long teamId;

    @Column(name="team_name", nullable = false, unique = true)
    private String teamName;

    @Column(name = "team_owner", nullable = false, unique = true)
    private String teamOwner;

}
