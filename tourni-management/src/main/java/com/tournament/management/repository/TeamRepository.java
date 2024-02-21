package com.tournament.management.repository;

import com.tournament.management.entity.app.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query("SELECT t.teamName FROM Team t WHERE t.teamId = :teamId AND t.isActive = true")
    Optional<String> findTeamNameByTeamId(@Param("teamId") Long teamId);

    @Query("SELECT t FROM Team t WHERE t.teamId IN :teamIds AND t.isActive = true")
    Optional<List<Team>> findTeamsByTeamIds(@Param("teamIds") List<Long> teamIds);

}
