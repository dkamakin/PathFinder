package com.github.pathfinder.searcher.api.data;

import java.util.List;
import com.google.common.base.MoreObjects;

public record GetChunksResponse(List<Chunk> chunks) {

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("chunks", chunks.size())
                .toString();
    }
}
