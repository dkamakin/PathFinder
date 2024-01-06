package com.github.pathfinder.searcher.api.data.point;

import com.google.common.base.MoreObjects;
import java.util.List;

public record SavePointsMessage(List<Point> points) {

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .toString();
    }
}
