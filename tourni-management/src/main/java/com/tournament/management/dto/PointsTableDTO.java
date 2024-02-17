package com.tournament.management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PointsTableDTO {

    private String teamName;

    private int played;

    private int won;

    private int lost;

    private int tied;

    private int noResult;

    private int points;

    private double netMatchRate;

}
