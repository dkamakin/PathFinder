package com.github.pathfinder.service.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.pathfinder.core.tools.impl.NullHelper;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record TestPathFile(List<TestFilePoint> points,
                           UUID sourceId,
                           UUID targetId,
                           TestExpectation expected) {

    @JsonCreator
    public TestPathFile(@JsonProperty("points")
                        List<TestFilePoint> points,
                        @JsonProperty("sourceId")
                        UUID sourceId,
                        @JsonProperty("targetId")
                        UUID targetId,
                        @JsonProperty("expected")
                        TestExpectation expected) {
        this.points   = points;
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.expected = expected;
    }

    public record TestFilePoint(UUID id,
                                Double altitude,
                                Double longitude,
                                Double latitude,
                                String landType,
                                Set<Connection> connections) {

        public TestFilePoint(@JsonProperty("id")
                             UUID id,
                             @JsonProperty("altitude")
                             Double altitude,
                             @JsonProperty("longitude")
                             Double longitude,
                             @JsonProperty("latitude")
                             Double latitude,
                             @JsonProperty("landType")
                             String landType,
                             @JsonProperty("connections")
                             Set<Connection> connections) {
            this.id          = id;
            this.altitude    = altitude;
            this.longitude   = longitude;
            this.latitude    = latitude;
            this.landType    = landType;
            this.connections = NullHelper.notNull(connections, Set::of);
        }

        public record Connection(UUID targetId,
                                 Double meters,
                                 Double weight) {

            public Connection(@JsonProperty("targetId")
                              UUID targetId,
                              @JsonProperty("weight")
                              Double meters,
                              @JsonProperty("weight")
                              Double weight) {
                this.targetId = targetId;
                this.meters   = meters;
                this.weight   = weight;
            }
        }

    }

    public record TestExpectation(List<UUID> path,
                                  Double meters) {

        @JsonCreator
        public TestExpectation(@JsonProperty("path")
                               List<UUID> path,
                               @JsonProperty("weight")
                               Double meters) {
            this.path   = path;
            this.meters = meters;
        }
    }

}
