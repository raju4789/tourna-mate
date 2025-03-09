package com.tournament.management.observers;


import com.tournament.management.dto.AddMatchResultRequest;

public interface MatchResultObserver {
    void update(AddMatchResultRequest addMatchResultRequest) throws Exception;
}

