package com.github.pathfinder.data.connection;

public record IterateStatistics(Integer batches,
                                Integer total,
                                Integer committedOperations,
                                Integer failedOperations,
                                Integer retries) {
}
