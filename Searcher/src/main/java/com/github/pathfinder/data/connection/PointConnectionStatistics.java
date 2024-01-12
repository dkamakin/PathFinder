package com.github.pathfinder.data.connection;

public record PointConnectionStatistics(Integer batches,
                                        Integer total,
                                        Integer committedOperations,
                                        Integer failedOperations,
                                        Integer retries) {
}
