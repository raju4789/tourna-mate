package com.tournament.tournimanager.observers;

import com.tournament.tournimanager.dto.AddMatchResultRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class MatchResultSubject {
    private final List<MatchResultObserver> observers;
    private final ExecutorService executorService;

    private static final Logger logger = (Logger) LoggerFactory.getLogger(MatchResultSubject.class);

    public MatchResultSubject() {
        this.observers = new ArrayList<>();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void addObserver(MatchResultObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(MatchResultObserver observer) {
        observers.remove(observer);
    }

    public void notifyObserversSequentially(AddMatchResultRequest addMatchResultRequest) {
        for (MatchResultObserver observer : observers) {
            try {
                Future<?> future = executorService.submit(() -> {
                    try {
                        logger.info("Notifying observer: {}", observer.getClass().getName());
                        observer.update(addMatchResultRequest);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                future.get();
            } catch (InterruptedException | ExecutionException | RuntimeException e) {
                throw new RuntimeException("Error while notifying observers s", e);
            } catch (Exception e) {
                throw new RuntimeException("Error while notifying observers", e);
            }
        }
    }


}

