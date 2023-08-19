package com.github.pathfinder.core.interfaces;

import java.io.Closeable;

@FunctionalInterface
public interface ICloseable extends Closeable {

    void close();

}
