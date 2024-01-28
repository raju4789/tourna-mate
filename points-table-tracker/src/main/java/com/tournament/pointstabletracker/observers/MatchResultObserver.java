package com.tournament.pointstabletracker.observers;

import com.tournament.pointstabletracker.dto.AddMatchResultRequest;

public interface MatchResultObserver {
    void update(AddMatchResultRequest addMatchResultRequest) throws Exception;
}

