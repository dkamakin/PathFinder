package com.github.pathfinder.searcher.api.data.point;

import com.github.pathfinder.searcher.api.data.IndexBox;
import com.google.common.base.MoreObjects;
import java.util.List;

public record SavePointsMessage(IndexBox box, List<Point> points) {

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("box", box.id())
                .add("points", points.size())
                .toString();
    }
}
