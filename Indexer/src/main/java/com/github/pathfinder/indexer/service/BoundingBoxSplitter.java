package com.github.pathfinder.indexer.service;

import com.github.pathfinder.core.tools.impl.BoundingBox;
import java.util.List;

public interface BoundingBoxSplitter {

    List<BoundingBox> split(BoundingBox box);

}
