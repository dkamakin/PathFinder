package com.github.pathfinder.database.mapper;

import java.util.function.Function;
import org.neo4j.driver.Value;

@FunctionalInterface
public interface Mapper<T> extends Function<Value, T> {

}
