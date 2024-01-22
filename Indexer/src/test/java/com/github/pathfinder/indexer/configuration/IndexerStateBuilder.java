package com.github.pathfinder.indexer.configuration;

import com.github.pathfinder.indexer.database.entity.IndexBoxEntity;
import com.github.pathfinder.indexer.database.entity.IndexBoxEntityBuilder;
import com.github.pathfinder.indexer.service.impl.IndexBoxUpdaterService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
@RequiredArgsConstructor
public class IndexerStateBuilder {

    private final IndexBoxUpdaterService updaterService;

    public IndexBoxEntity save(IndexBoxEntity box) {
        return updaterService.save(box);
    }

    public IndexBoxEntity randomCoords(IndexBoxEntityBuilder builder) {
        return updaterService.save(builder.max(Math.random(), Math.random())
                                           .min(Math.random(), Math.random())
                                           .build());
    }

}
