package com.tournament.tournimanager.observers;

import com.tournament.tournimanager.dto.AddMatchResultRequest;
import com.tournament.tournimanager.entity.app.TeamStats;
import com.tournament.tournimanager.entity.app.Tournament;
import com.tournament.tournimanager.exceptions.RecordNotFoundException;
import com.tournament.tournimanager.repository.TeamStatsRepository;
import com.tournament.tournimanager.repository.TournamentRepository;
import com.tournament.tournimanager.utils.ApplicationConstants;
import com.tournament.tournimanager.utils.NetRunRateCalculator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TeamStatsObserver implements MatchResultObserver {
    private final TeamStatsRepository teamStatsRepository;
    private final TournamentRepository tournamentRepository;

    private static final Logger logger = (Logger) LoggerFactory.getLogger(TeamStatsObserver.class);

    @Override
    public void update(AddMatchResultRequest matchResult) throws RecordNotFoundException {

        logger.info("Updating team stats for match result: {} {}", matchResult.getTeamOneId(), matchResult.getTeamTwoId());

        final long tournamentId = matchResult.getTournamentId();
        final long teamOneId = matchResult.getTeamOneId();
        final long teamTwoId = matchResult.getTeamTwoId();

        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow(() -> new RecordNotFoundException("No tournament not found for given id : " + tournamentId));

        TeamStats teamOneStats = teamStatsRepository.findByTeamIdAndTournamentId(teamOneId, tournamentId).orElseThrow(() -> new RecordNotFoundException("No stats found with teamId :" + teamOneId + "and tournamentId: " + tournamentId));

        TeamStats teamTwoStats = teamStatsRepository.findByTeamIdAndTournamentId(teamTwoId, tournamentId).orElseThrow(() -> new RecordNotFoundException("No stats found with teamId :" + teamTwoId + "and tournamentId: " + tournamentId));

        if (matchResult.getMatchResultStatus() != ApplicationConstants.MatchResultStatus.NO_RESULT) {
            // update team stats
            int teamOneScore = matchResult.getTeamOneScore();
            int teamOneWickets = matchResult.getTeamOneWickets();

            double teamOneOversPlayed = matchResult.getTeamOneOversPlayed();
            teamOneOversPlayed = NetRunRateCalculator.adjustOversPlayed(teamOneOversPlayed, tournament.getMaximumOversPerMatch(), teamOneWickets);

            int teamTwoScore = matchResult.getTeamTwoScore();
            int teamTwoWickets = matchResult.getTeamTwoWickets();

            double teamTwoOversPlayed = matchResult.getTeamTwoOversPlayed();
            teamTwoOversPlayed = NetRunRateCalculator.adjustOversPlayed(teamTwoOversPlayed, tournament.getMaximumOversPerMatch(), teamTwoWickets);

            teamOneStats.setTotalRunsScored(teamOneStats.getTotalRunsScored() + teamOneScore);
            teamOneStats.setTotalTeamOversPlayed(teamOneStats.getTotalTeamOversPlayed() + teamOneOversPlayed);
            teamOneStats.setTotalRunsConceded(teamOneStats.getTotalRunsConceded() + teamTwoScore);
            teamOneStats.setTotalOversBowled(teamOneStats.getTotalOversBowled() + teamTwoOversPlayed);
            teamOneStats.setRecordUpdatedDate(LocalDateTime.now());

            teamTwoStats.setTotalRunsScored(teamTwoStats.getTotalRunsScored() + teamTwoScore);
            teamTwoStats.setTotalTeamOversPlayed(teamTwoStats.getTotalTeamOversPlayed() + teamTwoOversPlayed);
            teamTwoStats.setTotalRunsConceded(teamTwoStats.getTotalRunsConceded() + teamOneScore);
            teamTwoStats.setTotalOversBowled(teamTwoStats.getTotalOversBowled() + teamOneOversPlayed);
            teamTwoStats.setRecordUpdatedDate(LocalDateTime.now());

            teamStatsRepository.saveAll(List.of(teamOneStats, teamTwoStats));

            logger.info("Team stats updated successfully for team one id: {} and team two id: {}", teamOneId, teamTwoId);

        }


    }
}

