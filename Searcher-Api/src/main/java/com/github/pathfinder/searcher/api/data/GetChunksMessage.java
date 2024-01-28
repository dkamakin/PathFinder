package com.github.pathfinder.searcher.api.data;

import com.google.common.base.MoreObjects;
import java.util.List;

public record GetChunksMessage(List<Integer> ids) {

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("ids", ids.size())
                .toString();
    }

}
