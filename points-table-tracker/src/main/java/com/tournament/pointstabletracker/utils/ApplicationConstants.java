package com.tournament.pointstabletracker.utils;

public class ApplicationConstants {

    public static final int NO_OF_WICKETS_PER_MATCH = 10;
    public static int NO_OF_BALLS_PER_OVER = 6;

    public static int NO_POINTS_FOR_WIN = 2;

    public enum MatchResultStatus {
        COMPLETED, TIED, NO_RESULT
    }

    public enum AppUserRole {
        ADMIN, USER
    }

}
