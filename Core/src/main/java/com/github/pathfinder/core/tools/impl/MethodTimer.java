package com.github.pathfinder.core.tools.impl;

import com.github.pathfinder.core.interfaces.IThrowingSupplier;
import java.time.Duration;
import java.util.function.Consumer;

public class MethodTimer {

    private final Consumer<Duration> handler;

    public MethodTimer(Consumer<Duration> handler) {
        this.handler = handler;
    }

    public <T, E extends Throwable> T throwable(IThrowingSupplier<T, E> method) throws E {
        var start = System.currentTimeMillis();
        try {
            return method.get();
        } finally {
            handler.accept(Duration.ofMillis(System.currentTimeMillis() - start));
        }
    }
}
