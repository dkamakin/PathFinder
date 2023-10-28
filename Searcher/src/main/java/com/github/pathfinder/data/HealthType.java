package com.github.pathfinder.data;

import lombok.Getter;

@Getter
public enum HealthType {
    HEALTHY("healthy"),
    WEAKENED("weakened"),
    WOUNDED("wounded");

    private final String graphName;

    HealthType(String graphName) {
        this.graphName = graphName;
    }

}
