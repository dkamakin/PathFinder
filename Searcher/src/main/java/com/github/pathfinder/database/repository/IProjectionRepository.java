package com.github.pathfinder.database.repository;

import com.github.pathfinder.data.projection.ProjectionStatistics;
import java.util.Collection;

public interface IProjectionRepository {

    Collection<String> all();

    Collection<String> deleteAll(Collection<String> graphNames);

    ProjectionStatistics createProjection(String graphName);

    boolean exists(String graphName);

}
