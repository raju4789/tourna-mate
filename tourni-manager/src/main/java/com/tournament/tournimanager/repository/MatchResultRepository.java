package com.tournament.tournimanager.repository;

import com.tournament.tournimanager.entity.app.MatchResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchResultRepository extends JpaRepository<MatchResult, Long> {
}
