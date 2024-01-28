package com.github.pathfinder.core.data;

import com.github.pathfinder.core.tools.impl.DoubleTools;
import com.google.common.base.Objects;
import com.google.common.math.DoubleMath;
import lombok.experimental.UtilityClass;

public record Coordinate(double latitude, double longitude) {

    @UtilityClass
    public static final class Constraint {

        public static final String LONGITUDE_MAX_VALUE_STRING = "180";
        public static final String LONGITUDE_MIN_VALUE_STRING = "-180";
        public static final String LATITUDE_MAX_VALUE_STRING  = "90";
        public static final String LATITUDE_MIN_VALUE_STRING  = "-90";

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Coordinate that = (Coordinate) o;
        return DoubleMath.fuzzyEquals(latitude, that.latitude, DoubleTools.EPSILON) &&
                DoubleMath.fuzzyEquals(longitude, that.longitude, DoubleTools.EPSILON);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(latitude, longitude);
    }

}
