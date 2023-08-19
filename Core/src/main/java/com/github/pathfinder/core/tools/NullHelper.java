package com.github.pathfinder.core.tools;

public class NullHelper {

    public static <T> T notNull(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

}
