package com.github.pathfinder.core.interfaces;

@FunctionalInterface
public interface IThrowingFunction<T, R, E extends Throwable> {

    R apply(T argument) throws E;

}
