package com.tournament.tournimanager.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DisplayName("Tests for NetRunRateCalculator")
public class NetRunRateCalculatorTest {

    @Test
    @DisplayName("Adjust Overs Played - All Out")
    public void testAdjustOversPlayed_AllOut() {
        double oversPlayed = NetRunRateCalculator.adjustOversPlayed(10.5, 20, ApplicationConstants.NO_OF_WICKETS_PER_MATCH);
        assertEquals(20, oversPlayed, 0.01);
    }

    @Test
    @DisplayName("Adjust Overs Played - Not All Out - Match completed in middle of an over")
    public void testAdjustOversPlayed_NotAllOut_CaseOne() {
        double oversPlayed = NetRunRateCalculator.adjustOversPlayed(12.3, 20, 8);
        assertEquals(12.5, oversPlayed, 0.01);
    }

    @Test
    @DisplayName("Adjust Overs Played - Not All Out - Match completed at end of an over")
    public void testAdjustOversPlayed_NotAllOut_CaseTwo() {
        double oversPlayed = NetRunRateCalculator.adjustOversPlayed(12, 20, 8);
        assertEquals(12.0, oversPlayed, 0.01);
    }

    @Test
    @DisplayName("Calculate Net Run Rate - Normal Case")
    public void testCalculateNetRunRate() throws Exception {
        double runsScored = 150;
        double oversPlayed = 25.5;
        double runsConceded = 120;
        double oversBowled = 30.2;

        double netRunRate = NetRunRateCalculator.calculateNetRunRate(runsScored, oversPlayed, runsConceded, oversBowled);

        // Recalculate the expected result
        double expectedNetRunRate = (runsScored / oversPlayed) - (runsConceded / oversBowled);

        assertEquals(expectedNetRunRate, netRunRate, 0.001);
    }


    @Test
    @DisplayName("Calculate Net Run Rate - Zero Overs Bowled")
    public void testCalculateNetRunRate_ZeroOversBowled() throws Exception {
        double runsScored = 150;
        double oversPlayed = 25.5;
        double runsConceded = 120;
        double oversBowled = 0;

        Exception exception = assertThrows(Exception.class, () ->
                NetRunRateCalculator.calculateNetRunRate(runsScored, oversPlayed, runsConceded, oversBowled));

        assertEquals("Overs bowled or overs played cannot be zero", exception.getMessage());
    }

    @Test
    @DisplayName("Calculate Net Run Rate - Zero Overs Played")
    public void testCalculateNetRunRate_ZeroOversPlayed() throws Exception {
        double runsScored = 0;
        double oversPlayed = 0;
        double runsConceded = 0;
        double oversBowled = 10;

        Exception exception = assertThrows(Exception.class, () ->
                NetRunRateCalculator.calculateNetRunRate(runsScored, oversPlayed, runsConceded, oversBowled));

        assertEquals("Overs bowled or overs played cannot be zero", exception.getMessage());
    }
}
