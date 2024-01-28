package com.github.pathfinder.indexer.service;

import com.github.pathfinder.core.data.BoundingBox;
import java.util.List;

public interface BoundingBoxSplitter {

    List<BoundingBox> split(BoundingBox box);

}
