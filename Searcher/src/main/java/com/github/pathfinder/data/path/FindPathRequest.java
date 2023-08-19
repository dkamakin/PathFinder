package com.github.pathfinder.data.path;

import com.github.pathfinder.data.Coordinate;
import com.github.pathfinder.data.HealthType;

public record FindPathRequest(Coordinate source,
                              Coordinate target,
                              HealthType health) {

}
