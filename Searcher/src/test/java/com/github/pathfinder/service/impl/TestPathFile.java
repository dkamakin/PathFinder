package com.github.pathfinder.service.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.pathfinder.core.tools.impl.NullHelper;
import com.github.pathfinder.database.entity.LandType;
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
                                LandType landType,
                                Set<TestFileConnection> connections) {

        public TestFilePoint(@JsonProperty("id")
                             UUID id,
                             @JsonProperty("altitude")
                             Double altitude,
                             @JsonProperty("longitude")
                             Double longitude,
                             @JsonProperty("latitude")
                             Double latitude,
                             @JsonProperty("landType")
                             LandType landType,
                             @JsonProperty("connections")
                             Set<TestFileConnection> connections) {
            this.id          = id;
            this.altitude    = altitude;
            this.longitude   = longitude;
            this.latitude    = latitude;
            this.landType    = landType;
            this.connections = NullHelper.notNull(connections, Set::of);
        }

        public record TestFileConnection(UUID targetId,
                                         Double distance) {

            public TestFileConnection(@JsonProperty("targetId")
                                      UUID targetId,
                                      @JsonProperty("distance")
                                      Double distance) {
                this.targetId = targetId;
                this.distance = distance;
            }
        }

    }

    public record TestExpectation(List<UUID> path,
                                  Double totalCost) {

        @JsonCreator
        public TestExpectation(@JsonProperty("path")
                               List<UUID> path,
                               @JsonProperty("totalCost")
                               Double totalCost) {
            this.path      = path;
            this.totalCost = totalCost;
        }
    }

}
