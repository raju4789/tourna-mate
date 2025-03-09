package com.tournament.management.repository;

import com.tournament.management.entity.app.MatchResult;
import io.micrometer.observation.annotation.Observed;
import org.springframework.data.jpa.repository.JpaRepository;

@Observed
public interface MatchResultRepository extends JpaRepository<MatchResult, Long> {
}
