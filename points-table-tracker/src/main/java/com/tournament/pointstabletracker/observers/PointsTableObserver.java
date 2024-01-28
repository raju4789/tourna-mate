package com.tournament.pointstabletracker.observers;

import com.tournament.pointstabletracker.dto.AddMatchResultRequest;
import com.tournament.pointstabletracker.entity.app.PointsTable;
import com.tournament.pointstabletracker.entity.app.TeamStats;
import com.tournament.pointstabletracker.exceptions.InvalidRequestException;
import com.tournament.pointstabletracker.exceptions.RecordNotFoundException;
import com.tournament.pointstabletracker.repository.PointsTableRepository;
import com.tournament.pointstabletracker.repository.TeamStatsRepository;
import com.tournament.pointstabletracker.utils.ApplicationConstants;
import com.tournament.pointstabletracker.utils.NetRunRateCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static com.tournament.pointstabletracker.utils.ApplicationConstants.NO_POINTS_FOR_WIN;

@Component
@RequiredArgsConstructor
@Slf4j
public class PointsTableObserver implements MatchResultObserver {
    private final PointsTableRepository pointsTableRepository;

    private final TeamStatsRepository teamStatsRepository;

    @Override
    public void update(AddMatchResultRequest matchResultRequest) throws RecordNotFoundException, InvalidRequestException {

        log.info("Updating points table for match result: {} {}", matchResultRequest.getTeamOneId(), matchResultRequest.getTeamTwoId());

        final long tournamentId = matchResultRequest.getTournamentId();
        final long teamOneId = matchResultRequest.getTeamOneId();
        final long teamTwoId = matchResultRequest.getTeamTwoId();

        TeamStats teamOneStats = teamStatsRepository.findByTeamIdAndTournamentId(teamOneId, tournamentId).orElseThrow(() -> new RecordNotFoundException("No stats found with teamId:" + teamOneId + "and tournamentId: " + tournamentId));

        TeamStats teamTwoStats = teamStatsRepository.findByTeamIdAndTournamentId(teamTwoId, tournamentId).orElseThrow(() -> new RecordNotFoundException("No stats found with teamId:" + teamTwoId + "and tournamentId: " + tournamentId));

        // update points table
        PointsTable teamOneMatchStats = pointsTableRepository.findByTeamIdAndTournamentId(teamOneId, tournamentId).orElseThrow(() -> new RecordNotFoundException("No points record found with teamId:" + teamOneId + "and tournamentId: " + tournamentId));
        PointsTable teamTwoMatchStats = pointsTableRepository.findByTeamIdAndTournamentId(teamTwoId, tournamentId).orElseThrow(() -> new RecordNotFoundException("No points record found with teamId:" + teamTwoId + "and tournamentId: " + tournamentId));

        teamOneMatchStats.setPlayed(teamOneMatchStats.getPlayed() + 1);
        teamTwoMatchStats.setPlayed(teamTwoMatchStats.getPlayed() + 1);

        teamTwoMatchStats.setRecordUpdatedDate(LocalDateTime.now());
        teamOneMatchStats.setRecordUpdatedDate(LocalDateTime.now());

        final double netRunRateTeamOne = NetRunRateCalculator.calculateNetRunRate(teamOneStats.getTotalRunsScored(), teamOneStats.getTotalTeamOversPlayed(), teamOneStats.getTotalRunsConceded(), teamOneStats.getTotalOversBowled());
        final double netRunRateTeamTwo = NetRunRateCalculator.calculateNetRunRate(teamTwoStats.getTotalRunsScored(), teamTwoStats.getTotalTeamOversPlayed(), teamTwoStats.getTotalRunsConceded(), teamTwoStats.getTotalOversBowled());

        if (matchResultRequest.getMatchResultStatus() == ApplicationConstants.MatchResultStatus.COMPLETED) {

            long winnerTeamId = matchResultRequest.getWinnerTeamId();

            if (winnerTeamId == teamOneId) {

                teamOneMatchStats.setWon(teamOneMatchStats.getWon() + 1);
                teamOneMatchStats.setPoints(teamOneMatchStats.getPoints() + NO_POINTS_FOR_WIN);

                teamTwoMatchStats.setLost(teamTwoMatchStats.getLost() + 1);

            } else if (winnerTeamId == teamTwoId) {

                teamTwoMatchStats.setWon(teamTwoMatchStats.getWon() + 1);
                teamTwoMatchStats.setPoints(teamTwoMatchStats.getPoints() + NO_POINTS_FOR_WIN);

                teamOneMatchStats.setLost(teamOneMatchStats.getLost() + 1);

            }

            teamOneMatchStats.setNetMatchRate(netRunRateTeamOne);
            teamTwoMatchStats.setNetMatchRate(netRunRateTeamTwo);

        } else if (matchResultRequest.getMatchResultStatus() == ApplicationConstants.MatchResultStatus.TIED) {
            teamOneMatchStats.setTied(teamOneMatchStats.getTied() + 1);
            teamTwoMatchStats.setTied(teamTwoMatchStats.getTied() + 1);

            teamOneMatchStats.setNetMatchRate(netRunRateTeamOne);
            teamTwoMatchStats.setNetMatchRate(netRunRateTeamTwo);
        } else if (matchResultRequest.getMatchResultStatus() == ApplicationConstants.MatchResultStatus.NO_RESULT) {
            teamOneMatchStats.setNoResult(teamOneMatchStats.getNoResult() + 1);
            teamTwoMatchStats.setNoResult(teamTwoMatchStats.getNoResult() + 1);
        }

        pointsTableRepository.saveAll(List.of(teamOneMatchStats, teamTwoMatchStats));

        log.info("Points table updated successfully for teamId: {} and teamId: {}", teamOneId, teamTwoId);

    }
}

/*
You are seasoned data analyst in a cricket matches analyzing company. You are given a task to come up with a prompt
to suggest a team on what should they do to be top 4 of points table of a tournament. you are given below data

1. you will be given current points table which contains team name, played, won, lost, tied, no result, points, net run rate
2. given Points table will be sorted by points and net run rate
3. 2 points for win, 1 point for tie, 0 for loss
3. If two teams have same points then net run rate will decide who will move up
4. You will be given total number of matches each team will play in that tournament
5. Your task is to suggest what each team should do in order to be in top 4 in points table
6. you need to suggest how many matches each team has to win to be top 4
8. You need to be able to decide which teams have already qualified for top 4 and which teams have been eliminated from top 4
9. You should also decide which teams which are not qualified still have a chance to qualify for top 4
10. Lets says if 2 teams have same number of points what each team should do to be in top 4  in terms of net run rate
11. When i say net run rate, if a team need to increase their story points by 0.23 what they have to score in first innings and what they have to restrict the opposition to in second innings
or if they bat second in how many overs they have to score runs scored by first team to increase their net run rate by 0.23
 */

