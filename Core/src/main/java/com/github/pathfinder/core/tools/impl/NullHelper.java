package com.github.pathfinder.core.tools.impl;

public class NullHelper {

    public static <T> T notNull(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

}
