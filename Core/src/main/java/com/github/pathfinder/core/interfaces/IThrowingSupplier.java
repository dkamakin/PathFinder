package com.github.pathfinder.core.interfaces;

@FunctionalInterface
public interface IThrowingSupplier<T, E extends Throwable> {

    T get() throws E;

}
