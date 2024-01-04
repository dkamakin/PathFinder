package com.github.pathfinder.core.executor;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface PlatformExecutor {

    <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks);

}
