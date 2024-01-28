package com.tournament.pointstabletracker.repository;

import com.tournament.pointstabletracker.entity.app.MatchResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchResultRepository extends JpaRepository<MatchResult, Long> {
}
