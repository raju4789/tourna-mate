package com.tournament.tournimanager.observers;


import com.tournament.tournimanager.dto.AddMatchResultRequest;

public interface MatchResultObserver {
    void update(AddMatchResultRequest addMatchResultRequest) throws Exception;
}

