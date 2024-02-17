package com.tournament.management.utils;

import com.tournament.management.exceptions.InvalidRequestException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NetRunRateCalculator {


    /*
     * Formula to calculate overs played:
     * If a team is all out in a match, then
     * the number of overs played = max number of overs available in the match
     * */
    public static double adjustOversPlayed(double oversPlayed, int maxOvers, int noOfWicketsFell) {
        if (noOfWicketsFell == ApplicationConstants.NO_OF_WICKETS_PER_MATCH) {
            return maxOvers;
        }

        int overPlayedIntegerPart = (int) oversPlayed;
        double overPlayedDecimalPart = (oversPlayed - overPlayedIntegerPart) * 10;

        return (overPlayedIntegerPart * ApplicationConstants.NO_OF_BALLS_PER_OVER + overPlayedDecimalPart)
                / ApplicationConstants.NO_OF_BALLS_PER_OVER;
    }

    /*
     * Formula to calculate net run rate:
     * Net Run Rate = (Total Runs Scored / Total Overs Faced) - (Total Runs Conceded / Total Overs Bowled)
     */
    public static double calculateNetRunRate(double runsScored, double oversPlayed, double runsConceded, double oversBowled) throws InvalidRequestException {
        if (oversBowled <= 0 || oversPlayed <= 0) {

            String errrorMessage = "Overs bowled:" + oversBowled + "or overs played:" + oversPlayed + " cannot be zero";
            log.error("Unable to calculate net run rate: {}", errrorMessage);
            throw new InvalidRequestException(errrorMessage);
        }
        return (runsScored / oversPlayed) - (runsConceded / oversBowled);
    }
}
