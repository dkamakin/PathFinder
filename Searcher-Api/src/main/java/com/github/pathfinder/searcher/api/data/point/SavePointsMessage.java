package com.github.pathfinder.searcher.api.data.point;

import com.google.common.base.MoreObjects;
import java.util.List;

public record SavePointsMessage(int id, List<Point> points) {

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .toString();
    }
}
