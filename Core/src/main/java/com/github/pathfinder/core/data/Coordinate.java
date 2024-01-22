package com.github.pathfinder.core.data;

import lombok.experimental.UtilityClass;

public record Coordinate(double latitude, double longitude) {

    @UtilityClass
    public static final class Constraint {

        public static final String LONGITUDE_MAX_VALUE_STRING = "180";
        public static final String LONGITUDE_MIN_VALUE_STRING = "-180";
        public static final String LATITUDE_MAX_VALUE_STRING  = "90";
        public static final String LATITUDE_MIN_VALUE_STRING  = "-90";

    }

}
