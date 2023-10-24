package com.github.pathfinder.core.tools.impl;

import java.util.function.Supplier;

public class NullHelper {

    public static <T> T notNull(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static <T> T notNull(T value, Supplier<T> defaultValue) {
        return value == null ? defaultValue.get() : value;
    }
}
